package rushhour.solving;

import rushhour.core.*;

import java.util.List;
import java.util.LinkedList;
import java.util.HashSet;

public class BreadthFirstSearchSolver implements Solver {
	public List<Move> solve(Board board) {
		LinkedList<SearchNode> queue = new LinkedList<>();
		HashSet<Long> visited = new HashSet<>();
		queue.offer(new SearchNode(board));
		while(!queue.isEmpty()) {
			SearchNode current = queue.poll();
			visited.add(current.board.hash());
			if(current.board.isSolved()) {
				break;
			}
			for(Move move : current.board.allPossibleMoves()) {
				Board neighborBoard = current.board.getNeighborBoard(move);
				if(!visited.contains(neighborBoard.hash())) {
					queue.offer(new SearchNode(neighborBoard, current.board, move));
				}
			}
		}
		// construct list from node tree
		LinkedList<Move> ret = new LinkedList<>();
		do {
			SearchNode node = queue.poll();
			if(node == null) {
				break;
			}
			ret.add(0, node.move);
		} while(!queue.isEmpty());
		return ret;
	}
}
