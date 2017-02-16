package rushhour.generation;

import rushhour.Util;
import rushhour.core.*;
import rushhour.io.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collections;
import java.io.File;
import java.util.Set;
import java.util.HashSet;
import java.nio.file.Path;

public class ConstraintSatisfier {

	public static final String usage = 
		"\tgenerate [GENERATION OPTIONS]\n\n" + 
		"Generation Options: \n" +
		"--numBoards NUM        Produce exactly NUM boards (will differ from total number of boards generated if constraints are given.)\n\n" +
		"--minDepth DEPTH       Only produce boards with depth of at least DEPTH\n\n" +
		"--maxPerDepth NUM      Produce at most NUM boards of each depth\n\n" +
		"--numCars NUM          Produce boards with exactly NUM cars\n\n" +
		"--minNumCars NUM       Produce boards with at least NUM cars (minimum=0, maximum=18)\n\n" +
		"--maxnNumCars NUM      Produce boards with at most NUM cars (minimum=0, maximum=18)\n\n" +
		"--unique               Only produce boards that exist within unique equivalence classes\n\n" +
		"--prevGraphs DIR       Only produce boards that exist within different equivalence classes than the board(s) in DIR\n\n" +
		"--maxVipX X            Only generate boards with vip.x <= X\n\n" +
		"--useHeuristics        Use heuristics to avoid generating unsolvable boards\n\n" +
		"--stats                Print statistics about the boards that were produced\n\n" +
		"--fullStats            Same as --stats but also print statistics about boards that were generated but not produced\n\n" +
		"--quiet                Don't print anything while generation is running\n\n" +
		"--puzzleFile           Dump each produced board to a puzzle file at ./generated_puzzles/<depth>/<index>.txt";

	// options
	private int maxVipX = 4; 
	private boolean onlySolvable = false;
	private boolean onlyUnique = false;
	private boolean useHeuristics = false; 
	private boolean setNumCars = false;
	private int targetNumCars = 11; // reasonable default
	private int minNumCars = 9; // reasonable default
	private int maxNumCars = 15; // reasonable default
	private int boardsToSave = 1; // reasonable default
	private int minDepth = -1; // reasonable default
	private boolean stats = false;
	private boolean fullStats = false;
	private int maxBoardsPerDepth = -1;
	private boolean puzzleOutToFile = false;
	private boolean quiet = false;
	private Set<Long> prevGraphs = new HashSet<>();
	private String prevGraphsDir = null;

	// STATS STUFF //
	// total counts
	private int totalBoardsGenerated = 0;
	private Map<Integer,Integer> numBoardsByBoardDepth = new HashMap<>();
	private Map<Integer,Integer> numBoardsByGraphDepth = new HashMap<>();
	private Set<Long> uniqueGraphs = new HashSet<>();
	private // per-numCars counts
	Map<Integer,Integer> totalBoardsGeneratedByNumCars = new HashMap<>();
	private Map<Integer,Integer> boardsSavedSoFarByNumCars = new HashMap<>();
	private Map<Integer,Map<Integer,Integer>> numBoardsByBoardDepthByNumCars = new HashMap<>();
	private Map<Integer,Map<Integer,Integer>> numBoardsByGraphDepthByNumCars = new HashMap<>();
	private Map<Integer,Integer> uniqueGraphsByNumCars = new HashMap<>();
	private // saved boards counts
	int boardsSavedSoFar = 0;
	private Map<Integer,Integer> boardsSavedSoFarByBoardDepth = new HashMap<>(); // for outputBoard filenames
	private Map<Integer,Integer> boardsSavedSoFarByGraphDepth = new HashMap<>();
	private Set<Long> uniqueGraphsOfSavedBoards = new HashSet<>(); // only for stats purposes

