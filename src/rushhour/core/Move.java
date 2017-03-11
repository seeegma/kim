package rushhour.core;

public class Move {
    // Index of car to move.
    public int index;
    // positive is to the right/down
    public int vector;

    public Move(int index, int vector) {
        this.index = index;
        this.vector = vector;
    }

	public String toString() {
		return this.index + " " + this.vector;
	}
}
