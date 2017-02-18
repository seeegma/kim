package rushhour.generation;

import rushhour.core.Board;

public interface BoardGenerator {
	public Board generate(int numCars, int maxVipX);
}
