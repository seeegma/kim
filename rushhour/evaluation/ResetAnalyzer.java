package rushhour.evaluation;

public class ResetAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the number of resets
     * @param log the log to analyze
     * @return the number of resets in the log
     */
    @Override
    public double analyze(String log) {
        String[] lines = log.split("\n");
        int resets = 0;

        for (String l : lines) {
            String[] vars = l.split(" ");
            if (vars.length > 1 && vars[1].equals("R")) {
                resets++;
            }
        }
        return resets;
    }
}