	public ConstraintSatisfier() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if(stats) {
					printAllStats();
				}
			}
		});
	}

	public boolean readArgs(String[] args) {
		if(args.length < 3) {
			return false;
		}
		for(int i=1; i<args.length; i++) {
			if(args[i].equals("--numBoards")) {
				this.boardsToSave = Integer.parseInt(args[i+1]);
				i++;
			} else if(args[i].equals("--unique")) {
				this.onlyUnique = true;
			} else if(args[i].equals("--maxPerDepth")) {
				this.maxBoardsPerDepth = Integer.parseInt(args[i+1]);
				i++;
			} else if(args[i].equals("--useHeuristics")) {
				this.useHeuristics = true;
			} else if(args[i].equals("--maxVipX")) {
				this.maxVipX = Integer.parseInt(args[i+1]);
				i++;
			} else if(args[i].equals("--numCars")) {
				this.targetNumCars = Integer.parseInt(args[i+1]);
				if(this.targetNumCars > 18) {
					System.err.println("No such boards exist, sorry!");
					System.exit(0);
				}
				this.setNumCars = true;
				i++;
			} else if(args[i].equals("--prevGraphs")) {
				this.prevGraphsDir = args[i+1];
				i++;
			} else if(args[i].equals("--minNumCars")) {
				this.minNumCars = Integer.parseInt(args[i+1]);
				i++;
			} else if(args[i].equals("--maxNumCars")) {
				this.maxNumCars = Integer.parseInt(args[i+1]);
				i++;
			} else if(args[i].equals("--minDepth")) {
				this.minDepth = Integer.parseInt(args[i+1]);
				i++;
			} else if(args[i].equals("--stats")) {
				this.stats = true;
			} else if(args[i].equals("--fullStats")) {
				this.fullStats = true;
				this.stats = true;
			} else if(args[i].equals("--puzzleFile")) {
				this.puzzleOutToFile = true;
			} else if(args[i].equals("--quiet")) {
				this.quiet = true;
			} else {
				System.err.println("unrecognized option '" + args[i] + "'");
				return false;
			}
		}
		return true;
	}

	public void satisfy() {
		if(prevGraphsDir != null) {
			if(!quiet) {
				System.err.println("importing prevGraphs...");
			}
			List<Path> paths = Util.getFilePaths(prevGraphsDir);
			for(Path path : paths) {
				this.prevGraphs.add(BoardIO.read(path.toAbsolutePath().toString()).getGraph().hash());
			}
			if(!quiet) {
				System.err.println("done.");
			}
		}
		// initialize some values
		for(int i=0; i<=18; i++) {
			numBoardsByBoardDepthByNumCars.put(i, new HashMap<Integer,Integer>());
			numBoardsByGraphDepthByNumCars.put(i, new HashMap<>());
		}
		BoardGenerator gen = new BoardGenerator(maxVipX, useHeuristics);
		Random rng = new Random();
		// it's generation time!
		while(boardsSavedSoFar < boardsToSave) {
			// generate a random board
			if(!setNumCars) {
				targetNumCars = rng.nextInt(maxNumCars - minNumCars + 1) + minNumCars;
			}
			Board randomBoard = gen.generate(targetNumCars);
			BoardGraph graph = randomBoard.getGraph();
			Long hash = graph.hash();
			int graphDepth = graph.maxDepth();
			int randomBoardDepth = graph.getDepthOfBoard(randomBoard);
			Board outputBoard = randomBoard;
			int outputBoardDepth = randomBoardDepth;
			boolean keepBoard = true; // whether or not we're going to save outputBoard
			// compute keepBoard
			if(prevGraphs.contains(hash)) {
				// make sure the graph isn't that of any of the boards we've been told to skip
				keepBoard = false;
			} if(onlyUnique && uniqueGraphs.contains(hash)) {
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
							outputBoardDepth--;
							if(outputBoardDepth < minDepth) {
								keepBoard = false;
								break;
							} else if(outputBoardDepth == 0) {
								break;
							}
						}
					}
				}
			}
			int numCars = randomBoard.numCars();
			// save it
			if(keepBoard) {
				// update stats that are always needed for this stuff (for indexing output boards)
				boardsSavedSoFar++;
				incrementMapValue(boardsSavedSoFarByBoardDepth, outputBoardDepth);
				// print it
				if(!quiet) {
					System.err.println();
					System.err.println("board " + boardsSavedSoFar);
					System.err.println(AsciiGen.getGridString(outputBoard));
					System.err.println("numCars: " + numCars);
					System.err.println("board depth: " + outputBoardDepth + ", graph depth: " + graphDepth);
				}
				// dump board to file
				if(puzzleOutToFile) {
					// write the board to a file
					int index = boardsSavedSoFarByBoardDepth.get(outputBoardDepth);
					String pathName = "generated_puzzles/" + outputBoardDepth + "/";
					File outDir = new File(pathName);
					outDir.mkdirs();
					String filename = pathName + index + ".txt";
					if(!quiet) {
						System.err.println("writing to '" + filename + "'...");
					}
					BoardIO.write(filename, outputBoard);
				}
			}
			// update stats
			if(stats) {
				// counts for total boards and for each numCars
				if(fullStats) {
					if(!uniqueGraphs.contains(hash)) {
						incrementMapValue(uniqueGraphsByNumCars, numCars);
					}
					incrementMapValue(totalBoardsGeneratedByNumCars, numCars);
					incrementMapValue(numBoardsByBoardDepth, randomBoardDepth);
					incrementMapValue(numBoardsByBoardDepthByNumCars.get(numCars), randomBoardDepth);
					incrementMapValue(numBoardsByGraphDepth, graphDepth);
					incrementMapValue(numBoardsByGraphDepthByNumCars.get(numCars), graphDepth);
				}
				// counts for saved boards only
				if(keepBoard) {
					uniqueGraphsOfSavedBoards.add(hash);
					incrementMapValue(boardsSavedSoFarByNumCars, numCars);
					incrementMapValue(boardsSavedSoFarByGraphDepth, graphDepth);
					// recall that boardsSavedSoFarByBoardDepth and boardsSavedSoFar are always updated in if(keepBoard) above
				}
				// general stats
				totalBoardsGenerated++;
				uniqueGraphs.add(hash); // needs to happen after the containment check above
			}
		}
	}

	private void printAllStats() {
		System.out.println();
		System.out.println(">>>>> STATS <<<<<");
		System.out.println();
		if(fullStats) {
			int minGeneratedNumCars = Collections.min(numBoardsByBoardDepthByNumCars.keySet());
			int maxGeneratedNumCars = Collections.max(numBoardsByBoardDepthByNumCars.keySet());
			for(int numCars = minGeneratedNumCars; numCars <= maxGeneratedNumCars; numCars++) {
				if(uniqueGraphsByNumCars.get(numCars) == null) {
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
		}
		printStats("=== SAVED ===", boardsSavedSoFar, uniqueGraphsOfSavedBoards.size(), boardsSavedSoFarByBoardDepth, boardsSavedSoFarByGraphDepth);
	}

	private static void incrementMapValue(Map<Integer,Integer> map, int key) {
		if(!map.containsKey(key)) {
			map.put(key, 0);
		}
		map.put(key, map.get(key)+1);
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

	private static void printStats(String desc, int totalBoardsGenerated, int numUniqueGraphs, Map<Integer,Integer> numBoardsByBoardDepth, Map<Integer,Integer> numBoardsByGraphDepth) {
		System.out.println(desc);
		System.out.println("NUMBER OF BOARDS GENERATED: " + totalBoardsGenerated);
		if(!numBoardsByGraphDepth.keySet().isEmpty()) {
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
	}

}
