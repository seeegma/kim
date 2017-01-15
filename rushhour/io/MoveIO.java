package rushhour.io;

import rushhour.core.*;

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

/**
 * Imports and Exports moves. Might come in handy if we explore very large
 * boards so we won't have to recalc paths.
 */
public final class MoveIO {
    /* Maybe this can export closer to SOLVE_LOG_FILE, but we will need to talk
    about it.*/

    /**
     * Reads in a list of moves from the given file and returns the list.
     * @param filename filename of the text file.
     * @return an ArrayList of Moves.
     */
    public static ArrayList<Move> read(String filename) {
        ArrayList<Move> moves = null;
        try {
            Scanner f = new Scanner(new File(filename + ".txt"), "utf-8");

            moves = new ArrayList<Move>();
            String parts[];
            long time;
            int index, amount;
            while (f.hasNextLine()) {
                parts = f.nextLine().split(" ");
                time = Long.parseLong(parts[0]);
                index = Integer.parseInt(parts[1]);
                amount = Integer.parseInt(parts[2]);
                moves.add(new Move(time, index, amount));
            }
            
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }

        return moves;
    }

    /**
     * Writes a list of moves to a text file.
     * @param filename filename of the text file.
     * @param m ArrayList of Moves
     */
    public static void write(String filename, ArrayList<Move> m) {
        try {
            PrintWriter pw = new PrintWriter(filename + ".txt", "UTF-8");

            int dx, dy;
            // TIME INDEX VECTOR
            for (int i = 0; i < m.size(); i++) {
                pw.print(i);
                pw.print(" ");
                pw.print(m.get(i).index);
                pw.print(" ");
                pw.println(m.get(i).amount);
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
}
