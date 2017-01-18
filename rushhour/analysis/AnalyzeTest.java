package rushhour.analysis;

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
        String[] lines = log.split("\n");
        String[][] logArr = new String[lines.length][];
        for (int i=0; i<lines.length; i++) {
            logArr[i] = lines[i].split(" ");
        }
        TimeAnalyzer time = new TimeAnalyzer();
        UndoAnalyzer undo = new UndoAnalyzer();
        MoveTimeAnalyzer movetime = new MoveTimeAnalyzer();
        System.out.println(time.analyze(logArr));
        System.out.println(undo.analyze(logArr));
        System.out.println(movetime.analyze(logArr));
    }
}
