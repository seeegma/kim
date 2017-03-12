package rushhour.core;

import rushhour.solving.Solver;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a position in a game of Rush Hour. Includes moving functionality.
 * Note that the board is represented as a width by height board with the top left
 * corner known to be 0, 0. Increasing x and y moves to the right and down
 * respectively. The VIP is at index 0, and the board is solved if it is
 * flush with the East/Right edge of the board.
 */
public class Board {
	private int width, height;
	private Grid grid;
	private final int EMPTY_SPOT = -1;
	// 0th car is vip
	private List<Car> cars;

	private EquivalenceClass equivalenceClass;

	// Basic constructor
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new Grid(width,height);
		this.cars = new ArrayList<Car>();
	}

	// Overloaded for importing from file
	public Board(int width, int height, List<Car> cars) {
		this.width = width;
		this.height = height;
		this.grid = new Grid(width,height);
		this.cars = new ArrayList<Car>();
		// can't just do this.cars = cars b/c we need to update grid
		for (int i = 0; i < cars.size(); i++) {
			this.addCar(cars.get(i));
		}
	}

	// for deep copying
	private Board(int width, int height, Grid grid, List<Car> cars) {
		this.width = width;
		this.height = height;
		this.grid = grid;
		this.cars = cars;
	}

	public boolean isSolved() {
		return cars.get(0).x == this.width-cars.get(0).length;
	}

	public int getWidth() {
		return this.width;
	}

	public int getOffset() {
		return (this.height+1)/2-1;
	}

	public int getHeight() {
		return this.height;
	}

	public Grid getGrid() {
		return this.grid;
	}

	public Long hash() {
		return this.grid.hash();
	}

	public EquivalenceClass getEquivalenceClass() {
		if(this.equivalenceClass == null) {
			this.equivalenceClass = new EquivalenceClass(this);
		}
		return this.equivalenceClass;
	}

	public int numCars() {
		return this.cars.size();
	}

	public List<Car> getCars() {
		return this.cars;
	}

	public Board copy() {
		ArrayList<Car> newCars = new ArrayList<Car>(this.cars.size());
		for (Car car : this.cars) {
			newCars.add(car.copy());
		}
		return (new Board(this.width, this.height, this.grid.copy(), newCars));
	}

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
		cars.add(newCar);
		for (int i = 0; i < newCar.length; i++) {
			grid.set(newCar.x + (dx*i),newCar.y + (dy*i),cars.size()-1);
		}
		this.equivalenceClass = null;
		return true;
	}

	public boolean canAddCar(Car newCar){
		boolean canPlace = true;
		int dx = 0;
		int dy = 0;
		if(newCar.horizontal) {
			dx++;
		} else {
			dy++;
		}
		for(int i = 0; i < newCar.length; i++) {
			if(grid.get(newCar.x + (dx*i), newCar.y + (dy*i)) != -1) {
				canPlace = false;
				break;
			}
		}
		return canPlace;
	}

	public boolean canMove(int vehicleIndex, int vector) {
		Car c = this.cars.get(vehicleIndex);
		if(vector == 0) {
			return true;
		}
		if(c.horizontal) {
			if(vector > 0) {
				for(int testX = c.x+c.length; testX < c.x+c.length+vector; testX++) {
					if(testX >= this.grid.width || this.grid.get(testX, c.y) != -1) {
						return false;
					}
				}
			} else {
				for(int testX = c.x-1; testX > c.x + vector-1; testX--) {
					if(testX < 0 || this.grid.get(testX, c.y) != -1) {
						return false;
					}
				}
			}
		} else {
			if(vector > 0) {
				for(int testY = c.y+c.length; testY < c.y+c.length+vector; testY++) {
					if(testY >= this.grid.height || this.grid.get(c.x, testY) != -1) {
						return false;
					}
				}
			} else {
				for(int testY = c.y-1; testY > c.y+vector-1; testY--) {
					if(testY < 0 || this.grid.get(c.x, testY) != -1) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean move(Move move) {
		return this.move(move.index, move.vector);
	}

	public boolean move(int index, int vector) {
		if(!this.canMove(index, vector)){
			return false;
		}
		Car c = this.cars.get(index);
		if(c.horizontal) {
			if(vector > 0) {
				// un-place car
				for(int i=0; i<c.length; i++) {
					this.grid.set(c.x + i, c.y, -1);
				}
				c.x += vector;
				// place car
				for(int i=0; i<c.length; i++) {
					this.grid.set(c.x + i, c.y, index);
				}
			} else {
				// un-place car
				for(int i=0; i<c.length; i++) {
					this.grid.set(c.x + i, c.y, -1);
				}
				c.x += vector;
				// place car
				for(int i=0; i<c.length; i++) {
					this.grid.set(c.x + i, c.y, index);
				}
			}
		} else {
			if(vector > 0) {
				// un-place car
				for(int i=0; i<c.length; i++) {
					this.grid.set(c.x, c.y + i, -1);
				}
				c.y += vector;
				// place car
				for(int i=0; i<c.length; i++) {
					this.grid.set(c.x, c.y + i, index);
				}
			} else {
				// un-place car
				for(int i=0; i<c.length; i++) {
					this.grid.set(c.x, c.y + i, -1);
				}
				c.y += vector;
				// place car
				for(int i=0; i<c.length; i++) {
					this.grid.set(c.x, c.y + i, index);
				}
			}
		}
		return true;
	}

	public void solve(Solver solver) {
		for(Move move : solver.getSolution(this)) {
			this.move(move);
		}
	}

	public Board getNeighborBoard(Move move) {
		Board ret = this.copy();
		ret.move(move.index, move.vector);
		return ret;
	}

	public Set<Move> allPossibleMoves() {
		Set<Move> moves = new HashSet<Move>();
		for(int vehicleIndex = 0; vehicleIndex<this.getCars().size(); vehicleIndex++) {
			int vector;
			// Creates all possible board positions moving to the starting direction
			Board currentState = this.copy();
			vector = -1;
			while(currentState.move(vehicleIndex, -1)) {
				moves.add(new Move(vehicleIndex, vector));
				vector--;
			}
			// Moves back to the original position
			currentState = this.copy();
			// Repeats in the reverse of the starting direction
			vector = 1;
			while(currentState.move(vehicleIndex, 1)) {
				moves.add(new Move(vehicleIndex, vector));
				vector++;
			}
		}
		return moves;
	}

	public boolean hasEmpty(){
		for(int y=0; y<this.height-1; y++) {
			for(int x=0; x<this.width-1; x++) {
				if(this.grid.get(x,y) == -1) {
					// check surrounding spots: to the right and below
					if(this.grid.get(x+1,y) == -1 || this.grid.get(x,y+1) == -1) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void clear(){
		this.equivalenceClass = null;
		this.grid.clear();
		this.cars.clear();
	}

	public boolean equals(Board other) {
		return this.grid.equals(other.getGrid());
	}

	public String toString() {
		return this.grid.toString();
	}

}
