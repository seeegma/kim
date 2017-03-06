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

public class EquivalenceClass extends BoardGraph {

	private Board farthest;
	private int maxDepth;

	public EquivalenceClass(Board startingBoard) {
		super();
		Vertex startingVertex = new Vertex(startingBoard);
		this.vertices.put(startingBoard.hash(), startingVertex);
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.offer(startingVertex);
		while(!queue.isEmpty()) {
			Vertex current = queue.poll();
			current.neighbors = new HashMap<Move,Vertex>();
			// fill in neighbors with vertices already in the graph
			for(Move move : current.board.allPossibleMoves()) {
				Board neighborBoard = current.board.getNeighborBoard(move);
				if(this.vertices.containsKey(neighborBoard.hash())) {
					// if the vertex exists in the graph, replace current's instance with the graph's
					current.neighbors.put(move, this.vertices.get(neighborBoard.hash()));
				} else {
					// otherwise add current's instance to the graph
					Vertex neighborVertex = new Vertex(neighborBoard);
					this.vertices.put(neighborBoard.hash(), neighborVertex);
					current.neighbors.put(move, neighborVertex);
					queue.offer(neighborVertex);
				}
			}
		}
		// find solutions TODO: move up there
		this.maxDepth = -1;
		queue = new LinkedList<Vertex>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		for(Vertex vert : vertices.values()) {
			if(vert.board.isSolved()) {
				vert.depth = 0;
				this.maxDepth = 0;
				queue.offer(vert);
				visited.add(vert);
				this.solutions.add(vert.board);
			}
		}
		// propogate depths outward from any solved states
		while(!queue.isEmpty()) {
			Vertex current = queue.poll();
			for(Vertex neighbor : current.neighbors.values()) {
				if(!visited.contains(neighbor)) {
					neighbor.depth = current.depth + 1;
					if(this.maxDepth < neighbor.depth) {
						this.maxDepth = neighbor.depth;
						this.farthest = neighbor.board;
					}
					visited.add(neighbor);
					queue.offer(neighbor);
				}
			}
		}
	}

	public int maxDepth() {
		return this.maxDepth;
	}

	public long hash() {
		return Collections.min(this.vertices.keySet());
	}

	public int getDepthOfBoard(Board b) {
		return getVertex(b).depth;
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

	public List<Board> pathToNearestSolution(Board b) {
		Vertex v = this.getVertex(b);
		List<Board> path = new ArrayList<Board>();
		Vertex current = v;
		while(current.depth != 0) {
			Set<Move> neighborMoves = current.neighbors.keySet();
			for(Move move : neighborMoves) {
				Vertex neighbor = current.neighbors.get(move);
				if(neighbor.depth == current.depth - 1) {
					path.add(neighbor.board);
					current = neighbor;
					break;
				}
			}
		}
		return path;
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

	public Board getFarthest() {
		return this.farthest;
	}

}

