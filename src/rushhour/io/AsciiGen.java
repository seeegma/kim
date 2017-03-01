package rushhour.io;

import rushhour.core.*;

import java.util.ArrayList;

public final class AsciiGen {

	private static final char[] symbols = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','x','y','z'};
	
	public static String getGridString(Board board){
		int height = board.getHeight() + 2;
		int width = board.getWidth()*2 + 5;
		int offset = board.getOffset() + 1;
		StringBuilder[] rows = new StringBuilder[height];
		for(int i=0; i<height; i++) {
			rows[i] = new StringBuilder();
		}
		// left corners
		rows[0].append(" .");
		rows[height-1].append(" `");
		// top and bottom walls
		for(int i=0; i<width-4; i++) {
			rows[0].append("=");
			rows[height-1].append("=");
		}
		// right corners
		rows[0].append(".");
		rows[height-1].append("`");
		// left wall
		for(int i=1; i<1+board.getHeight(); i++) {
			rows[i].append("|| ");
		}
		// now, the cars!
		for(int i=1; i<1+board.getHeight(); i++){
			rows[i].append(extractLine(board.getGrid().getRow(i-1)));
		}
		// right wall
		for(int i=1; i<1+board.getHeight(); i++) {
			rows[i].append("||");
		}
		// marking the exit path
		rows[offset].delete(width-2, rows[offset].length());

		// put it all together
		StringBuilder ret = new StringBuilder();
		int i;
		for(i=0; i<rows.length-1; i++) {
			ret.append(rows[i].toString());
		    ret.append("\n");
		}
		ret.append(rows[i].toString());
		return ret.toString();
	}

	private static StringBuilder extractLine(int[] line){
		StringBuilder t = new StringBuilder();
		for(int i=0; i<line.length; i++){
			if(line[i]==-1) {
				t.append("  ");
			} else { 
				t.append(symbols[line[i]]);
				t.append(" ");
			}
		}
		return t;
	}
	
}
