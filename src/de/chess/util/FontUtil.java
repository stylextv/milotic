package de.chess.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

public class FontUtil {
	
	public static void load() {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			
			loadFont(ge, "NotoSans-Regular.ttf");
			loadFont(ge, "Roboto-Regular.ttf");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			System.exit(1);
		}
	}
	
	private static void loadFont(GraphicsEnvironment ge, String name) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, FontUtil.class.getClassLoader().getResourceAsStream("assets/fonts/"+name));
		
		ge.registerFont(font);
	}
	
}
