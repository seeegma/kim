package rushhour.solving;

import rushhour.core.Board;
import rushhour.core.Move;
import rushhour.core.BoardGraph;

import java.util.List;
import java.util.LinkedList;

public class SearchNode {

	public BoardGraph.Vertex vertex;
	public Board board; // use one or the other
	public SearchNode parent;
	public Move move;
	public int depth;

	public SearchNode(BoardGraph.Vertex vertex) {
		this(vertex, null, null);
	}

	public SearchNode(Board board) {
		this(board, null, null);
	}

	public SearchNode(Board board, SearchNode parent, Move move) {
		this.board = board;
		this.setDepthAndMove(parent, move);
	}

	public SearchNode(BoardGraph.Vertex vertex, SearchNode parent, Move move) {
		this.vertex = vertex;
		this.setDepthAndMove(parent, move);
	}

	private void setDepthAndMove(SearchNode parent, Move move) {
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
