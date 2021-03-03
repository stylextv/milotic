package de.chess.game;

public class BitBoard {
	
	private long value;
	
	public BitBoard() {
		this(0);
	}
	public BitBoard(long value) {
		this.value = value;
	}
	
	public void clear() {
		value = 0;
	}
	
	public void or(long l) {
		value |= l;
	}
	
	public long orReturn(BitBoard b) {
		return value | b.getValue();
	}
	
	public void and(long l) {
		value &= l;
	}
	
	public long andReturn(long l) {
		return value & l;
	}
	public long andReturn(BitBoard b) {
		return value & b.getValue();
	}
	
	public void xor(long l) {
		value ^= l;
	}
	
	public long getValue() {
		return value;
	}
	
}
