package rushhour.analysis;
import java.util.List;
import rushhour.core.Move;

public class Log {
    public String solve_id;
    public String puzzle_id;
    public String status;
    public LogMove[] logArr;

    public Log(String solve_id, String puzzle_id, String status, String[][] logArr) {
        this.solve_id = solve_id;
        this.puzzle_id = puzzle_id;
        this.status = status;
        List<LogMove> log = new List<LogMove>();
        for (String[] s : logArr) {
            LogMove lm = new LogMove();
            lm.time = Long.parseLong(s[0]);
            if (s[1].equals("R")) {
                lm.type = LogMoveType.RESET;
            }
            else if (s[1].equals("U")) {
                lm.type = LogMoveType.UNDO;
            }
            else {
                lm.type = LogMoveType.NORMAL;
                lm.move = new Move(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
            }
            log.add(lm);
        }
        this.logArr = log.toArray(new LogMove[log.size()]);
    }
}
