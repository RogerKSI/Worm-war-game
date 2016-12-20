package logic;

import ui.GameManager;
import ui.GameManager.STATE;
import utility.AudioUtility;

public class TimeCount implements Runnable {

	public int time = 100;

	public TimeCount(int time) {
		this.time = time;
	}

	@Override
	public void run() {

		// set time
		while (true) {
			try {
				Thread.sleep(1000);
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

			time--;
			if (time < 8 && time > 0) {
				if (!AudioUtility.isWarning && GameManager.state == GameManager.STATE.game) {
					AudioUtility.playAudio(AudioUtility.warning, "loop");
					AudioUtility.isWarning = true;
				}
			}
			if (time <= 0) {
				AudioUtility.playAudio(AudioUtility.endGameSound, "play");
				break;
			}

		}

	}

	public int getTime() {
		return time;
	}

}
