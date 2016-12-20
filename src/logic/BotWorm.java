package logic;

import render.IRenderable;
import render.RenderManager;
import ui.GameManager;
import ui.GameManager.STATE;
import utility.Configuration;
import utility.InputUtility;
import utility.RandomUtility;

public class BotWorm implements Runnable {

	private Word wordTemp = null;

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(RandomUtility.rand(80, 400 - (240 - GameManager.timeGame.getTime() * 2)));
				synchronized (this) {
					if (GameManager.isPaused)
						this.wait();
				}
			} catch (InterruptedException e) {
			}

			if (GameManager.state == STATE.gameend || GameManager.state == STATE.menu)
				break;

			if (wordTemp != null) {
				if (wordTemp.isVisible()) {
					if (wordTemp.getWord().startsWith(InputUtility.getWord())
							&& wordTemp.getWord().length() > InputUtility.getWord().length())
						InputUtility.setWord(
								InputUtility.getWord() + wordTemp.getWord().charAt(InputUtility.getWord().length()));
					else
						InputUtility.setWord("" + wordTemp.getWord().charAt(0));
					wordTemp.checkInput(InputUtility.getWord());
					if (wordTemp.isDestroyed() || wordTemp.getPosition().getY() > Configuration.getScreenHeight())
						wordTemp = null;
				}
			} else {
				for (IRenderable entity : RenderManager.getInstance().getEntities()) {
					if (entity instanceof Word && entity.isVisible()) {
						wordTemp = (Word) entity;
						break;
					}
				}
			}
		}
	}
}