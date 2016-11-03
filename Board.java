import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a position in a game of Rush Hour. Includes moving functionality.
 * Note that the board is represented as a w by h board with the top left
 * corner known to be 0, 0. Increasing x and y moves to the right and down
 * respectively. The VIP is at index 0, and the board is solved if it is
 * flush with the East/Right edge of the board.
 */
public class Board {
	// TODO: introduce coordinate class just to make stuff more clear? Will make
	// coordinate conversion from Direction easier.
	private int w, h; // dimension of the board
	// grid representation of the board. Each slot contains an integer that
	// represents the index of the car in carList that is occupying the spot
	// on the board. We will use -1 to represent empty spaces.
	private Grid grid;
	private final int EMPTY_SPOT = -1;
	//private HashMap<Character,Car> carList;
	private ArrayList<Car> carList;

	// temp to match context free grammar used by txt file
	// Need to incorporate this into the code somehow...

	// Basic constructor
	public Board(int w, int h) {
		this.w = w;
		this.h = h;
		grid = new Grid(w,h);
		carList = new ArrayList<Car>();
	}

	// Overloaded for importing from file
	public Board(int w, int h, ArrayList<Car> c) {
		this.w = w;
		this.h = h;
		grid = new Grid(w,h);
		carList = new ArrayList<Car>();

		// Could just carList = c, but then would need to update grid...
		for (int i = 0; i < c.size(); i++) {
			this.addCar(c.get(i));
		}
	}

	public Board(int w, int h, Grid grid,ArrayList<Car> c) {
		this.w = w;
		this.h = h;
		this.grid = grid;
		this.carList = c;
	}


	public boolean isSolved() {
		return (carList.get(0).x==w-carList.get(0).length);
	}

	/**
	 * Getter for the width of the board.
	 * @return the width
	 */
	public int getWidth() {
		return this.w;
	}

	/**
	 * Getter for the height of the board.
	 * @return the height
	 */
	public int getHeight() {
		return this.h;
	}


	/**
	 * Getter for the list of cars on the board.
	 * @return an ArrayList<Car> of the cars
	 */
	public ArrayList<Car> getCars() {
		return this.carList;
	}

	/**
	 * Getter for the grid.
	 * @return the grid
	 */
	public Grid getGrid() {
		return this.grid;
	}


	/**
	 * Checks if two boards are equal.
	 * Assumes the two boards come from the same graph, i.e. are manipulations of each other. 
	 * @param the other board
	 * @return whether the boards are equal. 
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Board)) {
			return false;
		}
		Board b = (Board) obj;

		return this.grid.equals(b.grid);
	}

	/**
	 * Checks if two grids are equal.
	 * Assumes the two boards come from the same graph, i.e. are manipulations of each other. 
	 * @param the other grid
	 * @return whether the boards are equal. 
	 */
	public boolean equals(Grid g) {
		return this.grid.equals(g);
	}

	/**
	 * Copies a board and returns the new board.
	 * @param the board to be copied.
	 * @return the new board
	 */
	public Board copy() {
		ArrayList<Car> newCarList = new ArrayList<Car>(this.carList.size());
		for (Car car: this.carList) {
			newCarList.add(car.copy());
		}
		return (new Board(this.w, this.h, this.grid.copy(), newCarList));
	}

	/*
    /**
	 * Compresses this instance of the board into a node for our BFS alg.
	 * @param parent the parent node for the new node
	 * @return the new node with the compressed data of this instance of Board
	 /
	public Node compress(Node parent) {
		return new Node(grid, parent);
	}*/

	/**
	 * Decompresses a node of our "graph" into this board. Assumes the Board
	 * was used to generate this node in the first place through our BFS alg.
	 * @param n the compressed node to decompress
	 */
	public void decompress(Node n) {
		this.grid = n.grid;

		boolean[] isUpdated = new boolean[carList.size()];
		for (int i = 0; i < isUpdated.length; i++) {
			isUpdated[i] = false;
		}

		int k;
		for (int i = 0; i < grid.height; i++) { // assumes it's a matrix
			for (int j = 0; j < grid.width; j++) {
				k = grid.get(j,i); // j is x, i is y
				if(k != EMPTY_SPOT && !isUpdated[k]) {
					carList.get(k).x = j;
					carList.get(k).y = i;
					isUpdated[k] = true;
				}
			}
		}

		this.createGrid();
	}

	@Override
	public int hashCode() {
		return this.grid.hash();
	}

	/**
	 * Creates a new grid matrix using the current carList. Used when we're
	 * decompressing nodes.
	 */
	private void createGrid() {
		grid.clear();

		int dx, dy, tempx, tempy;
		Car c;
		for (int i = 0; i < carList.size(); i++) {
			c = carList.get(i);
			dx = 0;
			dy = 0;
			if (c.horizontal) {
				dx++;
			} else {
				dy++;
			}

			for (int j = 0; j < c.length; j++) {
				tempx = c.x + (dx * j);
				tempy = c.y + (dy * j);
				grid.set(tempx,tempy,i);
			}
		}
	}


