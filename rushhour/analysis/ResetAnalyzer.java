package rushhour.analysis;

public class ResetAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the number of resets
     * @param log the log to analyze
     * @return the number of resets in the log
     */
    @Override
    public double analyze(String[][] log) {
        int resets = 0;

        for (String[] vars : log) {
            if (vars.length > 1 && vars[1].equals("R")) {
                resets++;
            }
        }
        return resets;
    }
}
