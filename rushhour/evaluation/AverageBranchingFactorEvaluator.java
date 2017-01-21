package rushhour.evaluation;

import rushhour.core.*;

public class AverageBranchingFactorEvaluator implements Evaluator {
	public double eval(Board b, BoardGraph g) {
		int totalBranchingFactor = 0;
		for(BoardGraph.Vertex v : g.getVertices().values()) {
			totalBranchingFactor += v.neighbors.size();
		}
		return (double)totalBranchingFactor/g.getVertices().size();
	}
	public String description() {
		return "average branching factor";
	}
}
