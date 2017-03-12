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

	public class Edge {
		public Move move;
		public Vertex vertex;
		public Edge(Move move, Vertex vertex) {
			this.move = move;
			this.vertex = vertex;
		}
	}

	public class Vertex implements Comparable<Vertex> {
		public Board board;
		public Map<Move,Vertex> neighbors;
		public int depth;
		public Vertex(Board board) {
			this.board = board;
			this.depth = -2;
			this.neighbors = null;
		}
		// knits all neighbors into the graph, and returns any previously-unseen neighbors
		public LinkedList<Edge> expand() {
			if(this.neighbors == null) {
				this.neighbors = new HashMap<Move,Vertex>();
				LinkedList<Edge> unseen = new LinkedList<>();
				// fill in neighbors with vertices already in the graph
				for(Move move : this.board.allPossibleMoves()) {
					Board neighborBoard = this.board.getNeighborBoard(move);
					if(vertices.containsKey(neighborBoard.hash())) {
						// if the vertex exists in the graph, replace current's instance with the graph's
						this.neighbors.put(move, vertices.get(neighborBoard.hash()));
					} else {
						// otherwise add current's instance to the graph
						Vertex neighborVertex = new Vertex(neighborBoard);
						vertices.put(neighborBoard.hash(), neighborVertex);
						this.neighbors.put(move, neighborVertex);
						unseen.offer(new Edge(move, neighborVertex));
					}
				}
				return unseen;
			}
			return null;
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

	public void clear() {
		this.vertices.clear();
		this.solutions.clear();
	}

	protected void addVertex(Board board) {
		if(this.getVertex(board) == null) {
			this.vertices.put(board.hash(), new Vertex(board));
		}
	}

}
