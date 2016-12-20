package utility;

import java.util.Random;

public class RandomUtility {

	private static Random rand = new Random();

	public static int rand(int start, int end) {
		if (end < start)
			return start;
		return rand.nextInt(end - start + 1) + start;
	}

	public static double rand(double start, double end) {
		if (end < start)
			return start;
		return (rand.nextDouble() * (end - start)) + start;
	}
}
