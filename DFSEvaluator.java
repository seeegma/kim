import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

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
		ArrayList<Vertex> stack = new ArrayList<Vertex>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		int count = 0;
		stack.push(b.getGraph().getVertex(b));
		while(!stack.isEmpty()) {
			Vertex current = stack.pop();
			if (current.depth == 0) {
				break;
			}
			count++;
			ArrayList<Vertex> neighbors = new ArrayList<Vertex>(current.neighbors);
			for (Vertex neighbor : Collections.shuffle(neighbors)) {
				if (!visited.contains(neighbor)) {
					stack.push(neighbor);
				}
			}
		}
		return count;
	}
}