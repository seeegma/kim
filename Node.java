import java.util.ArrayList;

/**
 * 
 */
public class Node {
	public int[][] grid;
	public Node parent;
	
	public Node (int[][] grid, Node parent) {
		this.grid = grid;
		this.parent = parent;
	}
}
