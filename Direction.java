public enum Direction {
    UP, 
    DOWN, 
    LEFT, 
    RIGHT;
    
    /**
     * Defines which enums is considered horizontal.
     * @return whether the enum is horizontal or not
     */ 
    public boolean isHorizontal(){
        switch(this) {
            case UP:
            case DOWN:
                return false;
            
            default:
                return true;
        }
    }

    public Direction reverse() {
        switch(this) {
            case UP:
                return DOWN;
            case RIGHT:
                return LEFT;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT; 
            default:
                return null;
        }
    }

    /**
     * Converts cardinal (NESW) directions to a Direction enum.
     * @param card cardinal direction ("N", "E", "S", or "W")
     * @return the Direction associated with that cardinal direction
     */ 
    public static Direction ofCardinal(String card) {
    	switch(card) {
    		case "N":
    			return UP;
    		case "E":
    			return RIGHT;
    		case "S":
    			return DOWN;
    		case "W":
    			return LEFT; 
    		default:
    			return null;
    	}
    }

    /**
     * Converts Direction enum to a cardinal (NESW) direction.
     * @return cardinal direction ("N", "E", "S", or "W")
     */ 
    public char toCardinal() {
        switch(this) {
            case UP:
                return 'N';
            case RIGHT:
                return 'E';
            case DOWN:
                return 'S';
            case LEFT:
                return 'W'; 
            default:
                return '0';
        }
    }    
}
