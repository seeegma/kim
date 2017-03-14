package rushhour.learning;

import rushhour.solving.Heuristic;
import rushhour.solving.Feature;

public abstract class Learner {

	protected Feature[] features;

	public Learner(Feature[] features) {
		this.features = features;
	}

	public abstract Heuristic learn(Dataset dataset);

}
