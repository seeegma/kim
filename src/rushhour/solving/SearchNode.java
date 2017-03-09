package rushhour.solving;

import rushhour.core.Board;
import rushhour.core.Move;

import java.util.List;
import java.util.LinkedList;

public class SearchNode {

	public Board board;
	public SearchNode parent;
	public Move move;
	public int depth;

	public SearchNode(Board board) {
		this(board, null, null);
	}

	public SearchNode(Board board, SearchNode parent, Move move) {
		this.board = board;
		if(parent == null) {
			this.parent = null;
			this.move = null;
			this.depth = 0;
		} else {
			this.parent = parent;
			this.depth = this.parent.depth + 1;
			this.move = move;
		}
	}

	public List<Move> getPath() {
		List<Move> ret = new LinkedList<>();
		SearchNode cur = this;
		while(cur.move != null) {
			ret.add(0, cur.move);
			cur = cur.parent;
		}
		return ret;
	}

}
