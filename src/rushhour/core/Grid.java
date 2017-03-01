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

}
