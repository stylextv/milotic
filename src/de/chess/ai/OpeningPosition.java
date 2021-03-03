package de.chess.ai;

import de.chess.util.MathUtil;

public class OpeningPosition {
	
	private int[] moves;
	
	private int[] counts;
	
	private float[] weights;
	
	public OpeningPosition(int[] moves, int[] counts) {
		this.moves = moves;
		this.counts = counts;
		
		this.weights = new float[moves.length];
	}
	
	public void calcWeights() {
		float strength = 0.5f;
		
		int total = 0;
		
		for(int i : counts) {
			total += i;
		}
		
		float average = (float) total / counts.length;
		
		for(int i=0; i<counts.length; i++) {
			int c = counts[i];
			
			float offsetFromAvg = average - c;
			
			weights[i] = c + offsetFromAvg * strength;
		}
		
		float sum = 0;
		
		for(float f : weights) {
			sum += f;
		}
		
		float previousProbability = 0;
		
		for(int i=0; i<weights.length; i++) {
			weights[i] = previousProbability + weights[i] / sum;
			
			previousProbability = weights[i];
		}
	}
	
	public int getRandomMove() {
		float f = MathUtil.RANDOM.nextFloat();
		
		for(int i=0; i<moves.length; i++) {
			float w = weights[i];
			
			if(f <= w) {
				return moves[i];
			}
		}
		return -1;
	}
	
}
