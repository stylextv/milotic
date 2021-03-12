package de.chess.ai;

import de.chess.game.BitOperations;
import de.chess.game.Board;
import de.chess.game.BoardConstants;
import de.chess.game.LookupTable;
import de.chess.game.MoveGenerator;
import de.chess.game.PieceCode;

public class Evaluator {
	
	private static final int PAWN_VALUE = 30;
	private static final int KNIGHT_VALUE = 96;
	private static final int BISHOP_VALUE = 99;
	private static final int ROOK_VALUE = 153;
	private static final int QUEEN_VALUE = 264;
	private static final int KING_VALUE = 120;
	
	private static final int[] PAWN_TABLE = new int[] {
			 0,   0,   0,   0,   0,   0,   0,   0, 
			 5,  10,  10, -20, -20,  10,  10,   5, 
			 5,  -5, -10,   0,   0, -10,  -5,   5, 
			 0,   0,   0,  20,  20,   0,   0,   0, 
			 5,   5,  10,  25,  25,  10,   5,   5, 
			10,  10,  20,  30,  30,  20,  10,  10, 
			50,  50,  50,  50,  50,  50,  50,  50, 
			 0,   0,   0,   0,   0,   0,   0,   0
	};
	
	private static final int[] KNIGHT_TABLE = new int[] {
			-50, -40, -30, -30, -30, -30, -40, -50, 
			-40, -20,   0,   5,   5,   0, -20, -40, 
			-30,   5,  10,  15,  15,  10,   5, -30, 
			-30,   0,  15,  20,  20,  15,   0, -30, 
			-30,   5,  15,  20,  20,  15,   5, -30, 
			-30,   0,  10,  15,  15,  10,   0, -30, 
			-40, -20,   0,   0,   0,   0, -20, -40, 
			-50, -40, -30, -30, -30, -30, -40, -50
	};
	
	private static final int[] BISHOP_TABLE = new int[] {
			-20, -10, -10, -10, -10, -10, -10, -20, 
			-10,   5,   0,   0,   0,   0,   5, -10, 
			-10,  10,  10,  10,  10,  10,  10, -10, 
			-10,   0,  10,  10,  10,  10,   0, -10, 
			-10,   5,   5,  10,  10,   5,   5, -10, 
			-10,   0,   5,  10,  10,   5,   0, -10, 
			-10,   0,   0,   0,   0,   0,   0, -10, 
			-20, -10, -10, -10, -10, -10, -10, -20
	};
	
	private static final int[] ROOK_TABLE = new int[] {
			 0,   0,   0,   5,   5,   0,   0,   0, 
			-5,   0,   0,   0,   0,   0,   0,  -5, 
			-5,   0,   0,   0,   0,   0,   0,  -5, 
			-5,   0,   0,   0,   0,   0,   0,  -5, 
			-5,   0,   0,   0,   0,   0,   0,  -5, 
			-5,   0,   0,   0,   0,   0,   0,  -5, 
			 5,  10,  10,  10,  10,  10,  10,   5, 
			 0,   0,   0,   0,   0,   0,   0,   0
	};
	
	private static final int[] QUEEN_TABLE = new int[] {
			-20, -10, -10,  -5,  -5, -10, -10, -20, 
			-10,   0,   5,   0,   0,   0,   0, -10, 
			-10,   5,   5,   5,   5,   5,   0, -10, 
			  0,   0,   5,   5,   5,   5,   0,  -5, 
			 -5,   0,   5,   5,   5,   5,   0,  -5, 
			-10,   0,   5,   5,   5,   5,   0, -10, 
			-10,   0,   0,   0,   0,   0,   0, -10, 
			-20, -10, -10,  -5,  -5, -10, -10, -20
	};
	
	private static final int[] KING_TABLE = new int[] {
			 20,  30,  10,   0,   0,  10,  30,  20, 
			 20,  20,   0,   0,   0,   0,  20,  20, 
			-10, -20, -20, -20, -20, -20, -20, -10, 
			-20, -30, -30, -40, -40, -30, -30, -20, 
			-30, -40, -40, -50, -50, -40, -40, -30, 
			-30, -40, -40, -50, -50, -40, -40, -30, 
			-30, -40, -40, -50, -50, -40, -40, -30, 
			-30, -40, -40, -50, -50, -40, -40, -30
	};
	
