package de.chess.game;

public class TranspositionEntry {
	
	public static final int TYPE_EXACT = 0;
	public static final int TYPE_LOWER_BOUND = 1;
	public static final int TYPE_UPPER_BOUND = 2;
	
	private long key;
	private int depth;
	
	private Move m;
	
	private int type;
	
	private int score;
	
	private int age;
	
	public TranspositionEntry(long key, int depth, Move m, int type, int score, int age) {
		this.key = key;
		this.depth = depth;
		this.m = m;
		this.type = type;
		this.score = score;
		this.age = age;
	}
	
	public long getPositionKey() {
		return key;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public Move getMove() {
		return m;
	}
	
	public int getType() {
		return type;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getAge() {
		return age;
	}
	
}
