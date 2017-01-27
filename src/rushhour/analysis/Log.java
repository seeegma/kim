package rushhour.analysis;
import java.util.List;
import java.util.ArrayList;
import rushhour.core.Board;

public class Log {
    public String status;
	public Board board;
    public List<LogMove> moveList;

	public Log() {
		this.moveList = new ArrayList<LogMove>();
	}

}
