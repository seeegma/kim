import rushhour.core.*;

import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

public class NumBoards {
	
	Random rando = new Random();

	// public Car randomNewCar() {
	// 	Car newCar = new Car();
	// 	newCar.horizontal = rando.nextBoolean();
	// 	//Since there are more options for placing a 2 length car than a 3 length car, we need to weight 2 length cars more. 
	// 	if (rando.nextDouble() < 5d/9d) {
	// 		newCar.length = 2;
	// 		int longCoord = rando.nextInt(6);
	// 		int shortCoord = rando.nextInt(5);
	// 		if (newCar.horizontal) {
	// 			newCar.x = shortCoord;
	// 			newCar.y = longCoord;
	// 		}
	// 		else {
	// 			newCar.y = shortCoord;
	// 			newCar.x = longCoord;
	// 		}
	// 	}
	// 	else {
	// 		newCar.length = 3;
	// 		int longCoord = rando.nextInt(6);
	// 		int shortCoord = rando.nextInt(4);
	// 		if (newCar.horizontal) {
	// 			newCar.x = shortCoord;
	// 			newCar.y = longCoord;
	// 		}
	// 		else {
	// 			newCar.y = shortCoord;
	// 			newCar.x = longCoord;
	// 		}
	// 	}
	// 	return newCar;
	// }
	public Car randomNewCar() {
		return carFromSeed(rando.nextInt(108));
	}

	public Car carFromSeed(int seed) {
		int s = seed%108;
		Car newCar = new Car();
		if (s>53) {
			newCar.horizontal = false;
			s=s-54;
		}
		else {
			newCar.horizontal = true;
		}

		if (s>29) {
			newCar.length = 3;
			s= s-30;
			if (newCar.horizontal) {
				newCar.x = s%4;
				newCar.y = s/4;
				return newCar;
			}
			else {
				newCar.x = s/4;
				newCar.y = s%4;
				return newCar;
			}

		}
		else {
			newCar.length = 2;
			if (newCar.horizontal) {
				newCar.x = s%5;
				newCar.y = s/5;
				return newCar;
			}
			else {
				newCar.x = s/5;
				newCar.y = s%5;
				return newCar;
			}
		}
	}

	public static int factorial( int n )
    {
        if( n <= 1 )     // base case
            return 1;
        else
            return n * factorial( n - 1 );
    }

    public Board tryToCreateBoard(int numCars) {
    	Board board = new Board(6,6);
		Car vip = new Car(4,2,2,true);
		board.addCar(vip);
		for(int i=1; i<numCars;i++){
			Car randCar = this.randomNewCar();
			boolean addedCar = board.addCar(randCar);
			if (!addedCar) {
				return null;
			}
		}
		return board;
    }



	public static void main(String[] args) {
		AsciiGen agen = new AGen();
		int numTrials=10000;
		int numSuccesses = 100;
		NumBoards count = new NumBoards();
		for (int numCars = 1;numCars<12;numCars++) {
			int totalDepth = 0;
			int totalSize = 0;
			int successes = 0;
			ArrayList<Board> listOfBoards = new ArrayList<Board>();
			// for (int j = 0;j<numTrials;j++) {
			while(successes<numSuccesses) {
				Board board = count.tryToCreateBoard(numCars);
				if (board !=null) {
					BoardGraph graph = new BoardGraph(board);
					totalDepth = totalDepth + graph.depth;
					totalSize = totalSize + graph.numberOfSolvedStates;
					successes++;
				}
			}
			// System.out.println(successes + " successes");
			// successes = 0;
			// for (Board b : listOfBoards){
			// 	for (int i = 0;i<108;i++) {
			// 		if (b.copy().addCar(count.carFromSeed(i))) {
			// 			successes++;
			// 		}
			// 	}
			// }
			// System.out.println(successes);
			System.out.println("-----------------------");
			System.out.println("number of cars:" +numCars);
			System.out.println(totalDepth/numSuccesses);
			System.out.println(totalSize/numSuccesses);
			// System.out.println(successes);
			// System.out.println(Math.round((successes/(double)numTrials)*Math.pow(108,numCars-1)/(double)count.factorial(numCars-1)));
		}
		
	}
}
