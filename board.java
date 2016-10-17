import java.util.ArrayList;
import java.util.Dictionary;

public class Board {
    int dimx, dimy; //dimension of the board
    private ArrayList<boolean>[][] isFilled; // marks occupied spots on the board
    // Top left is (0, 0)
    private Dictionary<char,Car> carList;
    
    public Board() {
    }
    private class Car {
        private boolean horizontal;
        private int length;
        private int x; // most up/left coordinate of car
        private int y;
        char c; //may be unnecessary
    }
    
    public boolean move(char c, Direction d) {
        Car car = carList.get(c);
        if (c==null) {
            return false;
        }
        
        if (d.isHorizontal()!=c.horizontal) {
            return false;
        }
        
        int x = c.x; // checking isFilled
        int y = c.y;
        int dx = 0; // moving
        int dy = 0;
        int replacex = c.x ;
        int replacey = c.y ; // for changing isFilled
        switch(d) {
            case UP:
                y--;
                dy--;
                replacey += c.length - 1;
                break;
            case LEFT:
                x--;
                dx--;
                replacex += c.length - 1;
                break;
            case DOWN:
                y += c.length;
                break;
            case RIGHT:
                x += c.length;
                break;
        }
        // checks if the move is valid and if it's out of bounds
        if(isFilled[x][y] || x >= this.dimx || y >= this.dimy
           || x < 0 || y < 0) {
            return false;
        }
        
        c.x = c.x + 1;
        c.y = c.y + 1;
        //move the car
        return true;
    }
}

                        
                        
public enum Direction {
    UP, 
    DOWN, 
    LEFT, 
    RIGHT;
    
    public boolean isHorizontal(){
        switch(this) {
            case UP:
            case DOWN:
                return false;
            
            default:
                return true;
        }
    }
        
}