package rushhour.generation;

import rushhour.core.*;


public class DifficultyAdjust {

	RandomGraphGen rG;
	Board board;
	
	public BoardGraph getHarderBoard(BoardGraph originalBoard){
		int minMoves = originalBoard.depth;
		rG.generateBoard(minMoves);
		return rG.graph;
	}
	
	public BoardGraph getEasierBoard(BoardGraph originalBoard){
		int maxMoves = originalBoard.depth;
		rG.generateBoard(maxMoves);
		return rG.graph;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
