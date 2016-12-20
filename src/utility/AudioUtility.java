package utility;

import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioUtility {

	public static boolean isWarning = false;

	public static Clip bgMenuSound = getSound("res/sound/bgMenu.wav");
	public static Clip clickWormSound = getSound("res/sound/clickWorm.wav");
	public static Clip endGameSound = getSound("res/sound/endGame.wav");
	public static Clip bgGameSound = getSound("res/sound/bgGame.wav");
	public static Clip warning = getSound("res/sound/warning.wav");
	public static Clip clickButton = getSound("res/sound/clickButton.wav");

	public static Clip[] count = getArrayAudio("sound", "count", 1, 4);
	public static Clip[] typing = getArrayAudio("sound", "typing", 1, 4);

	private static boolean mute = false;

	private static Clip getSound(String dir) {
		try {
			ClassLoader loader = AudioUtility.class.getClassLoader();
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(loader.getResource(dir));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			return clip;
		} catch (Exception e) {
			return null;
		}
	}

	private static Clip[] getArrayAudio(String type, String prefix, int st, int en) {

		Clip[] au = new Clip[4];
		for (int i = st; i < en; i++) {
			au[i] = getSound("res/" + type + "/" + prefix + "_" + i + ".wav");
		}
		return au;
	}

	public static boolean isMute() {
		return mute;
	}

	public static void setMute(boolean mute) {
		AudioUtility.mute = mute;
	}

	public static void playAudio(Clip soundname, String mode) {
		try {
			if (!mute && !mode.equalsIgnoreCase("stop")) {
				soundname.setFramePosition(0);
				if (mode.equalsIgnoreCase("loop")) {
					soundname.loop(Sequencer.LOOP_CONTINUOUSLY);
				} else {
					soundname.start();
				}
			} else if (soundname.isRunning())
				soundname.stop();
		} catch (Exception e) {

		}

	}

}
