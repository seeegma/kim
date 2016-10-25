import java.util.ArrayList;

/**
 * 
 */
public class Node {
	public Grid grid;
	public Node parent;
    public int numMoves;
    public int prev;
	
	public Node (Grid grid, Node parent, int numMoves, int prev) {
		this.grid = grid;
		this.parent = parent;
        this.numMoves = numMoves;
        this.prev = prev;
	}
}
