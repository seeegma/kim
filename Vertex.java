import java.util.ArrayList;

public class Vertex {
		public Board board;
		public ArrayList<Vertex> neighbors;
		public long hash;
		public int depth;
		public Vertex parent;

		public Vertex(Board board, ArrayList<Vertex> neighbors, long hash) {
			this.board = board;
			this.neighbors = neighbors;
			this.hash = hash;			
		}
	}