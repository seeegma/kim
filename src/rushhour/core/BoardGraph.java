package rushhour.core;

import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

public abstract class BoardGraph {

	protected HashMap<Long,Vertex> vertices;
	protected Set<Long> solutions;

	protected class Vertex implements Comparable<Vertex> {
		public Board board;
		public Map<Move,Vertex> neighbors;
		public int depth;
		public Vertex(Board board) {
			this.board = board;
			this.depth = -2;
			this.neighbors = null;
		}
		public int compareTo(Vertex other) {
			return other.depth - this.depth;
		}
	}

	protected BoardGraph() {
		this.vertices = new HashMap<Long,Vertex>();
		this.solutions = new HashSet<>();
	}

	public int size() {
		return vertices.size();
	}

	protected Vertex getVertex(Board board) {
		return this.vertices.get(board.hash());
	}

	public Set<Board> solutions() {
		Set<Board> ret = new HashSet<>();
		for(Long hash : this.solutions) {
			ret.add(this.vertices.get(hash).board);
		}
		return ret;
	}

	protected void expandVertex(Vertex vertex) {
		// TODO
	}

	protected void addVertex(Board board) {
		if(this.getVertex(board) == null) {
			this.vertices.put(board.hash(), new Vertex(board));
		}
	}

}
