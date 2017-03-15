package rushhour;

import rushhour.Util;
import rushhour.core.*;
import rushhour.io.*;
import rushhour.solving.*;
import rushhour.learning.*;
import rushhour.generation.*;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collections;
import java.nio.file.Path;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class Main {
	private static final String usage =
		"Usage: java rushhour.Main [OPERATION] [ARGUMENTS]\n" + 
		"Supported operations:\n" +
		"\tprint <puzzle_file>\n" +
		"\tinfo <puzzle_file>\n" +
		"\tsolve <puzzle_file> [ --equiv | --ids | --bfs ] <puzzle_file> \n" +
		"\tsolve <puzzle_file> --astar --features <features> --weights <weights>\n" +
		"\tsolve <puzzle_file> --astar --features <features> --weightsFile <weights_file>\n" +
		"\tlearn <dataset> --features <features> [ --outFile <weights_file> ]\n" +
		"\ttest <dataset>  --features <features> --weights <weights> [LEARNING_OPTIONS]\n" +
		"\ttest <dataset>  --features <features> --weightsFile <weights_file> [LEARNING_OPTIONS]\n" +
		"Learning Options: \n" +
		"--regularize\n" +
		"--learningRate ALPHA\n" +
		"--complexityPenalty LAMBDA\n" +
		"--lossQ N\n" +
		"--regularizationQ N\n" +
		ConstraintSatisfier.usage;

	public static void main(String[] args) {
		if(args.length > 1) {
			String operation = args[0];
			String puzzleFile = null;
			if(operation.equals("print")) {
				puzzleFile = args[1];
				Board b = BoardIO.read(puzzleFile);
				System.out.println(b.toString());
			} else if(operation.equals("solve")) {
				Solver solver = null;
				if(args.length == 2) {
					puzzleFile = args[1];
					solver = new BreadthFirstSearchSolver();
				} else if(args.length >= 3) {
					if(args[1].equals("--equiv")) {
						solver = new EquivalenceClassSolver();
					} else if(args[1].equals("--ids")) {
						solver = new IterativeDeepeningSolver();
					} else if(args[1].equals("--bfs")) {
						solver = new BreadthFirstSearchSolver();
					} else if(args[1].equals("--astar")) {
						if(args.length < 4) {
							System.err.println("need feature list");
							System.exit(1);
						}
						Feature[] features = null;
						double[] weights = null;
						for(int i=3; i<args.length; i++) {
							if(args[i].equals("--features")) {
								features = Feature.vectorFromString(args[i+1]);
								i++;
							} else if(args[i].equals("--weights")) {
								weights = Util.vectorFromString(args[i+1]);
								if(weights.length != features.length) {
									System.err.println("different number of weights than features");
									System.exit(1);
								}
								i++;
							} else if(args[i].equals("--weightsFile")) {
								weights = Util.vectorFromFile(args[i+1]);
								i++;
							} else {
								System.err.println("unrecognized solve option " + args[i]);
								System.exit(1);
							}
						}
						Heuristic heuristic = new Heuristic(features, weights);
						solver = new AStarSearchSolver(heuristic);
					} else {
						System.err.println("unrecognized solver name");
						usage();
					}
					puzzleFile = args[2];
				}
				Board board = BoardIO.read(puzzleFile);
				SolveResult solution = solver.getSolution(board);
				System.out.println("path length: " + solution.path.size() + " moves");
				System.out.println("visited states: " + solution.visitedStates);
				System.out.println("solved board: ");
				System.out.println(solution.solvedBoard);
				// sanity check
				for(Move move : solution.path) {
					board.move(move);
				}
				if(!board.isSolved()) {
					System.err.println("ERROR: not actually a solution!");
				}
				if(!board.equals(solution.solvedBoard)) {
					System.err.println("ERROR: solved board not the result of following solution path");
				}
			} else if(operation.equals("features")) {
				if(args.length < 2) {
					System.err.println("need feature list");
					System.exit(1);
				}
				Board board = BoardIO.read(args[args.length-1]);
				System.out.println(board);
				Feature[] features = Feature.vectorFromString(args[1]);
				for(int i=0; i<features.length; i++) {
					System.out.println(features[i].toString() + ": " + features[i].value(board));
				}
			} else if(operation.equals("learn")) {
				Feature[] features = null;
				String outFileName = null;
				boolean regularize = false;
				Learner learner = null;
				// defaults
				double learningRate = 0.1;
				double complexityPentalty = 1.0;
				int regularizationQ = 2;
				int lossQ = 2;
				Dataset trainingSet = new Dataset(args[1]);
				Dataset devSet = null;
				for(int i=2; i<args.length; i++) {
					if(args[i].equals("--features")) {
						features = Feature.vectorFromString(args[++i]);
					} else if(args[i].equals("--outFile")) {
						outFileName = args[++i];
					} else if(args[i].equals("--regularize")) {
						regularize = true;
					} else if(args[i].equals("--learningRate")) {
						learningRate = Double.parseDouble(args[++i]);
					} else if(args[i].equals("--complexityPenalty")) {
						complexityPentalty = Double.parseDouble(args[++i]);
					} else if(args[i].equals("--regularizationQ")) {
						regularizationQ = Integer.parseInt(args[++i]);
					} else if(args[i].equals("--lossQ")) {
						lossQ = Integer.parseInt(args[++i]);
					} else if(args[i].equals("--devSet")) {
						devSet = new Dataset(args[++i]);
					} else {
						System.err.println("unrecognized learn option " + args[i]);
						System.exit(1);
					}
				}
				if(features == null) {
					System.err.println("need feature list");
					System.exit(1);
				}
				if(regularize) {
					learner = new RegularizedMultivariateRegressionLearner(features, learningRate, complexityPentalty, regularizationQ, lossQ);
				} else {
					learner = new MultivariateRegressionLearner(features);
				}
				Heuristic heuristic = learner.learn(trainingSet);
				System.out.println("learned weights: " + Arrays.toString(heuristic.getWeights()));
				// write to file
				if(outFileName != null) {
					Util.writeToFile(Util.vectorToString(heuristic.getWeights()), outFileName);
				}
				// display error
				for(int q=1; q<=2; q++) {
					System.out.println("L" + q + " error: ");
					System.out.println("\ttraining data: " + trainingSet.getMeanError(heuristic, q));
					if(devSet != null) {
						System.out.println("\tdevelopment data: " + devSet.getMeanError(heuristic, q));
					}
				}
			} else if(operation.equals("test")) {
				if(args.length != 4) {
					usage();
				}
				Dataset dataset = new Dataset(args[1]);
				Feature[] features = Feature.vectorFromString(args[2]);
				double[] weights = Util.vectorFromFile(args[3]);
				Heuristic heuristic = new Heuristic(features, weights);
				double L1error = dataset.getMeanError(heuristic, 1);
				double L2error = dataset.getMeanError(heuristic, 2);
				System.out.println("L1 error: " + L1error);
				System.out.println("L2 error: " + L2error);
			} else if(operation.equals("generate")) {
				ConstraintSatisfier csf = new ConstraintSatisfier();
				if(csf.readArgs(args)) {
					csf.satisfy();
				} else {
					usage();
				}
			} else if(operation.equals("check-unique")) {
				if(args.length == 3) {
					List<Path> oldPaths = Util.getFilePaths(args[1]);
					List<Path> newPaths = Util.getFilePaths(args[2]);
					Map<Long,Path> oldHashes = new HashMap<>();
					for(Path path : oldPaths) {
						oldHashes.put(BoardIO.read(path.toAbsolutePath().toString()).getEquivalenceClass().hash(), path);
					}
					boolean allUnique = true;
					for(Path path : newPaths) {
						Long newHash = BoardIO.read(path.toAbsolutePath().toString()).getEquivalenceClass().hash();
						if(oldHashes.containsKey(newHash)) {
							allUnique = false;
							System.out.println(path.toString() + " is in the same equivalence class as " + oldHashes.get(newHash).toString());
						}
					}
					if(allUnique) {
						System.out.println("all unique");
					}
				} else {
					usage();
				}
			} else if(operation.equals("info")) {
				puzzleFile = args[1];
				Board board = BoardIO.read(puzzleFile);
				EquivalenceClass graph = board.getEquivalenceClass();
				System.err.println("graph size: " + graph.size());
				System.err.println("graph depth: " + graph.maxDepth());
				System.err.println("board depth: " + graph.getDepthOfBoard(board));
				System.err.println("graph solutions: " + graph.solutions().size());
			} else if(operation.equals("test_old")) {
				puzzleFile = args[1];
				Board board = BoardIO.read(puzzleFile);
				System.err.println("finding solutions...");
				SolvedBoardGraph graph = SolvedBoardGraph.create(board);
				if(graph == null) {
					graph = SolvedBoardGraph.create(board.getEquivalenceClass().solutions().iterator().next());
				}
				int toDepth = Integer.parseInt(args[2]);
				System.err.println("propogating to depth " + toDepth + "...");
				graph.propogateDepths(toDepth);
				System.err.println("board depth: " + graph.getDepthOfBoard(board));
				System.err.println("graph depth: " + graph.maxDepth());
				System.err.println("graph size: " + graph.size());
				System.err.println("graph solutions: " + graph.solutions().size());
				System.err.println("farthest depth: " + graph.getDepthOfBoard(graph.getFarthest()));
			} else {
				usage();
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
