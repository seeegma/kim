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

public class BoardGraph {

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
		while(!queue.isEmpty()) {
			Vertex current = queue.poll();
			current.neighbors = new HashMap<Move,Vertex>();
			// fill in neighbors with vertices already in the graph
			for(Move move : current.board.allPossibleMoves()) {
				Board neighborBoard = current.board.getNeighborBoard(move);
				// If the vertex exists in the graph, replace current's instance with the graph's
				if(this.vertices.containsKey(neighborBoard.hash())) {
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
		int maxDepth = -1;
		int solvedStates = 0;
		queue = new LinkedList<Vertex>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		for(Vertex vert : vertices.values()) {
			if(vert.board.isSolved()) {
				vert.depth = 0;
				maxDepth = 0;
				queue.offer(vert);
				visited.add(vert);
				this.solutions.add(vert);
			}
		}
		while(!queue.isEmpty()) {
			Vertex current = queue.poll();
			for(Vertex neighbor : current.neighbors.values()) {
				if(!visited.contains(neighbor)) {
					neighbor.depth = current.depth + 1;
					if(maxDepth < neighbor.depth) {
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
		while(current.depth != 0) {
			for(Move move : current.neighbors.keySet()) {
				Vertex neighbor = current.neighbors.get(move);
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

	/**
	 * Uses a dumb walk starting from the first soln vertex to find a Board with
	 * maxDepth.
	 */
	public Board getFarthest() {
		if (this.solutions.size() == 0) {
			return null;
		}
		// takes element from set and sets up pq
		Vertex soln = solutions.iterator().next();
		PriorityQueue<Vertex> pq = new PriorityQueue<>();
		Set<Long> visited = new HashSet<>();
		visited.add(soln.board.hash());
		pq.add(soln);
		// "random" walk
		while(!pq.isEmpty()) {
			Vertex current = pq.poll();
			if(current.depth == this.maxDepth) {
				return current.board;
			}
			for(Vertex v : current.neighbors.values()) {
				if(!visited.contains(v.board.hash())) {
					pq.add(v);
				}
			}
			visited.add(current.board.hash());
		}
		return null;
	}

	public Board getOneBoardCloser(Board board) {
		Vertex v = this.getVertex(board);
		for(Vertex neighbor : v.neighbors.values()) {
			if(neighbor.depth < v.depth) {
				return neighbor.board;
			}
		}
		return null;
	}

	public static boolean hasMinDepth(Board board, int minDepth) {
		if(!board.isSolved()) {
			return false;
		}
		Vertex source = new Vertex(board);
		source.depth = 0;
		Map<Long,Integer> visitedDepths = new HashMap<Long,Integer>();
		LinkedList<Vertex> queue = new LinkedList<>();
		visitedDepths.put(board.hash(),0);
		queue.offer(source);
		// run DFS graph search
		while(!queue.isEmpty()) {
			Vertex cur = queue.poll();
			if(cur.depth == minDepth) {
				return true;
			}
			for(Vertex neighbor : cur.neighbors.values()) {
				if(visitedDepths.containsKey(neighbor.board.hash())) {
					continue;
				}
				neighbor.depth = cur.depth + 1;
				queue.offer(neighbor);
			}
		}
		return false;

	}

}
