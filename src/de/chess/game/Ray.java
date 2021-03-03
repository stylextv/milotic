package de.chess.game;

public class Ray {
	
	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;
	public static final int NORTH_EAST = 4;
	public static final int NORTH_WEST = 5;
	public static final int SOUTH_EAST = 6;
	public static final int SOUTH_WEST = 7;
	
	private static final long[][] RAYS = new long[8][64];
	
	static {
		for(int square = 0; square < 64; square++) {
			RAYS[NORTH][square] = 0x0101010101010100l << square;
			
			RAYS[SOUTH][square] = 0x0080808080808080l >>> (63 - square);
			
			RAYS[EAST][square] = 2 * ((1l << (square | 7)) - (1l << square));
			
			RAYS[WEST][square] = (1l << square) - (1l << (square & 56));
			
			RAYS[NORTH_WEST][square] = BitOperations.moveWest(0x102040810204000l, 7 - (square % 8)) << (square / 8) * 8;
			
			RAYS[NORTH_EAST][square] = BitOperations.moveEast(0x8040201008040200l, square % 8) << (square / 8) * 8;
			
			RAYS[SOUTH_WEST][square] = BitOperations.moveWest(0x40201008040201l, 7 - (square % 8)) >>> ((7 - square / 8) * 8);
			
			RAYS[SOUTH_EAST][square] = BitOperations.moveEast(0x2040810204080l, square % 8) >>> ((7 - square / 8) * 8);
		}
	}
	
	public static long getRay(int dir, int square) {
		return RAYS[dir][square];
	}
	
}
