import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;

public class BoardGraph {


	private class Vertex {
		public Board board;
		public ArrayList<Vertex> neighbors;
		public int hash;
		public int depth;

		public Vertex(Board board, ArrayList<Vertex> neighbors, int hash) {
			this.board = board;
			this.neighbors = neighbors;
			this.hash = hash;

		}
	}

	public HashMap<Integer,Vertex> vertices;
	public int depth;
	public int numberOfSolvedStates;


	public BoardGraph(Board startingBoard) {
		LinkedList<Board> queue = new LinkedList<Board>();
		HashMap<Integer,Vertex> vertexList = new HashMap<Integer,Vertex>();
		queue.offer(startingBoard);
		vertexList.put(startingBoard.hashCode(), new Vertex(startingBoard, null, startingBoard.hashCode()));
		while (!queue.isEmpty()) {
			Board current = queue.poll();
			ArrayList<Vertex> neighborList = new ArrayList<Vertex>();
			for (Board neighbor : current.getNeighbors()) {
				if (vertexList.containsKey(neighbor.hashCode())) {
					neighborList.add(vertexList.get(neighbor.hashCode()));
				}
				else {
					Vertex newVert = new Vertex(neighbor, null, neighbor.hashCode());
					vertexList.put(neighbor.hashCode(), newVert);
					queue.offer(neighbor);
					neighborList.add(newVert);
				}

			}
			vertexList.get(current.hashCode()).neighbors = neighborList;
		}
		this.vertices = vertexList;
		this.propogateDepths();
	}

	public void propogateDepths() {
		int maxDepth = 0;
		int solvedStates=0;
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		for(Vertex vert : vertices.values()) {
			if (vert.board.isSolved()) {
				vert.depth = 0;
				solvedStates++;
				queue.offer(vert);
				visited.add(vert);
			}
		}
		while (!queue.isEmpty()) {
			Vertex current = queue.poll();
			for (Vertex neighbor : current.neighbors) {
				if (!visited.contains(neighbor)) {
					neighbor.depth = current.depth + 1;
					if (maxDepth<neighbor.depth) {
						maxDepth = neighbor.depth;
					}
					visited.add(neighbor);
					queue.offer(neighbor);
				}
			}
		}
		this.numberOfSolvedStates = solvedStates;
		this.depth = maxDepth;
	}
	

	public int size() {
		return vertices.size();
	}
}