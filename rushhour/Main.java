package rushhour;

import rushhour.core.*;
import rushhour.io.*;
import rushhour.evaluation.*;
import rushhour.analysis.*;

import java.util.List;

public class Main {
	private static final String usage =
	   	"Usage: java rushhour.Main [OPERATION] [ARGUMENTS]\n" + 
		"Supported operations:\n" +
		"\tsolve <puzzle_file>\n" +
		"\tevaluate <puzzle_file>\n" +
		"\tanalyze <puzzle_file> <log_file>";
	public static void main(String[] args) {
		if(args.length > 1) {
			String operation = args[0];
			String puzzleFile = args[1];
			if(operation.equals("print")) {
				Board b = BoardIO.read(puzzleFile);
				AsciiGen.printGrid(b.getGrid());
			} else if(operation.equals("solve")) {
				Board b = BoardIO.read(puzzleFile);
				BoardGraph g = new BoardGraph(b);
				// TODO: eventually make a package rushhour.solving with
				// dedicated solving algorithms (perhaps iterative deepening)
				// that don't require building the entire graph
				List<Move> moves = g.movesToNearestSolution(b);
				for(Move m : moves) {
					System.out.println(m);
				}
			} else if(operation.equals("evaluate")) {
				Board b = BoardIO.read(puzzleFile);
				BoardGraph g = new BoardGraph(b);
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
				if(args.length < 3) {
					usage();
				}
				Board b = BoardIO.read(puzzleFile);
				String logFile = args[2];
				Log l = LogIO.read(logFile);
				l.board = b;
				Analyzer a;
				a = new MoveAnalyzer();
				System.out.println(a.description() + ": " + a.analyze(l));
				a = new TimeAnalyzer();
				System.out.println(a.description() + ": " + a.analyze(l));
				a = new MoveTimeAnalyzer();
				System.out.println(a.description() + ": " + a.analyze(l));
				a = new ResetAnalyzer();
				System.out.println(a.description() + ": " + a.analyze(l));
				a = new UndoAnalyzer();
				System.out.println(a.description() + ": " + a.analyze(l));
				a = new BackwardMoveAnalyzer();
				System.out.println(a.description() + ": " + a.analyze(l));
			}
		} else {
			usage();
		}
	}

	private static void usage() {
		System.err.println(usage);
		System.exit(1);
	}
}
