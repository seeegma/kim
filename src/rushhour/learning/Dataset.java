package rushhour.learning;

import rushhour.Util;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.nio.file.Path;

public class Dataset implements Iterable<Datum> {

	List<Datum> data;

	public Iterator<Datum> iterator() {
		return this.data.iterator();
	}

	public Dataset(String directory) {
		this.data = new LinkedList<>();
		for(Path path : Util.getFilePaths(directory)) {
			this.data.add(new Datum(path));
		}
	}

}
