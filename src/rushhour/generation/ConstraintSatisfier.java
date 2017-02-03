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
		Board board;
		System.out.println("finding solved board with " + targetNumCars + " cars of proper depth...");
		BoardGenerator gen = new BoardGenerator(true);
		// ReverseBoardGen gen = new ReverseBoardGen();
		int maxDepth = 0;
		if(minMoves != -1) {
			do {
				board = gen.generate(targetNumCars);
				if(board.getGraph().numSolutions() > 0) {
				   if(board.getGraph().maxDepth() > maxDepth) {
					System.out.print("numCars: " + board.numCars() + ", ");
					System.out.println("depth: " + board.getGraph().maxDepth());
					maxDepth = board.getGraph().maxDepth();
				   }
				} else {
					System.out.println("not solvable");
				}
			} while(board.getGraph().numSolutions() == 0 || board.getGraph().maxDepth() < minMoves);
		} else {
			board = gen.generate(targetNumCars);
		}
		System.out.println(minMoves);
		System.out.println("solved board:");
		System.out.println("depth: " + board.getGraph().maxDepth());
		return board;
	}

}
