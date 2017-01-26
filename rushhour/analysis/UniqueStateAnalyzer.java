package rushhour.analysis;

import rushhour.core.*;

import java.util.List;
import java.util.HashSet;

public class UniqueStateAnalyzer implements Analyzer {
	public String description() {
		return "repeated board states";
	}
	public double analyze(Log l) {
		Board b = l.board;
		BoardGraph g = b.getGraph();
		HashSet<Long> visited = new HashSet<Long>();
		for(LogMove m : l.moveList) {
			visited.add(l.board.hash());
			if(m.type == LogMoveType.NORMAL) {
				b.move(m.move.index, m.move.amount);
			}
		}
		return (double)visited.size();
	}
}

