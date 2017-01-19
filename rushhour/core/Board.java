package rushhour.core;

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
	//The first car in carList should always be the VIP car, and
	//many methods rely on the VIP having index 0.
	private ArrayList<Car> carList;

	private BoardGraph graph;

	// temp to match context free grammar used by txt file
	// Need to incorporate this into the code somehow...

	// Basic constructor
	public Board(int w, int h) {
		this.w = w;
		this.h = h;
		this.grid = new Grid(w,h);
		this.carList = new ArrayList<Car>();
	}

	// Overloaded for importing from file
	public Board(int w, int h, ArrayList<Car> c) {
		this.w = w;
		this.h = h;
		this.grid = new Grid(w,h);
		this.carList = new ArrayList<Car>();

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
		return carList.get(0).x == w-carList.get(0).length;
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

	public Grid getGrid() {
		return this.grid;
	}

	public Long hash() {
		return this.grid.hash();
	}

	/**
	 * Getter for the list of cars on the board.
	 * @return an ArrayList<Car> of the cars
	 */
	public ArrayList<Car> getCars() {
		return this.carList;
	}

	/**
	 * Copies a board and returns the new board.
	 * @param the board to be copied.
	 * @return the new board
	 */
	public Board copy() {
		ArrayList<Car> newCarList = new ArrayList<Car>(this.carList.size());
		for (Car car : this.carList) {
			newCarList.add(car.copy());
		}
		return (new Board(this.w, this.h, this.grid.copy(), newCarList));
	}

    /**
     * Adds a new car.
     * @param newCar a new car to be inserted
     */
    public boolean addCar(Car newCar) {
				if(!canAddCar(newCar)) {
						return false;
				}
        int dx = 0;
        int dy = 0;
        if (newCar.horizontal) {
            dx++;
        } else {
            dy++;
        }
      	carList.add(newCar);
        for (int i = 0; i < newCar.length; i++) {
            grid.set(newCar.x + (dx*i),newCar.y + (dy*i),carList.size()-1);
        }
        return true;
    }

    public boolean canAddCar(Car newCar){
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



		public boolean slide(int carNum, Direction d) {
			if(!canSlide(carNum,d)){
				return false;
			}
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
			return true;
		}

	/**
	 * @param num the number associated with the car to move
	 * @param d the Direction to move in
	 * @return whether the move succeeded or not. Will return true only if it
	 * moves any amount of blocks
	 */
	public boolean canSlide(int carNum, Direction d) {
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



    /**
     * Gets all possible moves of car at index i on board b in that current
     * position.
     * @param i the index of the car
     * @return an ArrayList of all possible grid positions that results from
     *      moving that car
     */
    public ArrayList<Board> allPossibleMoves() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
		for(int i = 0; i<this.getCars().size(); i++) {
			Direction d;
			// Find the starting direction
			if (this.getCars().get(i).horizontal) {
				d = Direction.LEFT;
			} else {
				d = Direction.UP;
			}
			// Creates all possible board positions moving to the starting direction
			Board currentState = this.copy();
			while(currentState.slide(i,d)) {
				neighbors.add(currentState.copy());
			}
			d = d.reverse();
			// Moves back to the original position
			currentState = this.copy();
			// Repeats in the reverse of the starting direction
			while(currentState.slide(i,d)) {
				neighbors.add(currentState.copy());
			}
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
				if(canAddCar(car1) || canAddCar(car2)){
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

	public boolean equals(Board other) {
		return this.grid.equals(other.getGrid());
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
                if (grid.get(j,i) != -1) {
				    System.out.print(String.format("%1$4s", grid.get(j,i)));
                } else {
                    System.out.print(String.format("%1$4s", "_"));
                }
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

}
