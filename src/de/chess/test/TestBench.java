package de.chess.test;

import de.chess.ai.OpeningBook;
import de.chess.game.Board;
import de.chess.game.BoardSquare;
import de.chess.game.LookupTable;
import de.chess.game.Move;
import de.chess.game.MoveGenerator;
import de.chess.game.MoveList;
import de.chess.game.Winner;
import de.chess.util.MathUtil;

public class TestBench {
	
	private static Board board;
	
	public static void main(String[] args) {
		board = new Board();
		
		LookupTable.initTables();
		OpeningBook.load();
		
		StockfishAI.start();
		
		runTest();
		
		StockfishAI.kill();
	}
	
	private static void runTest() {
		int games = 10;
		
		int stockfishWins = 0;
		int homeWins = 0;
		int draws = 0;
		
		for(int i=0; i<games; i++) {
			int homeSide = MathUtil.RANDOM.nextInt(2);
			
			int winner = runGame(homeSide);
			
			if(winner == homeSide) homeWins++;
			else if(winner == Winner.DRAW) draws++;
			else stockfishWins++;
		}
		
		System.out.println("----------");
		System.out.println("testing finished!");
		System.out.println("results:");
		System.out.println("home wins: "+homeWins+", stockfish wins: "+stockfishWins+", draws: "+draws);
	}
	
	private static int runGame(int homeSide) {
		int winner;
		
		while(true) {
			if(board.getSide() == homeSide) {
				board.makeAIMove();
			} else {
				playStockfishMove(board);
			}
			
			if((winner = board.findWinner()) != Winner.NONE) {
				break;
			}
		}
		
		board.reset();
		
		return winner;
	}
	
	public static Move playStockfishMove(Board b) {
		StockfishAI.requestMove(b);
		
		while(!StockfishAI.hasOutputMove()) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		String moveNotation = StockfishAI.getOutputMove();
		
		StockfishAI.flushOutputMove();
		
		int from = BoardSquare.getIndexFromNotation(moveNotation.substring(0, 2));
		int to = BoardSquare.getIndexFromNotation(moveNotation.substring(2, 4));
		
		Move m = null;
		
		MoveList list = new MoveList();
		
		MoveGenerator.generateAllMoves(b, list);
		
		for(int i=0; i<list.getCount(); i++) {
			Move move = list.getMove(i);
			
			if(from == move.getFrom() && to == move.getTo()) {
				m = move;
				break;
			}
		}
		
		System.out.println("---");
		System.out.println("played stockfish move: "+m);
		
		b.makeMove(m);
		
		return m;
	}
	
}