    /**
     * Adds a new car. Might be useful to be public fcn for testing.
     * @param newCar a new car to be inserted
     */
    public boolean addCar(Car newCar) {
        // update list
        
        boolean placeable = true;
        // update grid
        int dx = 0;
        int dy = 0;
        if (newCar.horizontal) {
            dx++;
        } else {
            dy++;
        }

        placeable = canPlace(newCar);
        
        if (placeable) { 
        	carList.add(newCar);
            for (int i = 0; i < newCar.length; i++) {

                grid.set(newCar.x + (dx*i),newCar.y + (dy*i),carList.size()-1);

            }
            
        }
        return(placeable);

    }
    
    public boolean canPlace(Car newCar){
    	boolean canPlace = true;
    	int dx = 0;
    	int dy = 0;
    	if (newCar.horizontal) {
            dx++;
        } else {
            dy++;
        }
    	
    	for (int i = 0; i < newCar.length; i++) {
			// since the newCar was added at the end of the array its index is:

            if (grid.get(newCar.x + (dx*i), newCar.y + (dy*i)) != -1) {
                canPlace = false;
                break;
            }
        }
    	return canPlace;
    }

	/**
	 * Moves the car in the direction d by amount or until it hits another car.
	 * @param num the number associated with the car to move
	 * @param d the Direction to move in
	 * @param amount the number of squares to move
	 * @return whether the move succeeded or not. Will return true only if it
	 * moves any amount of blocks
	 */
	public boolean canMove(int carNum, Direction d) {
		Car c = carList.get(carNum);
		if (c==null) {
			return false;
		}

		if (d.isHorizontal()!=c.horizontal) {
			return false;
		}


		switch(d) {
		case UP:
			if (c.y>0 && grid.get(c.x,c.y-1)==-1) {
				return true;
			}
			break;
		case LEFT:
			if (c.x>0 && grid.get(c.x-1,c.y)==-1) {
				return true;
			}
			break;
		case DOWN:
			if (c.y+c.length-1<h-1 && grid.get(c.x, (c.y+c.length-1)+1)==-1) {
				return true;
			}
			break;
		default:
			if (c.x+c.length-1<w-1 && grid.get(c.x+c.length-1+1, c.y)==-1) {
				return true;
			}
			break;

		}
		return false;
	}

	public void move(int carNum, Direction d) { //Assumes the move is legal. 
		Car c = carList.get(carNum);
		switch(d) {
		case UP:
			grid.set(c.x, c.y-1, carNum);
			grid.set(c.x, c.y+c.length-1, -1);
			c.y-=1;
			break;
		case LEFT:
			grid.set(c.x-1, c.y,carNum);
			grid.set(c.x+c.length-1, c.y,-1);
			c.x-=1;
			break;
		case DOWN:
			grid.set(c.x,c.y,-1);
			grid.set(c.x,c.y+c.length,carNum);
			c.y+=1;
			break;
		case RIGHT:
			grid.set(c.x, c.y,-1);
			grid.set(c.x+c.length,c.y,carNum);
			c.x+=1;
			break;
		default:
			System.out.println("Error encountered");
			break;
		}
	}

	// /**
	//  * Gets all the neighboring positions of the current positon.
	//  * @return a list of the Boards that are 1 move away from the current
	//  * board's position
	//  */
	// public ArrayList<Board> getNeighbors() {
	// 	ArrayList<Board> neighbors = new ArrayList<Board>();
	// 	for (int i=0;i<this.carList.size();i++) {
	// 		for (Direction d : Direction.values()) { 
	// 			if (canMove(i,d)) {
	// 				Board neighbor = this.copy();
	// 				neighbor.move(i,d);
	// 				neighbors.add(neighbor);
	// 			}
	// 		}
	// 	}
	// 	return neighbors;
	// }


