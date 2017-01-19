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
    public static ArrayList<LogMove> read(String filename) {
        ArrayList<LogMove> moves = null;
        try {
            Scanner f = new Scanner(new File(filename), "utf-8");
            moves = new ArrayList<LogMove>();
            String parts[];
            long time;
            int index = 0, amount = 0;
			LogMoveType type;
            while (f.hasNextLine()) {
                parts = f.nextLine().split(" ");
                time = Long.parseLong(parts[0]);
				char secondToken = parts[1].toCharArray()[0];
				if(secondToken == 'U' || secondToken == 'R') {
					type = LogMoveType.fromChar(secondToken);
				} else {
					type = LogMoveType.NORMAL;
					index = Integer.parseInt(parts[1]);
					amount = Integer.parseInt(parts[2]);
				}
                moves.add(new LogMove(time, type, new Move(index, amount)));
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
    public static void write(String filename, ArrayList<LogMove> m) {
        try {
            PrintWriter pw = new PrintWriter(filename, "UTF-8");

            int dx, dy;
            // TIME INDEX VECTOR
            for (int i = 0; i < m.size(); i++) {
                pw.print(i);
                pw.print(" ");
                pw.print(m.get(i).move.index);
                pw.print(" ");
                pw.println(m.get(i).move.amount);
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
