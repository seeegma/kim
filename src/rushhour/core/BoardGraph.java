package rushhour.core;

import rushhour.io.AsciiGen; // for debugging

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;

public class BoardGraph {

	HashMap<Long,Vertex> vertices;
	int maxDepth; // Maximum distance from a solved state of any board in the graph
	Set<Vertex> solutions;

	public class Vertex {
		public Board board;
		public Set<Vertex> neighbors;
		public int depth;
		public Vertex(Board board) {
			this.board = board;
			this.depth = -1;
			this.neighbors = null;
		}
	}

	public static class MaxDepthComparator implements Comparator<Vertex> {
		public int compare(Vertex v1, Vertex v2) {
			return v1.depth - v2.depth;
		}
	}

	public static class MinDepthComparator implements Comparator<Vertex> {
		public int compare(Vertex v1, Vertex v2) {
			return v2.depth - v1.depth;
		}
	}

	public BoardGraph(Board startingBoard) {
		this.vertices = new HashMap<Long,Vertex>();
		this.solutions = new HashSet<Vertex>();
		Vertex startingVertex = new Vertex(startingBoard);
		this.vertices.put(startingBoard.hash(), startingVertex);
		if(startingBoard.isSolved()) {
			this.solutions.add(startingVertex);
		}
		this.maxDepth = -1;
		this.fillEquivalenceClass();
	}

	public void fillEquivalenceClass() {
		// pick a random vertex to start with
		Vertex startingVertex = null;
		for(Long hash : this.vertices.keySet()) {
			startingVertex = this.vertices.get(hash);
			break;
		}
		// fill out the equivalence class starting from that vertex
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.offer(startingVertex);
		while (!queue.isEmpty()) {
			Vertex current = queue.poll();
			current.neighbors = new HashSet<Vertex>();
			// fill in neighbors with vertices already in the graph
			Set<Move> moves = current.board.allPossibleMoves();
			for (Move move : moves) {
				Board neighborBoard = current.board.getNeighborBoard(move);
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
		// propogate depth values and neighbor pointers
		queue = new LinkedList<Vertex>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		this.solutions = new HashSet<Vertex>();
		for(Vertex vert : vertices.values()) {
			if (vert.board.isSolved()) {
				vert.depth = 0;
				queue.offer(vert);
				visited.add(vert);
				solutions.add(vert);
			}
		}
		while (!queue.isEmpty()) {
			Vertex current = queue.poll();
			Set<Vertex> neighbors = current.neighbors;
			for (Vertex neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					neighbor.depth = current.depth + 1;
					if (this.maxDepth < neighbor.depth) {
						this.maxDepth = neighbor.depth;
					}
					visited.add(neighbor);
					queue.offer(neighbor);
				}
			}
		}
	}

	public int maxDepth() {
		if(this.maxDepth != -1) {
			return this.maxDepth;
		}
		// bfs for vertices
		Queue<Vertex> queue = new LinkedList<>();
		// pick a random vertex
		Vertex vertex = null;
		for(Vertex v : this.vertices.values()) {
			vertex = v;
			break;
		}
		queue.offer(vertex);
		Set<Long> visited = new HashSet<>();
		while(!queue.isEmpty()) {
			vertex = queue.poll();
			System.out.println(vertex.depth + ", " + maxDepth);
			AsciiGen.printGrid(vertex.board.getGrid());
			for(Vertex neighbor : vertex.neighbors) {
				if(!visited.contains(neighbor.board.hash()) && neighbor.depth >= vertex.depth) {
					queue.offer(neighbor);
					if(neighbor.depth > maxDepth) {
						maxDepth = neighbor.depth;
					}
				}
			}
			visited.add(vertex.board.hash());
		}
		return this.maxDepth;
	}

	public Vertex getVertex(Board b) {
		return this.vertices.get(b.hash());
	}

	public HashMap<Long,Vertex> getVertices() {
		return this.vertices;
	}

	/**
	 * Number of boards in this graph.
	 * @return size of graph
	 */
	public int size() {
		return this.vertices.size();
	}

	public int getDepth(Board b) {
		return this.getVertex(b).depth;
	}

	public List<Move> movesToNearestSolution(Board b) {
		Vertex v = this.getVertex(b);
		List<Move> moves = new ArrayList<Move>();
		Vertex current = v;
		while(!current.board.isSolved()) {
			Set<Move> possibleMoves = v.board.allPossibleMoves();
			for(Move move : possibleMoves) {
				Vertex neighbor = this.getVertex(v.board.getNeighborBoard(move));
				if(neighbor.depth == current.depth - 1) {
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
		while(current.depth != 0) {
			Set<Vertex> neighbors = current.neighbors;
			for(Vertex neighbor : neighbors) {
				if(neighbor.depth == current.depth - 1) {
					path.add(neighbor);
					current = neighbor;
					break;
				}
			}
		}
		return path;
	}


}
