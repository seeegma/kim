package rushhour.evaluation;

import rushhour.core.Board;
import rushhour.core.Car;

public class NumberOfLongCarsEvaluator implements Evaluator {
	public String description() {
		return "number of long cars";
	}
	public double eval(Board b) {
		double total = 0;
		for(Car c : b.getCars()) {
			if(c.length == 3) {
				total++;
			}
		}
		return total;
	}
}
