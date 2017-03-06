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
import java.util.Random;

public class BoardGraph {

	HashMap<Long,Vertex> vertices;
	Set<Board> solutions;

	public BoardGraph() {
		this.vertices = new HashMap<Long,Vertex>();
		this.solutions = new HashSet<>();
	}

	public Vertex getVertex(Board b) {
		return this.vertices.get(b.hash());
	}

	public Set<Board> solutions() {
		return this.solutions;
	}

	public int size() {
		return vertices.size();
	}

	public Board executeRandomWalkFrom(Board board, int length) {
		Random rng = new Random();
		for(int i=0; i<length; i++) {
			List<Move> moves = new ArrayList<>(board.allPossibleMoves());
			board = board.getNeighborBoard(moves.get(rng.nextInt(moves.size())));
		}
		return board;
	}

}
