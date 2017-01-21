package rushhour.io;

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
    public static ArrayList<Log> parseAllLogFiles(String filePath) {
        Path path = Paths.get(filePath);
        String currentId = "";
        String currentPuzzleId="";
        String currentStatus="";
        String[] currentLine;
        ArrayList<String[]> currentLog = new ArrayList<>();

        ArrayList<Log> logArrList = new ArrayList<>();
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
                    currentPuzzleId = currentLine[4];
                    currentStatus = currentLine[5];
                }
                if (!currentLine[0].equals(currentId) && !currentLog.isEmpty()) {
                    logArrList.add(new Log(currentId,currentPuzzleId,currentStatus,
                            currentLog.toArray(new String[currentLog.size()][])));

                    currentId = currentLine[0];
                    currentPuzzleId = currentLine[4];
                    currentStatus = currentLine[5];
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

    public static void analyzeWriteLogs(ArrayList<Log> logs) {
        TimeAnalyzer time = new TimeAnalyzer();
        UndoAnalyzer undo = new UndoAnalyzer();
        ResetAnalyzer reset = new ResetAnalyzer();
        MoveAnalyzer move = new MoveAnalyzer();
        MoveTimeAnalyzer movetime = new MoveTimeAnalyzer();

        FileWriter writer = null;
        try {
            writer = new FileWriter("out.csv");

            writer.append("solve_id,puzzle_id,status,time,undo,reset,move,movetime\n");

            for (Log log : logs) {
                writer.append(log.solve_id+",");
                writer.append(log.puzzle_id+",");
                writer.append(log.status+",");

                writer.append(time.analyze(log)+",");
                writer.append(undo.analyze(log)+",");
                writer.append(reset.analyze(log)+",");
                writer.append(move.analyze(log)+",");
                writer.append(movetime.analyze(log)+"\n");
            }

        } catch (IOException e) {
            System.out.println("Couldn't write to out.csv");
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println("Error closing the file writer");
            }
        }
    }

    public static void main(String[] args) {
        String path = "analyze.csv";
        ArrayList<Log> logs = parseAllLogFiles(path);
        AnalyzeTest tester = new AnalyzeTest();
        tester.testLog(logs.get(0));
        analyzeWriteLogs(logs);
    }
}
