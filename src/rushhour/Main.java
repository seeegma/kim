package rushhour;

import rushhour.core.*;
import rushhour.io.*;
import rushhour.evaluation.*;
import rushhour.analysis.*;
import rushhour.generation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collections;
import java.io.File;

public class Main {
	private static final String usage =
		"Usage: java rushhour.Main [OPERATION] [ARGUMENTS]\n" + 
		"Supported operations:\n" +
		"\tprint <puzzle_file>" +
		"\tsolve <puzzle_file>\n" +
		"\tevaluate [ --csv | --fields ] <puzzle_file>\n" +
		"\tanalyze [ --csv | --fields ] <puzzle_file> <log_file>\n" +
		"\tgenerate [--useHeuristics] [--numBoards n] [--numCars n]";
	public static void main(String[] args) {
		if(args.length > 1) {
			String operation = args[0];
			String puzzleFile = null;
			if(operation.equals("print")) {
				puzzleFile = args[1];
				Board b = BoardIO.read(puzzleFile);
				AsciiGen.printGrid(b.getGrid());
			} else if(operation.equals("solve")) {
				puzzleFile = args[1];
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
				String option = null;
				boolean asCsv = false, fields = false;
				if(args.length < 2 || args.length > 3) {
					usage();
				} else {
					if(args.length == 2) {
						option = args[1];
						if(option.equals("--fields")) {
							fields = true;
						} else {
							puzzleFile = args[1];
						}
					} else if(args.length == 3) {
						option = args[1];
						if(option.equals("--csv")) {
							asCsv = true;
						} else {
							usage();
						}
						puzzleFile = args[2];
					}
				}
				List<Evaluator> evaluators = new ArrayList<>();
				evaluators.add(new NumberOfCarsEvaluator());
				evaluators.add(new NumberOfLongCarsEvaluator());
				evaluators.add(new MinMovesToSolutionEvaluator());
				evaluators.add(new MinSlidesToSolutionEvaluator());
				evaluators.add(new AverageBranchingFactorEvaluator());
				evaluators.add(new AverageBranchingFactorOnPathToSolutionEvaluator());
				evaluators.add(new IrrelevancyEvaluator());
				//evaluators.add(new DFSEvaluator());
				evaluators.add(new WeightedScoreEvaluator());
				if(fields) {
					for(Evaluator e : evaluators) {
						System.out.print(e.description() + ",");
					}
				} else {
					Board b = BoardIO.read(puzzleFile);
					if(asCsv) {
						for(Evaluator e : evaluators) {
							System.out.print(e.eval(b) + ",");
						}
					} else {
						for(Evaluator e : evaluators) {
							System.out.println(e.description() + ": " + e.eval(b));
						}
					}
				}
			} else if(operation.equals("analyze")) {
				String option = null;
				boolean asCsv = false, fields = false;
				String logFile = null;
				if(args.length < 2 || args.length > 4) {
					usage();
				} else {
					if(args.length == 2) {
						option = args[1];
						if(option.equals("--fields")) {
							fields = true;
						} else {
							usage();
						}	
					} else if(args.length == 3) {
						puzzleFile = args[1];
						logFile = args[2];
					} else if(args.length == 4) {
						option = args[1];
						if(option.equals("--csv")) {
							asCsv = true;
						} else {
							usage();
						}
						puzzleFile = args[2];
						logFile = args[3];
					}
				}
				List<Analyzer> analyzers = new ArrayList<>();
				analyzers.add(new MoveAnalyzer());
				analyzers.add(new TimeAnalyzer());
				analyzers.add(new MoveTimeAnalyzer());
				analyzers.add(new ResetAnalyzer());
				analyzers.add(new UndoAnalyzer());
				analyzers.add(new BackwardMoveAnalyzer());
				analyzers.add(new MoveRatioAnalyzer());
				analyzers.add(new UniqueStateAnalyzer());
				if(fields) {
					for(Analyzer a : analyzers) {
						System.out.print(a.description() + ",");
					}
				} else {
					Board b = BoardIO.read(puzzleFile);
					Log l = LogIO.read(logFile);
					l.board = b;
					if(asCsv) {
						for(Analyzer a : analyzers) {
							System.out.print(a.analyze(l) + ",");
						}
					} else {
						for(Analyzer a : analyzers) {
							System.out.println(a.description() + ": " + a.analyze(l));
						}
					}
				}
			} else if(operation.equals("generate")) {
				if(args.length < 3) {
					usage();
				}
				boolean usingHeuristics = false; 
				boolean onlySolvable = false;
				boolean setNumCars = false;
				int numCars = 0;
				int numBoards = 1;
				boolean stats = false;
				boolean puzzleOutToFile = false;
				boolean printBoards = true;
				for(int i=1; i<args.length; i++) {
					if(args[i].equals("--useHeuristics")) {
						usingHeuristics = true;
					} else if(args[i].equals("--solvable")) {
						onlySolvable = true;
					} else if(args[i].equals("--numCars")) {
						numCars = Integer.parseInt(args[i+1]);
						if(numCars > 18) {
							System.err.println("No such boards exist, sorry!");
							System.exit(0);
						}
						setNumCars = true;
						i++;
					} else if(args[i].equals("--numBoards")) {
						numBoards = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--stats")) {
						stats = true;
					} else if(args[i].equals("--puzzleFile")) {
						puzzleOutToFile = true;
					} else if(args[i].equals("--noPrint")) {
						printBoards = false;
					} else {
						System.err.println("unrecognized option '" + args[i] + "'");
						usage();
					}
				}
				BoardGenerator gen = new BoardGenerator(usingHeuristics);
				Random rng = new Random();
				// stats
				Map<Integer,Integer> depths = new HashMap<>(); // depths to be deleted once final output is fixed
				Map<Integer,Map<Integer,Integer>> numCarsToDepths = new HashMap<>();
				Map<Integer,ArrayList<Long>> equivalenceMap = new HashMap<>();
				Map<Integer,ArrayList<Integer>> multiplicityMap = new HashMap<>();
				for (int i = 0; i <= 18; i++) {
					numCarsToDepths.put(i, new HashMap<Integer, Integer>());
					equivalenceMap.put(i, new ArrayList<Long>());
					multiplicityMap.put(i, new ArrayList<Integer>());
				}
				// depth and numCars of -1 is unsolvable
				numCarsToDepths.put(-1, new HashMap<Integer, Integer>());
				numCarsToDepths.get(-1).put(-1, 0);
				int boardsGenerated = 0;
				// ok go!
				int i = 0;
				while(i < numBoards) {
					if(!setNumCars) {
						numCars = rng.nextInt(8) + 9; // random number from 9 to 15
					}

					// generates a board in an equivalence class not seen before
					Board board = new Board(6, 6);
					boolean isUnique = false;
					while (!isUnique) {
						if(onlySolvable) {
							// keeps track of num of unsolvable boards generated
							numCarsToDepths.get(-1).put(-1, numCarsToDepths.get(-1).get(-1) - 1);
							do {
								numCarsToDepths.get(-1).put(-1, numCarsToDepths.get(-1).get(-1) + 1);
								board = gen.generate(numCars);
								boardsGenerated++;
							} while(board.getGraph().numSolutions() == 0);
						} else {
							board = gen.generate(numCars);
							boardsGenerated++;
						}

						if (stats) {
							// makes sure the new graph is in a unique equivalence class and updates all the stats stuff
							if (!equivalenceMap.containsValue(board.getGraph().hash())) {
								equivalenceMap.get(numCars).add(board.getGraph().hash());
								multiplicityMap.get(numCars).add(1);
								isUnique = true;
							} else {
								int j = equivalenceMap.get(numCars).indexOf(board.getGraph().hash());
								multiplicityMap.get(numCars).set(j, multiplicityMap.get(numCars).get(j)+1);
							}
						} else {
							isUnique = true;
						}
					}

					// now we have a board
					// TODO: check if we've seen its graph before
					if(stats) {
						// assumes 0 <= numCars <= 18
						int depth = board.getGraph().maxDepth();
						if(!numCarsToDepths.get(numCars).containsKey(depth)) {
							numCarsToDepths.get(numCars).put(depth, 1);
						} else {
							numCarsToDepths.get(numCars).put(depth, numCarsToDepths.get(numCars).get(depth)+1);
						}
					}

					if(puzzleOutToFile) {
						// String filename = ""; // TODO
						// No Microsoft, no
						String filename = "/generateOutput/";
						if (stats) {
							filename += numCars + "/" + i + ".txt";
						} else {
							filename += i + ".txt";
						}
						// BoardIO.write(filename, board);
					} else if(printBoards) {
						AsciiGen.printGrid(board.getGrid());
					}

					i++;
				}
				// stats output
				if(stats) {
					// TODO: keep multiplicities of equiv classes.
					// TODO: also store depths by numCars
					System.out.println("TOTAL NUMBER OF BOARDS GENERATED: " + boardsGenerated);
					System.out.println("TOTAL DEPTHS COUNT:");
					int minDepth = Collections.min(depths.keySet());
					if(minDepth == -1) {
						minDepth = 0;
						System.out.println("-1 --> " + depths.get(-1));
					}
					int maxDepth = Collections.max(depths.keySet());
					double totalDepth = 0;
					for(int d=minDepth; d<=maxDepth; d++) {
						if(depths.containsKey(d)) {
							System.out.println(" " + d + " --> " + depths.get(d));
							totalDepth += depths.get(d) * d;
						} else {
							System.out.println(" " + d + " --> 0");
						}
					}
					System.out.println("AVERAGE DEPTH OF SOLVABLE BOARDS: " + totalDepth/numBoards);
				}
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
