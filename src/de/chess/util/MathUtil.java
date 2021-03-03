package de.chess.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class MathUtil {
	
	public static final Random RANDOM = new Random();
	
	public static final DecimalFormat DECIMAL_FORMAT = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
	
	public static float sigmoid(float x) {
		return (1/(1 + (float) Math.pow(Math.E,(-1*(x*16-8)))));
	}
	public static double sigmoid(double x) {
		return (1/(1 + Math.pow(Math.E,(-1*(x*16-8)))));
	}
	
	public static double sigmoidCutOff(double x, float f) {
		float min = sigmoid(f);
		
		float range = 1 - min;
		
		x = f + (1 - f) * x;
		
		x = (x * 16 - 8);
		
		double result = (1/(1 + Math.pow(Math.E, -x)));
		
		return (result - min) / range;
	}
	
	public static float lerp(float a, float b, float speed) {
		return a + (b-a)*speed;
	}
	public static double lerp(double a, double b, float speed) {
		return a + (b-a)*speed;
	}
	
}
