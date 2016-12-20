package logic;

import utility.DrawingUtility;
import utility.Position;
import utility.RandomUtility;
import utility.Helper;

public class GreenWorm extends Worm {

	private boolean power;

	public GreenWorm(String word) {
		super(word, new Position(RandomUtility.rand(405, 981), RandomUtility.rand(60, 680)), word.length(), true);

		maxFrame = this.life * 8 > 120 ? 120 : this.life * 8;

		maxDelayMove = 2;
		maxDelayFrame = 3;
		step = 7.0;
		power = true;

		sprite = DrawingUtility.greenWormSprite;
	}

	public GreenWorm(String word, Position p, int divide) {
		super(word, p, word.length(), false);

		maxFrame = this.life * 4;
		maxDelayMove = 4;
		maxDelayFrame = 3;
		step = 16.0 / divide;
		power = false;
		sprite = DrawingUtility.greenWormSprite;
	}

	@Override
	public void update() {

		countDelayMove++;
		if (countDelayMove == maxDelayMove) {

			if (power) {
				Position p = findNewPosition();
				if (p != null)
					rotate = findNewAngle(p);
				power = false;
			}

			int intX = Helper.round(this.getPosition().getX() + Math.ceil(step * Math.cos(rotate)));
			int intY = Helper.round(this.getPosition().getY() + Math.ceil(step * Math.sin(rotate)));

			this.setPosition(new Position(intX, intY));

			eat();

			this.setDestroyed(outOfMap());

			countDelayMove = 0;

		}

		countDelayFrame++;
		if (countDelayFrame == maxDelayFrame) {
			countFrame = (++countFrame) % maxFrame;
			countDelayFrame = 0;
		}
	}

	public void hit() {
		super.hit();
	}

}
