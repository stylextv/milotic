package de.chess.game;

public class TranspositionTable {
	
	private static final TranspositionEntry[] MAP = new TranspositionEntry[100000000];
	
	public static void storeEntry(long key, int depth, Move m, int originalAlpha, int beta, int score, int age) {
		int type;
		
		if(score <= originalAlpha) type = TranspositionEntry.TYPE_UPPER_BOUND;
		else if(score >= beta) type = TranspositionEntry.TYPE_LOWER_BOUND;
		else type = TranspositionEntry.TYPE_EXACT;
		
		putEntry(key, depth, m, type, score, age);
	}
	
	public static void putEntry(long key, int depth, Move m, int type, int score, int age) {
		int index = getMapIndex(key);
		
		TranspositionEntry old = MAP[index];
		TranspositionEntry e = new TranspositionEntry(key, depth, m, type, score, age);
		
		if(old == null || shouldReplace(e, old)) MAP[index] = e;
	}
	
	public static TranspositionEntry getEntry(long key) {
		TranspositionEntry e = MAP[getMapIndex(key)];
		
		if(e == null || e.getPositionKey() != key) return null;
		return e;
	}
	
	private static int getMapIndex(long key) {
		int i = (int) (key % MAP.length);
		
		if(i < 0) i *= -1;
		
		return i;
	}
	
	private static boolean shouldReplace(TranspositionEntry e, TranspositionEntry old) {
		return e.getAge() > old.getAge() || e.getDepth() < old.getDepth();
	}
	
}
