package logic;

import java.awt.Color;

import ui.GameManager;
import ui.GameManager.STATE;
import utility.AudioUtility;
import utility.DrawingUtility;

public class LostCount implements Runnable {

	public int lost = 0;

	public LostCount(int lost) {
		this.lost = lost;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
				synchronized (this) {
					if (GameManager.isPaused) {
						AudioUtility.playAudio(AudioUtility.warning, "stop");
						AudioUtility.isWarning = false;
						this.wait();
					}
				}
			} catch (InterruptedException e) {
			}

			if (GameManager.state == STATE.gameend || GameManager.state == STATE.menu) {
				AudioUtility.playAudio(AudioUtility.warning, "stop");
				AudioUtility.isWarning = false;
				break;
			}
			
			int temp = 0;
			for (int i = DrawingUtility.currentGameScreen.getWidth() - 1; i >= 0; i--)
				for (int j = DrawingUtility.currentGameScreen.getHeight() - 1; j >= 0; j--)
					if (DrawingUtility.currentGameScreen.getRGB(i, j) == Color.black.getRGB())
						temp++;
			lost = (int) (temp * 100.0
					/ (DrawingUtility.currentGameScreen.getWidth() * DrawingUtility.currentGameScreen.getHeight()));

			if (lost == 99) {
				if (!AudioUtility.isWarning && GameManager.STATE.game == GameManager.state) {
					AudioUtility.playAudio(AudioUtility.warning, "loop");
					AudioUtility.isWarning = true;
				}
			}
			if (lost >= 100) {
				AudioUtility.playAudio(AudioUtility.endGameSound, "play");
				break;
			}
		}

	}

	public int getLost() {
		return lost;
	}
}
