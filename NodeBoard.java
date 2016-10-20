import java.util.ArrayList;

/**
 * 
 */
public class NodeBoard {
	public Board board;
	public NodeBoard parent;
	
	public NodeBoard (Board board, NodeBoard parent) {
		this.board = board;
		this.parent = parent;
	}
}
