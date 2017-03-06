package rushhour.core;

import java.util.Arrays;

/**
 * Container class for the board grid. Mostly exists so we can retrieve from
 * the grid using (x, y) coordinates instead of (y, x).
 */
public class Grid {

	public int height, width;
	int[][] matrix;
	private final int EMPTY_SPOT = -1;

	public Grid(int width, int height) {
		this.height = height;
		this.width = width;
		this.matrix = new int[height][width];
		this.clear();
	}

	public Grid(int width, int height, int[][] matrix) {
		this.height = height; 
		this.width = width;
		this.matrix = matrix;
	}

	public int get(int x, int y) {
		return matrix[y][x];
	}

	public void set(int x, int y, int val) {
		this.matrix[y][x]=val;
	}

	public int[] getRow(int y) {
		return matrix[y];
	}

	public int[] getColumn(int x) {
		int[] col = new int[height];
		for (int i = 0; i < height; i++) {
			col[i]= this.matrix[i][x];
		}
		return col;
	}

	public void clear() {
		for (int i = 0; i < this.height; i++) { // assumes it's a matrix
			for (int j = 0; j < this.width; j++) {
				this.set(j,i,EMPTY_SPOT);
			}
		}
	}

	public boolean equals(Grid g) {
		return Arrays.deepEquals(this.matrix, g.matrix);
	}

	public Grid copy() {
		int[][] newGrid = new int[this.height][this.width];
        for (int i=0;i<height;i++) {
        	for (int j=0;j<width;j++){
            	newGrid[i][j]=this.matrix[i][j];
            }
        }
        return (new Grid(this.width, this.height, newGrid));
	}

	public long hash() {
		long result = 1;
		for (int i = 0; i < this.height; i++) { // assumes it's a matrix
			for (int j = 0; j < this.width; j++) {
				result = result*15 + this.get(j,i);
			}
		}
		return result;
	}

	private static final char[] symbols = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','x','y','z'};
	
	public String toString() {
		int height = this.height + 2;
		int width = this.width*2 + 5;
		int offset = (this.height+1)/2;
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
		for(int i=1; i<1+this.height; i++) {
			rows[i].append("|| ");
		}
		// now, the cars!
		for(int i=1; i<1+this.height; i++){
			rows[i].append(extractLine(this.getRow(i-1)));
		}
		// right wall
		for(int i=1; i<1+this.height; i++) {
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
