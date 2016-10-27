import java.util.HashMap;

public class BoardGraph {


	private class Vertex {
		public Board board;
		public ArrayList<Vertex> neighbors;
		public int hash;

		public Vertex(Board board, ArrayList<Vertex> neighbors, int hash) {
			this.board = board;
			this.neighbors = neighbors;
			this.hash = hash;
		}
	}

	public HashMap<Integer,Vertex> vertices;


	public BoardGraph(Board startingBoard) {
		LinkedList<Board> queue = new LinkedList<Board>();
		HashSet<Integer> visited = new HashSet<Integer>();
		HashMap<Integer,Vertex> vertexList = new HashMap<Integer,Vertex>;
		queue.offer(startingBoard);
		visited.add(startingBoard.hashCode());
		vertexList.put(startingBoard.hashCode(), new Vertex(startingBoard, null, startingBoard.hashCode()));
		while (!queue.isEmpty()) {
			Board current = queue.poll();
			ArrayList<Vertex> neighborList = new ArrayList<Vertex>();
			for (Board neighbor : current.getNeighbors()) {
				if (vertexList.containsKey(neighbor.hashCode())) {
					neighborList.add(vertexList.get(neighbor.hashCode()));
				}
				else {
					visited.add(startingBoard.hashCode());
					Vertex newVert = new Vertex(neighbor, null, neighbor.hashCode());
					vertexList.put(neighbor.hashCode(), newVert);
					queue.offer(neighbor);
					neighborList.add(newVert);
				}

			}
			vertexList.get(current.hashCode()).neighbors = neighborList;
		}
		this.vertices = vertexList;
	}
	
}