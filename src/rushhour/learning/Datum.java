package rushhour.learning;

import rushhour.core.Board;
import rushhour.io.BoardIO;

import java.nio.file.Path;

public class Datum {

	public Board board;
	public int depth;
	private String id;

	public Datum(Path path) {
		this.id = path.toString();
		this.board = BoardIO.read(path.toAbsolutePath().toString());
		this.depth = Integer.parseInt(path.getParent().getFileName().toString());
	}

}
