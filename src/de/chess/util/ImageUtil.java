package de.chess.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageUtil {
	
	public static final ArrayList<BufferedImage> ICONS = new ArrayList<BufferedImage>();
	
	public static BufferedImage BOARD;
	
	public static BufferedImage BOARD_SHADOW;
	public static BufferedImage POPUP_SHADOW;
	public static BufferedImage PROMOTION_SHADOW;
	
	public static BufferedImage BLACK_BISHOP;
	public static BufferedImage BLACK_KING;
	public static BufferedImage BLACK_KNIGHT;
	public static BufferedImage BLACK_PAWN;
	public static BufferedImage BLACK_QUEEN;
	public static BufferedImage BLACK_ROOK;
	
	public static BufferedImage WHITE_BISHOP;
	public static BufferedImage WHITE_KING;
	public static BufferedImage WHITE_KNIGHT;
	public static BufferedImage WHITE_PAWN;
	public static BufferedImage WHITE_QUEEN;
	public static BufferedImage WHITE_ROOK;
	
	public static BufferedImage ATTACK_INDICATOR;
	
	public static void load() {
		try {
			ICONS.add(loadImage("icons/icon16.png"));
			ICONS.add(loadImage("icons/icon24.png"));
			ICONS.add(loadImage("icons/icon32.png"));
			ICONS.add(loadImage("icons/icon64.png"));
			ICONS.add(loadImage("icons/icon128.png"));
			ICONS.add(loadImage("icons/icon256.png"));
			
			BOARD = loadImage("board.png");
			
			BOARD_SHADOW = loadImage("board_shadow.png");
			POPUP_SHADOW = loadImage("popup_shadow.png");
			PROMOTION_SHADOW = loadImage("promotion_shadow.png");
			
			BLACK_BISHOP = loadImage("pieces/black_bishop.png");
			BLACK_KING = loadImage("pieces/black_king.png");
			BLACK_KNIGHT = loadImage("pieces/black_knight.png");
			BLACK_PAWN = loadImage("pieces/black_pawn.png");
			BLACK_QUEEN = loadImage("pieces/black_queen.png");
			BLACK_ROOK = loadImage("pieces/black_rook.png");
			
			WHITE_BISHOP = loadImage("pieces/white_bishop.png");
			WHITE_KING = loadImage("pieces/white_king.png");
			WHITE_KNIGHT = loadImage("pieces/white_knight.png");
			WHITE_PAWN = loadImage("pieces/white_pawn.png");
			WHITE_QUEEN = loadImage("pieces/white_queen.png");
			WHITE_ROOK = loadImage("pieces/white_rook.png");
			
			ATTACK_INDICATOR = loadImage("attack_indicator.png");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			System.exit(1);
		}
	}
	
	private static BufferedImage loadImage(String name) throws IOException {
		return ImageIO.read(ImageUtil.class.getClassLoader().getResourceAsStream("assets/textures/"+name));
	}
	
}
