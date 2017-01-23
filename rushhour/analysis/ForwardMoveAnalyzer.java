package rushhour.analysis;

import rushhour.core.*;
import rushhour.io.BoardIO;

public class ForwardMoveAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the number of moves
     * @param log the log to analyze
     * @return the number of moves in the log
     */
    public double analyze(Log log) {
        BoardGraph bg = new BoardGraph(log.board);
        int numForwardMoves = 0;
        int lastDepth = bg.getVertex(log.board).depth;
        for(LogMove line : log.moveList) {
            if(line.type == LogMoveType.NORMAL) {
                log.board.move(line.move.index,line.move.amount);
                if(bg.getVertex(log.board).depth == lastDepth-1) {
                    numForwardMoves++;
                }
                lastDepth = bg.getVertex(log.board).depth;
            }
        }
        return numForwardMoves;
    }
	public String description() {
		return "forward moves";
	}
}
