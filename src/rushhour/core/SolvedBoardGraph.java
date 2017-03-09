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

public class SolvedBoardGraph extends BoardGraph {

	private int maxDepth;
	private Board farthest;

	protected SolvedBoardGraph(Board solvedBoard) {
		// start with a solved board
		if(!solvedBoard.isSolved()) {
			this.farthest = null;
			this.maxDepth = -2;
			return;
		}
		Vertex source = new Vertex(solvedBoard);
		this.vertices.put(solvedBoard.hash(), source);
		this.solutions.add(source.board.hash());
		// find rest of solutions
		LinkedList<Vertex> queue = new LinkedList<>();
		queue.offer(source);
		while(!queue.isEmpty()) {
			Vertex current = queue.poll();
			current.neighbors = new HashMap<>();
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
					if(neighborVertex.board.isSolved()) {
						this.solutions.add(neighborVertex.board.hash());
						// ignore unsolved neighbors
						queue.offer(neighborVertex);
					}
				}
			}
		}
	}

	public static SolvedBoardGraph create(Board solvedBoard) {
		if(!solvedBoard.isSolved()) {
			return null;
		}
		return new SolvedBoardGraph(solvedBoard);
	}

	public void propogateDepths(int toDepth) {
		LinkedList<Vertex> queue;
		if(this.solutions.isEmpty()) {
			this.maxDepth = -1;
			return;
		}
		this.maxDepth = 0;
		queue = new LinkedList<>();
		for(long hash : this.solutions) {
			Vertex solvedVertex = this.vertices.get(hash);
			solvedVertex.depth = 0;
			queue.offer(solvedVertex);
		}
		while(!queue.isEmpty()) {
			Vertex current = queue.poll();
			if(toDepth != -1 && current.depth < toDepth) {
				for(Move move : current.board.allPossibleMoves()) {
					Board neighborBoard = current.board.getNeighborBoard(move);
					Vertex neighborVertex = new Vertex(neighborBoard);
					if(neighborVertex.depth == -2) {
						// propogate depth
						neighborVertex.depth = current.depth + 1;
						// update maxDepth and farthest
						if(this.maxDepth < neighborVertex.depth) {
							this.maxDepth = neighborVertex.depth;
							this.farthest = neighborVertex.board;
						}
						queue.offer(neighborVertex);
					}
				}
			}
		}
	}

	public int maxDepth() {
		return this.maxDepth;
	}

	public Board getFarthest() {
		return this.farthest;
	}

}

