package rushhour.analysis;

public interface Analyzer {
    /**
     * not scaled
     */
    public double analyze(String[][] log);

    /**
     * scaled
     */
    //public int getScore(Board b);
}
