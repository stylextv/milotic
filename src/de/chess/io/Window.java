package de.chess.io;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import de.chess.main.Constants;
import de.chess.main.Main;
import de.chess.render.Renderer;
import de.chess.ui.UIManager;
import de.chess.util.ImageUtil;

public class Window {
	
	public static final int MOUSE_PRESSED = 0;
	public static final int MOUSE_RELEASED = 1;
	
	private JFrame frame;
	private Renderer renderer;
	
	public Window(int width, int height) {
		this.frame = new JFrame(Constants.NAME);
		this.renderer = new Renderer();
		
		this.frame.setSize(width, height);
		this.frame.setMinimumSize(new Dimension(width - 80, height - 80));
		
		frame.setIconImages(ImageUtil.ICONS);
		
		centerOnScreen();
	}
	
	public void create() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				frame.setVisible(false);
				
				Main.stop();
			}
		});
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		frame.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Point p = getMousePosition();
				
				if(e.getButton() != MouseEvent.BUTTON1 || p == null) return;
				
				UIManager.onMouseClick(p, MOUSE_RELEASED);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				Point p = getMousePosition();
				
				if(e.getButton() != MouseEvent.BUTTON1 || p == null) return;
				
				UIManager.onMouseClick(p, MOUSE_PRESSED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		frame.add(renderer);
		
		frame.setVisible(true);
	}
	
	public void drawSync() {
		frame.repaint();
	}
	
	public void centerOnScreen() {
		Dimension monitor = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.setLocation((monitor.width - frame.getWidth()) / 2, (monitor.height - 40 - frame.getHeight()) / 2);
	}
	
	public int getWidth() {
		return renderer.getWidth();
	}
	
	public int getHeight() {
		return renderer.getHeight();
	}
	
	public Point getMousePosition() {
		return renderer.getMousePosition();
	}
	
}
