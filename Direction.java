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

    public static Direction cardinal(String card) {
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
}
