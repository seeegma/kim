import java.util.ArrayList;

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

    private void setSolvedBoard() {
        RandomBoardGen r = new RandomBoardGen(this.w, this.h, this.numCars);
        do {
            while (!r.generateBoard()) {
                // pass
            }
        } while (Solver.solveBoard(r.getBoard()) == null);
        this.originalBoard = r.getBoard();
    }

    public void generateBoard() {
        this.setSolvedBoard();
        this.graph = new BoardGraph(this.originalBoard);
        this.originalDepth = this.graph.getDepth(this.originalBoard);
        this.board = this.graph.getFarthest();
        this.depth = this.graph.depth;
    }

    private void setSolvedBoard(int minMoves) {
        RandomBoardGen r = new RandomBoardGen(this.w, this.h, this.numCars);
        do {
            while (!r.generateBoard()) {
                // pass
            }
        } while (Solver.solveBoard(r.getBoard()) == null ||
            Solver.solveBoard(r.getBoard()).size() - 1 <= minMoves);
        this.originalBoard = r.getBoard();
    }

    public void generateBoard(int minMoves) {
        this.setSolvedBoard(minMoves);
        this.graph = new BoardGraph(this.originalBoard);
        this.originalDepth = this.graph.getDepth(this.originalBoard);
        this.board = this.graph.getFarthest();
        this.depth = this.graph.depth;
    }

    /**
     * For testing purposes.
     */
    public static void main(String[] args) {
        RandomGraphGen g = new RandomGraphGen(6, 6, 12);
        g.generateBoard(20);
        System.out.println("New: " + g.depth);
        g.board.debug();
        System.out.println("Original:" + g.originalDepth);
        g.originalBoard.debug();
    }
}
