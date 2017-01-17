package rushhour.core;

import java.util.ArrayList;

public class Vertex {

	public Board board;
	public ArrayList<Vertex> neighbors;
	public int depth;
	public Vertex parent;

	public Vertex(Board board) {
		this.board = board;
		this.parent = null;
		this.depth = 0;
		this.neighbors = null;
	}

	public Vertex(Board board, ArrayList<Vertex> neighbors) {
		this(board);
		this.neighbors = neighbors;
	}

	/**
     * Gets all the neighboring positions of the current positon.
     * @return a list of the Boards that are 1 move away from the current
     *      board's position
     */
    public ArrayList<Vertex> getNeighbors() {
		if(this.neighbors != null) {
			return this.neighbors;
		}
        this.neighbors = new ArrayList<Vertex>();
        // Goes through each car in the board and gets all possible neighbors
        // moving that car can create
        for (int i = 0; i < this.board.getCars().size(); i++) {
            ArrayList<Board> lst = this.board.allPossibleMoves(i);
            for (Board b : lst) {
                neighbors.add(new Vertex(b));
            }
        }
        return neighbors;
    }


}
