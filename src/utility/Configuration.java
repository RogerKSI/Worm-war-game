package utility;


public class Configuration {
	private static int screenWidth = 1000;
	private static int screenHeight = 700;
	private static String title = "WormWar";
	private static int timePerGame = 120;

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static String getTitle() {
		return title;
	}

	public static int getTimePerGame() {
		return timePerGame;
	}

}
