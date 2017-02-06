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
import java.util.Set;
import java.util.HashSet;

public class Main {
	private static final String usage =
		"Usage: java rushhour.Main [OPERATION] [ARGUMENTS]\n" + 
		"Supported operations:\n" +
		"\tprint <puzzle_file>" +
		"\tsolve <puzzle_file>\n" +
		"\tevaluate [ --csv | --fields ] <puzzle_file>\n" +
		"\tanalyze [ --csv | --fields ] <puzzle_file> <log_file>\n" +
		"\tgenerate [--useHeuristics] [--boardsToGenerate n] [--numCars n]";
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
				// options
				boolean nontrivial = false; 
				boolean onlySolvable = false;
				boolean useHeuristics = false; 
				boolean setNumCars = false;
				int numCars = 0;
				int boardsToGenerate = 1;
				boolean stats = false;
				boolean puzzleOutToFile = false;
				boolean printBoards = true;
				for(int i=1; i<args.length; i++) {
					if(args[i].equals("--numBoards")) {
						boardsToGenerate = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--solvable")) {
						onlySolvable = true;
					} else if(args[i].equals("--useHeuristics")) {
						useHeuristics = true;
					} else if(args[i].equals("--nontrivial")) {
						nontrivial = true;
					} else if(args[i].equals("--numCars")) {
						numCars = Integer.parseInt(args[i+1]);
						if(numCars > 18) {
							System.err.println("No such boards exist, sorry!");
							System.exit(0);
						}
						setNumCars = true;
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
				BoardGenerator gen = new BoardGenerator(nontrivial, useHeuristics);
				Random rng = new Random();
				// stats
				Map<Integer,Integer> numBoardsByDepth = new HashMap<>(); // depths to be deleted once final output is fixed
				Map<Integer,Map<Integer,Integer>> numBoardsByDepthByNumCars = new HashMap<>(); // splits based on numCars
				//Map<Integer,Map<Long,Integer>> multiplicityByEquivalenceByNumCars = new HashMap<>(); // can calc the ratio of unique boards generated vs total solvable based on numCars of board
				Set<Long> graphHashes = new HashSet<>();
				for (int i = 0; i <= 18; i++) {
					numBoardsByDepthByNumCars.put(i, new HashMap<Integer, Integer>());
					// depth of -1 is unsolvable
					numBoardsByDepthByNumCars.get(i).put(-1, 0);
					//equivalenceMap.put(i, new ArrayList<Long>());
					//multiplicityMap.put(i, new ArrayList<Integer>());
				}
				// depth of -1 is unsolvable
				numBoardsByDepth.put(-1, 0);
				int boardsGenerated = 0;
				// ok go!
				int i = 0;
				while(i < boardsToGenerate) {
					if(!setNumCars) {
						numCars = rng.nextInt(8) + 9; // random number from 9 to 15
					}
					// generates a solvable board
					Board board = new Board(6, 6);
					if(onlySolvable) {
						// keeps track of num of unsolvable boards generated
						numBoardsByDepthByNumCars.get(numCars).put(-1, numBoardsByDepthByNumCars.get(numCars).get(-1) - 1);
						numBoardsByDepth.put(-1, numBoardsByDepth.get(-1)-1);
						do {
							numBoardsByDepthByNumCars.get(numCars).put(-1, numBoardsByDepthByNumCars.get(numCars).get(-1) + 1);
							numBoardsByDepth.put(-1, numBoardsByDepth.get(-1)+1);
							board = gen.generate(numCars);
							boardsGenerated++;
						} while(board.getGraph().numSolutions() == 0);
					} else {
						board = gen.generate(numCars);
						boardsGenerated++;
					}

					// uniqueness check
					boolean isUnique = false;
					if (stats) {
						// makes sure the new graph is in a unique equivalence class and updates all the stats stuff
						if (!graphHashes.contains(board.getGraph().hash())) {
							graphHashes.add(board.getGraph().hash());
							isUnique = true;
						}
						/*if (!equivalenceMap.containsValue(board.getGraph().hash())) {
							equivalenceMap.get(numCars).add(board.getGraph().hash());
							multiplicityMap.get(numCars).add(1);
							isUnique = true;
						} else {
							int j = equivalenceMap.get(numCars).indexOf(board.getGraph().hash());
							multiplicityMap.get(numCars).set(j, multiplicityMap.get(numCars).get(j)+1);
						}*/
					}

					// now we have a board
					// print it
					if(printBoards) {
						AsciiGen.printGrid(board.getGrid());
					}
					// deal with final things
					if(stats || puzzleOutToFile) {
						// get its depth
						int depth = board.getGraph().getDepthOfBoard(board);
						if(stats) {
							// increment numBoardsByDepth counter(s)
							if(!numBoardsByDepth.containsKey(depth)) {
								numBoardsByDepth.put(depth, 1);
							} else {
								numBoardsByDepth.put(depth, numBoardsByDepth.get(depth)+1);
							}
							// increments numBoardsByDepthByNumCars counter
							if (!numBoardsByDepthByNumCars.get(numCars).containsKey(depth)) {
								numBoardsByDepthByNumCars.get(numCars).put(depth, 1);
							} else {
								numBoardsByDepthByNumCars.get(numCars).put(depth, numBoardsByDepthByNumCars.get(numCars).get(depth)+1);
							}
						}
						// only outputs unique boards
						if(puzzleOutToFile && isUnique) {
							// write the board to a file
							int index = numBoardsByDepth.get(depth);
							String pathName = "generated_puzzles/" + depth + "/";
							File outDir = new File(pathName);
							outDir.mkdirs();
							String filename = pathName + index + ".txt";
							System.out.println("writing to '" + filename + "'...");
							BoardIO.write(filename, board);
						}
					}
					i++;
				}
				// stats output
				if(stats) {
					// TODO: keep multiplicities of equiv classes.
					// TODO: also print this stuff for numBoardsByDepthByNumCars
					System.out.println("TOTAL NUMBER OF BOARDS GENERATED: " + boardsGenerated);
					System.out.println("TOTAL DEPTHS COUNT:");
					int minDepth = Collections.min(numBoardsByDepth.keySet());
					int maxDepth = Collections.max(numBoardsByDepth.keySet());
					int maxDepthStringWidth = 5;
					int maxCountStringWidth = Collections.max(numBoardsByDepth.values()).toString().length();
					if(maxCountStringWidth < 5) {
						maxCountStringWidth = 5;
					}
					printRow("depth", "count", maxDepthStringWidth, maxCountStringWidth);
					printRow("-----", "-----", maxDepthStringWidth, maxCountStringWidth);
					if(minDepth == -1) { // nearly guaranteed to be the case
						minDepth = 0;
						printRow("-1", numBoardsByDepth.get(-1).toString(), maxDepthStringWidth, maxCountStringWidth);
					}
					double totalDepth = 0;
					for(int d=minDepth; d<=maxDepth; d++) {
						if(numBoardsByDepth.containsKey(d)) {
							printRow(new Integer(d).toString(), numBoardsByDepth.get(d).toString(), maxDepthStringWidth, maxCountStringWidth);
							totalDepth += numBoardsByDepth.get(d) * d;
						} else {
							printRow(new Integer(d).toString(), "0", maxDepthStringWidth, maxCountStringWidth);
						}
					}
					System.out.println("AVERAGE DEPTH OF SOLVABLE BOARDS: " + totalDepth/boardsToGenerate);
				}
			}
		} else {
			usage();
		}
	}

	private static void printRow(String col1, String col2, int col1Width, int col2Width) {
		System.out.print("| ");
		if(col1.length() < col1Width) {
			System.out.print(new String(new char[col1Width - col1.length()]).replace("\0", " "));
		}
		System.out.print(col1);
		System.out.print(" | ");
		if(col2.length() < col2Width) {
			System.out.print(new String(new char[col2Width - col2.length()]).replace("\0", " "));
		}
		System.out.print(col2);
		System.out.print(" |");
		System.out.println();
	}

	private static void usage() {
		System.err.println(usage);
		System.exit(1);
	}
}
