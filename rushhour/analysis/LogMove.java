package rushhour.core;

/*
* Class that represents a move on the board.
*/
public class LogMove {
    public long time;

    public LogMoveType type;

    public Move move;

    public LogMove(long time, LogMoveType type, Move move) {
	      this.time = time;
	      this.type = type;
	      this.move = move;
    }
}
