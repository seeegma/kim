package rushhour.core;

public class Move {
    // Index of car to move.
    public int index;
    // positive is to the right/down
    public int amount;

    public Move(int index, int amount) {
        this.index = index;
        this.amount = amount;
    }
}
