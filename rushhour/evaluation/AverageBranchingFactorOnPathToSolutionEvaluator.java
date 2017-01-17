package rushhour.evaluation;

import rushhour.core.*;

import java.util.ArrayList;

public class AverageBranchingFactorOnPathToSolutionEvaluator implements Evaluator {
	public double eval(Board b) {
		BoardGraph graph = new BoardGraph(b);
		int totalBranchingFactor = 0;
		ArrayList<Vertex> path = graph.solve(graph.getVertex(b));
		for(Vertex v : path) {
			totalBranchingFactor += v.getNeighbors().size();
		}
		return (double)totalBranchingFactor/path.size();
	}
}

