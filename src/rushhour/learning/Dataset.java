package rushhour.learning;

import rushhour.Util;
import rushhour.solving.Heuristic;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.nio.file.Path;

public class Dataset extends LinkedList<Datum> {

	public Dataset(String directory) {
		super();
		for(Path path : Util.getFilePaths(directory)) {
			this.add(new Datum(path));
		}
	}

	public double getMeanError(Heuristic heuristic) {
		double totalError = 0.0;
		for(Datum datum : this) {
			totalError += this.getSingleError(heuristic, datum);
		}
		return totalError/this.size();
	}

	private double getSingleError(Heuristic heuristic, Datum datum) {
		return Math.pow(datum.depth - heuristic.value(datum.board), 2.0);
	}

}
