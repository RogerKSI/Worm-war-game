package logic;

import render.IRenderable;
import render.RenderManager;
import ui.GameManager;
import ui.GameManager.STATE;
import utility.RandomUtility;

public class BotFarmer implements Runnable {

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(RandomUtility.rand(180, 610 - (360 - GameManager.timeGame.getTime() * 3)));
				synchronized (this) {
					if (GameManager.isPaused)
						this.wait();
				}
			} catch (InterruptedException e) {
			}

			if (GameManager.state == STATE.gameend || GameManager.state == STATE.menu)
				break;

			for (IRenderable entity : RenderManager.getInstance().getEntities()) {
				if (entity instanceof Worm && entity.isVisible()) {
					((Worm) entity).hit();
					break;
				}
			}
		}
	}
}