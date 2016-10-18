import java.util.ArrayList;
import java.util.Dictionary;

public class Board {
    int dimx, dimy; //dimension of the board
    private boolean[][] isFilled; // marks occupied spots on the board
    // Top left is (0, 0)
    private Dictionary<char,Car> carList;
    
    public Board() {
        //TODO
    }
    private class Car {
        private boolean horizontal;
        private int length;
        private int x; // most up/left coordinate of car
        private int y;
        char c; //may be unnecessary
    }
    
    public boolean canMove(char c, Direction d) {
        Car car = carList.get(c);
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

    public void move(char c, Direction d) { //Assumes the move is legal. 
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
