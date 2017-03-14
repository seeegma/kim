package rushhour.solving;

import rushhour.core.Board;

import java.util.Comparator;

/**
 * Represents a linear combination of features and weights
 */
public class Heuristic {

	private Feature[] features;
	private double[] weights;

	public Heuristic(Feature[] features, double[] weights) {
		if(features.length != weights.length) {
			System.err.println("lengths of feature vector and weight vector do not match!");
			System.exit(1);
		}
		this.features = features;
		this.weights = weights;
	}

	public double value(Board board) {
		double result = 0.0;
		for(int i=0; i<this.features.length; i++) {
			result += this.features[i].value(board) * this.weights[i];
		}
		return result;
	}

}
