/**
 * Car wrapper class. Note that the coordinates correspond to the left and
 * upper most block of the car on the board (aka the smallest x and y
 * coordinates). Length always "grows" to the right and down.
 */
public class Car {
    public boolean horizontal;
    public int length;
    public int x;
    public int y;
    //char c; // might want to change somehow?

    // constructor
    public Car(int x, int y, int length, boolean horiz) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.horizontal = horiz;
    }

    public Car copy() {
        return (new Car(this.x, this.y, this.length, this.horizontal));
    }
}
