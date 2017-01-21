package rushhour.evaluation;

import rushhour.core.*;

public interface Evaluator {

	public String description();

    public double eval(Board b, BoardGraph g);

}
