package rushhour.generation;

import rushhour.core.*;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

/**
 * Based off of:
 * https://www.researchgate.net/publication/221438347_What_Determines_Difficulty_of_Transport_Puzzles
 */
public class WeightedScoreEvaluator {
    // TODO: maybe use lambda functions?
    double PJRP_CONSTANT = 10;

    public String description() {
        return "TODO: explain gooder.";
    }

    public double eval(Board b, BoardGraph g) {
        return 0;
    }

    /**
     * Takes a random walk of the graph based off of a score function returns
     * the number of moves it took to reach a goal.
     */
    private int weightedWalk(Board b, BoardGraph g) {
        Random rng = new Random();
        rng.doubles(0, 1);
        return 0;
    }

    /**
     * The relatively simplistic scoring system based off of the paper.
     * Credits to Petr Jarusek and Radek Pelánek.
     */
    private Map<BoardGraph.Vertex,Double> scorePJRP (BoardGraph.Vertex v){
        Map<BoardGraph.Vertex,Double> scores =
            new HashMap<BoardGraph.Vertex,Double>();
        double score;
        for (BoardGraph.Vertex u : v.neighbors.values()) {
            score = u.depth;
            // moving closer to a solution
            if (v.depth > u.depth) {
                score += PJRP_CONSTANT;
            }
            scores.put(u, score);
        }
        return scores;
    }
}
