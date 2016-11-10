import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

public class BoardGraph {


	
	private class Vertex {
		public Board board;
		public ArrayList<Vertex> neighbors;
		public int hash;
		public int depth;
		public Vertex parent;

		public Vertex(Board board, ArrayList<Vertex> neighbors, int hash) {
			this.board = board;
			this.neighbors = neighbors;
			this.hash = hash;			
		}
	}

	//Hashmap of (hashOfBoard : vertex). This is the vertex list, as a hashmap for easy lookup.
	public HashMap<Integer,Vertex> vertices;
	//Maximum distance from a solved state of any board in the graph.
	public int depth;

	public int numberOfSolvedStates;

	//Constructor from board.
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
		this.propogateDepthsAndGraphs();
	}

	public void propogateDepthsAndGraphs() {
		int maxDepth = 0;
		int solvedStates=0;
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		for(Vertex vert : vertices.values()) {
			vert.board.setGraph(this);
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
	
	/**
	 * Number of boards in this graph. 
	 * @return size of graph
	 */
	public int size() {
		return vertices.size();
	}

	public Vertex getVertex(Board b) {
		return vertices.get(Board.hashCode());
	}

	public ArrayList<Vertex> solve(Vertex v) {
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		Vertex current = v;
		while (current.depth != 0) {
			for (Vertex neighbor : current.neighbors) {
				if (neighbor.depth == current.depth -1) {
					path.add(neighbor);
					current = neighbor;
					break;
				}
			}
		}
		Collections.reverse(path);
		return path;
	}

}