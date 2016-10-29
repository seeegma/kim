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
     * @param b the board to manipulate
     * @param parent the parent node
     * @return a list of the Boards that are 1 move away from the current
     *      board's position
     */
    public static ArrayList<Node> getNeighbors(Board b, Node parent) {
        ArrayList<Node> neighbors = new ArrayList<Node>();
        // Goes through each car in the board and gets all possible neighbors
        // moving that car can create
        for (int i = 0; i < b.getCars().size(); i++) {
            // So we don't move the same piece twice
            if (i != parent.prev) {
                ArrayList<Grid> lst = allPossibleMoves(b, i);
                for (Grid g : lst) {
                    neighbors.add(new Node(g, parent, parent.numMoves+1, i));
                }
            }
        }
        return neighbors;
    }

    /**
     * Gets all possible moves of car at index i on board b in that current
     * position.
     * @param b the board
     * @param i the index of the car
     * @return an ArrayList of all possible grid positions that results from
     *      moving that car
     */
    public static ArrayList<Grid> allPossibleMoves(Board b, int i) {
        ArrayList<Grid> neighbors = new ArrayList<Grid>();
        int count = 0;
        Direction d;
        // Find the starting direction
        if (b.getCars().get(i).horizontal) {
            d = Direction.LEFT;
        } else {
            d = Direction.UP;
        }

        // Creates all possible board positions moving to the starting direction
        while(b.canMove(i,d)) {
            b.move(i, d);
            Grid g = b.getGrid().copy();
            neighbors.add(g);
            count++;
        }
        // Moves back to the original position
        /* Making a method to revert this back in one method call might not be
        worth it since that would require at max 6 grid operations (moving a
        length 3 car to a new positon). Could have an if statement to determine
        whether to use this or that new method, but then that'd just overhead
        and isn't a huge improvement if at all, especially on a packed board*/
        d = d.reverse();
        for (int j = 0; j < count; j++) {
            b.move(i, d);
        }
        count = 0;
        // Repeats in the reverse of the starting direction
        while(b.canMove(i,d)) {
            b.move(i, d);
            Grid g = b.getGrid().copy();
            neighbors.add(g);
            count++;
        }
        // Resets back to the original state
        d = d.reverse();
        for (int j = 0; j < count; j++) {
            b.move(i, d);
        }
        return neighbors;
    }

    /**
     * Solves the Rush Hour position using BFS.
     * @return a list of Boards that represent the path to the solution with
     *      the original state as the head of the list. Returns NULL if no
     *      solution is found.
     */
    public static ArrayList<Grid> solveBoard(Board b1) {
        LinkedList<Node> queue = new LinkedList<Node>();
        HashSet<Integer> visited = new HashSet<Integer>();
        Board working = b1.copy();
        int count = 0;

        // Enqueue the root of the tree, aka the current position
        queue.offer(new Node(b1.getGrid(), null, 0, -1));
        //queue.offer(new Node(b1.getGrid(),null,0));

        Node solvedState = new Node(b1.getGrid().copy(), null, 0, -1);
		visited.add(working.getGrid().hash());
        //Node solvedState = new Node(b1.getGrid().copy(),null,0);
        boolean solutionFound = false;

        // Pretty standard BFS
        while (!queue.isEmpty()) {
            // Dequeue
            Node current = queue.poll();
            if (current.numMoves > count) {
                System.out.println(count);
                count++;
            }
            // Saving a hash of the dequeued board to note that we visisted it
            visited.add(current.grid.hash());

            // decompresses the grid into the board class
            working.decompress(current);
            if (working.isSolved()) {
                solvedState=current;
                solutionFound = true;
                break;
            }

            // Go through all positions that are 1 move away from the current
            for (Node n : AltSolver.getNeighbors(working, current)) {
                // Add to the queue if we have not visited a neighbor
                if (!visited.contains(n.grid.hash())) {
                    queue.offer(n);
					visited.add(n.grid.hash());
                }
            }
        }
            
        // figures out the path if there was a soln
        if (solutionFound) {
            ArrayList<Grid> path = new ArrayList<Grid>();
            Node current = solvedState;
            path.add(current.grid);
            while (current.parent!=null) {
                current=current.parent;
                path.add(current.grid);
            }
            Collections.reverse(path);
            return path;
        }
        //System.out.println("No solution found");
        return null;
    }
}