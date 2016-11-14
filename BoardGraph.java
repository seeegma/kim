import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

public class BoardGraph {


	
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

	//Hashmap of (hashOfBoard : vertex). This is the vertex list, as a hashmap for easy lookup.
	public HashMap<Long,Vertex> vertices;
	//Maximum distance from a solved state of any board in the graph.
	public int depth;

	public int numberOfSolvedStates;

	//Constructor from board.
	public BoardGraph(Board startingBoard) {
		LinkedList<Board> queue = new LinkedList<Board>();
		HashMap<Long,Vertex> vertexList = new HashMap<Long,Vertex>();
		queue.offer(startingBoard);
		vertexList.put(startingBoard.hash(), new Vertex(startingBoard, null, startingBoard.hash()));
		while (!queue.isEmpty()) {
			Board current = queue.poll();

			//to be current's neighborList
			ArrayList<Vertex> neighborList = new ArrayList<Vertex>();
			for (Board neighbor : current.getNeighbors()) {
				//If the vertex exists, add it to neighbor list
				if (vertexList.containsKey(neighbor.hash())) {
					neighborList.add(vertexList.get(neighbor.hash()));
				}
				//otherwise create it and add it.
				else {
					Vertex newVert = new Vertex(neighbor, null, neighbor.hash());
					vertexList.put(neighbor.hash(), newVert);
					queue.offer(neighbor);
					neighborList.add(newVert);
				}

			}
			vertexList.get(current.hash()).neighbors = neighborList;
		}
		this.vertices = vertexList;
		this.propogateDepthsAndGraphs();
		//this.debug();
	}

	public void propogateDepthsAndGraphs() {
		int numberOfVisitedStates = 0;
		int maxDepth = 0;
		int solvedStates=0;
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		for(Vertex vert : vertices.values()) {
			numberOfVisitedStates++;
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
			numberOfVisitedStates--;
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
		return vertices.get(b.hash());
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

	public void debug() {
		for (Vertex v : this.vertices.values()) {
			//System.out.println("**********");
			// if(v.neighbors.size()==0){
			// 	System.out.println("vertex has no neighbors");
			// }

			for (Vertex w : v.neighbors) {
				if(!w.neighbors.contains(v)) {
					System.out.println("*******************************");
					AGen.printGrid(v.board.getGrid());
					System.out.println(v.hash);
					for (Vertex x: v.neighbors){
						AGen.printGrid(x.board.getGrid());
						System.out.println(x.hash);
					}
					System.out.println("Error");
					AGen.printGrid(w.board.getGrid());
					System.out.println(w.hash);
					// for (Vertex x : w.neighbors) {
					// 	//AGen.printGrid(x.board.getGrid());
					// 	System.out.println(x.hash);
					// }
					for (Board b : v.board.getNeighbors()) {
						AGen.printGrid(b.getGrid());
						System.out.println(b.hash());
					}
				}
			}
		}
	}

}