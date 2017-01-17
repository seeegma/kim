package rushhour.analysis;

public class MoveTimeAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the avgerage time taken per move
     * @param log the log to analyze
     * @return the average time taken per move
     */
    @Override
    public double analyze(String log) {
        String[] lines = log.split("\n");
        double time = new TimeAnalyzer().analyze(log);
        return time/lines.length;
    }
}
