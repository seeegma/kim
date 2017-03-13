package rushhour.solving;

import rushhour.core.*;

import java.util.List;
import java.util.LinkedList;

public class IterativeDeepeningSolver implements Solver {

	private int iterations;

	public SolveResult getSolution(Board board) {
		// run a depth-limited BFS tree search
		SearchNode solvedNode = null;
		int depthLimit = 1;
		LinkedList<SearchNode> stack = new LinkedList<>();
		do {
			System.err.println("depthLimit = " + depthLimit);
			stack.clear();
			solvedNode = this.depthLimitedDFS(board, stack, depthLimit++);
		} while(solvedNode == null);
		System.err.println("iterations: " + this.iterations);
		// extract path
		return new SolveResult(solvedNode.getPath(), solvedNode.board, this.iterations);
	}

	private SearchNode depthLimitedDFS(Board board, LinkedList<SearchNode> stack, int depthLimit) {
		stack.offer(new SearchNode(board));
		while(!stack.isEmpty()) {
			SearchNode cur = stack.pop();
			this.iterations++;
			if(cur.board.isSolved()) {
				return cur;
			}
			if(cur.depth == depthLimit) {
				continue;
			}
			for(Move move : cur.board.allPossibleMoves()) {
				stack.push(new SearchNode(cur.board.getNeighborBoard(move), cur, move));
			}
		}
		return null;
	}

}
