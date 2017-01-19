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
				BoardGraph g = new BoardGraph(b);
				List<Move> path = g.pathToNearestSolution(b);
				for(Move m : path) {
					System.out.println(m);
				}
			} else if(operation.equals("evaluate")) {
			} else if(operation.equals("analyze")) {
			}
		}
	}
}
