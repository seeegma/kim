package rushhour.solving;

import rushhour.core.*;

import java.util.List;
import java.util.LinkedList;

public class IterativeDeepeningSolver implements Solver {
	public List<Move> solve(Board board) {
		// run a depth-limited BFS tree search
		List<Move> ret = new LinkedList<>();
		int depthLimit = 1;
		do {
			System.err.println("depthLimit = " + depthLimit);
			ret = this.depthLimitedDFS(board, new LinkedList<Move>(), depthLimit++);
		} while(ret == null);
		return ret;
	}

	private LinkedList<Move> depthLimitedDFS(Board board, LinkedList<Move> prevPath, int depthLimit) {
		if(depthLimit == 0) {
			return null;
		}
		if(board.isSolved()) {
			return prevPath;
		}
		for(Move move : board.allPossibleMoves()) {
			LinkedList<Move> curPath = new LinkedList<>(prevPath);
			curPath.add(move);
			LinkedList<Move> newPath = this.depthLimitedDFS(board.getNeighborBoard(move), curPath, depthLimit - 1);
			if(newPath != null) {
				return newPath;
			}
		}
		return null;
	}
}