	private static final int[] KING_TABLE_ENDGAME = new int[] {
			-50, -30, -30, -30, -30, -30, -30, -50,
			-30, -30,   0,   0,   0,   0, -30, -30,
			-30, -10,  20,  30,  30,  20, -10, -30,
			-30, -10,  30,  40,  40,  30, -10, -30,
			-30, -10,  30,  40,  40,  30, -10, -30,
			-30, -10,  20,  30,  30,  20, -10, -30,
			-30, -20, -10,   0,   0, -10, -20, -30,
			-50, -40, -30, -20, -20, -30, -40, -50
	};
	
	private static final int[][] TABLES = new int[][] {
			null,
			null,
			PAWN_TABLE,
			KNIGHT_TABLE,
			BISHOP_TABLE,
			ROOK_TABLE,
			QUEEN_TABLE,
			KING_TABLE,
			KING_TABLE_ENDGAME
	};
	
	private static final int[] VALUES = new int[] {
			0,
			0,
			PAWN_VALUE,
			KNIGHT_VALUE,
			BISHOP_VALUE,
			ROOK_VALUE,
			QUEEN_VALUE,
			KING_VALUE
	};
	
	private static final int[] MIRROR_TABLE = new int[] {
			56,  57,  58,  59,  60,	 61,  62,  63,
			48,	 49,  50,  51,  52,	 53,  54,  55,
			40,	 41,  42,  43,  44,	 45,  46,  47,
			32,	 33,  34,  35,  36,	 37,  38,  39,
			24,	 25,  26,  27,  28,	 29,  30,  31,
			16,  17,  18,  19,  20,	 21,  22,  23,
			 8,   9,  10,  11,  12,  13,  14,  15,
			 0,   1,   2,   3,   4,   5,   6,	7
	};
	
	private static int[] ATTACKER_AMOUNT_WEIGHTS = new int[] {
			0,
			50,
			75,
			88,
			94,
			97,
			99
	};
	
	static {
		for(int i=PieceCode.PAWN; i<TABLES.length; i++) {
			int[] table = TABLES[i];
			
			int valueIndex = i;
			
			if(valueIndex >= VALUES.length) valueIndex = VALUES.length - 1;
			
			int base = VALUES[valueIndex];
			
			for(int j=0; j<64; j++) {
				int m = table[j];
				
				table[j] = Math.round(base + base * m / 350f);
			}
		}
	}
	
	public static int eval(Board b) {
		b.countPieces();
		
		int endgameWeight = b.getEndgameWeight();
		int normalWeight = 256 - endgameWeight;
		
		int score = 0;
		
		for(int i=0; i<12; i++) {
			int color = PieceCode.getColorFromSpriteCode(i);
			int type = PieceCode.getTypeFromSpriteCode(i);
			
			for(int j=0; j<b.getPieceAmount(i); j++) {
				
				int index = b.getPieceIndex(i, j);
				
				if(type == PieceCode.KING) {
					
					if(color == PieceCode.WHITE) {
						index = MIRROR_TABLE[index];
						
						score += (TABLES[type][index] * normalWeight + TABLES[type + 1][index] * endgameWeight) / 256;
					} else {
						score -= (TABLES[type][index] * normalWeight + TABLES[type + 1][index] * endgameWeight) / 256;
					}
					
				} else {
					
					if(color == PieceCode.WHITE) {
						index = MIRROR_TABLE[index];
						
						score += TABLES[type][index];
					} else {
						score -= TABLES[type][index];
					}
					
				}
			}
		}
		
		long occupiedSquares = b.getBitBoard(PieceCode.WHITE).orReturn(b.getBitBoard(PieceCode.BLACK));
		
		score += evalKingSafety(b, PieceCode.WHITE, normalWeight, occupiedSquares);
		score -= evalKingSafety(b, PieceCode.BLACK, normalWeight, occupiedSquares);
		
		score += evalEarlyQueenDevelopment(b, PieceCode.WHITE, normalWeight);
		score -= evalEarlyQueenDevelopment(b, PieceCode.BLACK, normalWeight);
		
		if(b.getSide() == PieceCode.WHITE) return score;
		return -score;
	}
	
