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
        if (i == 0) {
            return false;
        }

        // actually moves the car
        c.x += dx * i;
        c.y += dy * i;
        return true;
    }