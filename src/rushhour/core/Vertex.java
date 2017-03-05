package rushhour.core;

import java.util.Map;
import java.util.HashMap;

public class Vertex implements Comparable<Vertex> {
	public Board board;
	public Map<Move,Vertex> neighbors;
	public int depth;
	public Vertex(Board board) {
		this.board = board;
		this.depth = -2;
		this.neighbors = null;
	}
	public Map<Move,Vertex> getNeighbors() {
		if(this.neighbors == null) {
			this.neighbors = new HashMap<Move,Vertex>();
			for(Move m : this.board.allPossibleMoves()) {
				Board neighborBoard = this.board.getNeighborBoard(m);
				this.neighbors.put(m, new Vertex(neighborBoard));
			}
		}
		return this.neighbors;
	}
	public int compareTo(Vertex other) {
		return other.depth - this.depth;
	}
}
