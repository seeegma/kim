package rushhour.analysis;

public class TimeAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the time taken
     * @param log the log to analyze
     * @return the time taken to solve the puzzle (in milliseconds)
     */
    @Override
    public double analyze(String[][] log) {
        long start = Long.parseLong(log[0][0]);
        long end = Long.parseLong(log[log.length-1][0]);
        return end-start;
    }
}
