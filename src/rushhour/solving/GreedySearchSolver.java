package rushhour.solving;

import rushhour.core.*;

import java.util.PriorityQueue;

public class GreedySearchSolver extends BoardGraph implements Solver {

	private Heuristic heuristic;

	public GreedySearchSolver(Heuristic heuristic) {
		super();
		this.heuristic = heuristic;
	}

	public SolveResult getSolution(Board board) {
		this.clear();
		this.addVertex(board);
		PriorityQueue<SearchNode> queue = new PriorityQueue<SearchNode>(100, this.heuristic);
		queue.offer(new SearchNode(this.getVertex(board)));
		int statesVisited = 0;
		while(!queue.isEmpty()) {
			SearchNode current = queue.poll();
			statesVisited++;
			if(current.vertex.board.isSolved()) {
				// construct list from node tree
				return new SolveResult(current.getPath(), current.vertex.board, statesVisited);
			}
			for(Edge edge : current.vertex.expand()) {
				queue.offer(new SearchNode(edge.vertex, current, edge.move));
			}
		}
		return null;
	}
}
