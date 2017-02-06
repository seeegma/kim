package rushhour.evaluation;

import rushhour.core.*;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

/**
 * Based off of:
 * https://www.researchgate.net/publication/221438347_What_Determines_Difficulty_of_Transport_Puzzles
 */
public class WeightedScoreEvaluator implements Evaluator {
    int TRIALS = 50000;

    // Based on the PJRP paper, adds this constant to the score (distance to soln) of the state if it moves closer => more likely to go to states closer to soln
    // constant so it has more effect if the score is lower
    double PJRP_CONSTANT = 50;
    

    // Variables for reset. Idea is that if movesWithNoProgress of moves goes by without any progress towards solution, then
    // chance to reset is RESET_CHANCE^(movesWithNoProgress/RESET_NUM)
    boolean USE_RESET = true;
    int RESET_NUM = 4;
    double RESET_CHANCE = .5;
    int movesWithNoProgress = 0;

    public String description() {
        return "weighted walk length";
    }

    public double eval(Board b) {
        return this.multiTrialEval(b);
    }

    private double multiTrialEval(Board b) {
        double total = 0;
        for (int i = 0; i < TRIALS; i++) {
            total += this.weightedWalk(b);
        }
        return total/TRIALS;
    }

    /**
     * Takes a random walk of the graph based off of a score function returns
     * the number of moves it took to reach a goal.
     */
    private double weightedWalk(Board b) {
        Random rng = new Random();
		BoardGraph g = b.getGraph();
        BoardGraph.Vertex current = g.getVertex(b);
        double count = 0;
        while (current.depth != 0) {
            Map<BoardGraph.Vertex,Double> probs = this.probsPJRP(current);
            double total = 0;
            double threshold = rng.nextDouble();
            boolean madeProgress = true;
            // Checks each state the current vertex can get to
            for (BoardGraph.Vertex v : probs.keySet()) {
                total += probs.get(v);
                if (total > threshold) {
                    if (v.depth >= current.depth) {
                        madeProgress = false;
                    }
                    current = v;
                    count++;
                    break; // breaks out of the for loop
                }
            }

            // does reset
            if (USE_RESET) {
                if (!madeProgress) {
                    movesWithNoProgress++;
                }

                if (movesWithNoProgress >= RESET_NUM) {
                    // we do want int division here
                    double chance = Math.pow(RESET_CHANCE, movesWithNoProgress/RESET_NUM);
                }
            }
        }
        return count;
    }

    /**
     * The relatively simplistic scoring system based off of the paper.
     * Credits to Petr Jarusek and Radek Pelánek.
     */
    private Map<BoardGraph.Vertex,Double> probsPJRP(BoardGraph.Vertex v) {
        Map<BoardGraph.Vertex,Double> probs =
            new HashMap<BoardGraph.Vertex,Double>();
        double score;
        double total = 0;
        for (BoardGraph.Vertex u : v.neighbors.values()) {
            score = u.depth;
            // moving closer to a solution
            if (v.depth > u.depth) {
                score += PJRP_CONSTANT;
            }
            total += score;
            probs.put(u, score);
        }
        // Lambda expressions require final variables
        final double finalTotal = total;
        // should turn all the scores into probabilities
        probs.replaceAll((k, w) -> w/finalTotal);
        return probs;
    }
	
	/**
     * The relatively simplistic scoring system based off of the paper.
     * Credits to Petr Jarusek and Radek Pelánek.
	 * 
	 * Instead of adding a constant, adds the constant multiplied by the reciprocal of the vertex depth.
     */
    private Map<BoardGraph.Vertex,Double> probsPJRPWithMem(BoardGraph.Vertex v) {
        Map<BoardGraph.Vertex,Double> probs =
            new HashMap<BoardGraph.Vertex,Double>();
        double score;
        double total = 0;
        for (BoardGraph.Vertex u : v.neighbors.values()) {
            score = u.depth;
            // moving closer to a solution
            if (v.depth > u.depth) {
                score += PJRP_CONSTANT;
            }
            total += score;
            probs.put(u, score);
        }
        // Lambda expressions require final variables
        final double finalTotal = total;
        // should turn all the scores into probabilities
        probs.replaceAll((k, w) -> w/finalTotal);
        return probs;
    }
}
