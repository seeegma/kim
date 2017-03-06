package rushhour.solving;

import rushhour.core.*;

import java.util.List;

public class EquivalenceClassSolver implements Solver {
	public List<Move> solve(Board board) {
		EquivalenceClass graph = new EquivalenceClass(board);
		if(graph.maxDepth() > -1) {
			return graph.movesToNearestSolution(board);
		} else {
			return null;
		}
	}
}
