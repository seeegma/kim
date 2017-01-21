package rushhour.evaluation;

import rushhour.core.*;

/**
 * This evaluator finds the minimum number of moves
 * to a solution. 
 */

public class MinMovesToSolutionEvaluator implements Evaluator {

	/**
     * Returns the score using this evaluation method.
     * @param b the board to evaluate
     * @return the score
     */
	public double eval(Board b, BoardGraph g) {
		return g.getDepth(b);
	}

	public String description() {
		return "minimum moves to solution";
	}
	
}
