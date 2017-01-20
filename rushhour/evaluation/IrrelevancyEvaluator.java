package rushhour.evaluation;

import rushhour.core.*;

import java.util.List;

/**
 * Attempts to evaluate how difficult a board is based on how many irrelevant
 * cars are on the board. An irrelevant car is one that is never moved. Since
 * there might be different numbers of irrelevant cars for each solve path, we
 * will take the minimum of all possible paths.
 *
 * The idea behind this is that the less different cars one has to move, the
 * easier it is to find a solution since the puzzle is smaller in a sense.
 */
public class IrrelevancyEvaluator implements Evaluator {
    // TODO: GO THROUGH ALL PATHS
    // seems hard to believe that we will have different optimal paths that
    // slide different cards?

    /**
     * Returns the score using this evaluation method.
     * @param b the board to evaluate
     * @return the score
     */
    public double eval(Board b) {
        boolean[] wasUsed = new boolean[b.getCars().size()];
        for (int i = 0; i < wasUsed.length; i++) {
           wasUsed[i] = false;
        }

        // should really just pass the graph around...
        BoardGraph bg = new BoardGraph(b);

        List<Move> moves = bg.pathToNearestSolution(b);

        // goes through and checks which ones were used.
        for (int j = 0; j < moves.size(); j++) {
            wasUsed[moves.get(j).index] = true;
        }

        double score = 0;
        for (int i = 0; i < wasUsed.length; i++) {
            if (!wasUsed[i]) {
                score += 1;
           }
        }
        return score;
    }

    public double getScore(Board b) {
        return eval(b);
    }
}
