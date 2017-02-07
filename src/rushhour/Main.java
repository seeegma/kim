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
		"\tprint <puzzle_file>\n" +
		"\tsolve <puzzle_file>\n" +
		"\tevaluate [ --csv | --fields ] <puzzle_file>\n" +
		"\tanalyze [ --csv | --fields ] <puzzle_file> <log_file>\n" +
		"\tgenerate [--useHeuristics] [--numBoards n] [--maxPerDepth n] [--numCars n] [--minNumCars] [--maxNumCars] [--quiet] [--minDepth n] [--unique] [--nontrivial] [--stats] [--puzzleFile]";
	public static void main(String[] args) {
		if(args.length > 1) {
			String operation = args[0];
			String puzzleFile = null;
			if(operation.equals("print")) {
				puzzleFile = args[1];
				Board b = BoardIO.read(puzzleFile);
				System.out.println(AsciiGen.getGridString(b));
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
				evaluators.add(new DFSEvaluator());
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
				boolean onlyUnique = false;
				boolean useHeuristics = false; 
				boolean setNumCars = false;
				int targetNumCars = 11; // reasonable default
				int minNumCars = 9; // reasonable default
				int maxNumCars = 15; // reasonable default
				int boardsToSave = 1; // reasonable default
				int minDepth = -1; // reasonable default
				boolean stats = false;
				int maxBoardsPerDepth = -1;
				boolean puzzleOutToFile = false;
				boolean quiet = false;
				for(int i=1; i<args.length; i++) {
					if(args[i].equals("--numBoards")) {
						boardsToSave = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--unique")) {
						onlyUnique = true;
					} else if(args[i].equals("--maxPerDepth")) {
						maxBoardsPerDepth = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--useHeuristics")) {
						useHeuristics = true;
					} else if(args[i].equals("--nontrivial")) {
						nontrivial = true;
					} else if(args[i].equals("--numCars")) {
						targetNumCars = Integer.parseInt(args[i+1]);
						if(targetNumCars > 18) {
							System.err.println("No such boards exist, sorry!");
							System.exit(0);
						}
						setNumCars = true;
						i++;
					} else if(args[i].equals("--minNumCars")) {
						minNumCars = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--maxNumCars")) {
						maxNumCars = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--minDepth")) {
						minDepth = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--stats")) {
						stats = true;
					} else if(args[i].equals("--puzzleFile")) {
						puzzleOutToFile = true;
					} else if(args[i].equals("--quiet")) {
						quiet = true;
					} else {
						System.err.println("unrecognized option '" + args[i] + "'");
						usage();
					}
				}
				BoardGenerator gen = new BoardGenerator(nontrivial, useHeuristics);
				Random rng = new Random();
				// STATS STUFF //
				// total counts
				int totalBoardsGenerated = 0;
				Map<Integer,Integer> numBoardsByBoardDepth = new HashMap<>();
				Map<Integer,Integer> numBoardsByGraphDepth = new HashMap<>();
				Set<Long> uniqueGraphs = new HashSet<>();
				// per-numCars counts
				Map<Integer,Integer> totalBoardsGeneratedByNumCars = new HashMap<>();
				Map<Integer,Integer> boardsSavedSoFarByNumCars = new HashMap<>();
				Map<Integer,Map<Integer,Integer>> numBoardsByBoardDepthByNumCars = new HashMap<>();
				Map<Integer,Map<Integer,Integer>> numBoardsByGraphDepthByNumCars = new HashMap<>();
				Map<Integer,Integer> uniqueGraphsByNumCars = new HashMap<>();
				for(int i=0; i<=18; i++) {
					totalBoardsGeneratedByNumCars.put(i, 0);
					boardsSavedSoFarByNumCars.put(i, 0);
					numBoardsByBoardDepthByNumCars.put(i, new HashMap<Integer,Integer>());
					numBoardsByGraphDepthByNumCars.put(i, new HashMap<>());
					uniqueGraphsByNumCars.put(i, 0);
				}
				// saved boards counts
				int boardsSavedSoFar = 0;
				Map<Integer,Integer> boardsSavedSoFarByBoardDepth = new HashMap<>(); // for outputBoard filenames
				Map<Integer,Integer> boardsSavedSoFarByGraphDepth = new HashMap<>();
				Set<Long> uniqueGraphsOfSavedBoards = new HashSet<>(); // only for stats purposes
				//
				// it's generation time!
				while(boardsSavedSoFar < boardsToSave) {
					// generate a random board
					if(!setNumCars) {
						targetNumCars = rng.nextInt(maxNumCars - minNumCars + 1) + minNumCars;
					}
					Board randomBoard = gen.generate(targetNumCars);
					BoardGraph graph = randomBoard.getGraph();
					int graphDepth = graph.maxDepth();
					int randomBoardDepth = graph.getDepthOfBoard(randomBoard);
					Board outputBoard = randomBoard;
					int outputBoardDepth = randomBoardDepth;
					boolean keepBoard = true; // whether or not we're going to save outputBoard
					// compute keepBoard
					if(onlyUnique && uniqueGraphs.contains(randomBoard.getGraph().hash())) {
						// make sure the graph is in a unique equivalence class, if necessary
						keepBoard = false;
					} else if(minDepth > -1 || maxBoardsPerDepth > -1) {
						// see if we need to increase the depth of outputBoard
						// (either because randomBoard's depth is too small
						// or because we already have enough boards of that depth)
						if(outputBoardDepth < minDepth || (boardsSavedSoFarByBoardDepth.containsKey(outputBoardDepth) && boardsSavedSoFarByBoardDepth.get(outputBoardDepth) == maxBoardsPerDepth)) {
							if(graphDepth < minDepth) {
								// if using the highest depth board in the graph won't be enough
								keepBoard = false;
							} else {
								outputBoard = graph.getFarthest();
								outputBoardDepth = graph.maxDepth();
								// see if we need to backtrack
								while(boardsSavedSoFarByBoardDepth.containsKey(outputBoardDepth) && boardsSavedSoFarByBoardDepth.get(outputBoardDepth) == maxBoardsPerDepth) {
									outputBoard = graph.getOneBoardCloser(outputBoard);
									outputBoardDepth = graph.getDepthOfBoard(outputBoard);
									if(outputBoardDepth == 0) {
										break;
									} else if(outputBoardDepth < minDepth) {
										keepBoard = false;
										break;
									}
								}
							}
						}
					}
					// ---
					// update non-depth-related stats
					totalBoardsGenerated++;
					int numCars = randomBoard.numCars();
					if(!uniqueGraphs.contains(randomBoard.getGraph().hash())) {
						uniqueGraphs.add(randomBoard.getGraph().hash());
						incrementMapValue(uniqueGraphsByNumCars, numCars);
					}
					incrementMapValue(totalBoardsGeneratedByNumCars, numCars);
					// save it
					if(keepBoard) {
						boardsSavedSoFar++;
						incrementMapValue(boardsSavedSoFarByNumCars, numCars);
						if(!uniqueGraphsOfSavedBoards.contains(randomBoard.getGraph().hash())) {
							uniqueGraphsOfSavedBoards.add(randomBoard.getGraph().hash());
						}
						// print it
						if(!quiet) {
							System.out.println();
							System.out.println("board " + boardsSavedSoFar);
							System.out.println(AsciiGen.getGridString(outputBoard));
							System.out.println("numCars: " + numCars);
						}
					}
					// if we need the depth
					if(stats || maxBoardsPerDepth > -1 || (keepBoard && puzzleOutToFile)) {
						if(keepBoard) {
							// update boardsSavedSoFarByBoardDepth
							if(!boardsSavedSoFarByBoardDepth.containsKey(outputBoardDepth)) {
								boardsSavedSoFarByBoardDepth.put(outputBoardDepth, 0);
							}
							incrementMapValue(boardsSavedSoFarByBoardDepth, outputBoardDepth);
							// update boardsSavedSoFarByGraphDepth
							if(!boardsSavedSoFarByGraphDepth.containsKey(graphDepth)) {
								boardsSavedSoFarByGraphDepth.put(graphDepth, 0);
							}
							incrementMapValue(boardsSavedSoFarByGraphDepth, graphDepth);
							// print depths
							if(!quiet) {
								System.out.print("board depth: " + outputBoardDepth + ", ");
								System.out.println("graph depth: " + graphDepth);
							}
						}
						// update depth-related stats
						if(stats) {
							// increment numBoardsByBoardDepth
							if(!numBoardsByBoardDepth.containsKey(randomBoardDepth)) {
								numBoardsByBoardDepth.put(randomBoardDepth, 0);
							}
							incrementMapValue(numBoardsByBoardDepth, randomBoardDepth);
							// increment numBoardsByBoardDepthByNumCars
							if(!numBoardsByBoardDepthByNumCars.get(numCars).containsKey(randomBoardDepth)) {
								numBoardsByBoardDepthByNumCars.get(numCars).put(randomBoardDepth, 0);
							}
							incrementMapValue(numBoardsByBoardDepthByNumCars.get(numCars), randomBoardDepth);
							// increment numBoardsByGraphDepth
							if(!numBoardsByGraphDepth.containsKey(graphDepth)) {
								numBoardsByGraphDepth.put(graphDepth, 0);
							}
							incrementMapValue(numBoardsByGraphDepth, graphDepth);
							// increment numBoardsByGraphDepthByNumCars
							if(!numBoardsByGraphDepthByNumCars.get(numCars).containsKey(graphDepth)) {
								numBoardsByGraphDepthByNumCars.get(numCars).put(graphDepth, 0);
							}
							incrementMapValue(numBoardsByGraphDepthByNumCars.get(numCars), graphDepth);
						}
						// dump board to file
						if(keepBoard && puzzleOutToFile) {
							// write the board to a file
							int index = boardsSavedSoFarByBoardDepth.get(outputBoardDepth);
							String pathName = "generated_puzzles/" + outputBoardDepth + "/";
							File outDir = new File(pathName);
							outDir.mkdirs();
							String filename = pathName + index + ".txt";
							if(!quiet) {
								System.out.println("writing to '" + filename + "'...");
							}
							BoardIO.write(filename, outputBoard);
						}
					}
				}
				// stats output
				if(stats) {
					System.out.println(">>>>> STATS <<<<<");
					System.out.println();
					int minGeneratedNumCars = Collections.min(numBoardsByBoardDepthByNumCars.keySet());
					int maxGeneratedNumCars = Collections.max(numBoardsByBoardDepthByNumCars.keySet());
					for(int numCars = minGeneratedNumCars; numCars <= maxGeneratedNumCars; numCars++) {
						if(uniqueGraphsByNumCars.get(numCars) == 0) {
							continue;
						}
						totalBoardsGeneratedByNumCars.get(numCars);
						uniqueGraphsByNumCars.get(numCars);
						numBoardsByBoardDepthByNumCars.get(numCars);
						printStats("=== NUMCARS=" + numCars + " ===", totalBoardsGeneratedByNumCars.get(numCars), uniqueGraphsByNumCars.get(numCars), numBoardsByBoardDepthByNumCars.get(numCars), numBoardsByGraphDepthByNumCars.get(numCars));
						System.out.println();
					}
					printStats("=== TOTAL ===", totalBoardsGenerated, uniqueGraphs.size(), numBoardsByBoardDepth, numBoardsByGraphDepth);
					System.out.println();
					printStats("=== SAVED ===", boardsToSave, uniqueGraphsOfSavedBoards.size(), boardsSavedSoFarByBoardDepth, boardsSavedSoFarByGraphDepth);
				}
			}
		} else {
			usage();
		}
	}

	private static void incrementMapValue(Map<Integer,Integer> map, int key) {
		map.put(key, map.get(key)+1);
	}

	private static void printStats(String desc, int totalBoardsGenerated, int numUniqueGraphs, Map<Integer,Integer> numBoardsByBoardDepth, Map<Integer,Integer> numBoardsByGraphDepth) {
		System.out.println(desc);
		System.out.println("NUMBER OF BOARDS GENERATED: " + totalBoardsGenerated);
		System.out.println("NUMBER OF UNIQUE EQUIVALENCE CLASSES: " + numUniqueGraphs);
		System.out.println("DEPTHS COUNT:");
		int minDepth = Collections.min(numBoardsByBoardDepth.keySet());
		int maxDepth = Collections.max(numBoardsByGraphDepth.keySet());
		int maxDepthStringWidth = 5;
		int maxCountStringWidth = Collections.max(numBoardsByBoardDepth.values()).toString().length();
		if(maxCountStringWidth < 6) {
			maxCountStringWidth = 6;
		}
		printRow("depth", "boards", "graphs", maxDepthStringWidth, maxCountStringWidth, maxCountStringWidth);
		printRow("-----", "------", "------", maxDepthStringWidth, maxCountStringWidth, maxCountStringWidth);
		if(minDepth == -1) { // nearly guaranteed to be the case
			minDepth = 0;
			numBoardsByBoardDepth.get(-1).toString();
			numBoardsByGraphDepth.get(-1).toString();
			printRow("-1", numBoardsByBoardDepth.get(-1).toString(), numBoardsByGraphDepth.get(-1).toString(), maxDepthStringWidth, maxCountStringWidth, maxCountStringWidth);
		}
		double totalBoardDepth = 0;
		double totalGraphDepth = 0;
		int solvableBoards = 0;
		for(int d=minDepth; d<=maxDepth; d++) {
			String count1, count2;
			if(numBoardsByBoardDepth.containsKey(d)) {
				count1 = numBoardsByBoardDepth.get(d).toString();
				totalBoardDepth += numBoardsByBoardDepth.get(d) * d;
				solvableBoards += numBoardsByBoardDepth.get(d);
			} else {
				count1 = "0";
			}
			if(numBoardsByGraphDepth.containsKey(d)) {
				count2 = numBoardsByGraphDepth.get(d).toString();
				totalGraphDepth += numBoardsByGraphDepth.get(d) * d;
			} else {
				count2 = "0";
			}
			printRow(new Integer(d).toString(), count1, count2, maxDepthStringWidth, maxCountStringWidth, maxCountStringWidth);
		}
		System.out.println("NUMBER OF SOLVABLE BOARDS: " + solvableBoards);
		System.out.println("AVERAGE BOARD DEPTH OF SOLVABLE BOARDS: " + totalBoardDepth/solvableBoards);
		System.out.println("AVERAGE GRAPH DEPTH OF SOLVABLE BOARDS: " + totalGraphDepth/solvableBoards);
	}

	private static void printRow(String col1, String col2, String col3, int col1Width, int col2Width, int col3Width) {
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
		System.out.print(" | ");
		if(col3.length() < col3Width) {
			System.out.print(new String(new char[col3Width - col3.length()]).replace("\0", " "));
		}
		System.out.print(col3);
		System.out.print(" |");
		System.out.println();
	}

	private static void usage() {
		System.err.println(usage);
		System.exit(1);
	}
}
