import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

/**
 * Imports and Exports the board from and into a text file.
 */
public class BoardIO {
	// pretty sure there's a nice keyword for these kinds of classes

	/**
	 * Reads in the board from the given file and returns the board.
	 * @param filename of the text file.
	 * @return a board object based on the information in the file.
	 */
	public static Board read(String filename) {
		// Have to fix AltBoard and the file structure...
		int width = 0;
	    int height = 0;

	    Direction d = null;
	    int offset = 0;

		ArrayList<Car> c = new ArrayList<Car>();

		// TODO: check file integrity?
		// opens file and gets all the data
		try {
		    Scanner f = new Scanner(new File(filename + ".txt"));
		    System.out.println("Open!");

		    // First line is the board dimensions: width height
		    String parts[] = f.nextLine().split(" ");
		    width = Integer.parseInt(parts[0]);
		    height = Integer.parseInt(parts[1]);

		    // Second line is exit: location offset
		    // Location is a cardinal direction (NESW)
		    // offset is int
		    parts = f.nextLine().split(" ");
		    d = Direction.cardinal(parts[0]);
		    offset = Integer.parseInt(parts[1]);

		    // Next lines are vehicles: x y length horiz
		    // Horiz is a boolean
		    // First vehicle is the VIP
		    /*x = new ArrayList<Integer>();
		    y = new ArrayList<Integer>();
		    len = new ArrayList<Integer>();
		    horiz = new ArrayList<Boolean>();*/
		    int x, y, len;
		    boolean horiz;
		    while (f.hasNextLine()) {
		    	parts = f.nextLine().split(" ");
		    	x = Integer.parseInt(parts[0]);
		    	y = Integer.parseInt(parts[1]);
		    	len = Integer.parseInt(parts[2]);
		    	horiz = Boolean.parseBoolean(parts[3]);
		    	c.add(new Car(x, y, len, horiz));
		    }
		    
		    f.close();
		} catch (FileNotFoundException e) {
		    System.out.println("File not found!");
		    e.printStackTrace();
		}

		return new Board(width, height, d, offset, c);
		//return null;
	}

	/**
	 * For testing purposes.
	 */
	public static void main(String[] args) {
		BoardIO.read("test");
	}
}
