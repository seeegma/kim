import java.util.ArrayList;
import java.util.HashSet;
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
		int numTrials = 1;
		double count = 0;
		for(int trial = 0;trial<numTrials;trial++) {
			LinkedList<Vertex> stack = new LinkedList<Vertex>();
			HashSet<Vertex> visited = new HashSet<Vertex>();
			stack.push(b.getGraph().getVertex(b));
			while(!stack.isEmpty()) {
				Vertex current = stack.pop();
				if (current.depth == 0) {
					break;
				}
				count++;
				ArrayList<Vertex> neighbors = new ArrayList<Vertex>(current.neighbors);
				Collections.shuffle(neighbors);
				for (Vertex neighbor : neighbors) {
					if (!visited.contains(neighbor)) {
						stack.push(neighbor);
					}
				}
			}
		}
		return count/numTrials;
	}

	public static void main(String[] args) {
		Board board = BoardIO.read("Puzzle/16moves98");
		//Board board = BoardIO.read("simplePuzzle");
		DFSEvaluator evaler = new DFSEvaluator();
		for (int i = 0; i<100;i++){
		System.out.println(evaler.eval(board));
		}
	}
}