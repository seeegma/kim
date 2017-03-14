package rushhour.solving;

import rushhour.core.*;

import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;

public class BreadthFirstSearchSolver extends BoardGraph implements Solver {
	public SolveResult getSolution(Board board) {
		this.clear();
		this.addVertex(board);
		HashSet<Long> visited = new HashSet<>();
		LinkedList<SearchNode> queue = new LinkedList<>();
		queue.offer(new SearchNode(this.getVertex(board)));
		while(!queue.isEmpty()) {
			SearchNode current = queue.poll();
			if(visited.contains(current.board.hash())) {
				continue;
			}
			if(current.vertex.board.isSolved()) {
				// construct list from node tree
				return new SolveResult(current.getPath(), current.vertex.board, visited.size());
			}
			visited.add(current.board.hash());
			current.vertex.expand();
			for(Edge edge : current.vertex.neighbors) {
				if(visited.contains(edge.vertex.board.hash())) {
					continue;
				}
				queue.offer(new SearchNode(edge.vertex, current, edge.move));
			}
		}
		return null;
	}
}
