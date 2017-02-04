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
    // TODO: maybe use lambda functions?
    // Could make more functions/heuristics
    double PJRP_CONSTANT = 50;
	double RECIP_CONST;
    int TRIALS = 50000;

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
		this.RECIP_CONST = g.maxDepth();
        double count = 0;
        while (current.depth != 0) {
            Map<BoardGraph.Vertex,Double> probs = this.probsPJRP(current);
            double total = 0;
            double threshold = rng.nextDouble();
            // Checks each state the current vertex can get to
            for (BoardGraph.Vertex v : probs.keySet()) {
                total += probs.get(v);
                if (total > threshold) {
                    current = v;
                    count++;
                    break; // breaks out of the for loop
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
    private Map<BoardGraph.Vertex,Double> probsPJRPRecip(BoardGraph.Vertex v) {
        Map<BoardGraph.Vertex,Double> probs =
            new HashMap<BoardGraph.Vertex,Double>();
        double score;
        double total = 0;
        for (BoardGraph.Vertex u : v.neighbors.values()) {
            score = u.depth;
            // moving closer to a solution
            if (v.depth > u.depth) {
                score += (((float)RECIP_CONST)/v.depth);
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
