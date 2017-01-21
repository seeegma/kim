package rushhour.analysis;

public class ResetAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the number of resets
     * @param log the log to analyze
     * @return the number of resets in the log
     */
    @Override
    public double analyze(Log log) {
        int resets = 0;

        for (LogMove mv : log.moveList) {
            if (mv.type == LogMoveType.RESET) {
                resets++;
            }
        }
        return resets;
    }
}
