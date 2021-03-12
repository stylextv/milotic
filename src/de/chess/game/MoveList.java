package de.chess.game;

import de.chess.ai.MoveEvaluator;

public class MoveList {
	
	private Move[] moves;
	
	private int count;
	
	private int picked;
	
	public MoveList() {
		moves = new Move[BoardConstants.MAX_POSSIBLE_MOVES];
	}
	
	public Move[] getMoves() {
		return moves;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setMoves(Move[] moves) {
		this.moves = moves;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void addMove(int from, int to, int captured, int promoted, int flag) {
		moves[count] = new Move(from, to, captured, promoted, flag);
		count++;
	}
	
	public Move getMove(int i) {
		return moves[i];
	}
	
	public void setMove(Move move, int i) {
		moves[i] = move;
	}
	
	public boolean hasMovesLeft() {
		return picked < count;
	}
	
	public void applyBestMove(Move best) {
		int hash = best.getHash();
		
		for(int i=0; i<count; i++) {
			Move m = moves[i];
			
			if(m.getHash() == hash) {
				m.setScore(MoveEvaluator.SCORE_BEST_MOVE);
				
				break;
			}
		}
	}
	
	public void reset() {
		picked = 0;
		
		for(int i=0; i<count; i++) {
			Move m = moves[i];
			
			m.setPicked(false);
		}
	}
	
	public Move next() {
		picked++;
		
		Move best = null;
		
		for(int i=0; i<count; i++) {
			Move m = moves[i];
			
			if(!m.wasPicked()) {
				if(best == null || m.getScore() > best.getScore()) {
					best = m;
				}
			}
		}
		
		best.setPicked(true);
		
		return best;
	}
	
}
