package de.chess.game;

import java.util.ArrayList;

public class MoveGenerator {
	
	private static final long PAWN_DOUBLEMOVE_WHITE = 280375465082880l;
	private static final long PAWN_DOUBLEMOVE_BLACK = 16711680;
	
	public static final int[] KING_START_POSITION = new int[] {
			60, 4
	};
	
	public static final int[] ROOK1_START_POSITION = new int[] {
			56, 0
	};
	public static final int[] ROOK2_START_POSITION = new int[] {
			63, 7
	};
	
	public static void generateAllMoves(Board b, MoveList list) {
		int side = b.getSide();
		int facing = 1;
		int opponentSide = (side + 1) % 2;
		
		if(side == PieceCode.WHITE) facing = -1;
		
		long possibleSquares = BitOperations.inverse(b.getBitBoard(side).getValue());
		long occupiedSquares = b.getBitBoard(side).orReturn(b.getBitBoard(opponentSide));
		long emptySquares = BitOperations.inverse(occupiedSquares);
		
		addPawnMoves(b, list, side, opponentSide, facing, emptySquares);
		addKnightMoves(b, list, side, possibleSquares);
		addKingMoves(b, list, side, possibleSquares, emptySquares);
		
		addSliderMoves(b, list, side, possibleSquares, occupiedSquares, PieceCode.BISHOP, LookupTable.RELEVANT_BISHOP_MOVES, LookupTable.BISHOP_MAGIC_VALUES, LookupTable.BISHOP_MAGIC_INDEX_BITS, LookupTable.BISHOP_MOVES);
		addSliderMoves(b, list, side, possibleSquares, occupiedSquares, PieceCode.ROOK, LookupTable.RELEVANT_ROOK_MOVES, LookupTable.ROOK_MAGIC_VALUES, LookupTable.ROOK_MAGIC_INDEX_BITS, LookupTable.ROOK_MOVES);
	}
	
	private static void addPawnMoves(Board b, MoveList list, int side, int opponentSide, int facing, long emptySquares) {
		long pawnSquares = b.getBitBoard(side).andReturn(b.getBitBoard(PieceCode.PAWN));
		
		long pawnMoves = pawnSquares;
		if(side == PieceCode.WHITE) pawnMoves >>>= 8;
		else pawnMoves <<= 8;
		
		long possiblePawnMoves = pawnMoves & emptySquares;
		
		long possiblePawnMovesAdd = possiblePawnMoves;
		
		while(possiblePawnMovesAdd != 0) {
			int index = BitOperations.bitScanForward(possiblePawnMovesAdd);
			
			addPawnMove(list, side, index - 8 * facing, index, 0, MoveFlag.NONE);
			
			possiblePawnMovesAdd ^= BoardConstants.BIT_SET[index];
		}
		
		long possiblePawnDoubleMoves = possiblePawnMoves;
		if(side == PieceCode.WHITE) possiblePawnDoubleMoves &= PAWN_DOUBLEMOVE_WHITE;
		else possiblePawnDoubleMoves &= PAWN_DOUBLEMOVE_BLACK;
		
		if(side == PieceCode.WHITE) possiblePawnDoubleMoves >>>= 8;
		else possiblePawnDoubleMoves <<= 8;
		
		possiblePawnDoubleMoves = possiblePawnDoubleMoves & emptySquares;
		
		while(possiblePawnDoubleMoves != 0) {
			int index = BitOperations.bitScanForward(possiblePawnDoubleMoves);
			
			addPawnMove(list, side, index - 16 * facing, index, 0, MoveFlag.DOUBLE_PAWN_ADVANCE);
			
			possiblePawnDoubleMoves ^= BoardConstants.BIT_SET[index];
		}
		
		long opponentSquares = b.getBitBoard(opponentSide).getValue();
		
		long pawnAttacksLeft = (pawnMoves << 1);
		long pawnAttacksRight = (pawnMoves >>> 1);
		
		int enPassant = b.getEnPassant();
		
		if(enPassant != BoardSquare.NONE) {
			long moveTo = BoardConstants.BIT_SET[enPassant];
			
			int enPassantX = enPassant % 8;
			
			if(enPassantX != 0 && (pawnAttacksLeft & moveTo) != 0) {
				list.addMove(enPassant - 8 * facing - 1, enPassant, 0, 0, MoveFlag.EN_PASSANT);
			}
			if(enPassantX != 7 && (pawnAttacksRight & moveTo) != 0) {
				list.addMove(enPassant - 8 * facing + 1, enPassant, 0, 0, MoveFlag.EN_PASSANT);
			}
		}
		
		pawnAttacksLeft = pawnAttacksLeft & opponentSquares;
		pawnAttacksRight = pawnAttacksRight & opponentSquares;
		
		while(pawnAttacksLeft != 0) {
			int index = BitOperations.bitScanForward(pawnAttacksLeft);
			
			if(index % 8 != 0) {
				addPawnMove(list, side, index - 8 * facing - 1, index, b.getPieceType(index), MoveFlag.NONE);
			}
			
			pawnAttacksLeft ^= BoardConstants.BIT_SET[index];
		}
		while(pawnAttacksRight != 0) {
			int index = BitOperations.bitScanForward(pawnAttacksRight);
			
			if(index % 8 != 7) {
				addPawnMove(list, side, index - 8 * facing + 1, index, b.getPieceType(index), MoveFlag.NONE);
			}
			
			pawnAttacksRight ^= BoardConstants.BIT_SET[index];
		}
	}
	
