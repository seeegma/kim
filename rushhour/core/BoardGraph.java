package rushhour.core;

import rushhour.io.AsciiGen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

public class BoardGraph {


	//Hashmap of (hashOfBoard : vertex). This is the vertex list, as a hashmap for easy lookup.
	//Requirements: easy lookup, quick iteration
	public HashMap<Long,Vertex> vertices;
	//Maximum distance from a solved state of any board in the graph.
	public int depth;

	public int numberOfSolvedStates;

	// We can keep track of these vertices since space is not an issue.
	// Linked list of solution vertices.
	public LinkedList<Vertex> solutions;

	//Constructor from board.
	public BoardGraph(Board startingBoard) {
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		HashMap<Long,Vertex> vertexList = new HashMap<Long,Vertex>();
		queue.offer(new Vertex(startingBoard));
		vertexList.put(startingBoard.hash(), new Vertex(startingBoard));
		while (!queue.isEmpty()) {
			Vertex current = queue.poll();

			//to be current's neighborList
			ArrayList<Vertex> neighborList = new ArrayList<Vertex>();
			for (Vertex neighbor : current.getNeighbors()) {
				//If the vertex exists, add it to neighbor list
				if (vertexList.containsKey(neighbor.board.hash())) {
					neighborList.add(vertexList.get(neighbor.board.hash()));
				}
				//otherwise create it and add it.
				else {
					vertexList.put(neighbor.board.hash(), neighbor);
					queue.offer(neighbor);
					neighborList.add(neighbor);
				}

			}
			current.neighbors = neighborList;
		}
		this.vertices = vertexList;
		// propogate Depth values And Graph pointers
		int numberOfVisitedStates = 0;
		int maxDepth = 0;
		int solvedStates=0;
		queue = new LinkedList<Vertex>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		this.solutions = new LinkedList<Vertex>();
		for(Vertex vert : vertices.values()) {
			numberOfVisitedStates++;
			if (vert.board.isSolved()) {
				vert.depth = 0;
				solvedStates++;
				queue.offer(vert);
				visited.add(vert);
				solutions.add(vert);
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


	public Vertex getVertex(Board b) {
		Vertex v = vertices.get(b.hash());
		if (v==null) {
			System.out.println("vertex not found");
		}
		return v;
	}

	/**
	 * Returns one of the Boards that is the farthest distance from any
	 * solution.
	 * @return a Board that is the farthest from any solution.
	 */
	public Board getFarthest() {
		for (Vertex vert : vertices.values()) {
			if (vert.depth == this.depth) {
				return vert.board;
			}
		}
	}

	public int getDepth(Board b) {
		return getVertex(b).depth;
	}

	/**
	 * Number of boards in this graph.
	 * @return size of graph
	 */
	public int size() {
		return vertices.size();
	}

	public ArrayList<Vertex> solve(Vertex v) {
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		Vertex current = v;
		while (current.depth != 0) {
			for (Vertex neighbor : current.neighbors) {
				if (neighbor.depth == current.depth - 1) {
					path.add(neighbor);
					current = neighbor;
					break;
				}
			}
		}
		Collections.reverse(path);
		return path;
	}

	/**
	 * Returns the vertex u which, via the parent pointer, indicates
	 * a shortest path from v to u.
	 */
	private Vertex pathHelper(Vertex v, Vertex u) {
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		v.parent = null;
		queue.offer(v);
		visited.add(v);
		while (!queue.isEmpty()) {
			Vertex current = queue.poll();
			if (current == u) {
				return current;
			}
			for (Vertex neighbor : current.getNeighbors()) {
				if (!visited.contains(neighbor)) {
					neighbor.parent = current;
					queue.offer(neighbor);
					visited.add(neighbor);
				}
			}
		}

		// should never hit this
		return null;
	}

	/**
	 * Returns a path of vertices from vertex v to u.
	 */
	private ArrayList<Vertex> path(Vertex v, Vertex u) {
		// Check if vertex u is even on the graph.
		if (this.vertices.get(u.board.hash()) == null) {
			return null;
		}

		Vertex current = this.pathHelper(v, u);
		if (current == null) {
			System.out.println("Something's wrong with BoardGraph.path().");
			return null;
		}

		ArrayList<Vertex> path = new ArrayList<Vertex>();
		do {
			path.add(current);
			current = current.parent;
		} while(current != null);

		return path;
	}

	/**
	 * Returns the distance from v to u.
	 */
	public int distance(Vertex v, Vertex u) {
		// note -1 here since path contains v and u.
		return this.path(v, u).size() - 1;
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
					AsciiGen.printGrid(v.board.getGrid());
					System.out.println(v.board.hash());
					for (Vertex x: v.neighbors){
						AsciiGen.printGrid(x.board.getGrid());
						System.out.println(x.board.hash());
					}
					System.out.println("Error");
					AsciiGen.printGrid(w.board.getGrid());
					System.out.println(w.board.hash());
					for (Vertex b : v.getNeighbors()) {
						AsciiGen.printGrid(b.board.getGrid());
						System.out.println(b.board.hash());
					}
				}
			}
		}
	}

}
