import java.util.ArrayList;

/**
 * 
 */
public class Node {
    // grid that represents the current position
	public Grid grid;
    // the parent node
	public Node parent;
    // the number of moves from the original position
    public int numMoves;
    // index of car that was previously moved
    public int prev;
	
	public Node (Grid grid, Node parent, int numMoves, int prev) {
		this.grid = grid;
		this.parent = parent;
        this.numMoves = numMoves;
        this.prev = prev;
	}
}
