package rushhour.io;

import rushhour.analysis.AnalyzeTest;

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

    /**
     * Converter method, takes in a csv of all solve logs ordered by solve_id then s
     * @param filePath the file containing all solve logs to be parsed
     * @return an arraylist of solve logs for each solve_id
     */
    public static ArrayList<String[][]> parseAllLogFiles(String filePath) {
        Path path = Paths.get(filePath);
        String currentId = "";
        String[] currentLine;
        ArrayList<String[]> currentLog = new ArrayList<>();
        ArrayList<String[][]> logArrList = new ArrayList<>();
        String[] logLine;

        try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8.name())) {
            // Pass through the header in the csv
            scanner.nextLine();
            while (scanner.hasNextLine()){
                currentLine = scanner.nextLine().split(",");
                if (currentLine.length == 0)
                    continue;
                if (currentId.equals("")) {
                    currentId = currentLine[0];
                }
                if (!currentLine[0].equals(currentId) && !currentLog.isEmpty()) {
                    logArrList.add(currentLog.toArray(new String[currentLog.size()][]));
                    currentId = currentLine[0];
                    currentLog = new ArrayList<>();
                }
                logLine = currentLine[3].split(" ");
                if (logLine.length == 1) {
                    logLine = new String[]{currentLine[2], logLine[0]};
                } else {
                    logLine = new String[]{currentLine[2], logLine[0], logLine[1]};
                }
                currentLog.add(logLine);
            }
        } catch (IOException e) {
            System.out.println("Couldn't find file");
        }
        return logArrList;
    }

    public static void main(String[] args) {
        String path = "analyze.csv";
        List<String[][]> logs = parseAllLogFiles(path);
        AnalyzeTest tester = new AnalyzeTest();
        System.out.println(logs.get(0).length);
        tester.testLog(logs.get(0));
    }
}