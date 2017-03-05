package rushhour.core;

import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Map;
import java.util.PriorityQueue;

public class BoardGraph {

	HashMap<Long,Vertex> vertices;
	Set<Vertex> solutions;

	public BoardGraph() {
		this.vertices = new HashMap<Long,Vertex>();
		this.solutions = new HashSet<>();
	}

	public Vertex getVertex(Board b) {
		return this.vertices.get(b.hash());
	}

	public int numSolutions() {
		return this.solutions.size();
	}

	public int size() {
		return vertices.size();
	}

}
