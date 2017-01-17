package rushhour;

import rushhour.core.*;
import rushhour.io.*;
import rushhour.evaluation.*;

import java.util.ArrayList;

public class Testing {
	public static void main(String[] args) {
		Board b = BoardIO.read(args[0]);
		Evaluator e = new AverageBranchingFactorOnPathToSolutionEvaluator();
		System.out.println(e.eval(b));
	}
}