	private static void addPawnMove(MoveList list, int side, int from, int to, int captured, int flag) {
		boolean promoted = false;
		int toY = to / 8;
		
		if((side == PieceCode.WHITE && toY == 0) || (side == PieceCode.BLACK && toY == 7)) {
			promoted = true;
		}
		
		if(promoted) {
			
			list.addMove(from, to, captured, PieceCode.QUEEN, flag);
			list.addMove(from, to, captured, PieceCode.KNIGHT, flag);
			list.addMove(from, to, captured, PieceCode.ROOK, flag);
			list.addMove(from, to, captured, PieceCode.BISHOP, flag);
			
		} else {
			
			list.addMove(from, to, captured, 0, flag);
		}
	}
	
	private static void addKnightMoves(Board b, MoveList list, int side, long possibleSquares) {
		long knightSquares = b.getBitBoard(side).andReturn(b.getBitBoard(PieceCode.KNIGHT));
		
		while(knightSquares != 0) {
			int index = BitOperations.bitScanForward(knightSquares);
			
			long moves = LookupTable.KNIGHT_MOVES[index] & possibleSquares;
			
			while(moves != 0) {
				int to = BitOperations.bitScanForward(moves);
				
				list.addMove(index, to, b.getPieceType(to), 0, MoveFlag.NONE);
				
				moves ^= BoardConstants.BIT_SET[to];
			}
			
			knightSquares ^= BoardConstants.BIT_SET[index];
		}
	}
	
	private static void addKingMoves(Board b, MoveList list, int side, long possibleSquares, long emptySquares) {
		long kingSquares = b.getBitBoard(side).andReturn(b.getBitBoard(PieceCode.KING));
		
		int index = BitOperations.bitScanForward(kingSquares);
		
		long moves = LookupTable.KING_MOVES[index] & possibleSquares;
		
		while(moves != 0) {
			int to = BitOperations.bitScanForward(moves);
			
			list.addMove(index, to, b.getPieceType(to), 0, MoveFlag.NONE);
			
			moves ^= BoardConstants.BIT_SET[to];
		}
		
		if(side == PieceCode.WHITE) {
			
			if((b.getCastlePerms() & Castling.WHITE_QUEEN_SIDE) == 0) {
				addCastlingMove(b, list, side, index, Castling.WHITE_QUEEN_SIDE, emptySquares);
			}
			if((b.getCastlePerms() & Castling.WHITE_KING_SIDE) == 0) {
				addCastlingMove(b, list, side, index, Castling.WHITE_KING_SIDE, emptySquares);
			}
			
		} else {
			
			if((b.getCastlePerms() & Castling.BLACK_QUEEN_SIDE) == 0) {
				addCastlingMove(b, list, side, index, Castling.BLACK_QUEEN_SIDE, emptySquares);
			}
			if((b.getCastlePerms() & Castling.BLACK_KING_SIDE) == 0) {
				addCastlingMove(b, list, side, index, Castling.BLACK_KING_SIDE, emptySquares);
			}
		}
	}
	
	private static void addCastlingMove(Board b, MoveList list, int side, int from, int castlingSide, long emptySquares) {
		int dir = -1;
		
		int flag = MoveFlag.CASTLING_QUEEN_SIDE;
		
		if(castlingSide == Castling.WHITE_KING_SIDE || castlingSide == Castling.BLACK_KING_SIDE) {
			dir = 1;
			
			flag = MoveFlag.CASTLING_KING_SIDE;
		}
		
		int to = from + dir * 2;
		
		int square = from;
		
		while(true) {
			square += dir;
			
			int x = square % 8;
			
			if(x == 0 || x == 7) break;
			
			if((emptySquares & BoardConstants.BIT_SET[square]) == 0) {
				return;
			}
		}
		
		for(int i = 0; i < 3; i++) {
			square = from + dir * i;
			
			if(b.isUnderAttack(square, side)) {
				return;
			}
		}
		
		list.addMove(from, to, 0, 0, flag);
	}
	
	private static void addSliderMoves(Board b, MoveList list, int side, long possibleSquares, long occupiedSquares, int type, long[] moveTable, long[] magicValues, int[] magicIndices, long[][] finalMoveTable) {
		long squares = b.getBitBoard(side).andReturn(b.getBitBoard(type).orReturn(b.getBitBoard(PieceCode.QUEEN)));
		
		while(squares != 0) {
			int index = BitOperations.bitScanForward(squares);
			
			long moves = getSliderMoves(index, occupiedSquares, moveTable, magicValues, magicIndices, finalMoveTable);
			
			moves = moves & possibleSquares;
			
			while(moves != 0) {
				int to = BitOperations.bitScanForward(moves);
				
				list.addMove(index, to, b.getPieceType(to), 0, MoveFlag.NONE);
				
				moves ^= BoardConstants.BIT_SET[to];
			}
			
			squares ^= BoardConstants.BIT_SET[index];
		}
	}
	
	public static long getSliderMoves(int square, long occupiedSquares, long[] moveTable, long[] magicValues, int[] magicIndices, long[][] finalMoveTable) {
		long blockers = moveTable[square] & occupiedSquares;
		
		int key = (int) ((blockers * magicValues[square]) >>> (64 - magicIndices[square]));
		
		return finalMoveTable[square][key];
	}
	
	public static ArrayList<Move> getMovesForIndex(int index, Board b, MoveList list, boolean legalOnly) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for(int i=0; i<list.getCount(); i++) {
			Move m = list.getMove(i);
			
			if(m.getFrom() == index) {
				if(!legalOnly || b.isLegalMove(m)) {
					moves.add(m);
				}
			}
		}
		
		return moves;
	}
	
}
