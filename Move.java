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

    /**
     * Returns the move that cased the parent node of m to become node m.
     * @param m the child node
     * @return the move that transforms the position of the parent node of m to
     *      m's current position
     */
    public static Move fromChildNode(Node m) {
        if (m.parent == null) {
            return null;
        }

        int dx = 0;
        int dy = 0;
        int x = 0;
        int y = 0;
        boolean found = false;
        // find the most upper left corner of the car
        for (int i = 0; i < m.grid.height; i++) {
            for (int j = 0; j < m.grid.width; j++) {
                if (!found && m.grid.get(j, i) == m.prev) {
                    found = true;
                    y = i;
                    x = j;
                    // break doesn't break out of nested loops so it's useless
                }
            }
        }

        if (!found) {
            System.out.println("MOVE.FROMCHILDNODE BUG 1");
            return null;
        }

        boolean isHoriz = false;
        // determines the orientation of the car
        // System.out.println(x + " " + y);
        if (m.grid.width > x+1 && m.grid.get(x+1, y) == m.prev) {
            isHoriz = true;
        }

        // determines the x or y position of the parent node's car
        found = false;
        int amount = 0;
        if (isHoriz) {
            for (int k = 0; k < m.grid.width; k++) {
                if (!found && m.parent.grid.get(k, y) == m.prev) {
                    found = true;
                    amount = k;
                }
            }
        } else {
            for (int k = 0; k < m.grid.height; k++) {
                if (!found && m.parent.grid.get(x, k) == m.prev) {
                    found = true;
                    amount = k;
                }
            }
        }

        if (!found) {
            System.out.println("MOVE.FROMCHILDNODE BUG 2");
            return null;
        }

        if (isHoriz) {
            amount = x - amount;
        } else {
            amount = y - amount;
        }

        return new Move(0, m.prev, amount);
    }

    public void debug() {
        System.out.println("t: " + time + ", i: " + index + ", amt: " + amount);
    }
}
