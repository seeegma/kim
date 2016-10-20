import java.util.ArrayList;

/**
 * 
 */
public class NodeBoard {
	public Board board;
	public NodeBoard parent;
	public int numMoves;
	
	public NodeBoard (Board board, NodeBoard parent, int numMoves) {
		this.board = board;
		this.parent = parent;
		this.numMoves = numMoves;
	}
}
