package de.chess.ui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import de.chess.game.PieceCode;
import de.chess.game.Winner;
import de.chess.io.Window;
import de.chess.main.Constants;
import de.chess.main.Main;

public class UIManager {
	
	private static Window window;
	
	private static int width;
	private static int height;
	
	private static int mouseX;
	private static int mouseY;
	
	private static int mouseXMoved;
	
	public static void createWindow() {
		window = new Window(Constants.WINDOW_DEFAULT_WIDTH + 16, Constants.WINDOW_DEFAULT_HEIGHT + 39);
		window.create();
	}
	
	public static void update() {
		BoardUI.update(Main.getBoard());
	}
	
	public static void drawFrame(Graphics2D graphics) {
		width = window.getWidth();
		height = window.getHeight();
		
		Point p = window.getMousePosition();
		if(p != null) {
			mouseXMoved = p.x - mouseX;
			mouseX = p.x;
			mouseY = p.y;
		} else mouseXMoved = 0;
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		graphics.setColor(Constants.COLOR_BACKGROUND);
		graphics.fillRect(0, 0, width, height);
		
		BoardUI.drawBoard(graphics, Main.getBoard());
		
		MoveIndicatorUI.drawMoves(graphics, Main.getBoard());
		
		PromotionUI.updateDropDown(graphics);
		
		PopupUI.updatePopup(graphics);
	}
	
	public static void onMouseClick(Point p, int type) {
		if(type == Window.MOUSE_PRESSED && !PopupUI.isInitiallyOpen()) BoardUI.onMouseClick(p);
		
		if(PopupUI.isActive() && type == Window.MOUSE_RELEASED) {
			
			boolean b1 = PopupUI.isHoveringButton(true, p.x, p.y, width, height);
			boolean b2 = PopupUI.isHoveringButton(false, p.x, p.y, width, height);
			
			if(b1 || b2) {
				
				if(b1) BoardUI.setHumanSide(PieceCode.WHITE);
				else BoardUI.setHumanSide(PieceCode.BLACK);
				
				BoardUI.clearLastMove();
				
				if(!PopupUI.isInitiallyOpen()) Main.getBoard().reset();
				
				PopupUI.clearInitialOpen();
				
				BoardUI.setWinner(Winner.NONE);
			}
		}
	}
	
	public static void drawSync() {
		window.drawSync();
	}
	
	public static int getWidth() {
		return width;
	}
	
	public static int getHeight() {
		return height;
	}
	
	public static int getMouseX() {
		return mouseX;
	}
	
	public static int getMouseY() {
		return mouseY;
	}
	
	public static int getMouseXMoved() {
		return mouseXMoved;
	}
	
}
