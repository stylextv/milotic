package de.chess.main;

import de.chess.ai.OpeningBook;
import de.chess.game.Board;
import de.chess.game.LookupTable;
import de.chess.sound.SoundUtil;
import de.chess.ui.UIManager;
import de.chess.util.FontUtil;
import de.chess.util.ImageUtil;

public class Main {
	
	private static boolean running = true;
	
	private static long lastFrameTime;
	private static long frameCounter;
	private static long lastFrameCounterReset;
	
	private static Board board;
	
	public static void main(String[] args) {
		try {
			ImageUtil.load();
			
			FontUtil.load();
			
			SoundUtil.load();
			
			board = new Board();
			
			UIManager.createWindow();
			
			LookupTable.initTables();
			OpeningBook.load();
			
			startGameLoop();
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void startGameLoop() throws InterruptedException {
		long now = System.nanoTime();
		
		lastFrameTime = now;
		lastFrameCounterReset = now;
		
		while(running) {
			runGameLoop();
		}
		
		System.exit(0);
	}
	
	private static void runGameLoop() throws InterruptedException {
		UIManager.update();
		UIManager.drawSync();
		
		long sleep = 16666667 - System.nanoTime() + lastFrameTime;
		
		if(sleep > 0) {
			Thread.sleep(sleep/1000000);
		}
		
		long now = System.nanoTime();
		
		frameCounter++;
		
		if(frameCounter == 100) {
			if(Constants.PRINT_FPS) System.out.println("fps: "+(1000000000f / ((now - lastFrameCounterReset) / (float) frameCounter)));
			
			lastFrameCounterReset = now;
			frameCounter = 0;
		}
		
		lastFrameTime = now;
	}
	
	public static void stop() {
		running = false;
	}
	
	public static boolean isRunning() {
		return running;
	}
	
	public static Board getBoard() {
		return board;
	}
	
}
