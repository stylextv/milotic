package de.chess.ai;

import de.chess.game.Board;
import de.chess.game.Move;
import de.chess.game.MoveList;

public class MoveEvaluator {
	
	public static void eval(MoveList list, Board b) {
		for(int i=0; i<list.getCount(); i++) {
			Move m = list.getMove(i);
			
			int score = 0;
			
			if(m.getCaptured() != 0) {
				score = 10 * Evaluator.getPieceValue(m.getCaptured()) - Evaluator.getPieceValue(b.getPieceType(m.getFrom()));
			}
			
			score += Evaluator.getPieceValue(m.getPromoted());
			
			m.setScore(score);
		}
	}
	
}
