package rushhour.evaluation;

import rushhour.core.Board;

public class NumberOfCarsEvaluator implements Evaluator {
	public String description() {
		return "number of cars";
	}
	public double eval(Board b) {
		return (double)b.numCars();
	}
}
