package rushhour.solving;

import rushhour.core.Board;
import rushhour.core.Move;

import java.util.List;

public class SearchNode {
	public Board board;
	public Board parent;
	public Move move;
	public List<Board> neighbors;
	public SearchNode(Board board) {
		this(board, null, null);
	}
	public SearchNode(Board board, Board parent, Move move) {
		this.board = board;
		this.parent = parent;
		this.move = move;
	}
}
