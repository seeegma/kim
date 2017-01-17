package rushhour.evaluation;

public class TimeAnalyzer implements Analyzer {

    /**
     * Analyzes a log based on the time taken
     * @param log the log to analyze
     * @return the time taken to solve the puzzle (in milliseconds)
     */
    @Override
    public double analyze(String log) {
        String[] lines = log.split("\n");

        long start = Long.parseLong(lines[0].split(" ")[0]);
        long end = Long.parseLong(lines[lines.length-1].split(" ")[0]);
        return end-start;
    }

    // Example running function
    public static void main(String[] args) {
        String log = "1479235708567 5 2+\n" +
        "1479235709971 8 1+\n" +
        "1479235709244 8 1+\n" +
        "1479235710571 0 2+\n" +
        "1479235711466 0 2";
        TimeAnalyzer zer = new TimeAnalyzer();
        System.out.println(zer.analyze(log));
    }
}
