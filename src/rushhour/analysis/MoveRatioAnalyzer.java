package rushhour.analysis;

public class MoveRatioAnalyzer implements Analyzer {
	public String description() {
		return "backward moves / total moves";
	}
	public double analyze(Log l) {
		double backwardMoves = (new BackwardMoveAnalyzer()).analyze(l);
		double totalMoves = (new MoveAnalyzer()).analyze(l);
		return backwardMoves/totalMoves;
	}
}
