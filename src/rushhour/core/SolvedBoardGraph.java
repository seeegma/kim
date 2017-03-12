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

public class SolvedBoardGraph extends DepthGraph {

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
		source.expand();
		this.vertices.put(solvedBoard.hash(), source);
		this.solutions.add(source.board.hash());
		// find rest of solutions within this connected component
		LinkedList<Vertex> queue = new LinkedList<>();
		queue.offer(source);
		while(!queue.isEmpty()) {
			// if(this.vertices.size() % 10000 == 0) {
			// 	System.err.print(this.vertices.size() + ",");
			// }
			Vertex current = queue.poll();
			current.depth = 0;
			LinkedList<Vertex> newVertices = current.expand();
			if(newVertices == null) {
				continue;
			}
			System.err.print("okokok");
			for(Vertex newVertex : newVertices) {
				Board neighborBoard = newVertex.board;
				if(neighborBoard.isSolved()) {
					this.solutions.add(neighborBoard.hash());
					queue.offer(newVertex);
				}
			}
		}
		System.err.print("; ");
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
					// knit its neighbors into the graph
					current.expand();
					// then propogate the depths outward from current
					for(Vertex neighborVertex : current.neighbors.values()) {
						Board neighborBoard = neighborVertex.board;
						if(neighborVertex.depth == -2) {
							// check if it's in a new connected component of solutions
							if(neighborBoard.isSolved()) {
								// if it's not, then we need to add this component and start over
								again = true;
								System.err.print(";");
								this.addSolutions(neighborBoard);
								break mainloop;
							}
							// propogate depth
							neighborVertex.depth = current.depth + 1;
							// update maxDepth and farthest
							if(this.maxDepth < neighborVertex.depth) {
								this.maxDepth = neighborVertex.depth;
								System.err.print(maxDepth + ",");
								this.farthest = neighborVertex.board;
							}
							queue.offer(neighborVertex);
						}
					}
				}
			}
		} while(again);
	}

}

