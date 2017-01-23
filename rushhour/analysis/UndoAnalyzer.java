package rushhour.analysis;

public class UndoAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the number of resets
     * @param log the log to analyze
     * @return the number of undos in a log
     */
    public double analyze(Log log) {
        int undos = 0;

        for (LogMove mv : log.moveList) {
            if (mv.type == LogMoveType.UNDO) {
                undos++;
            }
        }
        return undos;
    }
	public String description() {
		return "undos";
	}
}