	private static int evalKingSafety(Board b, int side, int normalWeight, long occupiedSquares) {
		int safety = 0;
		
		int index = b.getPieceIndex(PieceCode.getSpriteCode(side, PieceCode.KING), 0);
		
		int x = index % 8;
		int y = index / 8;
		
		int dir = -8;
		if(side == PieceCode.BLACK) dir = 8;
		
		if(normalWeight != 0) {
			if(x < 3 || x > 4) {
				int shieldY;
				
				boolean needsShield;
				
				if(side == PieceCode.WHITE) {
					shieldY = 6;
					
					needsShield = y > 5;
				} else {
					shieldY = 1;
					
					needsShield = y < 2;
				}
				
				if(needsShield) {
					int shieldX = x;
					
					if(shieldX < 3) shieldX = 1;
					else if(shieldX > 4) shieldX = 6;
					
					for(int i=-1; i<2; i++) {
						int squareX = shieldX + i;
						int square = shieldY * 8 + squareX;
						
						boolean closed = checkFriendlyPawn(b, square, side) || checkFriendlyPawn(b, square + dir, side);
						
						if(!closed) {
							int penalty = 20;
							
							int disToKing = x - squareX;
							if(disToKing < 0) disToKing = -disToKing;
							
							if(disToKing < 2) penalty = 25;
							
							safety -= (penalty * normalWeight) / 256;
						}
					}
				}
			}
			
			long bishopMoves = MoveGenerator.getSliderMoves(index, occupiedSquares, LookupTable.RELEVANT_BISHOP_MOVES, LookupTable.BISHOP_MAGIC_VALUES, LookupTable.BISHOP_MAGIC_INDEX_BITS, LookupTable.BISHOP_MOVES);
			long rookMoves = MoveGenerator.getSliderMoves(index, occupiedSquares, LookupTable.RELEVANT_ROOK_MOVES, LookupTable.ROOK_MAGIC_VALUES, LookupTable.ROOK_MAGIC_INDEX_BITS, LookupTable.ROOK_MOVES);
			
			long queenMoves = bishopMoves | rookMoves;
			
			int sliderAttackSquareAmount = BitOperations.countBits(queenMoves);
			
			safety -= (sliderAttackSquareAmount / 4 * normalWeight) / 256;
		}
		
		int opponentSide = (side + 1) % 2;
		
		int opponentPawnCode = PieceCode.getSpriteCode(opponentSide, PieceCode.PAWN);
		int opponentPawnAmount = b.getPieceAmount(opponentPawnCode);
		
		for(int i=0; i<opponentPawnAmount; i++) {
			int square = b.getPieceIndex(opponentPawnCode, i);
			
			int disX = square % 8 - x;
			int disY = square / 8 - y;
			
			if(disX < 0) disX = -disX;
			if(disY < 0) disY = -disY;
			
			int dis = disX + disY;
			
			if(dis < 4) {
				safety -= (4 - dis) * 10;
			}
		}
		
		long kingZone = LookupTable.KING_MOVES[index] | BoardConstants.BIT_SET[index];
		
		int extraSquare = index + dir * 2;
		
		if(extraSquare >= 0 && extraSquare < 64) kingZone |= BoardConstants.BIT_SET[extraSquare];
		
		int attackingPiecesAmount = 0;
		int valueOfAttacks = 0;
		
		int opponentKnightCode = PieceCode.getSpriteCode(opponentSide, PieceCode.KNIGHT);
		int opponentBishopCode = PieceCode.getSpriteCode(opponentSide, PieceCode.BISHOP);
		int opponentRookCode = PieceCode.getSpriteCode(opponentSide, PieceCode.ROOK);
		int opponentQueenCode = PieceCode.getSpriteCode(opponentSide, PieceCode.QUEEN);
		
		for(int i=0; i<b.getPieceAmount(opponentKnightCode); i++) {
			int square = b.getPieceIndex(opponentKnightCode, i);
			
			long attackingSquares = LookupTable.KNIGHT_MOVES[square] & kingZone;
			
			int l = BitOperations.countBits(attackingSquares);
			
			if(l != 0) {
				attackingPiecesAmount++;
				valueOfAttacks += l * 4;
			}
		}
		
		for(int i=0; i<b.getPieceAmount(opponentBishopCode); i++) {
			int square = b.getPieceIndex(opponentBishopCode, i);
			
			long attackingSquares = MoveGenerator.getSliderMoves(square, occupiedSquares, LookupTable.RELEVANT_BISHOP_MOVES, LookupTable.BISHOP_MAGIC_VALUES, LookupTable.BISHOP_MAGIC_INDEX_BITS, LookupTable.BISHOP_MOVES) & kingZone;
			
			int l = BitOperations.countBits(attackingSquares);
			
			if(l != 0) {
				attackingPiecesAmount++;
				valueOfAttacks += l * 4;
			}
		}
		
		for(int i=0; i<b.getPieceAmount(opponentRookCode); i++) {
			int square = b.getPieceIndex(opponentRookCode, i);
			
			long attackingSquares = MoveGenerator.getSliderMoves(square, occupiedSquares, LookupTable.RELEVANT_ROOK_MOVES, LookupTable.ROOK_MAGIC_VALUES, LookupTable.ROOK_MAGIC_INDEX_BITS, LookupTable.ROOK_MOVES) & kingZone;
			
			int l = BitOperations.countBits(attackingSquares);
			
			if(l != 0) {
				attackingPiecesAmount++;
				valueOfAttacks += l * 8;
			}
		}
		
		for(int i=0; i<b.getPieceAmount(opponentQueenCode); i++) {
			int square = b.getPieceIndex(opponentQueenCode, i);
			
			long attackingSquares = MoveGenerator.getSliderMoves(square, occupiedSquares, LookupTable.RELEVANT_ROOK_MOVES, LookupTable.ROOK_MAGIC_VALUES, LookupTable.ROOK_MAGIC_INDEX_BITS, LookupTable.ROOK_MOVES);
			
			attackingSquares |= MoveGenerator.getSliderMoves(square, occupiedSquares, LookupTable.RELEVANT_BISHOP_MOVES, LookupTable.BISHOP_MAGIC_VALUES, LookupTable.BISHOP_MAGIC_INDEX_BITS, LookupTable.BISHOP_MOVES);
			
			attackingSquares &= kingZone;
			
			int l = BitOperations.countBits(attackingSquares);
			
			if(l != 0) {
				attackingPiecesAmount++;
				valueOfAttacks += l * 16;
			}
		}
		
		if(attackingPiecesAmount != 0) {
			safety -= valueOfAttacks * ATTACKER_AMOUNT_WEIGHTS[attackingPiecesAmount - 1] / 100;
		}
		
		if(safety == 0) return 0;
		
		int opponentMaterial = 0;
		
		opponentMaterial += b.getPieceAmount(opponentPawnCode);
		opponentMaterial += b.getPieceAmount(opponentKnightCode) * 3;
		opponentMaterial += b.getPieceAmount(opponentBishopCode) * 3;
		opponentMaterial += b.getPieceAmount(opponentRookCode) * 5;
		opponentMaterial += b.getPieceAmount(opponentQueenCode) * 9;
		
		int totalMaterial = 8 * 1 + 4 * 3 + 2 * 5 + 1 * 9;
		
		return safety * opponentMaterial / totalMaterial;
	}
	
	private static boolean checkFriendlyPawn(Board b, int index, int side) {
		int code = b.getPiece(index);
		
		if(code == -1) return false;
		
		if(PieceCode.getTypeFromSpriteCode(code) != PieceCode.PAWN) return false;
		
		return PieceCode.getColorFromSpriteCode(code) == side;
	}
	
	private static int evalEarlyQueenDevelopment(Board b, int side, int normalWeight) {
		if(normalWeight == 0) return 0;
		
		int code = PieceCode.getSpriteCode(side, PieceCode.QUEEN);
		
		if(b.getPieceAmount(code) == 1) {
			int y = b.getPieceIndex(code, 0) / 8;
			
			int firstY = 0;
			
			if(side == PieceCode.WHITE) firstY = 7;
			
			if(y == firstY) return 0;
			else {
				int count = 0;
				
				for(int x=0; x<8; x++) {
					if(b.getPiece(firstY * 8 + x) != -1) count++;
				}
				
				int dis = y - firstY;
				if(dis < 0) dis = -dis;
				
				if(dis > 4) dis = 4;
				
				return -(count * 5 * dis / 4 * normalWeight) / 256;
			}
		}
		return 0;
	}
	
	public static int getPieceValue(int type) {
		return VALUES[type];
	}
	
}
