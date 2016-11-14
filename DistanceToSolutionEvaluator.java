

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
		return b.getGraph().getVertex(b).depth;
	}

	public static void main(String[] args) {
		Board board = BoardIO.read("Puzzle/16moves98");
		//Board board = BoardIO.read("simplePuzzle");
		DistanceToSolutionEvaluator evaler = new DistanceToSolutionEvaluator();
		System.out.println(evaler.eval(board));
	}
}