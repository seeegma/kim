package rushhour.core;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;

public class BoardGraph {

	public static class Vertex {
		public Board board;
		public Set<Vertex> neighbors;
		public int depth;
		public Vertex parent;
		public Vertex(Board board) {
			this.board = board;
			this.parent = null;
			this.depth = 0;
			this.neighbors = null;
		}
	}

	public static class VertexComparator implements Comparator<Vertex> {
		public int compare(Vertex v1, Vertex v2) {
			return v1.depth - v2.depth;
		}
	}

	HashMap<Long,Vertex> vertices;

	public int depth; //Maximum distance from a solved state of any board in the graph.

	int numberOfSolvedStates;

	// Linked list of solution vertices.
	LinkedList<Vertex> solutions;

	public BoardGraph(Board startingBoard) {
		this.vertices = new HashMap<Long,Vertex>();
		this.numberOfSolvedStates = 0;
		this.solutions = null;
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
			current.neighbors = new HashSet<Vertex>();
			// fill in neighbors with vertices already in the graph
			Map<Move,Board> currentPossibleMoves = current.board.allPossibleMoves();
			Set<Move> moves = currentPossibleMoves.keySet();
			for (Move move : moves) {
				Board neighborBoard = currentPossibleMoves.get(move);
				// If the vertex exists in the graph, replace current's instance with the graph's
				if (this.vertices.containsKey(neighborBoard.hash())) {
					current.neighbors.add(this.vertices.get(neighborBoard.hash()));
				}
				// otherwise add current's instance to the graph
				else {
					Vertex neighborVertex = new Vertex(neighborBoard);
					this.vertices.put(neighborBoard.hash(), neighborVertex);
					current.neighbors.add(neighborVertex);
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
		this.solutions = new LinkedList<Vertex>();
		for(Vertex vert : vertices.values()) {
			numberOfVisitedStates++;
			if (vert.board.isSolved()) {
				vert.depth = 0;
				solvedStates++;
				queue.offer(vert);
				visited.add(vert);
				solutions.add(vert);
			}
		}
		while (!queue.isEmpty()) {
			Vertex current = queue.poll();
			numberOfVisitedStates--;
			Set<Vertex> neighbors = current.neighbors;
			for (Vertex neighbor : neighbors) {
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
		this.numberOfSolvedStates = solvedStates;
		this.depth = maxDepth;
	}

	public Vertex getVertex(Board b) {
		return this.vertices.get(b.hash());
	}

	public HashMap<Long,Vertex> getVertices() {
		return this.vertices;
	}

	/**
	 * Returns one of the Boards that is the farthest distance from any
	 * solution.
	 * @return a Board that is the farthest from any solution.
	 */
	public Board getFarthest() {
		for (Vertex vert : vertices.values()) {
			if (vert.depth == this.depth) {
				return vert.board;
			}
		}
		return null;
	}

	public int getDepth(Board b) {
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
			Map<Move,Board> possibleMoves = v.board.allPossibleMoves();
			for (Move move : possibleMoves.keySet()) {
				Vertex neighbor = this.getVertex(possibleMoves.get(move));
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
			Set<Vertex> neighbors = current.neighbors;
			for (Vertex neighbor : neighbors) {
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
