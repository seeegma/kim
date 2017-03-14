package rushhour.solving;

import rushhour.core.Board;

public interface Feature {

	public double value(Board board);

	public String toString();

	public static Feature fromString(String name) {
		if(name.equals("solved")) {
			return new SolvedFeature();
		} else if(name.equals("blocking")) {
			return new BlockingFeature();
		} else if(name.equals("forward")) {
			return new ForwardBlockingFeature();
		} else {
			System.err.println("ERROR: unrecognized feature name!");
			System.exit(1);
			return null;
		}
	}

	public static Feature[] vectorFromString(String names) {
		String[] split = names.split(",");
		Feature[] vector = new Feature[split.length];
		for(int i=0; i<split.length; i++) {
			vector[i] = fromString(split[i]);
		}
		return vector;
	}
}

