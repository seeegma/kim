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

public class SolvedBoardGraph extends DepthGraph {

	private SolvedBoardGraph(Board solvedBoard) {
		this.addSolutions(solvedBoard);
	}

	public static SolvedBoardGraph create(Board solvedBoard) {
		if(!solvedBoard.isSolved()) {
			return null;
		}
		return new SolvedBoardGraph(solvedBoard);
	}

}

