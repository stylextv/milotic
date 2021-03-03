package de.chess.render;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import de.chess.ui.UIManager;

public class Renderer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics graphics) {
		try {
			UIManager.drawFrame((Graphics2D) graphics);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
