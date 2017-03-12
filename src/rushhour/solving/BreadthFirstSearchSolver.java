package rushhour.solving;

import rushhour.core.*;

import java.util.List;
import java.util.LinkedList;
import java.util.HashSet;

public class BreadthFirstSearchSolver implements Solver {
	public List<Move> getSolution(Board board) {
		LinkedList<SearchNode> queue = new LinkedList<>();
		HashSet<Long> visited = new HashSet<>();
		queue.offer(new SearchNode(board));
		SearchNode solvedBoard = null;
		int iterations = 0;
		while(!queue.isEmpty()) {
			iterations++;
			SearchNode current = queue.poll();
			visited.add(current.board.hash());
			if(current.board.isSolved()) {
				solvedBoard = current;
				break;
			}
			for(Move move : current.board.allPossibleMoves()) {
				Board neighborBoard = current.board.getNeighborBoard(move);
				if(!visited.contains(neighborBoard.hash())) {
					queue.offer(new SearchNode(neighborBoard, current, move));
				}
			}
		}
		System.err.println("iterations: " + iterations);
		// construct list from node tree
		return solvedBoard.getPath();
	}
}
