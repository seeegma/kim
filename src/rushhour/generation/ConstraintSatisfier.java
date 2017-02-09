package rushhour.generation;

import rushhour.core.*;
import rushhour.io.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collections;
import java.io.File;
import java.util.Set;
import java.util.HashSet;

public class ConstraintSatisfier {

	// options
	public boolean nontrivial = false; 
	public boolean onlySolvable = false;
	public boolean onlyUnique = false;
	public boolean useHeuristics = false; 
	public boolean setNumCars = false;
	public int targetNumCars = 11; // reasonable default
	public int minNumCars = 9; // reasonable default
	public int maxNumCars = 15; // reasonable default
	public int boardsToSave = 1; // reasonable default
	public int minDepth = -1; // reasonable default
	public boolean stats = false;
	public boolean fullStats = false;
	public int maxBoardsPerDepth = -1;
	public boolean puzzleOutToFile = false;
	public boolean quiet = false;
	public Set<Long> prevGraphs = new HashSet<>();

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
	// saved boards counts
	int boardsSavedSoFar = 0;
	Map<Integer,Integer> boardsSavedSoFarByBoardDepth = new HashMap<>(); // for outputBoard filenames
	Map<Integer,Integer> boardsSavedSoFarByGraphDepth = new HashMap<>();
	Set<Long> uniqueGraphsOfSavedBoards = new HashSet<>(); // only for stats purposes

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

	public void satisfy() {
		// initialize some values
		for(int i=0; i<=18; i++) {
			totalBoardsGeneratedByNumCars.put(i, 0);
			boardsSavedSoFarByNumCars.put(i, 0);
			numBoardsByBoardDepthByNumCars.put(i, new HashMap<Integer,Integer>());
			numBoardsByGraphDepthByNumCars.put(i, new HashMap<>());
			uniqueGraphsByNumCars.put(i, 0);
		}
		BoardGenerator gen = new BoardGenerator(nontrivial, useHeuristics);
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
