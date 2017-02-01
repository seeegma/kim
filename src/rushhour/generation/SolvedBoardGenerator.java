package rushhour.generation;

import rushhour.core.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class SolvedBoardGenerator {

	public static Board generate(int targetNumCars) {
		Board board = new Board(6, 6);
		// vip in solved position
		Car vip = new Car(4, 2, 2, true);
		board.addCar(vip);
		// keep track of boards we've tried
		HashSet<Long> triedBoards = new HashSet<Long>();
		// store all remaining possible car positions
		List<Car> cars = new ArrayList<Car>();
		for(int x=0; x<6; x++) {
			for(int y=0; y<6; y++) {
				// horiz
				for(int length=2; length<=3 && x+length-1<6; length++) {
					Car toAdd = new Car(x, y, length, true);
					if(!toAdd.equals(vip)) {
						cars.add(toAdd);
					}
				}
				// vert
				for(int length=2; length<=3 && y+length-1<6; length++) {
					cars.add(new Car(x, y, length, false));
				}
			}
		}
		while(board.numCars() < targetNumCars) {
			Collections.shuffle(cars);
			for(Car c : cars) {
				if(board.canAddCar(c)) {
					board.addCar(c);
					updateCars(cars, c);
					break;
				}
			}
		}
		return board;
	}

	private static void updateCars(List<Car> cars, Car toRemove) {
		cars.remove(toRemove);
		if(toRemove.horizontal) {
			for(int i=0; i<cars.size(); i++) { // can't use iterator b/c concurrency
				Car c = cars.get(i);
				for(int x=toRemove.x; x<toRemove.x+toRemove.length; x++) {
					if(c.occupiesPos(x, toRemove.y)) {
						cars.remove(c);
						break;
					}
				}
			}
		} else {
			for(int i=0; i<cars.size(); i++) { // can't use iterator b/c concurrency
				Car c = cars.get(i);
				for(int y=toRemove.y; y<toRemove.y+toRemove.length; y++) {
					if(c.occupiesPos(toRemove.x, y)) {
						cars.remove(c);
						break;
					}
				}
			}
		}
	}

}