	/**
     * Gets all the neighboring positions of the current positon.
     * @param b the board to manipulate
     * @param parent the parent node
     * @return a list of the Boards that are 1 move away from the current
     *      board's position
     */
    public ArrayList<Board> getNeighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        // Goes through each car in the board and gets all possible neighbors
        // moving that car can create
        for (int i = 0; i < this.getCars().size(); i++) {
            ArrayList<Board> lst = allPossibleMoves(i);
            for (Board b : lst) {
                neighbors.add(b);
            }
        }
        return neighbors;
    }

    /**
     * Gets all possible moves of car at index i on board b in that current
     * position.
     * @param b the board
     * @param i the index of the car
     * @return an ArrayList of all possible grid positions that results from
     *      moving that car
     */
    public ArrayList<Board> allPossibleMoves(int i) {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        Direction d;
        // Find the starting direction
        if (this.getCars().get(i).horizontal) {
            d = Direction.LEFT;
        } else {
            d = Direction.UP;
        }

        // Creates all possible board positions moving to the starting direction
        Board currentState = this;
        while(currentState.canMove(i,d)) {
        	Board newBoard = this.copy();
            newBoard.move(i, d);
            neighbors.add(newBoard);
            currentState = newBoard;
        }
        // Moves back to the original position
        /* Making a method to revert this back in one method call might not be
        worth it since that would require at max 6 grid operations (moving a
        length 3 car to a new positon). Could have an if statement to determine
        whether to use this or that new method, but then that'd just overhead
        and isn't a huge improvement if at all, especially on a packed board*/
        d = d.reverse();

        currentState = this;
        
        // Repeats in the reverse of the starting direction
        while(currentState.canMove(i,d)) {
        	Board newBoard = this.copy();
            newBoard.move(i, d);
            neighbors.add(newBoard);
            currentState = newBoard;
        }

        
        return neighbors;
    }

	/**
	 * Checks if there is a place to put a car in the grid
	 * @return true if a car can be placed
	 */
	public boolean hasEmpty(){
		for(int y = 0;y<this.h-1;y++){
			for (int x = 0; x< this.w-1;x++){
				Car car1 = new Car(x,y,2,true);
				Car car2 = new Car(x,y,2,false);
				if(canPlace(car1) || canPlace(car2)){
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Clears board
	 */
	public void clear(){
		this.grid.clear();
	}

	/**
	 * Solves the Rush Hour position.
	 * @return a list of Boards that represent the path to the solution.
	 */
	public ArrayList<Board> solve() {
		LinkedList<NodeBoard> queue = new LinkedList<NodeBoard>();
		HashSet<Integer> visited = new HashSet<Integer>();
		int count = 0;
		queue.offer(new NodeBoard(this,null,0));
		visited.add(this.grid.hash());
		NodeBoard solvedState = new NodeBoard(this,null,0);
		boolean solutionFound = false;
		while (!queue.isEmpty()) {
			NodeBoard current = queue.poll();
			if (current.numMoves > count) {
				System.out.println(count);
				count++;
			}
			if (current.board.isSolved()) {
				solvedState=current;
				solutionFound = true;
				//break;
			}
			ArrayList<Board> neighbors = current.board.getNeighbors();
			int numAdded = 0;
			for (Board b : neighbors) {
				if (!visited.contains(b.grid.hash())) {
					queue.offer(new NodeBoard(b,current,current.numMoves+1));
					visited.add(b.grid.hash());
					numAdded++;
				}
			}
		}

		if (!solutionFound) {
			System.out.println("No solution found");
			return null;
		}
		else {
			ArrayList<Board> path = new ArrayList<Board>();
			NodeBoard current = solvedState;
			path.add(current.board);
			while (current.parent!=null) {
				current=current.parent;
				path.add(current.board);
			}
			Collections.reverse(path);
			return path;
		}
	}


	/**
	 * @Exclude
	 * For testing purposes. Prints out the innards.
	 */
	public void debug() {
		System.out.println("---------------------------");
		System.out.println("Dimensions: w" + w + " h" + h);
		System.out.println();

		System.out.println("grid:");
		for(int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				System.out.print(String.format("%1$4s", grid.get(j,i)));
			}
			System.out.println();
		}
		System.out.println();

		Car car;
		for(int i = 0; i < carList.size(); i++) {
			car = carList.get(i);
			System.out.println("Car #" + i + ": " + car.x + " " + car.y + " "
					+ car.length + " " + car.horizontal + " " + i);
		}
		System.out.println("---------------------------");
	}

	/**
	 * For testing purposes.
	 */
	public static void main(String[] args) {
		/*
        Board b = new Board(6, 6, Direction.RIGHT, 2);
        Car c = new Car(0, 0, 2, true);
        b.addCar(c);*/

		/*
        Car car0 = new Car(0,2,2,true);
        Car car1 = new Car(0,3,3,true);
        Car car2 = new Car(4,0,2,true);
        Car car3 = new Car(2,0,3,false);
        Car car4 = new Car(5,3,3,false);
        ArrayList<Car> cList= new ArrayList<Car>(5);
        cList.add(car0);
        cList.add(car1);
        cList.add(car2);
        cList.add(car3);
        cList.add(car4);
        Board board = new Board(6,6,cList);*/

		// Car car0 = new Car(0,2,2,true);
		// ArrayList<Car> cList = new ArrayList<Car>(1);
		// cList.add(car0);
		// Board board = new Board(6,6,cList);


		// board.move(2,Direction.LEFT);
		//agen.printGrid(agen.outputGrid(board.grid));

		Board board = BoardIO.read("93moves");


		board.debug();
		/*
		for (Board b : board.solve()) {
			AGen.printGrid(AGen.getPrintableGrid(b.grid));
		}*/


		//AltSolver.solveBoard(board);
        
        /*
        for (Grid b : AltSolver.solveBoard(board)) {
            board.decompress(new Node(b, null, 0, 0));
            board.debug();
        }*/

        ArrayList<Grid> soln = AltSolver.solveBoard(board);
        ArrayList<Move> moves = AltSolver.solveBoardWithMoves(board);
        for (int i = 0; i < soln.size(); i++) {
            board.decompress(new Node(soln.get(i), null, 0, 0));
            board.debug();
            if (i < moves.size()) {
                moves.get(i).debug();
            }
        }
	}

}
