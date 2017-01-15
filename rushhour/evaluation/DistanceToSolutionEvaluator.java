package rushhour.evaluation;

import rushhour.core.*;



/**
 * This evaluator finds the minimum number of moves
 * to a solution. 
 */

public class DistanceToSolutionEvaluator implements Evaluator {

	/**
     * Returns the score using this evaluation method.
     * @param b the board to evaluate
     * @return the score
     */
	public double eval(Board b) {
		return (new BoardGraph(b)).getVertex(b).depth;
	}

	
}
