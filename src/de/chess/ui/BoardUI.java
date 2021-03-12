package de.chess.ui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import de.chess.game.Board;
import de.chess.game.Move;
import de.chess.game.MoveFlag;
import de.chess.game.MoveGenerator;
import de.chess.game.MoveList;
import de.chess.game.PieceCode;
import de.chess.game.Winner;
import de.chess.main.Constants;
import de.chess.main.Main;
import de.chess.sound.SoundUtil;
import de.chess.util.ImageUtil;
import de.chess.util.MathUtil;

public class BoardUI {
	
	public static int humanSide = PieceCode.WHITE;
	
	private static Move lastMove;
	private static double lastMoveState;
	
	private static long lastHumanMove;
	
	private static int selected = -1;
	private static ArrayList<Move> selectedMoves;
	
	private static int selectedSquare;
	
	private static Move[] pawnPromotions; 
	
	private static int winner = Winner.NONE;
	
	private static Point mouseClick;
	
	public static void update(Board board) {
		if(lastMove != null) {
			
			if(lastMoveState < 1) {
				lastMoveState += 0.04f;
			} else {
				lastMoveState = 1;
			}
		}
		
		boolean noWinner = winner == Winner.NONE;
		
		if(noWinner && board.getSide() != humanSide && System.currentTimeMillis() - lastHumanMove >= 700 && PopupUI.getState() == 0) {
			int side = board.getSide();
			
			lastMove = board.makeAIMove();
			lastMoveState = 0;
			
			SoundUtil.playMoveSound(lastMove, side);
			
			checkForWinner(board);
		}
		
		if(mouseClick != null) {
			
			if(pawnPromotions != null) {
				int i = PromotionUI.isHoveringBox(mouseClick.x, mouseClick.y, UIManager.getWidth(), UIManager.getHeight());
				
				if(i != -1) {
					clearLastMove();
					
					board.makeMove(pawnPromotions[i]);
					
					lastMove = pawnPromotions[i];
					lastMoveState = 0;
					
					checkForWinner(board);
					
					SoundUtil.playMoveSound(lastMove, board.getSide());
					
					lastHumanMove = System.currentTimeMillis();
				}
				
				pawnPromotions = null;
				
				mouseClick = null;
				
				return;
			}
			
			if(winner != Winner.NONE) {
				return;
			}
			
			int mx = mouseClick.x - UIManager.getWidth()/2 + Constants.BOARD_SIZE/2;
			int my = mouseClick.y - UIManager.getHeight()/2 + Constants.BOARD_SIZE/2;
			
			mouseClick = null;
			
			if(mx >= 0 && my >= 0) {
				int boardX = mx / Constants.TILE_SIZE;
				int boardY = my / Constants.TILE_SIZE;
				
				if(boardX < 8 && boardY < 8) {
					
					int index = boardY * 8 + boardX;
					
					if(humanSide == PieceCode.BLACK) {
						index = 63 - index;
					}
					
					if(selected == -1) {
						selectPiece(board, index, boardX, boardY);
					} else {
						Move[] moves = new Move[4];
						int l = 0;
						
						for(Move check : selectedMoves) {
							if(index == check.getTo()) {
								moves[l] = check;
								
								l++;
							}
						}
						
						clearSelectedPiece();
						
						if(l != 0) {
							if(l == 1) {
								clearLastMove();
								
								int side = board.getSide();
								
								board.makeMove(moves[0]);
								
								lastMove = moves[0];
								lastMoveState = 0;
								
								SoundUtil.playMoveSound(moves[0], side);
								
								checkForWinner(board);
								
								lastHumanMove = System.currentTimeMillis();
							} else {
								PromotionUI.setSide(board.getSide());
								
								int x = moves[0].getTo() % 8;
								
								if(humanSide == PieceCode.BLACK) {
									x = 7 - x;
								}
								
								PromotionUI.setOffset(x);
								
								pawnPromotions = moves;
							}
						} else if(index != selectedSquare) {
							selectPiece(board, index, boardX, boardY);
						}
					}
					
				} else clearSelectedPiece();
			} else clearSelectedPiece();
		}
	}
	
