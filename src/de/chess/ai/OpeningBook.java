package de.chess.ai;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class OpeningBook {
	
	private static final HashMap<Long, OpeningPosition> POSITIONS = new HashMap<Long, OpeningPosition>();
	
	public static void load() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(OpeningBook.class.getClassLoader().getResourceAsStream("assets/opening_book.txt")));
			
			String s;
			
			while((s = reader.readLine()) != null) {
				String[] split = s.split(" ");
				
				long key = Long.parseLong(split[0]);
				
				int l = split.length - 1;
				
				int[] moves = new int[l];
				int[] counts = new int[l];
				
				for(int i=1; i<split.length; i++) {
					String[] move = split[i].split("\\(");
					
					String countString = move[1];
					
					countString = countString.substring(0, countString.length() - 1);
					
					int hash = Integer.parseInt(move[0]);
					int count = Integer.parseInt(countString);
					
					int index = i - 1;
					
					moves[index] = hash;
					counts[index] = count;
				}
				
				OpeningPosition p = new OpeningPosition(moves, counts);
				
				p.calcWeights();
				
				POSITIONS.put(key, p);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			
			System.exit(1);
		}
	}
	
	public static OpeningPosition getOpeningPosition(long key) {
		return POSITIONS.get(key);
	}
	
}
