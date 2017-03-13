package rushhour.solving;

import rushhour.core.*;

import java.util.List;
import java.util.LinkedList;

public class BreadthFirstSearchSolver extends BoardGraph implements Solver {
	public SolveResult getSolution(Board board) {
		this.clear();
		this.addVertex(board);
		LinkedList<SearchNode> queue = new LinkedList<>();
		queue.offer(new SearchNode(this.getVertex(board)));
		while(!queue.isEmpty()) {
			SearchNode current = queue.poll();
			if(current.vertex.board.isSolved()) {
				// construct list from node tree
				return new SolveResult(current.getPath(), current.vertex.board, this.size());
			}
			for(Edge edge : current.vertex.expand()) {
				queue.offer(new SearchNode(edge.vertex, current, edge.move));
			}
		}
		return null;
	}
}
