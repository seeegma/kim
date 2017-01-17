package rushhour.evaluation;

// Tester class for analyzers
public class AnalyzeTest {

    // Example running function
    public static void main(String[] args) {
        String log = "1479235708567 5 2+\n" +
        "1479235709971 8 1+\n" +
        "1479235709244 8 1+\n" +
        "1479235710571 0 2+\n" +
        "1479235710870 U\n" +
        "1479235710900 R\n" +
        "1479235711466 0 2";
        TimeAnalyzer time = new TimeAnalyzer();
        UndoAnalyzer undo = new UndoAnalyzer();
        System.out.println(time.analyze(log));
        System.out.println(undo.analyze(log));
    }
}
