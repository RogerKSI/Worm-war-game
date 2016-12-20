package utility;

public class InputUtility {

	private static String word = "";
	private static int countTyping = 0;

	public static String getWord() {
		return word;
	}

	public static void setWord(String word) {
		if (!word.equals("")) {
			countTyping++;
			countTyping %= 3;
			AudioUtility.playAudio(AudioUtility.typing[countTyping + 1], "play");
		}
		InputUtility.word = word;
	}

}
