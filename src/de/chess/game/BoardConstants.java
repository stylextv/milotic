package de.chess.game;

public class BoardConstants {
	
	public static final int BOARD_SIZE_SQ = 64;
	
	public static final int MAX_GAME_MOVES = 2048;
	
	public static final int MAX_POSSIBLE_MOVES = 256;
	
	public static final long[] BIT_SET = new long[64];
	
	static {
		for(int i=0; i<BIT_SET.length; i++) {
			long l = 1l << i;
			
			BIT_SET[i] = l;
		}
	}
	
}
