package rushhour.core;

public class DepthGraph extends BoardGraph {

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


}
