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
    public Board(int w, int h, Direction d, int o, ArrayList<Integer> x,
        ArrayList<Integer>  y, ArrayList<Integer> len,
        ArrayList<Boolean> horiz) {
        this.w = w;
        this.h = h;
        this.exit = d;
        this.offset = o;
        isFilled = new boolean[w][h];
        carList = new ArrayList<Car>();

        for (int i = 0; i < x.size(); i++) {
            this.addCar(x.get(i), y.get(i), len.get(i), horiz.get(i));
        }
    }

    public int getWidth() {
        return this.w;
    }

    public int getHeight() {
        return this.h;
    }

    public int getExitDirection() {
        return this.exit;
    }

    public int getExitOffset() {
        return this.o;
    }

    /**
     * Adds a new car. Might be useful to be public fcn for testing.
     */
    public void addCar(int x, int y, int length, boolean horiz) {
        Car newCar = new Car(x, y, length, horiz);

        // update list
        carList.add(newCar);

// TODO: CHECK THAT THE NEW CAR IS NOT IN ANOTHER CAR
        // update isFilled
        int dx = 0;
        int dy = 0;
        if (horiz) {
            dx++;
        } else {
            dy++;
        }
        for (int i = 0; i < length; i++) {
            isFilled[x + (dx*i)][y + (dy*i)] = true;
        }

        // debugging
        this.debug();
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
        if (c==null) {
            return false;
        }
        
        // valid move direction
        if (d.isHorizontal()!=c.horizontal) {
            return false;
        }
        
        // check if the moves are valid
        int x = c.x;
        int y = c.y;
        int dx = 0; // the direction as a unit vector
        int dy = 0;
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
                break;
            case RIGHT:
                dx++;
                break;
        }
        // checks if the move is valid and if it's out of bounds
        // also changes the isFilled board to what it will be once the car moves
        // since we're looping through its indices anyways.
        int tempx, tempy;
        int i = 1;
        boolean collision = false;
        while (i <= amount && !collision) {
            tempx = x + (dx * i);
            tempy = y + (dy * i);
            // check if it's out of bounds or is already filled
            if (tempx >= this.w || tempx < 0 || tempy >= this.h || tempy < 0
                || isFilled[tempx][tempy]) {
                collision = true;
            }
            isFilled[tempx][tempy] = true; // the square the car moved to
            // the square the car is no longer occuping
            isFilled[tempx-(dx*c.length)][tempy-(dy*c.length)] = false;
            i++;
        }
        // decrements i if we had an early collision since i is one square ahead
        // of where the car will move to
        if (collision) { 
            i--;
        }

        // actually moves the car
        c.x = dx * i;
        c.y = dx * i;
        return true;
    }

    /**
     * @Exclude
     * For testing purposes. Prints out the innards.
     */
    private void debug() {
        System.out.println(w + " " + h);

        for(int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (isFilled[j][i])
                    System.out.print("1");
                else
                    System.out.print("0");
            }
            System.out.println();
        }

        Car car;
        for(int i = 0; i < carList.size(); i++) {
            car = carList.get(i);
            System.out.println(car.x + " " + car.y + " " + car.length + " "
                + car.horizontal + " " + i);
        }
    }

    /**
     * For testing purposes.
     */
    public static void main(String[] args) {
        Board b = new Board(6, 6, Direction.RIGHT, 2);
        b.addCar(0, 0, 2, true);
    }
}
