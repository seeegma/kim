package rushhour.analysis;

public class UndoAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the number of resets
     * @param log the log to analyze
     * @return the number of undos in a log
     */
    @Override
    public double analyze(String[][] log) {
        int undos = 0;

        for (String[] vars : log) {
            if (vars.length > 1 && vars[1].equals("U")) {
                undos++;
            }
        }
        return undos;
    }
}
