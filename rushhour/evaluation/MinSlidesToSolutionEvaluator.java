package rushhour.evaluation;

import rushhour.core.*;

import java.util.List;

/**
 * This evaluator finds the minimum number of slides
 * to a solution. 
 */

public class MinSlidesToSolutionEvaluator implements Evaluator {

	/**
     * Returns the score using this evaluation method.
     * @param b the board to evaluate
     * @return the score
     */
	public double eval(Board b) {
		BoardGraph g = new BoardGraph(b);
		List<Move> path = g.pathToNearestSolution(b);
		int slides = 0;
		for(Move m : path) {
			slides += Math.abs(m.amount);
		}
		return (double)slides;
	}

	
}
