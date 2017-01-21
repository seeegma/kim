package rushhour.analysis;

public class TimeAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the time taken
     * @param log the log to analyze
     * @return the time taken to solve the puzzle (in milliseconds)
     */
    @Override
    public double analyze(Log log) {
        long start = log.moveList.get(0).time;
        long end = log.moveList.get(log.moveList.size()-1).time;
        return end-start;
    }
}
