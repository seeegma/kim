package rushhour.solving;

import rushhour.core.Board;

public class SolvedFeature implements Feature {
	public double value(Board board) {
		return board.isSolved() ? 0 : 1;
	}
	public String toString() {
		return "solved";
	}
}
