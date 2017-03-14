package rushhour.solving;

import rushhour.core.*;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;

public class AStarSearchSolver extends BoardGraph implements Solver {

	private NeighborComparator comparator;

	protected class NeighborComparator implements Comparator<SearchNode> {

		private Heuristic heuristic;

		protected NeighborComparator(Heuristic heuristic) {
			this.heuristic = heuristic;
		}

		private double value(SearchNode node) {
			return node.depth + this.heuristic.value(node.board);
		}

		public int compare(SearchNode node1, SearchNode node2) {
			double diff = this.value(node1) - this.value(node2);
			if(diff < 0) {
				return -1;
			} else if(diff > 0) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	public AStarSearchSolver(Heuristic heuristic) {
		super();
		this.comparator = new NeighborComparator(heuristic);
	}

	public SolveResult getSolution(Board board) {
		this.clear();
		this.addVertex(board);
		HashSet<Long> visited = new HashSet<>();
		PriorityQueue<SearchNode> queue = new PriorityQueue<SearchNode>(100, this.comparator);
		queue.offer(new SearchNode(this.getVertex(board)));
		int statesVisited = 0;
		while(!queue.isEmpty()) {
			SearchNode current = queue.poll();
			if(visited.contains(current.board.hash())) {
				continue;
			}
			statesVisited++;
			visited.add(current.board.hash());
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
