package rushhour.generation;

import rushhour.core.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Uses RandomBoardGen and BoardGraph to generate a relatively hard board.
 */
public class RandomGraphGen {
    public int w;
    public int h;
    public int numCars;

    private Board board;
    private Board originalBoard;
    public BoardGraph graph;
    // useful for stats
    public int depth = 0;
    public int originalDepth = 0;

    public RandomGraphGen() {
        this.w = 0;
        this.h = 0;
        this.numCars = 0;
    }

    public RandomGraphGen(int w, int h, int numCars) {
        this.w = w;
        this.h = h;
        this.numCars = numCars;
    }

    public void set(int w, int h, int numCars) {
        this.w = w;
        this.h = h;
        this.numCars = numCars;
    }

    /**
     * Sets the originalBoard as a randomly generated solveable board.
     */
    private void setSolvedBoard() {
        /*
        RandomBoardGen r = new RandomBoardGen(this.w, this.h, this.numCars);
        do {
            while (!r.generateBoard()) {
                // pass
            }
        } while (Solver.solveBoard(r.getBoard()) == null);
        this.originalBoard = r.getBoard();*/
    }

    /**
     * Generates an unsolved configuration of Rush Hour. We first use
     * setSolvedBoard to get a randomly generated solveable board, and then
     * build a graph of all possible moves and find a configuration that is the 
     * farthest from any solution.
     */
    public void generateBoard() {
        this.setSolvedBoard();
        this.graph = new BoardGraph(this.originalBoard);
        this.originalDepth = this.graph.depth;
        this.board = this.graph.getFarthest();
        this.depth = this.graph.depth;
    }

    /**
     * Generates an unsolved configuration of Rush Hour that requires at least
     * minMoves to solve. We first use setSolvedBoard to get a board with at
     * least minMoves, and then build a graph of all possible moves and find a
     * configuration that is the farthest from any solution.
     */
    public void generateBoard(int minMoves) {
        this.setSolvedBoard(minMoves);
        this.graph = new BoardGraph(this.originalBoard);
        this.originalDepth = this.graph.getDepth(this.originalBoard);
        this.board = this.graph.getFarthest();
        this.depth = this.graph.depth;
    }

    /**
     * Sets the originalBoard as a randomly generated solveable board that
     * requires at least minMoves to solve.
     */
    /* OLD
    private void setSolvedBoard(int minMoves) {
        RandomBoardGen r = new RandomBoardGen(this.w, this.h, this.numCars);
        do {
            while (!r.generateBoard()) {
                // pass
            }
        } while (Solver.solveBoard(r.getBoard()) == null ||
            Solver.solveBoard(r.getBoard()).size() - 1 <= minMoves);
        this.originalBoard = r.getBoard();
    }*/

    private void setSolvedBoard(){
        this.originalBoard = this.randomSolvedBoard();
    }

    private Board randomSolvedBoard(int numCars) {
        Board board = new Board(6, 6);
        // Adds the VIP
        board.addCar(new Car(4, 2, 2, true));

        for (int i = 0; i < numCars; i++) {
            board.addCar(this.findAcceptableCar(board));
        }
    }

    private Car findAcceptableCar(Board board) {
        
    }
    

    /**
     * For testing purposes.
     */
    public static void main(String[] args) {
        RandomGraphGen g = new RandomGraphGen(6, 6, 12);
        g.generateBoard();
        System.out.println("New: " + g.depth);
        g.board.debug();
        System.out.println("Original:" + g.originalDepth);
        g.originalBoard.debug();

        // testing vs Solver and BoardGraph.path()
        /*
        System.out.println(g.graph.solutions.size());
        System.out.println(g.graph.vertices.size());
        int lowest = 0;
        boolean once = true;
        System.out.println("New:");
        BoardGraph.Vertex v = g.graph.getVertex(g.board);
        for (BoardGraph.Vertex u : g.graph.solutions) {
            int temp = g.graph.distance(v, u);
            if (once) {
                lowest = temp;
                once = false;
            }
            if (lowest > temp) {
                lowest = temp;
            }
        }
        System.out.println("Lowest: " + lowest);
        int solv = Solver.solveBoard(g.board).size()-1;
        System.out.println("Solver: " + solv);

        lowest = 0;
        once = true;
        System.out.println("Original:");
        v = g.graph.getVertex(g.originalBoard);
        for (BoardGraph.Vertex u : g.graph.solutions) {
            int temp = g.graph.distance(v, u);
            if (once) {
                lowest = temp;
                once = false;
            }
            if (lowest > temp) {
                lowest = temp;
            }
        }
        System.out.println("Lowest: " + lowest);
        solv = Solver.solveBoard(g.originalBoard).size()-1;
        System.out.println("Solver: " + solv);*/
    }
}
