package rushhour.solving;

import rushhour.core.*;

import java.util.List;

public class EquivalenceClassSolver implements Solver {
	public SolveResult getSolution(Board board) {
		EquivalenceClass graph = new EquivalenceClass(board);
		if(graph.maxDepth() > -1) {
			List<Move> path = graph.movesToNearestSolution(board);
			Board solvedBoard = board.copy();
			for(Move m : path) {
				solvedBoard.move(m);
			}
			return new SolveResult(path, solvedBoard, graph.size());
		} else {
			return null;
		}
	}
}
