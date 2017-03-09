package rushhour.solving;

import rushhour.core.*;

import java.util.List;
import java.util.LinkedList;

public class IterativeDeepeningSolver implements Solver {

	public List<Move> solve(Board board) {
		// run a depth-limited BFS tree search
		SearchNode solvedBoard = null;
		int depthLimit = 1;
		LinkedList<SearchNode> stack = new LinkedList<>();
		do {
			System.err.println("depthLimit = " + depthLimit);
			stack.clear();
			solvedBoard = this.depthLimitedDFS(board, stack, depthLimit++);
		} while(solvedBoard == null);
		// extract path
		return solvedBoard.getPath();
	}

	private SearchNode depthLimitedDFS(Board board, LinkedList<SearchNode> stack, int depthLimit) {
		stack.offer(new SearchNode(board));
		while(!stack.isEmpty()) {
			SearchNode cur = stack.pop();
			if(cur.depth == depthLimit) {
				continue;
			}
			if(cur.board.isSolved()) {
				return cur;
			}
			for(Move move : board.allPossibleMoves()) {
				stack.push(new SearchNode(cur.board.getNeighborBoard(move), cur, move));
			}
		}
		return null;
	}

}
