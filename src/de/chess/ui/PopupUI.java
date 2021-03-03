package de.chess.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

import de.chess.game.PieceCode;
import de.chess.game.Winner;
import de.chess.main.Constants;
import de.chess.util.ImageUtil;
import de.chess.util.MathUtil;

public class PopupUI {
	
	private static final int SHADOW_MARGIN = 80;
	
	private static final BufferedImage BUFFER = new BufferedImage(400 + SHADOW_MARGIN, 300 + SHADOW_MARGIN, BufferedImage.TYPE_INT_ARGB);
	private static final Graphics2D BUFFER_GRAPHICS = (Graphics2D) BUFFER.getGraphics();
	
	private static float state = 1;
	
	private static int displayWinner = Winner.NONE;
	
	private static boolean initialOpen = true;
	
	private static float whiteButtonState;
	private static float blackButtonState;
	
	static {
		BUFFER_GRAPHICS.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		BUFFER_GRAPHICS.setBackground(new Color(0, 0, 0, 0));
	}
	
	public static BufferedImage generate(String title, float alpha) {
		whiteButtonState = MathUtil.lerp(whiteButtonState, isHoveringButton(true, UIManager.getMouseX(), UIManager.getMouseY(), UIManager.getWidth(), UIManager.getHeight()) ? 1 : 0, 0.3f);
		blackButtonState = MathUtil.lerp(blackButtonState, isHoveringButton(false, UIManager.getMouseX(), UIManager.getMouseY(), UIManager.getWidth(), UIManager.getHeight()) ? 1 : 0, 0.3f);
		
		BUFFER_GRAPHICS.clearRect(0, 0, BUFFER.getWidth(), BUFFER.getHeight());
		
		int width = BUFFER.getWidth() - SHADOW_MARGIN;
		int height = BUFFER.getHeight() - SHADOW_MARGIN;
		
		int off = SHADOW_MARGIN / 2;
		
		BUFFER_GRAPHICS.drawImage(ImageUtil.POPUP_SHADOW, 0, 2, null);
		
		BUFFER_GRAPHICS.setColor(Constants.COLOR_UI);
		BUFFER_GRAPHICS.fillRoundRect(off, off, width, height, 4, 4);
		
		BUFFER_GRAPHICS.setColor(Constants.COLOR_TEXT_GREY);
		BUFFER_GRAPHICS.setFont(Constants.FONT_TITLE);
		
		BUFFER_GRAPHICS.drawString(title, off + width/2 - BUFFER_GRAPHICS.getFontMetrics().stringWidth(title)/2, off + height/2 - 48);
		
//		int j = Constants.BRIGHTNESS_BLACK;
//		
//		BUFFER_GRAPHICS.setColor(new Color(j, j, j, 255 - (int) (32 * buttonState)));
//		BUFFER_GRAPHICS.fillRoundRect(off + width/2 - BUTTON_WIDTH/2, off + height/2 + 10, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_HEIGHT, BUTTON_HEIGHT);
//		
//		BUFFER_GRAPHICS.setColor(Constants.COLOR_WHITE);
//		BUFFER_GRAPHICS.setFont(Constants.FONT_REGULAR);
//		
//		String text = "Nochmal";
//		
//		BUFFER_GRAPHICS.drawString(text, off + width/2 - BUFFER_GRAPHICS.getFontMetrics().stringWidth(text)/2, off + height/2 + 10 + BUTTON_HEIGHT/2 + BUFFER_GRAPHICS.getFontMetrics().getHeight()/5 + 1);
		
		int buttonY = 140;
		
		int buttonDis = 84 / 2 + 16;
		
		drawColorButton(PieceCode.WHITE, off + width / 2 - buttonDis - 84 / 2, off + buttonY, 84, whiteButtonState);
		drawColorButton(PieceCode.BLACK, off + width / 2 + buttonDis - 84 / 2, off + buttonY, 84, blackButtonState);
		
		DataBuffer data = BUFFER.getRaster().getDataBuffer();
		
		for(int i=0; i<data.getSize(); i++) {
			int rgb = data.getElem(i);
			
			int a = (rgb >>> 24) & 0xFF;
			a = Math.round(alpha * a);
			
			rgb = (rgb & 0x00FFFFFF) + (a << 24);
			
			data.setElem(i, rgb);
		}
		
		return BUFFER;
	}
	
	private static void drawColorButton(int side, int x, int y, int size, float state) {
		BufferedImage image = PieceCode.getSprite(PieceCode.getSpriteCode(side, PieceCode.KING));
		
		int r = Constants.COLOR_UI_LIGHTER.getRed();
		int g = Constants.COLOR_UI_LIGHTER.getGreen();
		int b = Constants.COLOR_UI_LIGHTER.getBlue();
		
		state = state * 0.2f;
		
		r += r * state;
		g += g * state;
		b += b * state;
		
		BUFFER_GRAPHICS.setColor(new Color(r, g, b));
		
		BUFFER_GRAPHICS.fillRoundRect(x, y, size, size, 8, 8);
		
		BUFFER_GRAPHICS.drawImage(image, x + (size - image.getWidth()) / 2, y + (size - image.getHeight()) / 2, null);
	}
	
	public static void updatePopup(Graphics2D graphics) {
		boolean show = isActive();
		
		state = MathUtil.lerp(state, show ? 1 : 0, 0.2f);
		
		if(state > 0.005f) {
			String s;
			
			if(displayWinner == Winner.NONE) s = "Wähle eine Farbe";
			else if(displayWinner == Winner.DRAW) s = "Unentschieden";
			else s = displayWinner == BoardUI.getHumanSide() ? "Sieg" : "Niederlage";
			
			drawPopup(graphics, s);
		} else {
			state = 0;
			
			whiteButtonState = 0;
			blackButtonState = 0;
		}
	}
	
	private static void drawPopup(Graphics2D graphics, String title) {
		BufferedImage image = generate(title, state);
		
		float x = (UIManager.getWidth() - image.getWidth()) / 2;
		float y = (UIManager.getHeight() - image.getHeight()) / 2;
		
		x = x - (1 - state) * 40;
		
		AffineTransform trans = new AffineTransform();
		
		trans.translate(x, y);
		
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		graphics.drawImage(image, trans, null);
		
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
	}
	
	public static boolean isActive() {
		return (BoardUI.getWinner() != Winner.NONE && BoardUI.getLastMoveState() == 1) || initialOpen;
	}
	
	public static boolean isHoveringButton(boolean left, int mx, int my, int width, int height) {
		int buttonDis = 84 / 2 + 16;
		
		int x = width / 2 + (left ? -buttonDis : buttonDis) - 84 / 2;
		int y = height / 2 - BUFFER.getHeight() / 2 + SHADOW_MARGIN / 2 + 140;
		
		x = x - (int) ((1 - state) * 40);
		
		x = mx - x;
		y = my - y;
		
		return x >= 0 && y>= 0 && x < 84 && y < 84;
	}
	
	public static void setDisplayedWinner(int w) {
		displayWinner = w;
	}
	
	public static float getState() {
		return state;
	}
	
	public static boolean isInitiallyOpen() {
		return initialOpen;
	}
	
	public static void clearInitialOpen() {
		initialOpen = false;
	}
	
}
