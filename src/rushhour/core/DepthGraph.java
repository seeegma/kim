package rushhour.core;

import java.util.LinkedList;

public abstract class DepthGraph extends BoardGraph {

	protected int maxDepth;
	protected Board farthest;

	public int maxDepth() {
		return this.maxDepth;
	}

	public int getDepthOfBoard(Board b) {
		return this.getVertex(b).depth;
	}

	public Board getFarthest() {
		return this.farthest;
	}

	protected void addSolutions(Board solvedBoard) {
		System.err.print("-,");
		Vertex source = new Vertex(solvedBoard);
		this.vertices.put(solvedBoard.hash(), source);
		this.solutions.add(source.board.hash());
		// find rest of solutions within this connected component
		LinkedList<Vertex> queue = new LinkedList<>();
		queue.offer(source);
		while(!queue.isEmpty()) {
			Vertex current = queue.poll();
			current.depth = 0;
			LinkedList<Vertex> newVertices = current.expand();
			if(newVertices == null) {
				continue;
			}
			for(Vertex newVertex : newVertices) {
				Board neighborBoard = newVertex.board;
				if(neighborBoard.isSolved()) {
					this.solutions.add(neighborBoard.hash());
					queue.offer(newVertex);
				}
			}
		}
	}

	public void propogateDepths(int toDepth) {
		System.err.print("0,");
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
								this.addSolutions(neighborBoard);
								break mainloop;
							}
							// propogate depth
							neighborVertex.depth = current.depth + 1;
							// update maxDepth and farthest
							if(this.maxDepth < neighborVertex.depth) {
								this.maxDepth = neighborVertex.depth;
								this.farthest = neighborVertex.board;
								System.err.print(this.maxDepth + ",");
							}
							queue.offer(neighborVertex);
						}
					}
				}
			}
		} while(again);
	}

	// from equivalenceclass constructor
	// public void propogateDepths(int toDepth) {
	// 	// propogate depths outward from any solved states
	// 	// (first add all solutions to the queue and visited set)
	// 	HashSet<Long> visited = new HashSet<>();
	// 	for(long hash : this.solutions) {
	// 		queue.offer(this.vertices.get(hash));
	// 		visited.add(hash);
	// 	}
	// 	while(!queue.isEmpty()) {
	// 		Vertex current = queue.poll();
	// 		for(Vertex neighbor : current.neighbors.values()) {
	// 			if(!visited.contains(neighbor.board.hash())) {
	// 				neighbor.depth = current.depth + 1;
	// 				if(this.maxDepth < neighbor.depth) {
	// 					this.maxDepth = neighbor.depth;
	// 					this.farthest = neighbor.board;
	// 				}
	// 				visited.add(neighbor.board.hash());
	// 				queue.offer(neighbor);
	// 			}
	// 		}
	// 	}
	// }
    //
    //
}
