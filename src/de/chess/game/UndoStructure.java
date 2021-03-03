package de.chess.game;

public class UndoStructure {
	
	private long positionKey;
	private int enPassant;
	private int castlePerms;
	private int fiftyMoveCounter;
	
	public UndoStructure() {}
	
	public UndoStructure(long positionKey, int enPassant, int castlePerms, int fiftyMoveCounter) {
		this.positionKey = positionKey;
		this.enPassant = enPassant;
		this.castlePerms = castlePerms;
		this.fiftyMoveCounter = fiftyMoveCounter;
	}
	
	public long getPositionKey() {
		return positionKey;
	}
	
	public void setPositionKey(long positionKey) {
		this.positionKey = positionKey;
	}
	
	public int getEnPassant() {
		return enPassant;
	}
	
	public void setEnPassant(int enPassant) {
		this.enPassant = enPassant;
	}
	
	public int getCastlePerms() {
		return castlePerms;
	}
	
	public void setCastlePerms(int castlePerms) {
		this.castlePerms = castlePerms;
	}
	
	public int getFiftyMoveCounter() {
		return fiftyMoveCounter;
	}
	
	public void setFiftyMoveCounter(int fiftyMoveCounter) {
		this.fiftyMoveCounter = fiftyMoveCounter;
	}
	
}
