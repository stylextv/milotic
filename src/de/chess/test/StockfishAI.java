package de.chess.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import de.chess.game.Board;

public class StockfishAI {
	
	private static final int DIFFICULTY = 5; // 4 = 2000, 5 = 2300, 6 = 2700
	
	private static final int[] SKILL_LEVELS = new int[] {
			3,
			6,
			9,
			11,
			14,
			17,
			20,
			20
	};
	
	private static final int[] DEPTHS = new int[] {
			1,
			2,
			3,
			4,
			6,
			8,
			10,
			12
	};
	
	private static Process process;
	
	private static boolean running;
	private static boolean kill;
	
	private static Board inputBoard;
	private static String outputMove;
	
	public static void start() {
		try {
			running = true;
			process = new ProcessBuilder("engines/stockfish_13_win_x64_bmi2.exe").start();
			
			new Thread(() -> {
				try {
					InputStream is = process.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					String line;
					OutputStream os = process.getOutputStream();
					OutputStreamWriter osr=new OutputStreamWriter(os);
					BufferedWriter bw=new BufferedWriter(osr);
					
					writeLine(bw, "setoption name Skill Level value " + SKILL_LEVELS[DIFFICULTY]);
					
					while(!kill) {
						if(br.ready()) {
							line = br.readLine();
							
							if(line.startsWith("bestmove ")) {
								outputMove = line.split(" ")[1];
							}
						}
						if(inputBoard != null) {
							writeLine(bw, "position fen "+inputBoard.getFen());
							writeLine(bw, "go depth " + DEPTHS[DIFFICULTY]);
							
							inputBoard = null;
						}
						
						Thread.sleep(100);
					}
					
					br.close();
					bw.close();
				} catch (IOException | InterruptedException ex) {
					ex.printStackTrace();
				}
				
				running = false;
			}).start();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	private static void writeLine(BufferedWriter bw, String s) throws IOException {
		bw.write(s);
		bw.newLine();
		bw.flush();
	}
	
	public static void kill() {
		if(process != null) {
			kill = true;
			process.destroy();
			
			while(running) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException ex) {}
			}
		}
	}
	
	public static void requestMove(Board b) {
		inputBoard = b;
	}
	
	public static boolean hasOutputMove() {
		return outputMove != null;
	}
	
	public static String getOutputMove() {
		return outputMove;
	}
	
	public static void flushOutputMove() {
		outputMove = null;
	}
	
}
