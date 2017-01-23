package rushhour.analysis;

public interface Analyzer {
    /**
     * not scaled
     */
    public double analyze(Log log);

	public String description();

}
