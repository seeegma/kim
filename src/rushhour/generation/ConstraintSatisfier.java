package rushhour.generation;

import rushhour.core.*;
import rushhour.evaluation.*;
import rushhour.io.AsciiGen;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Collections;


public class ConstraintSatisfier {
	public static Board satisfy(List<Constraint> constraints) {
		// determine number of cars based on constraints
		int targetNumCars = 11; // reasonable default
		int minMoves = -1;
		for(Constraint c : constraints) {
			if(c.metric instanceof NumberOfCarsEvaluator) {
				targetNumCars = (int)c.maxValue;
			} else if(c.metric instanceof MinMovesToSolutionEvaluator) {
				minMoves = (int)c.maxValue;
				if(targetNumCars == -1) {
					targetNumCars = (int)(0.0255 * minMoves + 10.774);
				}
			}
		}
		// generate solved boards with this number of cars until we have one with the proper depth
		Board solvedBoard;
		System.out.println("finding solved board with " + targetNumCars + " cars of proper depth...");
		// ReverseBoardGen gen = new ReverseBoardGen();
		if(minMoves != -1) {
			do {
				// TODO: start at numCars = 9, generate the board with a nice high
				// depth, then keep trying different random cars to add until the depth
				// is higher, and keep going
				solvedBoard = SolvedBoardGenerator.generate(targetNumCars);
				System.out.print("found board...");
				System.out.print("numCars: " + solvedBoard.numCars() + ", ");
				System.out.println("depth: " + solvedBoard.getGraph().maxDepth());
				if(solvedBoard.getGraph().maxDepth() == 0) {
					AsciiGen.printGrid(solvedBoard.getGrid());
				}
			} while(solvedBoard.getGraph().maxDepth() < minMoves);
		} else {
			solvedBoard = SolvedBoardGenerator.generate(targetNumCars);
		}
		System.out.println(minMoves);
		System.out.println("solved board:");
		System.out.println("depth: " + solvedBoard.getGraph().maxDepth());
		AsciiGen.printGrid(solvedBoard.getGrid());
		// (eventually)
		// return satisfyGeneral(solvedBoard, constraints);
		// --
		// (for now)
		// find a board in the graph with the proper depth
		System.out.println("searching through graph...");
		BoardGraph.Vertex v = solvedBoard.getGraph().getVertex(solvedBoard);
		Set<Long> visited = new HashSet<>();
		PriorityQueue<BoardGraph.Vertex> queue = new PriorityQueue<>(new BoardGraph.MaxDepthComparator());
		queue.offer(v);
		while(!queue.isEmpty()) {
			v = queue.poll();
			if(visited.contains(v.board.hash())) {
				continue;
			}
			// System.out.println("depth: " + v.depth);
			if(v.depth == minMoves) {
				System.out.println("returning: ");
				System.out.println(v.board);
				return v.board;
			}
			List<BoardGraph.Vertex> neighbors = new ArrayList<>(v.neighbors);
			Collections.shuffle(neighbors);
			for(BoardGraph.Vertex neighbor : neighbors) {
				if(!visited.contains(neighbor.board.hash())) {
					queue.offer(neighbor);
				}
			}
			visited.add(v.board.hash());
		}
		System.out.println("returning: null");
		return null;
	}

	private static Board satisfyGeneral(Board board, List<Constraint> constraints) {
		// fill out the equivalence class of this board
		BoardGraph graph = board.getGraph();
		// search the graph for boards that satisfy the contstraints
		LinkedList<BoardGraph.Vertex> frontier = new LinkedList<>();
		Set<BoardGraph.Vertex> visited = new HashSet<>();
		frontier.offer(graph.getVertex(board));
		BoardGraph.Vertex cur;
		while(!frontier.isEmpty()) {
			cur = frontier.poll();
			if(visited.contains(cur)) {
				continue;
			}
			if(meetsAllConstraints(cur.board, constraints)) {
				return cur.board;
			}
			for(BoardGraph.Vertex neighbor : cur.neighbors) {
				if(!visited.contains(neighbor)) {
					frontier.offer(neighbor);
				}
			}
		}
		return null;
	}

	private static boolean meetsAllConstraints(Board board, List<Constraint> constraints) {
		for(Constraint c : constraints) {
			if(!(c.minValue <= c.metric.eval(board) && c.metric.eval(board) <= c.maxValue)) {
				return false;
			}
		}
		return true;
	}
}
