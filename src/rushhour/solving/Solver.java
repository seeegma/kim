package rushhour.solving;

import rushhour.core.*;

import java.util.List;

public abstract class Solver {
	public abstract List<Move> getSolution(Board board);
	public Board solve(Board board) {
		for(Move move : this.getSolution(board)) {
			board.move(move);
		}
		return board;
	}
}
