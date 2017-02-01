package rushhour.generation;

import rushhour.evaluation.Evaluator;

public class Constraint {
	public Evaluator metric;
	public double minValue, maxValue;
	public Constraint(Evaluator metric, double minValue, double maxValue) {
		this.metric = metric;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
}
