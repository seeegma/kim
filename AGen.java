
public class AGen {
	
	final static int len = 6;
	final static int hei = 6;
	//print height
	final static int phei = 9;
	//print length
	final static int plen = 8;
	final static int exitL = 2;

	public static void main(String[] args) {
		
		//creates random input grid for now
		char[][] inputG = emptyGrid();
		
		//makes 2D grid into printable format
		String[] out = outputGrid(inputG);
		
		//print grids individually
		//printGrid(out);		
		//printGrid(outputGrid(addCar(inputG)));
		
		//print multiple grids out at once
		String[][] grids = {out, outputGrid(addCar(inputG))};
		printMG(grids);

	}
	
	/**
	 * Prints multiple grids w/ move counter
	 * @param grids
	 */
	public static void printMG(String[][] grids){
		for(int i =0; i < grids.length;i++){
			System.out.println("    Move "+i+"  ");
			printGrid(grids[i]);
		}
		
	}
	
	/**
	 * Prints grid
	 * @param out
	 */
	public static void printGrid(String[] out){
		for (int i=0; i < phei; i++){
			System.out.println(out[i]);
		}
		
	}
	
	/**
	 * outputs a string[] of 2D char array
	 * @param inputG char[][] input grid 
	 * @return String[] of grid
	 */
	
	public static String[] outputGrid(char[][] inputG){
		String[] fin = new String[phei]; //6 rows plus ceiling and floors = 8 for now
		//visual delimiters for top and bottom
		fin[0] = " vvvvvvvvvvvvv";
		fin[7] = " vvvvvvvvvvvvv";
		fin[8] = "";
		//gets individual lines
		for(int i = 0;i<hei;i++){
			fin[i+1] = extLine(inputG[i]);
		}
		
		//marking the exit path
		fin[exitL] = fin[exitL] + "==>";
		
		return fin;
	}
	
	/**
	 * extracts visual grid String from char[]
	 * @param line char[]
	 * @return String in visual format
	 */
	
	public static String extLine(char[] line){
		String t = "| ";
		for(int i=0;i<len;i++){
			t = t + Character.toString(line[i]) + " ";
		}
		t = t + "|";
		
		return t;
	}
	
	
	/** creates empty char[][] 6x6 currently
	 * @return empty char[len][hei]
	 */
	public static char[][] emptyGrid(){
		char[][] arr = new char[len][hei];
		for(int i=0;i<len;i++){
			for (int j=0;j<hei;j++){
				arr[i][j] = '_';
			}
		}
		
		return arr;
	}
	
	/** for now just adds cars manually by user
	 * code for testing out visual
	 * @param arr char[][] an array to put cars into
	 * @return char[][] with cars added to it
	 */
	public static char[][] addCar(char[][] arr){//, int x, int y, int len, int hor){
		arr[2][3] = 'A';
		arr[2][4] = 'A';
		arr[0][0] = 'B';
		arr[1][0] = 'B';
		
		return arr;
	}

}
