import java.util.Random;

public class NumBoards {
	
	Random rando = new Random();

	public Car randomNewCar() {
		Car newCar = new Car();
		newCar.horizontal = rando.nextBoolean();
		//Since there are more options for placing a 2 length car than a 3 length car, we need to weight 2 length cars more. 
		if (rando.nextDouble() < 5d/9d) {
			newCar.length = 2;
			int longCoord = rando.nextInt(6);
			int shortCoord = rando.nextInt(5);
			if (newCar.horizontal) {
				newCar.x = shortCoord;
				newCar.y = longCoord;
			}
			else {
				newCar.y = shortCoord;
				newCar.x = longCoord;
			}
		}
		else {
			newCar.length = 3;
			int longCoord = rando.nextInt(6);
			int shortCoord = rando.nextInt(4);
			if (newCar.horizontal) {
				newCar.x = shortCoord;
				newCar.y = longCoord;
			}
			else {
				newCar.y = shortCoord;
				newCar.x = longCoord;
			}
		}
		return newCar;
	}

	public static int factorial( int n )
    {
        if( n <= 1 )     // base case
            return 1;
        else
            return n * factorial( n - 1 );
    }



	public static void main(String[] args) {
		AGen agen = new AGen();
		int numCars=12;
		int numTrials=1000000;
		NumBoards count = new NumBoards();
		int successes = 0;
		for (int j = 0;j<numTrials;j++) {


			Board board = new Board(6,6);
			boolean valid = true;
			for(int i=0; i<=numCars;i++){
				Car randCar = count.randomNewCar();
				if (!board.addCar(randCar)) {
					valid = false;
					break;
				}
			}
			if (valid) {
				successes++;
				//agen.printGrid(agen.getPrintableGrid(board.getGrid()));
			}
		}
		System.out.println(successes);
		System.out.println((successes/(double)numTrials)*Math.pow(108,numCars)/(double)count.factorial(numCars));
	}
}