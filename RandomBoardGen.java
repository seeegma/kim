import java.util.Random;

public class RandomBoardGen {
	
	int numCars;
	int h;
	int w;
	private Board board;
	Random random = new Random();
	AGen aaGen = new AGen();
	
	
	public RandomBoardGen(int w, int h, int numCars){
		this.numCars = numCars;
		Board board = new Board(w,h);
		this.board = board;
		addVIP();
		this.w = w;
		this.h = h;
	}
	
	public Board getBoard(){
		return board;
	}
	
	public void newBoard(){
		Board board = new Board(w,h);
		this.board = board;
		addVIP();
	}
	
	public void addVIP(){
		Car vip = new Car(2,2,2,true);
		Board temp = new Board(6,6);
		temp.addCar(vip);
		System.out.println("what");
		aaGen.printGrid(temp.getGrid());
	}
	
	/**
	 * Generates board with n cars
	 * If fails to make board after 100 tries, stops
	 * @return true is success, false if fails after 100 tries
	 */
	public boolean generateBoard(int x, int y){
//		Board board = new Board(h,w);
			
		int i = 0;
		boolean succ;
		while(i < 100){
			succ = addNRCars();
			if (succ == true){
				return true;
			} else {
				//board = new Board(h,w);
				newBoard();
				addVIP();
				i++;
			}
		}
		return false;
	}
	
	/**
	 * Add N random Cars. If fails, clears boards
	 * @return true if succ, false if fail
	 */
	public boolean addNRCars(){
		int i = numCars;
		boolean succ;
		while (i > 0){
			//if there is no space to put in a car
			if (!this.board.hasEmpty()){
				return false;
			}
			
			succ = addRandomCar();
			if (succ == true){
				aaGen.printGrid(this.board.getGrid());
				i--;
			}
		}
		return true;
	}
	
	/**
	 * Tries to add random car.
	 * @param board
	 * @return true if successful, false if not
	 */
	public boolean addRandomCar(){
		int len = random.nextInt(2);
		len += 2;
		int hor = random.nextInt(2);
		int x = 0;
		int y = 0;
		
		if(len == 2 && hor == 0){
			x = random.nextInt(this.board.getHeight()-1);
			y = random.nextInt(this.board.getWidth()-2);
		} else if (len == 3 && hor == 0){
			x = random.nextInt(this.board.getHeight()-1);
			y = random.nextInt(this.board.getWidth()-3);
		} else if (len == 2 && hor == 1){
			x = random.nextInt(this.board.getHeight()-2);
			y = random.nextInt(this.board.getWidth()-1);
		} else {
			x = random.nextInt(this.board.getHeight()-3);
			y = random.nextInt(this.board.getWidth()-1);
		}

		
		boolean hori = true;
		if (hor == 0){ hori = false;}
		
		Car car = new Car(x,y,len,hori);
		return this.board.addCar(car);
	}
	
	public static void main(String[] args){
		RandomBoardGen rgen = new RandomBoardGen(6,6,2);
		System.out.println(rgen.generateBoard(6,6));
		AGen aGen = new AGen();
		
		aGen.printGrid(rgen.getBoard().getGrid());
	
	}

}
