package rushhour.learning;

import rushhour.solving.Heuristic;
import rushhour.solving.Feature;

import org.ejml.simple.*;

import java.util.Arrays;

public class MultivariateRegressionLearner extends Learner {

	public MultivariateRegressionLearner(Feature[] features) {
		super(features);
	}

	public Heuristic learn(Dataset dataset) {
		int numFeatures = this.features.length;
		int numData = dataset.size();
		SimpleMatrix Y = new SimpleMatrix(numData, 1);
		SimpleMatrix X = new SimpleMatrix(numData, numFeatures);
		// fill in the matrices
		for(int i=0; i<numData; i++) {
			Y.set(i, 0, dataset.get(i).depth);
			for(int j=0; j<numFeatures; j++) {
				X.set(i, j, this.features[j].value(dataset.get(i).board));
			}
		}
		SimpleMatrix X_T = X.transpose();
		SimpleMatrix product = X_T.mult(X);
		SimpleMatrix inverse = product.invert();
		SimpleMatrix vector = X_T.mult(Y);
		SimpleMatrix optimalWeightMatrix = inverse.mult(vector);
		boolean debug = false;
		if(debug) {
			System.err.println("X:");
			System.err.println(X);
			System.err.println("transpose:");
			System.err.println(X_T);
			System.err.println("product:");
			System.err.println(product);
			System.err.println("inverse:");
			System.err.println(inverse);
			System.err.println("vector:");
			System.err.println(vector);
			System.err.println("optimalMatrix:");
			System.err.println(optimalWeightMatrix);
		}
		double[] optimalWeightArray = new double[numFeatures];
		for(int j=0; j<numFeatures; j++) {
			optimalWeightArray[j] = optimalWeightMatrix.get(j, 0);
		}
		return new Heuristic(this.features, optimalWeightArray);
	}

}
