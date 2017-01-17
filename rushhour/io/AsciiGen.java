package rushhour.io;

import rushhour.core.*;

import java.util.ArrayList;
import java.util.Arrays;


public final class AsciiGen {
	
	final static int len = 6;
	final static int hei = 6;
	//print height
	final static int phei = 9;
	//print length
	final static int plen = 8;
	final static int exitL = 3;

	public static void main(String[] args) {
		
		Board board = BoardIO.read("33moves");
		
		//creates random input grid for now
		Grid inputG = new Grid(len,hei);		
		Grid inputG2 = new Grid(len,hei);
		Grid inputG3 = new Grid(len,hei);
		
		//addCar(inputG2, 1, board.getCars().get(0));
		//addCars(inputG3, board.getCars());

		
		Grid[] mInpG = {inputG, inputG2, inputG3};
		
		//makes 2D grid into printable format
		//String[] printableGrid = getPrintableGrid(inputG);
		
		//print grids individually
		//printGrid(printableGrid);		
		//printGrid(outputGrid(addCar(inputG)));
		
		//ArrayList<Car> carsTest = new ArrayList<Car>();
		
		//print multiple grids out at once
		//printGrids(mInpG);

	}

	/**
	 * Prints multiple grids w/ move counter
	 * @param grids
	 */
	public static void printGrids(Grid[] grids){
		for(int i =0; i < grids.length;i++){
			System.out.println("    Move "+i+"  ");
			printGrid(grids[i]);
		}
		
	}
	
	
	/**
	 * Prints grid
	 * @param out
	 */
	public static void printGrid(Grid grid){
		String[] pGrid = getPrintableGrid(grid);
		for (int i=0; i < phei; i++){
			System.out.println(pGrid[i]);
		}
		
	}
	
	/**
	 * outputs a string[] of 2D char array
	 * @param inputG char[][] input grid 
	 * @return String[] of grid
	 * @todo this prints the transpose of the board. Probably due to the fact that int[][] is really more like (int[])[] so indices are switched around as you read from outside in. 
	 */
	
	public static String[] getPrintableGrid(Grid inputG){
		String[] fin = new String[phei]; //6 rows plus ceiling and floors = 8 for now
		//visual delimiters for top and bottom
		fin[0] = " vvvvvvvvvvvvv";
		fin[7] = " vvvvvvvvvvvvv";
		fin[8] = "";
		//gets individual lines
		for(int i = 0;i<hei;i++){
			fin[i+1] = extractLine(inputG.getRow(i));
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
	
	public static String extractLine(int[] line){
		String t = "| ";
		for(int i=0;i<len;i++){
			if (line[i]==-1) {
				t = t + "  ";
			}
			else { 
				t = t + symbolList().get(line[i]) + " ";
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
	 * @param grid char[][] an array to put cars into
	 * @return char[][] with cars added to it
	 */
	//replace arr with internal call? replace xylenhor with Car object call maybe
	public static Grid addCar(Grid grid, int index, Car car){
		grid.set(car.x, car.y, index);
		if (car.horizontal){ 
			grid.set(car.x+1, car.y, index);
			if (car.length > 2){ grid.set(car.x+2, car.y, index); }
		} else { 
			grid.set(car.x, car.y+1, index); 
			if (car.length > 2){ grid.set(car.x, car.y+2, index);  }
		}		
		
		return grid;
	}
	
	
	/**
	 * Adds multiple cars given an ArrayList<Car> (could be just one car)
	 * @param arr
	 * @param cars
	 * @return char[][] of cars
	 */
	public static Grid addCars(Grid grid, ArrayList<Car> cars){
		for (int i = 0; i< cars.size(); i++){
			addCar(grid, i, cars.get(i));
		}	
		
		return grid;
		
	}
	
	
	/**
	 * Symbols to use for the cars
	 * Add anything you want to be shown
	 * @return
	 */
	
	public static ArrayList<String> symbolList(){
		//change this to whatever you want
		String symbols = "ABCDEFGHIJKLMNOPQRST";
		String[] symbs = symbols.split("");
		ArrayList<String> symb = new ArrayList<String>();
		
		for(int i = 0; i < symbs.length;i++){
			symb.add(symbs[i]);
		}	
		
		return symb;
	}

}
