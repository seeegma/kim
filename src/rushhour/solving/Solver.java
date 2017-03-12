package rushhour.solving;

import rushhour.core.*;

import java.util.List;

public interface Solver {
	public List<Move> getSolution(Board board);
}
