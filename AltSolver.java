import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds an alternative solver to see if we can improve on it without ruining
 * the working one.
 */
public final class AltSolver {
    /**
     * Gets all the neighboring positions of the current positon.
     * @return a list of the Boards that are 1 move away from the current
     * board's position
     */
    public static ArrayList<Board> getNeighbors(Board b) {
        ArrayList<Car> c = b.getCars();
        ArrayList<Board> neighbors = new ArrayList<Board>();
        for (int i = 0;i < c.size();i++) {
            for (Direction d : Direction.values()) { 
                if (b.canMove(i,d)) {
                    Board neighbor = b.copy();
                    neighbor.move(i,d);
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    /**
     * Solves the Rush Hour position.
     * @return a list of Boards that represent the path to the solution.
     */
    public static ArrayList<Board> solve(Board b1) {
        LinkedList<NodeBoard> queue = new LinkedList<NodeBoard>();
        HashSet<Integer> visited = new HashSet<Integer>();
        queue.offer(new NodeBoard(b1,null,0));
        NodeBoard solvedState = new NodeBoard(b1,null,0);
        boolean solutionFound = false;
        while (!queue.isEmpty()) {
            NodeBoard current = queue.poll();
            visited.add(current.board.getGrid().hash());
            if (current.board.isSolved()) {
                solvedState=current;
                solutionFound = true;
                break;
            }
            for (Board b : current.board.getNeighbors()) { 
                boolean isNew = true;
                if (visited.contains(b.getGrid().hash())) {
                    isNew = false;
                }
                if (isNew) {
                    queue.offer(new NodeBoard(b,current,current.numMoves+1));
                }
            }
        }
        
        if (!solutionFound) {
            System.out.println("No solution found");
            return null;
        }
        else {
            ArrayList<Board> path = new ArrayList<Board>();
            NodeBoard current = solvedState;
            path.add(current.board);
            while (current.parent!=null) {
                current=current.parent;
                path.add(current.board);
            }
            Collections.reverse(path);
            return path;
        }
    }
}