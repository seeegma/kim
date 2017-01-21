package rushhour;

import rushhour.core.*;
import rushhour.io.*;
import rushhour.evaluation.*;
import rushhour.analysis.*;

import java.util.List;

public class Main {
	public static void main(String[] args) {
		if(args.length > 1) {
			String operation = args[0];
			String filename = args[1];
			if(operation.equals("solve")) {
				// TODO: eventually make a package rushhour.solving with
				// dedicated solving algorithms (perhaps iterative deepening)
				// that don't require building the entire graph
				Board b = BoardIO.read(filename);
				BoardGraph g = new BoardGraph();
				g.fillEquivalenceClass(b);
				List<Move> moves = g.movesToNearestSolution(b);
				for(Move m : moves) {
					System.out.println(m);
				}
			} else if(operation.equals("evaluate")) {
				Board b = BoardIO.read(filename);
				BoardGraph g = new BoardGraph();
				g.fillEquivalenceClass(b);
				Evaluator e;
				e = new MinMovesToSolutionEvaluator();
				System.out.println(e.description() + ": " + e.eval(b, g));
				e = new MinSlidesToSolutionEvaluator();
				System.out.println(e.description() + ": " + e.eval(b, g));
				e = new AverageBranchingFactorEvaluator();
				System.out.println(e.description() + ": " + e.eval(b, g));
				e = new AverageBranchingFactorOnPathToSolutionEvaluator();
				System.out.println(e.description() + ": " + e.eval(b, g));
				e = new IrrelevancyEvaluator();
				System.out.println(e.description() + ": " + e.eval(b, g));
				e = new DFSEvaluator();
				System.out.println(e.description() + ": " + e.eval(b, g));
			} else if(operation.equals("analyze")) {
			}
		}
	}
}
