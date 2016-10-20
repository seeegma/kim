import java.util.Arrays;

public class Grid {
	int height, width;
	int[][] matrix;


	public Grid(int width, int height) {
		this.height = height;
		this.width = width;
		this.matrix = new int[height][width];
		for (int i=0;i<height;i++) {
			for (int j=0;j<width;j++) {
				this.matrix[i][j]=-1;
			}
		}
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


	public boolean equals(Grid g) {
		return Arrays.deepEquals(this.matrix, g.matrix);
	}

	public Grid copy() {
		int[][] newGrid = new int[this.height][this.width];
        for (int i=0;i<height;i++) {
            newGrid[i]=this.matrix[i].clone();
        }
        return (new Grid(this.width, this.height, newGrid));
	}

	public int hash() {
		return Arrays.deepHashCode(this.matrix);
	}

}