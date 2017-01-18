package rushhour.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class LogIO {
    
    /**
     * Converter method
     * @param filePath the file path of the log
     * @return the array representation of the log file
     */
    public static String[][] parseLogFile(String filePath) {
        String[][] logArr;
        logArr = new String[][]{{""}, {""}};
        Path path = Paths.get(filePath);
        try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8.name())) {
            while (scanner.hasNextLine()){
                //process each line in some way
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't find file");
        }
        return logArr;
    }

    public static void main(String[] args) {
        String path = "example.txt";
        parseLogFile(path);
    }
}