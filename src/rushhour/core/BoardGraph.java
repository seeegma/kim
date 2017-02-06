package rushhour.core;

import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

public class BoardGraph {

	public class Vertex {
		public Board board;
		public HashMap<Move,Vertex> neighbors;
		public int depth;
		public Vertex parent;
		public Vertex(Board board) {
			this.board = board;
			this.parent = null;
			this.depth = -1;
			this.neighbors = null;
		}
	}

	HashMap<Long,Vertex> vertices;
	int maxDepth;
	Set<Vertex> solutions;

	public BoardGraph(Board startingBoard) {
		this.vertices = new HashMap<Long,Vertex>();
		this.solutions = new HashSet<>();
		this.fillEquivalenceClass(startingBoard);
	}

	public void fillEquivalenceClass(Board startingBoard) {
		Vertex startingVertex;
		if(this.getVertex(startingBoard) != null) {
			startingVertex = this.getVertex(startingBoard);
		} else {
			startingVertex = new Vertex(startingBoard);
		}
		this.vertices.put(startingBoard.hash(), startingVertex);
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.offer(startingVertex);
		while (!queue.isEmpty()) {
			Vertex current = queue.poll();
			current.neighbors = new HashMap<Move,Vertex>();
			// fill in neighbors with vertices already in the graph
			for (Move move : current.board.allPossibleMoves()) {
				Board neighborBoard = current.board.getNeighborBoard(move);
				// If the vertex exists in the graph, replace current's instance with the graph's
				if (this.vertices.containsKey(neighborBoard.hash())) {
					current.neighbors.put(move, this.vertices.get(neighborBoard.hash()));
				}
				// otherwise add current's instance to the graph
				else {
					Vertex neighborVertex = new Vertex(neighborBoard);
					this.vertices.put(neighborBoard.hash(), neighborVertex);
					current.neighbors.put(move, neighborVertex);
					queue.offer(neighborVertex);
				}

			}
		}
		// propogate depth values and graph pointers
		int numberOfVisitedStates = 0;
		int maxDepth = 0;
		int solvedStates = 0;
		queue = new LinkedList<Vertex>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		for(Vertex vert : vertices.values()) {
			if (vert.board.isSolved()) {
				vert.depth = 0;
				queue.offer(vert);
				visited.add(vert);
				this.solutions.add(vert);
			}
		}
		while (!queue.isEmpty()) {
			Vertex current = queue.poll();
			Set<Move> moves = current.neighbors.keySet();
			for (Move move : moves) {
				Vertex neighbor = current.neighbors.get(move);
				if (!visited.contains(neighbor)) {
					neighbor.depth = current.depth + 1;
					if (maxDepth<neighbor.depth) {
						maxDepth = neighbor.depth;
					}
					visited.add(neighbor);
					queue.offer(neighbor);
				}
			}
		}
		this.maxDepth = maxDepth;
	}

	public Vertex getVertex(Board b) {
		return this.vertices.get(b.hash());
	}

	public HashMap<Long,Vertex> getVertices() {
		return this.vertices;
	}

	public int maxDepth() {
		return this.maxDepth;
	}

	public long hash() {
		return Collections.min(this.vertices.keySet());
	}

	public int numSolutions() {
		return this.solutions.size();
	}

	public int getDepthOfBoard(Board b) {
		return getVertex(b).depth;
	}

	/**
	 * Number of boards in this graph.
	 * @return size of graph
	 */
	public int size() {
		return vertices.size();
	}

	public List<Move> movesToNearestSolution(Board b) {
		Vertex v = this.getVertex(b);
		List<Move> moves = new ArrayList<Move>();
		Vertex current = v;
		while (current.depth != 0) {
			Set<Move> neighborMoves = current.neighbors.keySet();
			for (Move move : neighborMoves) {
				Vertex neighbor = current.neighbors.get(move);
				if (neighbor.depth == current.depth - 1) {
					moves.add(move);
					current = neighbor;
					break;
				}
			}
		}
		return moves;
	}

	public List<Vertex> pathToNearestSolution(Board b) {
		Vertex v = this.getVertex(b);
		List<Vertex> path = new ArrayList<Vertex>();
		Vertex current = v;
		while (current.depth != 0) {
			Set<Move> neighborMoves = current.neighbors.keySet();
			for (Move move : neighborMoves) {
				Vertex neighbor = current.neighbors.get(move);
				if (neighbor.depth == current.depth - 1) {
					path.add(neighbor);
					current = neighbor;
					break;
				}
			}
		}
		return path;
	}

}
