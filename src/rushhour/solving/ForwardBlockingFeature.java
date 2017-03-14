package rushhour.solving;

import rushhour.core.*;

import java.util.HashSet;
import java.util.HashSet;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplistic heuristic for A*. Mostly for proof of concept. Returns the number
 * of cars blocking the VIP's path to the exit.
 */
public class ForwardBlockingFeature implements Feature {
	private Board board;
	private HashMap<HashSet<Car>, HashSet<Car>> exitMap;
	private int exitHeight;

	private Car vip;

	public String toString() {
		return "forward";
	}

	public double value(Board board) {
		this.board = board;
		if (this.board.isSolved()) { return 0; }

		this.vip = this.board.getCars().get(0);
		this.exitHeight = vip.y;
		this.exitMap = new HashMap<HashSet<Car>, HashSet<Car>>();

		addExitStrategiesToMap();

		if (exitMap.isEmpty()) {
			// System.out.println("No viable strategies, or it is already solved.");
			return 0;
		}

		return getNumMovesInBestExitStrategy();
	}

	private void addExitStrategiesToMap() {
		for (int x = this.vip.x + this.vip.length; x < this.board.getWidth(); x++) {
			Integer car = this.board.getGrid().get(x, this.vip.y);
			if (car > 0) {
				addExitStrategiesForCar(this.board.getCars().get(car));
			}
		}
	}

	private void addExitStrategiesForCar(Car car) {
		boolean canEscapeAbove = canEscapeInDirection(car, true);
		boolean canEscapeBelow = canEscapeInDirection(car, false);

		// if (canEscapeAbove) { System.out.println("can escape above"); }
		// else { System.out.println("    can't escape above"); }
		// if (canEscapeBelow) { System.out.println("can escape below"); }
		// else { System.out.println("    can't escape below"); }

		HashSet<Car> aboveBlockingCars = new HashSet<Car>();
		HashSet<Car> belowBlockingCars = new HashSet<Car>();
		for (int y = 0; y < board.getHeight(); y++) {
			int blockingCarNum = this.board.getGrid().get(car.x, y);

			// add the blockingCar to the correct HashSet if it is a valid escape
			if (blockingCarNum > 0) {
				Car blockingCar = this.board.getCars().get(blockingCarNum);
				if (!blockingCar.equals(car) && y < this.exitHeight && canEscapeAbove) {
					aboveBlockingCars.add(blockingCar);
				} else if (!blockingCar.equals(car) && y > exitHeight && canEscapeBelow) {
					belowBlockingCars.add(blockingCar);
				}
				// skip the rest of this car in the loop
				y += blockingCar.horizontal ? 0 : blockingCar.length - 1;
			}
		}

		if (!exitMap.containsKey(aboveBlockingCars)) {
			exitMap.put(aboveBlockingCars, new HashSet<Car>());
		}
		exitMap.get(aboveBlockingCars).add(car);

		if (!exitMap.containsKey(belowBlockingCars)) {
			exitMap.put(belowBlockingCars, new HashSet<Car>());
		}
		exitMap.get(belowBlockingCars).add(car);
	}

	private boolean canEscapeInDirection(Car car, boolean up) {
		int start = 0;
		int end = car.y + car.length;
		if (!up) {
			start = car.y;
			end = this.board.getHeight();
		}

		int blockingAmt = 0;
		for (int y = start; y < end; y++) {
			int blockingCarNum = this.board.getGrid().get(car.x, y);
			if (blockingCarNum > 0 && !this.board.getCars().get(blockingCarNum).horizontal) {
				blockingAmt++;
			}
		}

		// returns true if the amount of space we have is less than the amount of
		// space we're going to take up
		// System.out.println(String.format("\nstuff: %s", blockingAmt));
		// System.out.println(String.format("start: %s", start));
		// System.out.println(String.format("end: %s", end));
		// System.out.println(String.format("space: %s", (up ? exitHeight : end - exitHeight - 1)));
		return blockingAmt <= (up ? exitHeight : end - exitHeight - 1);
	}

	private int getNumMovesInBestExitStrategy() {
		HashSet<Car> carsToMove = new HashSet<Car>();
		while (!this.exitMap.isEmpty()) {
			double bestRatio = 0;
			HashSet<Car> bestStrategy = new HashSet<Car>();

			// find the strategy with the highest ratio of carsVoting to carsToMove
			for (HashSet<Car> strategy : this.exitMap.keySet()) {
				if (strategy.isEmpty()) {
					continue;
				}
				double ratio = (double)this.exitMap.get(strategy).size() / strategy.size();
				// System.out.println(String.format("\ncarsInStrat: %s", strategy.size()));
				// System.out.println(String.format("carsVoting: %s", this.exitMap.get(strategy).size()));
				if (ratio > bestRatio) {
					bestRatio = ratio;
					bestStrategy = strategy;
				}
			}
			// System.out.println(String.format("\nstrat size: %s", bestStrategy.size()));

			// the cars in bestStrategy now have a way out, so we can remove their
			// votes from the other strategies
			for (Car votingCar : this.exitMap.get(bestStrategy)) {
				List<HashSet<Car>> strategiesWithCar = new ArrayList<HashSet<Car>>();
				for (HashSet<Car> strategy : this.exitMap.keySet()) {
					strategiesWithCar.add(strategy);
				}
				for (HashSet<Car> strategy : strategiesWithCar) {
					if (!strategy.equals(bestStrategy)) {
						this.exitMap.get(strategy).remove(votingCar);
					}
				}
				carsToMove.add(votingCar);
			}
			carsToMove.addAll(bestStrategy);

			// remove the best strategy from the list of possible strategies
			this.exitMap.remove(bestStrategy);

			// remove strategies with no more cars voting for them
			List<HashSet<Car>> emptyStrategies = new ArrayList<HashSet<Car>>();
			for (HashSet<Car> strategy : this.exitMap.keySet()) {
				if (this.exitMap.get(strategy).isEmpty()) {
					emptyStrategies.add(strategy);
				}
			}
			this.exitMap.keySet().removeAll(emptyStrategies);
		}

		return carsToMove.size();
	}
}
