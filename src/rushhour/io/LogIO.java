package rushhour.io;

import rushhour.core.Move;
import rushhour.analysis.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LogIO {

    /**
     * Converter method
     * @param filePath the file path of the log
     * @return the array representation of the log file
     */
    public static Log read(String filePath) {
		Log log = new Log();
        Path path = Paths.get(filePath);
        try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8.name())) {
            while (scanner.hasNextLine()){
				String[] line = scanner.nextLine().split(" ");
				LogMove logMove = new LogMove();
				logMove.time = Long.parseLong(line[0]);
				if(line[1].equals("R")) {
					logMove.type = LogMoveType.RESET;
				} else if(line[1].equals("U")) {
					logMove.type = LogMoveType.UNDO;
				} else {
					logMove.move = new Move(Integer.parseInt(line[1]), Integer.parseInt(line[2]));
					logMove.type = LogMoveType.NORMAL;
				}
				log.moveList.add(logMove);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't find file");
        }
        return log;
    }
}
