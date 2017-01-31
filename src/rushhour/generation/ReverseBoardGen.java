package rushhour.generation;

import rushhour.core.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class ReverseBoardGen {
    int WIDTH = 6;
    int HEIGHT = 6;
    int VIP_X = 4;
    int VIP_Y = 2;
    int VIP_LEN = 2;
    Board board;
    // a linked list of all free coordinate slots. used with Random
    LinkedList<Coord> coordList;
    // pseudo-grid for easy Coord object lookup. Format: grid[x][y].
    Coord[][] grid;
    int freeSpots;
    int numCars;
    Random RNG = new Random();

    private class Coord {
        int x;
        int y;
        Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public ReverseBoardGen() {
        this.board = new Board(WIDTH, HEIGHT);
        this.coordList = new LinkedList<>();
        this.grid = new Coord[WIDTH][HEIGHT];
        this.freeSpots = WIDTH * HEIGHT;
        this.numCars = 0;

        Coord temp;
        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                temp = new Coord(i, j);
                coordList.add(temp);
                grid[i][j] = temp;
            }
        }
    }

    /**
     * Removes a coordinate from the coordList when it is occupied.
     */
    private boolean removeCoord(Coord c) {
        if (this.coordList.remove(c)) {
            this.freeSpots -= 1;
            return true;
        }

        return false;
    }

    public Board genBoard(int minMoves) {
        this.board = new Board(WIDTH, HEIGHT);
        this.randomSolvedBoard(minMoves);
		return this.board;
    }

    private void randomSolvedBoard() {
        this.randomSolvedBoard(this.RNG.nextInt(WIDTH * HEIGHT / 2));
    }

    /**
     * Generates a board with at most maxCars Cars.
     */
    private void randomSolvedBoard(int maxCars) {
        // Adds the VIP and removes from the coordList since those spots are
        // occupied
        this.board.addCar(new Car(VIP_X, VIP_Y, VIP_LEN, true));
        this.freeSpots -= VIP_LEN;
        this.numCars++;
        for (int i = 0; i < VIP_LEN; i++) {
            this.coordList.remove(this.grid[VIP_X + i][VIP_Y]);
        }

        // Attempts to add numCars of Cars into the Board.
        while (this.numCars < maxCars && freeSpots > 0) {
            Car temp = this.findAcceptableCar();
            if (temp != null) {
                board.addCar(temp);
                numCars++;
            }
        }
    }

    /**
     * Pretty nasty function...
     */
    private Car findAcceptableCar() {
        int x, y;
        boolean isolated;
        do {
            isolated = false;
            // i will be the index of Coord we use in coordList.
            int i = this.RNG.nextInt(freeSpots); // pick a random free spot
            Coord candidate = coordList.get(i);
            x = candidate.x;
            y = candidate.y;

            // determines if we can at least have a Car of len 2 that is on this
            // Coord. Also finds the orientation.
            ArrayList<Coord> directions = new ArrayList<>();
            if (x+1 < WIDTH && coordList.contains(grid[x+1][y])) {
                directions.add(new Coord(1,0));
            }
            if (x-1 >= 0 && coordList.contains(grid[x-1][y])) {
                directions.add(new Coord(-1,0));
            }
            if (y+1 < HEIGHT && coordList.contains(grid[x][y+1])) {
                directions.add(new Coord(0,1));
            }
            if (y-1 >= 0 && coordList.contains(grid[x][y-1])) {
                directions.add(new Coord(0,-1));
            }

            if (directions.size() == 0) {
                // in this case this spot is surrounded by occupied spots, so
                // we can't put a car here.
                isolated = true;
                this.removeCoord(candidate);
            } else {
                // picks a random orientation for the car
                int j = this.RNG.nextInt(directions.size());
                int dx = directions.get(j).x;
                int dy = directions.get(j).y;
                Coord d = new Coord(dx, dy);
                ArrayList<Coord> locations = new ArrayList<Coord>();
                locations.add(candidate);
                locations.add(grid[x+dx][y+dy]);

                // Determines len 2 or 3. If it's len 2, we do nothing here.
                if (this.RNG.nextInt(2) == 1) {
                    // len = 3
                    if (x+2*dx >= 0 && x+2*dx < WIDTH &&
                        y+2*dy >= 0 && y+2*dy < HEIGHT) {
                        Coord third = grid[x+2*dx][y+2*dy];
                        if (coordList.contains(third)) {
                            locations.add(grid[x+2*dx][y+2*dy]);
                        }
                    }
                }

                // find the upper left most coordinate
                int carx = WIDTH;
                int cary = HEIGHT;
                for (int k = 0; k < locations.size(); k++) {
                    Coord temp = locations.get(k);
                    if (carx > temp.x) {
                        carx = temp.x;
                    }
                    if (cary > temp.y) {
                        cary = temp.y;
                    }
                    // since we're looping through, we might as well cleanup
                    this.removeCoord(temp);
                }

                // find orientation
                boolean horizontal = true;
                if (dy != 0) {
                    horizontal = false;
                }
				// return lots and lots of fun cars
				// or eat bagels
                return new Car(carx, cary, locations.size(), horizontal);
            }
        } while (isolated && this.freeSpots > 0);

        return null;
    }
}
