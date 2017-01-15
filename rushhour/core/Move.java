package rushhour.core;

import java.util.ArrayList;

/*
 * Class that represents a move on the board.
 */
public class Move {
    public long time;

    // Index of car to move.
    public int index;
    // positive is to the right/down
    public int amount;

    public Move(long time, int index, int amount) {
        this.time = time;
        this.index = index;
        this.amount = amount;
    }

    public void debug() {
        System.out.println("t: " + time + ", i: " + index + ", amt: " + amount);
    }
}
