package rushhour.evaluation;

import rushhour.core.*;

public class AverageBranchingFactorEvaluator implements Evaluator {
	public double eval(Board b) {
		int totalBranchingFactor = 0;
		for(BoardGraph.Vertex v : b.getGraph().getVertices().values()) {
			totalBranchingFactor += v.neighbors.size();
		}
		return (double)totalBranchingFactor/b.getGraph().getVertices().size();
	}
	public String description() {
		return "average branching factor";
	}
}
