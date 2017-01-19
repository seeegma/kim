package rushhour.core;

import java.util.HashMap;

public class Vertex {

	public Board board;
	public HashMap<Move,Vertex> neighbors;
	public int depth;
	public Vertex parent;

	public Vertex(Board board) {
		this.board = board;
		this.parent = null;
		this.depth = 0;
		this.neighbors = null;
	}

}
