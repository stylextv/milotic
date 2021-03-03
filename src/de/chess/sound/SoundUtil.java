package de.chess.sound;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.chess.game.Move;
import de.chess.game.PieceCode;
import de.chess.main.Main;

public class SoundUtil {
	
	public static final float VOLUME = 0.85f;
	
	public static Clip WHITE_MOVE;
	public static Clip WHITE_CAPTURE;
	
	public static Clip BLACK_MOVE;
	public static Clip BLACK_CAPTURE;
	
	public static void load() {
		try {
			
			WHITE_MOVE = loadWav("move/move.wav");
			WHITE_CAPTURE = loadWav("move/capture.wav");
			BLACK_MOVE = loadWav("move/move.wav");
			BLACK_CAPTURE = loadWav("move/capture.wav");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			System.exit(1);
		}
	}
	
	private static Clip loadWav(String name) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		Clip clip = AudioSystem.getClip();
		
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(SoundUtil.class.getClassLoader().getResource("assets/sounds/"+name));
        
        clip.open(inputStream);
        
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        
        float min = volumeControl.getMinimum() / 2;
        float gain = min - (min * VOLUME);
        
        volumeControl.setValue(gain);
        
        return clip;
	}
	
	public static void playMoveSound(Move m, int side) {
		if(m.getCaptured() == 0) {
			
			if(side == PieceCode.WHITE) play(WHITE_MOVE);
			else play(BLACK_MOVE);
			
		} else {
			
			if(side == PieceCode.WHITE) play(WHITE_CAPTURE);
			else play(BLACK_CAPTURE);
			
		}
	}
	
	public static void play(Clip clip) {
		if(Main.isRunning()) {
			clip.setFramePosition(0);
			clip.start();
		}
	}
	
}
