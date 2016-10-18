import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a position in a game of Rush Hour. Includes moving functionality.
 * Note that the board is represented as a w by h board with the top left
 * corner known to be 0, 0. Increasing x and y moves to the right and down
 * respectively.
 */
public class Board {
// TODO: introduce coordinate class just to make stuff more clear? Will make
// coordinate conversion from Direction easier.
    int w, h; // dimension of the board
    // marks occupied spots on the board
    private boolean[][] isFilled;
    //private HashMap<Character,Car> carList;
    private ArrayList<Car> carList;

    // temp to match context free grammar used by txt file
    // Need to incorporate this into the code somehow...
    Direction exit;
    int offset;

    // Basic constructor
    public Board(int w, int h, Direction d, int o) {
        this.w = w;
        this.h = h;
        this.exit = d;
        this.offset = o;
        isFilled = new boolean[w][h];
        carList = new ArrayList<Car>();
    }
    
    // Overloaded for importing from file
    public Board(int w, int h, Direction d, int o, ArrayList<Car> c) {
        this.w = w;
        this.h = h;
        this.exit = d;
        this.offset = o;
        isFilled = new boolean[w][h];
        carList = new ArrayList<Car>();

        // Could just carList = c, but then would need to update isFilled...
        for (int i = 0; i < c.size(); i++) {
            this.addCar(c.get(i));
        }
    }

    /**
     * Getter for the width of the board.
     * @return the width
     */
    public int getWidth() {
        return this.w;
    }

    /**
     * Getter for the height of the board.
     * @return the height
     */
    public int getHeight() {
        return this.h;
    }

    /**
     * Getter for the Direction of the exit of the board.
     * @return the Direction of the exit
     */
    public Direction getExitDirection() {
        return this.exit;
    }

    /**
     * Getter for the offset of the exit of the board.
     * @return the offset of the exit
     */
    public int getExitOffset() {
        return this.offset;
    }

    /**
     * Getter for the list of cars on the board.
     * @return an ArrayList<Car> of the cars
     */
    public ArrayList<Car> getCars() {
        return this.carList;
    }

    /**
     * Adds a new car. Might be useful to be public fcn for testing.
     * @param newCar a new car to be inserted
     */
    public void addCar(Car newCar) {
        // update list
        carList.add(newCar);

// TODO: CHECK THAT THE NEW CAR IS NOT IN ANOTHER CAR
        // update isFilled
        int dx = 0;
        int dy = 0;
        if (newCar.horizontal) {
            dx++;
        } else {
            dy++;
        }
        for (int i = 0; i < newCar.length; i++) {
            isFilled[newCar.x + (dx*i)][newCar.y + (dy*i)] = true;
        }
        debug();
    }
    
    /**
     * Moves the car in the direction d by amount or until it hits another car.
     * @param num the number associated with the car to move
     * @param d the Direction to move in
     * @param amount the number of squares to move
     * @return whether the move succeeded or not. Will return true only if it
     * moves any amount of blocks
     */
    // remember that Isaac has his own move fcn figured out. Should compare.
    // Maybe num can identify the car.
    public boolean canMove(int carNum, Direction d) {
        Car c = carList.get(carNum);
        if (c==null) {
            return false;
        }
        
        if (d.isHorizontal()!=c.horizontal) {
            return false;
        }
        

        switch(d) {
            case UP:
                if (c.y>0 && !isFilled[c.x][c.y-1]) {
                    return false;
                }
                break;
            case LEFT:
                if (c.x>0 && !isFilled[c.x-1][c.y]) {
                    return false;
                }
                break;
            case DOWN:
                if (c.y+c.length-1<dimy-1 && !isFilled[c.x][(c.y+c.length-1)+1]) {
                    return false;
                }
                break;
            default:
                if (c.x+c.length-1<dimx-1 && !isFilled[c.x+c.length-1+1][c.y]) {
                    return false;
                }
                break;

        }
        return true;
    }

    public void move(int carNum, Direction d) { //Assumes the move is legal. 
        Car c = carList.get(carNum);
        switch(d) {
            case UP:
                isFilled[c.x][c.y-1]=true;
                isFilled[c.x][c.y+c.length-1]=false;
                c.y-=1;
                break;
            case LEFT:
                isFilled[c.x-1][c.y]=true;
                isFilled[c.x+c.length-1][c.y]=false;
                c.x-=1;
                break;
            case DOWN:
                isFilled[c.x][c.y]=false;
                isFilled[c.x][c.y+c.length]=true;
                c.y+=1;
                break;
            default:
                isFilled[c.x][c.y]=false;
                isFilled[c.x+c.length]=true;
                c.x+=1;
                break;
        }
    }

    /**
     * @Exclude
     * For testing purposes. Prints out the innards.
     */
    public void debug() {
        System.out.println("---------------------------");
        System.out.println("Dimensions: w" + w + " h" + h);
        System.out.println();

        System.out.println("isFilled:");
        for(int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (isFilled[j][i])
                    System.out.print("1 ");
                else
                    System.out.print("0 ");
            }
            System.out.println();
        }
        System.out.println();

        Car car;
        for(int i = 0; i < carList.size(); i++) {
            car = carList.get(i);
            System.out.println("Car #" + i + ": " + car.x + " " + car.y + " "
                + car.length + " " + car.horizontal + " " + i);
        }
        System.out.println("---------------------------");
    }

    /**
     * For testing purposes.
     */
    public static void main(String[] args) {
        /*Board b = new Board(6, 6, Direction.RIGHT, 2);
        Car c = new Car(0, 0, 2, true);
        b.addCar(c);*/

        Board b = BoardIO.read("test");
        b.debug();
        System.out.println(b.move(0, Direction.UP, 1));
        System.out.println(b.move(0, Direction.RIGHT, 1));
        System.out.println(b.move(3, Direction.RIGHT, 1));
        System.out.println(b.move(3, Direction.UP, 1));
        System.out.println(b.move(3, Direction.DOWN, 1));
        b.debug();
        BoardIO.write("test2", b);
    }
}
