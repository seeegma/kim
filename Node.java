import java.util.ArrayList;

/**
 * 
 */
public class Node {
	public Grid grid;
	public Node parent;
	
	public Node (Grid grid, Node parent) {
		this.grid = grid;
		this.parent = parent;
	}
}
