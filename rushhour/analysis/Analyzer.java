package rushhour.analysis;

public interface Analyzer {
    /**
     * not scaled
     */
    double analyze(String[][] log);

    /**
     * scaled
     */
    //public int getScore(Board b);
}
