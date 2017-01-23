package rushhour.analysis;

import rushhour.core.*;
import rushhour.io.BoardIO;

public class ForwardMoveAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the number of moves
     * @param log the log to analyze
     * @return the number of moves in the log
     */
    @Override
    public double analyze(Log log) {
        Board board = read("rushhour/puzzles/"+log.puzzle_id+".txt");
        BoardGraph bg = new BoardGraph();
        bg.fillEquivalenceClass(board);
        int numForwardMoves = 0;
        int lastDepth = bg.getVertex(board).depth;
        for(LogMove line : log.moveList) {
            if(line.type = LogMoveType.NORMAL) {
                board.move(line.move.index,line.move.amount);
                if(bg.getVertex(board).depth == lastDepth-1) {
                    numForwardMoves++;
                }
                lastDepth = bg.getVertex(board).depth;
            }
        }
        return numForwardMoves;
    }
}
