package rushhour.analysis;

public class Log {
    public String solve_id;
    public String puzzle_id;
    public String status;
    public String[][] logArr;

    public Log(String solve_id, String puzzle_id, String status, String[][] logArr) {
        this.solve_id = solve_id;
        this.puzzle_id = puzzle_id;
        this.status = status;
        this.logArr = logArr;
    }
}
