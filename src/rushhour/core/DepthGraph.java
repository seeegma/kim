package rushhour.core;

public class DepthGraph extends BoardGraph {

	protected int maxDepth;
	protected Board farthest;

	public int maxDepth() {
		return this.maxDepth;
	}

	public int getDepthOfBoard(Board b) {
		return this.getVertex(b).depth;
	}

	public Board getFarthest() {
		return this.farthest;
	}

}
