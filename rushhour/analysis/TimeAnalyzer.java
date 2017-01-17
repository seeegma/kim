package rushhour.analysis;

public class TimeAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the time taken
     * @param log the log to analyze
     * @return the time taken to solve the puzzle (in milliseconds)
     */
    @Override
    public double analyze(String log) {
        String[] lines = log.split("\n");

        long start = Long.parseLong(lines[0].split(" ")[0]);
        long end = Long.parseLong(lines[lines.length-1].split(" ")[0]);
        return end-start;
    }
}
