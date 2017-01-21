package rushhour.evaluation;

import rushhour.core.*;

import java.util.List;

public class AverageBranchingFactorOnPathToSolutionEvaluator implements Evaluator {
	public double eval(Board b, BoardGraph g) {
		int totalBranchingFactor = 0;
		List<BoardGraph.Vertex> path = g.pathToNearestSolution(b);
		for(BoardGraph.Vertex v : path) {
			totalBranchingFactor += v.neighbors.size();
		}
		return (double)totalBranchingFactor/path.size();
	}
	public String description() {
		return "average branching factor on path to solution";
	}
}
