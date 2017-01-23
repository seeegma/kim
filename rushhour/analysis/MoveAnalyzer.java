package rushhour.analysis;

public class MoveAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the number of moves
     * @param log the log to analyze
     * @return the number of moves in the log
     */
    public double analyze(Log log) {
        return log.moveList.size();
    }
	public String description() {
		return "moves";
	}
}
