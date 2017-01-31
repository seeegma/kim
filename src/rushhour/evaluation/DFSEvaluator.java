package rushhour.evaluation;

import rushhour.core.*;
import rushhour.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collections;
import java.util.LinkedList;

/**
 * This evaluator runs a DFS search, randomly adding neighbors
 * to the stack, but not visiting a state more than once. This
 * is meant to simulate a human player playing randomly. 
 */

public class DFSEvaluator implements Evaluator {

	/**
     * Returns the score using this evaluation method.
     * @param b the board to evaluate
     * @return the score
     */
	public double eval(Board b) {
		BoardGraph graph = b.getGraph();
		int numTrials = 500;
		double count = 0;
		for(int trial = 0;trial<numTrials;trial++) {
			LinkedList<BoardGraph.Vertex> stack = new LinkedList<BoardGraph.Vertex>();
			HashSet<BoardGraph.Vertex> visited = new HashSet<BoardGraph.Vertex>();
			stack.push(graph.getVertex(b));
			while(!stack.isEmpty()) {
				BoardGraph.Vertex current = stack.pop();
				if (current.depth == 0) {
					break;
				}
				count++;
				List<BoardGraph.Vertex> neighbors = new ArrayList<BoardGraph.Vertex>(current.neighbors);
				Collections.shuffle(neighbors);
				for (BoardGraph.Vertex neighbor : neighbors) {
					if (!visited.contains(neighbor)) {
						stack.push(neighbor);
					}
				}
			}
		}
		return count/numTrials;
	}
	public String description() {
		return "depth-first search evaluator";
	}
}
