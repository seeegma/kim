import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collections;

/**
 * Represents a position in a game of Rush Hour. Includes moving functionality.
 * Note that the board is represented as a w by h board with the top left
 * corner known to be 0, 0. Increasing x and y moves to the right and down
 * respectively.
 */
public class Board {
// TODO: introduce coordinate class just to make stuff more clear? Will make
// coordinate conversion from Direction easier.
    private int w, h; // dimension of the board
    // grid representation of the board. Each slot contains an integer that
	// represents the index of the car in carList that is occupying the spot
	// on the board. We will use -1 to represent empty spaces.
    private int[][] grid;
	private final int EMPTY_SPOT = -1;
    //private HashMap<Character,Car> carList;
    private ArrayList<Car> carList;

    // temp to match context free grammar used by txt file
    // Need to incorporate this into the code somehow...

    // Basic constructor
    public Board(int w, int h) {
        this.w = w;
        this.h = h;
        grid = new int[w][h];
        carList = new ArrayList<Car>();
    }
    
    // Overloaded for importing from file
    public Board(int w, int h, ArrayList<Car> c) {
        this.w = w;
        this.h = h;
        grid = new int[w][h];
        for (int i=0;i<w;i++) {
            for (int j=0;j<h;j++){
                grid[i][j]=-1;
            }
        }
        carList = new ArrayList<Car>();

        // Could just carList = c, but then would need to update grid...
        for (int i = 0; i < c.size(); i++) {
            this.addCar(c.get(i));
        }
    }

    public Board(int w, int h, int[][] grid,ArrayList<Car> c) {
        this.w = w;
        this.h = h;
        grid = grid;
        carList = c;
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
     * Checks if two boards are equal.
     * Assumes the two boards come from the same graph, i.e. are manipulations of each other. 
     * @param the other board
     * @return whether the boards are equal. 
     */
    public boolean equals(Board b) {
        return Arrays.deepEquals(this.grid, b.grid);
    }

    /**
     * Checks if two grids are equal.
     * Assumes the two boards come from the same graph, i.e. are manipulations of each other. 
     * @param the other grid
     * @return whether the boards are equal. 
     */
    public boolean equals(int[][] g) {
        return Arrays.deepEquals(this.grid, g);
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
        int[][] newGrid = new int[this.w][this.h];
        for (int i=0;i<w;i++) {
            newGrid[i]=this.grid[i].clone();
        }
        return (new Board(this.w, this.h, newGrid, newCarList));
    }

    /**
	 * Compresses this instance of the board into a node for our BFS alg.
	 * @param parent the parent node for the new node
	 * @return the new node with the compressed data of this instance of Board
	 */
	public Node compress(Node parent) {
		return new Node(grid, parent);
	}
	
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
		for (int i = 0; i < grid[0].length; i++) { // assumes it's a matrix
			for (int j = 0; j < grid.length; j++) {
				k = grid[j][i]; // j is x, i is y
				if(k != EMPTY_SPOT && !isUpdated[k]) {
					carList.get(k).x = j;
					carList.get(k).y = i;
					isUpdated[k] = true;
				}
			}
		}
		
		this.createGrid();
	}
	
	/**
	 * Clears the grid so that it's empty.
	 */
	private void clearGrid() {
		for (int i = 0; i < grid[0].length; i++) { // assumes it's a matrix
			for (int j = 0; j < grid.length; j++) {
				grid[j][i] = EMPTY_SPOT;
			}
		}
	}
	
	/**
	 * Creates a new grid matrix using the current carList. Used when we're
	 * decompressing nodes.
	 */
	private void createGrid() {
		this.clearGrid();
		
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
				grid[tempx][tempy] = i;
			}
		}
	}

    /**
     * Adds a new car. Might be useful to be public fcn for testing.
     * @param newCar a new car to be inserted
     */
    public void addCar(Car newCar) {
        // update list
        carList.add(newCar);

// TODO: CHECK THAT THE NEW CAR IS NOT IN ANOTHER CAR
        // update grid
        int dx = 0;
        int dy = 0;
        if (newCar.horizontal) {
            dx++;
        } else {
            dy++;
        }
        for (int i = 0; i < newCar.length; i++) {
			// since the newCar was added at the end of the array its index is:
            grid[newCar.x + (dx*i)][newCar.y + (dy*i)] = carList.size()-1;
        }
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
                if (c.y>0 && grid[c.x][c.y-1]!=-1) {
                    return true;
                }
                break;
            case LEFT:
                if (c.x>0 && grid[c.x-1][c.y]!=-1) {
                    return true;
                }
                break;
            case DOWN:
                if (c.y+c.length-1<h-1 && grid[c.x][(c.y+c.length-1)+1]!=-1) {
                    return true;
                }
                break;
            default:
                if (c.x+c.length-1<w-1 && grid[c.x+c.length-1+1][c.y]!=-1) {
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
                grid[c.x][c.y-1]=carNum;
                grid[c.x][c.y+c.length-1]=-1;
                c.y-=1;
                break;
            case LEFT:
                grid[c.x-1][c.y]=carNum;
                grid[c.x+c.length-1][c.y]=-1;
                c.x-=1;
                break;
            case DOWN:
                grid[c.x][c.y]=-1;
                grid[c.x][c.y+c.length]=carNum;
                c.y+=1;
                break;
            default:
                grid[c.x][c.y]=-1;
                grid[c.x+c.length][c.y]=carNum;
                c.x+=1;
                break;
        }
    }

    public ArrayList<Board> getNeighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        for (int i=0;i<this.carList.size();i++) {
            for (Direction d : Direction.values()) { 
                if (canMove(i,d)) {
                    Board neighbor = this.copy();
                    neighbor.move(i,d);
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }


        
    public ArrayList<Board> solve() {
        LinkedList<NodeBoard> queue = new LinkedList<NodeBoard>();
        ArrayList<NodeBoard> visited = new ArrayList<NodeBoard>();
        queue.offer(new NodeBoard(this,null));
        NodeBoard solvedState = new NodeBoard(this,null);
        boolean solutionFound = false;
        while (!queue.isEmpty()) {
            NodeBoard current = queue.poll();
            visited.add(current);
            if (current.board.isSolved()) {
                solvedState=current;
                solutionFound = true;
                break;
            }
            for (Board b : current.board.getNeighbors()) { 
                boolean isNew = true;
                for (NodeBoard node : visited) {
                    if (b.equals(node.board)) {
                        isNew = false;
                        break;
                    }
                }
                if (isNew) {
                    queue.offer(new NodeBoard(b,current));
                }
            }
        }
        if (!solutionFound) {
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
                System.out.println(grid[i][j]);
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
        /*Board b = new Board(6, 6, Direction.RIGHT, 2);
        Car c = new Car(0, 0, 2, true);
        b.addCar(c);*/

        // Board b = BoardIO.read("test");
        // b.debug();
        // System.out.println(b.move(0, Direction.UP, 1));
        // System.out.println(b.move(0, Direction.RIGHT, 1));
        // System.out.println(b.move(3, Direction.RIGHT, 1));
        // System.out.println(b.move(3, Direction.UP, 1));
        // System.out.println(b.move(3, Direction.DOWN, 1));
        // b.debug();
        // BoardIO.write("test2", b);

        AGen agen = new AGen();
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
        Board board = new Board(6,6,cList);
        agen.printGrid(agen.outputGrid(board.grid));
    }
}