	private static void selectPiece(Board board, int index, int boardX, int boardY) {
		int p = board.getPiece(index);
		
		if(p != -1 && PieceCode.getColorFromSpriteCode(p) == board.getSide()) {
			MoveList list = new MoveList();
			
			MoveGenerator.generateAllMoves(board, list);
			
			ArrayList<Move> moves = MoveGenerator.getMovesForIndex(index, board, list, true);
			
			selectPiece(p, boardX, boardY, index, moves);
		}
	}
	
	private static void selectPiece(int p, int x, int y, int index, ArrayList<Move> moves) {
		selected = p;
		selectedMoves = moves;
		
		selectedSquare = index;
	}
	
	private static void clearSelectedPiece() {
		selected = -1;
	}
	
	public static void drawBoard(Graphics2D graphics, Board board) {
		graphics.drawImage(ImageUtil.BOARD_SHADOW, (UIManager.getWidth() - ImageUtil.BOARD_SHADOW.getWidth()) / 2, (UIManager.getHeight() - ImageUtil.BOARD_SHADOW.getHeight()) / 2 - 30 / 2 + 2, null);
		
		drawFrame(graphics);
		
		graphics.drawImage(ImageUtil.BOARD, (UIManager.getWidth() - ImageUtil.BOARD.getWidth()) / 2, (UIManager.getHeight() - ImageUtil.BOARD.getHeight()) / 2, null);
		
		int offsetX = (UIManager.getWidth() - Constants.BOARD_SIZE) / 2;
		int offsetY = (UIManager.getHeight() - Constants.BOARD_SIZE) / 2;
		
		if(lastMove != null) {
			int from = lastMove.getFrom();
			int to = lastMove.getTo();
			
			if(humanSide == PieceCode.BLACK) {
				from = 63 - from;
				to = 63 - to;
			}
			
			int fromX = from % 8;
			int fromY = from / 8;
			int toX = to % 8;
			int toY = to / 8;
			
			fromX = offsetX + fromX * Constants.TILE_SIZE;
			fromY = offsetY + fromY * Constants.TILE_SIZE;
			toX = offsetX + toX * Constants.TILE_SIZE;
			toY = offsetY + toY * Constants.TILE_SIZE;
			
			graphics.setColor(Constants.COLOR_BOARD_LASTMOVE);
			
			graphics.fillRect(fromX, fromY, Constants.TILE_SIZE, Constants.TILE_SIZE);
			graphics.fillRect(toX, toY, Constants.TILE_SIZE, Constants.TILE_SIZE);
		}
		
		if(selected != -1) {
			int square = selectedSquare;
			
			if(BoardUI.getHumanSide() == PieceCode.BLACK) {
				square = 63 - square;
			}
			
			int tileSize = Constants.TILE_SIZE;
			
			graphics.setColor(Constants.COLOR_BOARD_SELECT);
			
			graphics.fillRect(offsetX + (square % 8) * tileSize, offsetY + (square / 8) * tileSize, tileSize, tileSize);
		}
		
		for(int x=0; x<8; x++) {
			for(int y=0; y<8; y++) {
				int i = y * 8 + x;
				
				int p = board.getPiece(i);
				
				if(p != -1) drawPiece(graphics, p, x, y, offsetX, offsetY);
			}
		}
	}
	
	public static void drawFrame(Graphics2D graphics) {
		int frameSize = 12;
		
		int upperHeight = 42;
		
		int w = Constants.BOARD_SIZE + frameSize * 2;
		
		int x = (UIManager.getWidth() - w) / 2;
		
		int y = (UIManager.getHeight() - w) / 2 - upperHeight + frameSize;
		
		graphics.setColor(Constants.COLOR_UI);
		
		graphics.fillRect(x, y, w, w - frameSize + upperHeight);
		
		int height = upperHeight - 2;
		
		int circleSize = 12;
		
		graphics.setColor(Constants.COLOR_GREEN);
		
		graphics.fillArc(x + 14, y + height / 2 - circleSize / 2, 12, 12, 0, 360);
		
		graphics.setColor(Constants.COLOR_TEXT_GREY);
		
		graphics.setFont(Constants.FONT_REGULAR);
		
		graphics.drawString(Constants.NAME + " (" + Constants.ELO + ")", x + 14 + circleSize + 9, y + height / 2 + graphics.getFontMetrics().getHeight() / 4 + 1);
	}
	
