package de.chess.main;

import java.awt.Color;
import java.awt.Font;

public class Constants {
	
	public static final String NAME = "Milotic";
	public static final int ELO = 2100;
	
	public static final int WINDOW_DEFAULT_WIDTH = 1100;
	public static final int WINDOW_DEFAULT_HEIGHT = 950;
	
	public static final int TILE_SIZE = 72;
	public static final int BOARD_SIZE = TILE_SIZE * 8;
	
	public static final Color COLOR_BACKGROUND = new Color(0x161512);
	public static final Color COLOR_UI = new Color(0x262421);
	public static final Color COLOR_UI_LIGHT = new Color(0x302E2C);
	public static final Color COLOR_UI_LIGHTER = new Color(0x3A3733);
	
	public static final Color COLOR_TEXT_GREY = new Color(0xbababa);
	
	public static final Color COLOR_BOARD_LASTMOVE = new Color(155, 199, 0, 105);
	public static final Color COLOR_BOARD_SELECT = new Color(20, 85, 30, 128);
	
	public static final int BRIGHTNESS_BLACK = 0;
	public static final Color COLOR_BLACK = new Color(BRIGHTNESS_BLACK, BRIGHTNESS_BLACK, BRIGHTNESS_BLACK);
	public static final Color COLOR_WHITE = new Color(0xFFFFFF);
	public static final Color COLOR_GREEN = new Color(0x5C8D24);
	public static final Color COLOR_BLUE = new Color(0x3692E7);
	
	public static final Font FONT_REGULAR = new Font("Noto Sans", 0, 0).deriveFont(16.8f);
	public static final Font FONT_TITLE = new Font("Roboto", 0, 30);
	
	public static final boolean PRINT_FPS = false;
	
}
