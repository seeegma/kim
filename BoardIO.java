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
	public static void read(String filename) {
		// Have to fix AltBoard and the file structure...
		int width;
	    int height;

	    Direction d;
	    int offset;

		ArrayList<Integer> x = new ArrayList<Integer>();
	    ArrayList<Integer> y = new ArrayList<Integer>();
	    ArrayList<Integer> len = new ArrayList<Integer>();
		ArrayList<Boolean> horiz = new ArrayList<Boolean>();

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
		    while (f.hasNextLine()) {
		    	parts = f.nextLine().split(" ");
		    	x.add(Integer.parseInt(parts[0]));
		    	y.add(Integer.parseInt(parts[1]));
		    	len.add(Integer.parseInt(parts[2]));
		    	horiz.add(Boolean.parseBoolean(parts[3]));
		    }
		    
		    f.close();
		} catch (FileNotFoundException e) {
		    System.out.println("File not found!");
		    e.printStackTrace();
		}

		// put into board once we figure that out...
		// testing now
		for (int i = 0; i < x.size(); i++) {
			System.out.println(x.get(i) + " " + y.get(i) + " " + len.get(i)
				+ " " + horiz.get(i));
		}

		//return null;
	}

	/**
	 * For testing purposes.
	 */
	public static void main(String[] args) {
		BoardIO.read("test");
	}
}