	private static void drawPiece(Graphics2D graphics, int p, int x, int y, int offX, int offY) {
		int pos1X = x;
		int pos1Y = y;
		
		int pos2X = -1;
		int pos2Y = -1;
		
		int index = y*8 + x;
		
		if(pawnPromotions != null) {
			Move m = pawnPromotions[0];
			
			if(index == m.getFrom()) {
				pos1X = m.getTo() % 8;
				pos1Y = m.getTo() / 8;
			}
		} else if(lastMove != null && lastMoveState != 1) {
			
			if(lastMove.getTo() == index) {
				
				pos1X = lastMove.getFrom() % 8;
				pos1Y = lastMove.getFrom() / 8;
				pos2X = x;
				pos2Y = y;
				
			} else if(y == lastMove.getTo() / 8 && ((lastMove.getFlag() == MoveFlag.CASTLING_QUEEN_SIDE && x == 3) || (lastMove.getFlag() == MoveFlag.CASTLING_KING_SIDE && x == 5))) {
				
				int fromX = 0;
				
				if(lastMove.getFlag() == MoveFlag.CASTLING_KING_SIDE) fromX = 7;
				
				pos1X = fromX;
				pos1Y = lastMove.getFrom() / 8;
				pos2X = x;
				pos2Y = y;
			}
		}
		
		if(humanSide == PieceCode.BLACK) {
			pos1X = 7 - pos1X;
			pos1Y = 7 - pos1Y;
			
			if(pos2X != -1) {
				pos2X = 7 - pos2X;
				pos2Y = 7 - pos2Y;
			}
		}
		
		pos1X = offX + pos1X * Constants.TILE_SIZE;
		pos1Y = offY + pos1Y * Constants.TILE_SIZE;
		
		BufferedImage sprite = PieceCode.getSprite(p);
		
		if(pos2X == -1) {
			
			graphics.drawImage(sprite, pos1X, pos1Y, null);
			
		} else {
			pos2X = offX + pos2X * Constants.TILE_SIZE;
			pos2Y = offY + pos2Y * Constants.TILE_SIZE;
			
			double d = MathUtil.sigmoidCutOff(lastMoveState, 0.25f);
			
			AffineTransform trans = new AffineTransform();
			trans.translate(pos1X+(pos2X-pos1X)*d, pos1Y+(pos2Y-pos1Y)*d);
			
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			
			graphics.drawImage(sprite, trans, null);
			
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}
	}
	
	public static void onMouseClick(Point p) {
		if(Main.getBoard().getSide() == humanSide) {
			mouseClick = p;
		}
	}
	
	public static void clearLastMove() {
		lastMove = null;
	}
	
	private static void checkForWinner(Board b) {
		winner = b.findWinner();
		
		if(winner != Winner.NONE) {
			PopupUI.setDisplayedWinner(winner);
		}
	}
	
	public static int getHumanSide() {
		return humanSide;
	}
	
	public static void setHumanSide(int side) {
		humanSide = side;
	}
	
	public static int getWinner() {
		return winner;
	}
	
	public static void setWinner(int w) {
		winner = w;
	}
	
	public static double getLastMoveState() {
		return lastMoveState;
	}
	
	public static int getSelectedPiece() {
		return selected;
	}
	
	public static int getSelectedSquare() {
		return selectedSquare;
	}
	
	public static ArrayList<Move> getSelectedMoves() {
		return selectedMoves;
	}
	
	public static Move[] getPawnPromotions() {
		return pawnPromotions;
	}
	
}
