import java.util.Random;

import rushhour.generation.*;
import rushhour.io.*;
import rushhour.core.*;

public class MainPuzzGen {

	public static void main(String[] args) {
		
		Random r = new Random();
		RandomBoardGen rgn;
		BoardGraph bg;
		Board b;
		boolean test;
		
		int i = 0;
		while(i < 100){
			
//			int v = r.nextInt(4);
			rgn = new RandomBoardGen(6,6,11);
			test = rgn.generateBoard();
			
			b = rgn.getBoard();
			bg = new BoardGraph(b);
			
			
			int maxD = bg.maxDepth();			
			
			if (maxD > 30){
				System.out.println(i+" mD: "+ maxD);
				BoardIO.write("Moves"+maxD+"ID"+(i+100)+".txt", b);
				i++;
			}
		}

	}

}
