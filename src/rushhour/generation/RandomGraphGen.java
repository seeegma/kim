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
    public BoardGraph graph;
    // useful for stats

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
     *//*
    public void generateBoard() {
        ReverseBoardGen temp = new ReverseBoardGen();
        temp.genBoard();
        this.board = temp.getBoard();
        this.graph = new BoardGraph(this.board);
        this.board = this.graph.getFarthest();
    }*/

    /**
     * Generates an unsolved configuration of Rush Hour that requires at least
     * minMoves to solve. We first use setSolvedBoard to get a board with at
     * least minMoves, and then build a graph of all possible moves and find a
     * configuration that is the farthest from any solution.
     */
    public void generateBoard(int minMoves) {
        ReverseBoardGen temp = new ReverseBoardGen();
        temp.genBoard(minMoves);
        this.board = temp.getBoard();
        this.graph = new BoardGraph(this.board);
        this.board = this.graph.getFarthest();
    }

    /**
     * For testing purposes.
     */
    public static void main(String[] args) {

    }
}
