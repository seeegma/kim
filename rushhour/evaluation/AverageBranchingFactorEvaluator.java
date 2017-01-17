package rushhour.evaluation;

import rushhour.core.*;

public class AverageBranchingFactorEvaluator implements Evaluator {
	public double eval(Board b) {
		BoardGraph graph = new BoardGraph(b);
		int totalBranchingFactor = 0;
		for(Vertex v : graph.vertices.values()) {
			totalBranchingFactor += v.getNeighbors().size();
		}
		return (double)totalBranchingFactor/graph.vertices.size();
	}
}
