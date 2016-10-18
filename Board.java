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
     */
    public int getWidth() {
        return this.w;
    }

    /**
     * Getter for the height of the board.
     */
    public int getHeight() {
        return this.h;
    }

    /**
     * Getter for the Direction of the exit of the board.
     */
    public Direction getExitDirection() {
        return this.exit;
    }

    /**
     * Getter for the offset of the exit of the board.
     */
    public int getExitOffset() {
        return this.offset;
    }

    /**
     * Getter for the list of cars on the board.
     */
    public ArrayList<Car> getCars() {
        return this.carList;
    }

    /**
     * Adds a new car. Might be useful to be public fcn for testing.
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
     */
    // remember that Isaac has his own move fcn figured out. Should compare.
    // Maybe num can identify the car.
    public boolean move(int num, Direction d, int amount) {
        // valid car
        //Car c = carList.get(ch);
        Car c = carList.get(num);
        if (c == null) {
            return false;
        }
        
        // valid move direction
        if (d.isHorizontal()!= c.horizontal) {
            return false;
        }
        
        // check if the moves are valid
        int x = c.x;
        int y = c.y;
        int dx = 0; // the direction as a unit vector
        int dy = 0;
        int xolen = 0; // adds the length if we're moving right or down
        int yolen = 0;
        // determines which way we're using a unit vector
        switch(d) {
            case UP:
                dy--;
                break;
            case LEFT:
                dx--;
                break;
            case DOWN:
                dy++;
                yolen += c.length - 1;
                break;
            case RIGHT:
                dx++;
                xolen += c.length - 1;
                break;
        }
        // checks if the move is valid and if it's out of bounds
        // also changes the isFilled board to what it will be once the car moves
        // since we're looping through its indices anyways.
        int tempx, tempy;
        int i = 1;
        boolean collision = false;
        while (i <= amount && !collision) {
            tempx = x + (dx * i) + xolen;
            tempy = y + (dy * i) + yolen;
            System.out.println(tempx + " " + tempy);
            // check if it's out of bounds or is already filled
            if (tempx >= this.w || tempx < 0 || tempy >= this.h || tempy < 0
                || isFilled[tempx][tempy]) {
                collision = true;
            } else {
                isFilled[tempx][tempy] = true; // the square the car moved to
                // the square the car is no longer occuping
                isFilled[tempx-(dx*c.length)][tempy-(dy*c.length)] = false;
                i++;
            }
        }
        // decrements i if we had an early collision since i is one square ahead
        // of where the car will move to or if there was no collision due to the
        // extra i++ as the end of the else statement
        i--;

        // actually moves the car
        c.x += dx * i;
        c.y += dy * i;
        return true;
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
    }
}
