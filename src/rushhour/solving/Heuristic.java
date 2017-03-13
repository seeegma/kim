package rushhour.solving;

import rushhour.core.*;

import java.util.Comparator;

public abstract class Heuristic implements Comparator<SearchNode> {

	public abstract double heuristicValue(Board board);

	public int compare(SearchNode node1, SearchNode node2) {
		return (int)this.heuristicValue(node1.board) - (int)this.heuristicValue(node2.board);
	}

}
