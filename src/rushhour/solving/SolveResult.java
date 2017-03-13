package rushhour.solving;

import rushhour.core.Board;
import rushhour.core.Move;

import java.util.List;

public class SolveResult {
	public List<Move> path;
	public Board solvedBoard;
	public int visitedStates;
	public SolveResult(List<Move> path, Board solvedBoard, int visitedStates) {
		this.path = path;
		this.solvedBoard = solvedBoard;
		this.visitedStates = visitedStates;
	}
}
