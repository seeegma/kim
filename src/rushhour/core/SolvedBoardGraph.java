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

	private SolvedBoardGraph(Board solvedBoard) {
		this.addSolutions(solvedBoard);
	}

	public static SolvedBoardGraph create(Board solvedBoard) {
		if(!solvedBoard.isSolved()) {
			return null;
		}
		return new SolvedBoardGraph(solvedBoard);
	}

	private void addSolutions(Board solvedBoard) {
		Vertex source = new Vertex(solvedBoard);
		this.vertices.put(solvedBoard.hash(), source);
		this.solutions.add(source.board.hash());
		// find rest of solutions within this connected component
		LinkedList<Vertex> queue = new LinkedList<>();
		queue.offer(source);
		while(!queue.isEmpty()) {
			Vertex current = queue.poll();
			current.depth = 0;
			current.neighbors = new HashMap<>();
			for(Move move : current.board.allPossibleMoves()) {
				Board neighborBoard = current.board.getNeighborBoard(move);
				if(neighborBoard.isSolved()) {
					this.solutions.add(neighborBoard.hash());
					if(this.vertices.containsKey(neighborBoard.hash())) {
						// if the vertex exists in the graph, replace current's instance with the graph's
						current.neighbors.put(move, this.vertices.get(neighborBoard.hash()));
					} else {
						// otherwise create a new vertex
						Vertex neighborVertex = new Vertex(neighborBoard);
						this.vertices.put(neighborBoard.hash(), neighborVertex);
						current.neighbors.put(move, neighborVertex);
						queue.offer(neighborVertex);
					}
				}
			}
		}
	}

	public void propogateDepths(int toDepth) {
		LinkedList<Vertex> queue = new LinkedList<>();
		boolean again;
		do {
			again = false;
			this.maxDepth = 0;
			queue.clear();
			for(long hash : this.solutions) {
				Vertex vertex = this.vertices.get(hash);
				queue.offer(vertex);
				vertex.depth = 0;
			}
mainloop:	while(!queue.isEmpty()) {
				Vertex current = queue.poll();
				if(current.depth < toDepth || toDepth == -1) {
					for(Move move : current.board.allPossibleMoves()) {
						Board neighborBoard = current.board.getNeighborBoard(move);
						Vertex neighborVertex = new Vertex(neighborBoard);
						if(!this.vertices.containsKey(neighborBoard.hash())) {
							// check if it's in a new connected component of solutions
							if(neighborBoard.isSolved()) {
								// if it's not, then we need to add this component and start over
								again = true;
								this.addSolutions(neighborBoard);
								break mainloop;
							}
							this.vertices.put(neighborBoard.hash(), neighborVertex);
							// propogate depth
							neighborVertex.depth = current.depth + 1;
							// update maxDepth and farthest
							if(this.maxDepth < neighborVertex.depth) {
								this.maxDepth = neighborVertex.depth;
								System.err.print(maxDepth);
								this.farthest = neighborVertex.board;
							}
							queue.offer(neighborVertex);
						}
					}
				}
			}
		} while(again);
	}

	public int maxDepth() {
		return this.maxDepth;
	}

	public int getDepthOfBoard(Board b) {
		return this.getVertex(b).depth;
	}

	public Board getFarthest() {
		return this.farthest;
	}

}

