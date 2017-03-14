package rushhour.learning;

import rushhour.solving.Heuristic;
import rushhour.solving.Feature;

import org.ejml.simple.*;

public class MultivariateRegressionLearner extends Learner {

	public MultivariateRegressionLearner(Feature[] features) {
		super(features);
	}

	public Heuristic learn(Dataset dataset) {
		int numFeatures = this.features.length;
		int numData = dataset.size();
		double[] weights = new double[numFeatures];
		double[][] trainingOutputs = new double[numFeatures][1];
		double[][] dataMatrix = new double[numFeatures][numData];
		// fill in trainingOutputs and dataMatrix
		for(int i=0; i<numData; i++) {
			trainingOutputs[0][i] = dataset.get(i).depth;
			for(int j=0; j<numFeatures; j++) {
				dataMatrix[i][j] = this.features[j].value(dataset.get(i).board);
			}
		}
		// turn into matrices
		SimpleMatrix X = new SimpleMatrix(dataMatrix);
		SimpleMatrix X_T = X.transpose();
		SimpleMatrix y = new SimpleMatrix(trainingOutputs);
		SimpleMatrix inverse = X_T.mult(X).invert();
		SimpleMatrix optimalWeightMatrix = inverse.mult(X_T).mult(y);
		double[] optimalWeightArray = new double[numFeatures];
		for(int j=0; j<numFeatures; j++) {
			optimalWeightArray[j] = optimalWeightMatrix.getIndex(j, 0);
		}
		// TODO solve using equation
		return new Heuristic(this.features, optimalWeightArray);
	}

}
