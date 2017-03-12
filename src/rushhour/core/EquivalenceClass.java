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

public class EquivalenceClass extends DepthGraph {

	public EquivalenceClass(Board startingBoard) {
		super();
		this.maxDepth = -1;
		Vertex startingVertex = new Vertex(startingBoard);
		this.vertices.put(startingBoard.hash(), startingVertex);
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.offer(startingVertex);
		while(!queue.isEmpty()) {
			Vertex current = queue.poll();
			// check if it's a solution
			if(current.board.isSolved()) {
				current.depth = 0;
				this.maxDepth = 0;
				this.solutions.add(current.board.hash());
			}
			// knit its neighbors into the graph
			LinkedList<Vertex> newVertices = current.expand();
			// and add the unseen neighbors to the queue
			for(Vertex newVertex : newVertices) {
				queue.offer(newVertex);
			}
		}
	}

	public long hash() {
		return Collections.min(this.vertices.keySet());
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

}

