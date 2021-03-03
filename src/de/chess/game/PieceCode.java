package de.chess.game;

import java.awt.image.BufferedImage;

import de.chess.util.ImageUtil;

public class PieceCode {
	
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	
	public static final int PAWN = 2;
	public static final int KNIGHT = 3;
	public static final int BISHOP = 4;
	public static final int ROOK = 5;
	public static final int QUEEN = 6;
	public static final int KING = 7;
	
	public static final int LAST = KING;
	
	private static final BufferedImage[] SPRITES = new BufferedImage[] {
			ImageUtil.WHITE_PAWN,
			ImageUtil.WHITE_KNIGHT,
			ImageUtil.WHITE_BISHOP,
			ImageUtil.WHITE_ROOK,
			ImageUtil.WHITE_QUEEN,
			ImageUtil.WHITE_KING,
			
			ImageUtil.BLACK_PAWN,
			ImageUtil.BLACK_KNIGHT,
			ImageUtil.BLACK_BISHOP,
			ImageUtil.BLACK_ROOK,
			ImageUtil.BLACK_QUEEN,
			ImageUtil.BLACK_KING
	};
	
	private static char[] FEN_NOTATIONS = new char[] {
			'P', 'N', 'B', 'R', 'Q', 'K',
			'p', 'n', 'b', 'r', 'q', 'k'
	};
	
	public static BufferedImage getSprite(int spriteCode) {
		return SPRITES[spriteCode];
	}
	
	public static int getSpriteCode(int color, int type) {
		return type - PAWN + color * 6;
	}
	
	public static int getColorFromSpriteCode(int code) {
		return code / 6;
	}
	public static int getTypeFromSpriteCode(int code) {
		return code % 6 + PAWN;
	}
	
	public static char getFenNotation(int code) {
		return FEN_NOTATIONS[code];
	}
	
	public static int getCodeFromFenNotation(char ch) {
		for(int i=0; i<FEN_NOTATIONS.length; i++) {
			if(FEN_NOTATIONS[i] == ch) return i;
		}
		
		return -1;
	}
	
}
