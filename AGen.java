import java.util.ArrayList;


public final class AGen {
	
	final static int len = 6;
	final static int hei = 6;
	//print height
	final static int phei = 9;
	//print length
	final static int plen = 8;
	final static int exitL = 3;

	public static void main(String[] args) {		
		
		//creates random input grid for now
		Grid inputG = new Grid(len,hei);
		
		//makes 2D grid into printable format
		String[] out = outputGrid(inputG);
		
		//print grids individually
		//printGrid(out);		
		//printGrid(outputGrid(addCar(inputG)));
		ArrayList<Car> carsTest = new ArrayList<Car>();
		
		//print multiple grids out at once
		//int[][] grids = {out, outputGrid(addCars(inputG, carsTest))};
		//printMG(grids);

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
	 * @todo this prints the transpose of the board. Probably due to the fact that int[][] is really more like (int[])[] so indices are switched around as you read from outside in. 
	 */
	
	public static String[] outputGrid(Grid inputG){
		String[] fin = new String[phei]; //6 rows plus ceiling and floors = 8 for now
		//visual delimiters for top and bottom
		fin[0] = " vvvvvvvvvvvvv";
		fin[7] = " vvvvvvvvvvvvv";
		fin[8] = "";
		//gets individual lines
		for(int i = 0;i<hei;i++){
			fin[i+1] = extLine(inputG.getRow(i));
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
	
	public static String extLine(int[] line){
		String t = "| ";
		for(int i=0;i<len;i++){
			if (line[i]==-1) {
				t = t + "  ";
			}
			else { 
				t = t + Integer.toString(line[i]) + " ";
			}
		}
		t = t + "|";
		
		return t;
	}
	
	
	/** creates empty char[][] 6x6 currently
	 * @return empty char[len][hei]
	 */
	public static int[][] emptyGrid(){
		int[][] arr = new int[len][hei];
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
	//replace arr with internal call? replace xylenhor with Car object call maybe
	public static int[][] addCar(int[][] arr, int index, int x, int y, int len, boolean hor){
		int ind = (int)index;
		arr[x][y] = ind;
		if (hor){ 
			arr[x+1][y] = ind; 
			if (len > 2){ arr[x+2][y] = ind; }
		} else { 
			arr[x][y+1] = ind; 
			if (len > 2){ arr[x][y+2] = ind;  }
		}		
		
		//arr[2][3] = 'A';
		//arr[2][4] = 'A';

		
		return arr;
	}
	
	
	/**
	 * Adds multiple cars given an ArrayList<Car> (could be just one car)
	 * @param arr
	 * @param cars
	 * @return char[][] of cars
	 */
	public static int[][] addCars(int[][] arr, ArrayList<Car> cars){
		for (int i = 0; i< cars.size(); i++){
			Car car = cars.get(i);
			addCar(arr, i, car.x, car.y, car.length, car.horizontal);
		}	
		
		return arr;
		
	}

}
