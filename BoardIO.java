import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

/**
 * Imports and Exports the board from and into a text file.
 */
public final class BoardIO {
	/**
	 * Reads in the board from the given file and returns the board.
	 * @param filename filename of the text file.
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
		    Scanner f = new Scanner(new File(filename + ".txt"), "utf-8");
		    //System.out.println("Open!");

		    // First line is the board dimensions: width height
		    String parts[] = f.nextLine().split(" ");
		    width = Integer.parseInt(parts[0]);
		    height = Integer.parseInt(parts[1]);

		    // Second line is exit: location offset
		    // Location is a cardinal direction (NESW)
		    // offset is int
            /*
		    parts = f.nextLine().split(" ");
		    d = Direction.ofCardinal(parts[0]);
		    offset = Integer.parseInt(parts[1]);*/

		    // Next lines are vehicles: x y length horiz
		    // Horiz is a boolean
		    // First vehicle is the VIP
		    int x, y, len;
		    boolean horiz;
		    while (f.hasNextLine()) {
		    	parts = f.nextLine().split(" ");
		    	x = Integer.parseInt(parts[0]);
		    	y = Integer.parseInt(parts[1]);
		    	len = Integer.parseInt(parts[2]);
		    	if (parts[3].equals("T")) {
					horiz = true;
				} else {
					horiz = false;
				}
		    	c.add(new Car(x, y, len, horiz));
		    }
		    
		    f.close();
		} catch (FileNotFoundException e) {
		    System.out.println("File not found!");
		    e.printStackTrace();
		}

		return new Board(width, height, c);
	}

	/**
	 * Writes the board to a text file.
	 * @param filename filename of the text file.
	 * @param b the board.
	 * @todo deal with the deletion of exit direction and offset, which are redundant.
	 */
	public static void write(String filename, Board b) {
		try {
			PrintWriter pw = new PrintWriter(filename + ".txt", "UTF-8");

            // First line is the board dimensions: width height
            pw.print(b.getWidth());
            pw.print(" ");
            pw.print(b.getHeight());
            pw.println();

            // Second line is exit: location offset
            // Location is a cardinal direction (NESW)
            // offset is int
            /*
            pw.print(b.getExitDirection().toCardinal()); 
            pw.print(" ");
            pw.print(b.getExitOffset());
            pw.println();*/

            // Next lines are vehicles: x y length horiz
            // Horiz is a boolean
            // First vehicle is the VIP
            ArrayList<Car> cars = b.getCars();
            for (int i = 0; i < cars.size(); i++) {
                Car c = cars.get(i);
                pw.print(c.x);
                pw.print(" ");
                pw.print(c.y);
                pw.print(" ");
                pw.print(c.length);
                pw.print(" ");
				if (c.horizontal) {
					pw.print("T");
				} else {
					pw.print("F");
				}
                pw.println();
            }

			pw.close();
		} catch (FileNotFoundException e) {
			System.out.println("Can't write file!");
           e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("Bad encoding for writing!");
		    e.printStackTrace();
        }
    }

    /**
	 * For testing purposes.
	 */
	public static void main(String[] args) {
		BoardIO.read("test");
	}
}
