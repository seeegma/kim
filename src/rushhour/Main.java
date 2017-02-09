package rushhour;

import rushhour.core.*;
import rushhour.io.*;
import rushhour.evaluation.*;
import rushhour.analysis.*;
import rushhour.generation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collections;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.io.IOException;
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
				// evaluators.add(new NumberOfCarsEvaluator());
				// evaluators.add(new NumberOfLongCarsEvaluator());
				evaluators.add(new MinMovesToSolutionEvaluator());
				// evaluators.add(new MinSlidesToSolutionEvaluator());
				// evaluators.add(new AverageBranchingFactorEvaluator());
				// evaluators.add(new AverageBranchingFactorOnPathToSolutionEvaluator());
				// evaluators.add(new IrrelevancyEvaluator());
				// evaluators.add(new DFSEvaluator());
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
				ConstraintSatisfier csf = new ConstraintSatisfier();
				for(int i=1; i<args.length; i++) {
					if(args[i].equals("--numBoards")) {
						csf.boardsToSave = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--unique")) {
						csf.onlyUnique = true;
					} else if(args[i].equals("--maxPerDepth")) {
						csf.maxBoardsPerDepth = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--useHeuristics")) {
						csf.useHeuristics = true;
					} else if(args[i].equals("--nontrivial")) {
						csf.nontrivial = true;
					} else if(args[i].equals("--numCars")) {
						csf.targetNumCars = Integer.parseInt(args[i+1]);
						if(csf.targetNumCars > 18) {
							System.err.println("No such boards exist, sorry!");
							System.exit(0);
						}
						csf.setNumCars = true;
						i++;
					} else if(args[i].equals("--prevGraphs")) {
						List<Path> paths = getFilePaths(args[i+1]);
						for(Path path : paths) {
							csf.prevGraphs.add(BoardIO.read(path.toAbsolutePath().toString()).getGraph().hash());
						}
					} else if(args[i].equals("--minNumCars")) {
						csf.minNumCars = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--maxNumCars")) {
						csf.maxNumCars = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--minDepth")) {
						csf.minDepth = Integer.parseInt(args[i+1]);
						i++;
					} else if(args[i].equals("--stats")) {
						csf.stats = true;
					} else if(args[i].equals("--fullStats")) {
						csf.fullStats = true;
						csf.stats = true;
					} else if(args[i].equals("--puzzleFile")) {
						csf.puzzleOutToFile = true;
					} else if(args[i].equals("--quiet")) {
						csf.quiet = true;
					} else {
						System.err.println("unrecognized option '" + args[i] + "'");
						usage();
					}
				}
				csf.satisfy();
			} else if(operation.equals("check-unique")) {
				if(args.length == 3) {
					List<Path> oldPaths = getFilePaths(args[1]);
					List<Path> newPaths = getFilePaths(args[2]);
					Map<Long,Path> oldHashes = new HashMap<>();
					for(Path path : oldPaths) {
						oldHashes.put(BoardIO.read(path.toAbsolutePath().toString()).getGraph().hash(), path);
					}
					boolean allUnique = true;
					for(Path path : newPaths) {
						Long newHash = BoardIO.read(path.toAbsolutePath().toString()).getGraph().hash();
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
			} else {
				usage();
			}
		}
	}

	private static List<Path> getFilePaths(String filename) {
		Path path = Paths.get(filename);
		if(path.toFile().isDirectory()) {
			return getFilePathsHelper(new LinkedList<Path>(), path);
		} else {
			List<Path> ret = new LinkedList<>();
			ret.add(path);
			return ret;
		}
	}

	private static List<Path> getFilePathsHelper(List<Path> ret, Path dir) {
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for(Path path : stream) {
				if(path.toFile().isDirectory()) {
					getFilePathsHelper(ret, path);
				} else {
					ret.add(path);
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return ret;
	} 

	private static void usage() {
		System.err.println(usage);
		System.exit(1);
	}
}
