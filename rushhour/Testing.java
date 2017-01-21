package rushhour;

import rushhour.core.*;
import rushhour.io.*;
import rushhour.evaluation.*;

import java.util.ArrayList;
import java.util.List;

public class Testing {
	public static void main(String[] args) {
		Board b = BoardIO.read(args[0]);
		BoardGraph g = new BoardGraph(b);
		List<Move> path = g.solve(g.getVertex(b));
		for(Move m : path) {
			System.out.println(m);
		}
		System.out.println(path.size());
	}
}
