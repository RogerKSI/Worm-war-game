package logic;

import utility.DrawingUtility;
import utility.Helper;
import utility.Position;
import utility.RandomUtility;

public class YellowWorm extends Worm {

	private boolean power;

	private double finalAngle;
	private int maxDelayRotate;
	private int countDelayRotate = 0;

	public YellowWorm(String word) {
		super(word, new Position(RandomUtility.rand(405, 981), RandomUtility.rand(60, 680)), word.length(), true);

		maxFrame = this.life * 4;

		maxDelayMove = 3;
		maxDelayFrame = 2;
		maxDelayRotate = RandomUtility.rand(30, 50);
		step = 7.0;

		finalAngle = this.rotate;

		sprite = DrawingUtility.yellowWormSprite;
	}

	private void findNewAngle() {
		finalAngle = RandomUtility.rand(0.1, Math.PI * 2 - 0.1);
	}

	@Override
	public void update() {

		countDelayMove += 1;
		if (countDelayMove == maxDelayMove) {

			if (power) {
				Position p = findNewPosition();
				if (p != null)
					rotate = findNewAngle(p);
				power = false;
			}

			if (this.rotate - finalAngle < -(0.15 / this.life))
				rotate += 0.15 / this.life;
			else if (this.rotate - finalAngle > 0.15 / this.life)
				rotate -= 0.15 / this.life;

			int intX = Helper.round(this.getPosition().getX() + Math.ceil(step * Math.cos(rotate)));
			int intY = Helper.round(this.getPosition().getY() + Math.ceil(step * Math.sin(rotate)));

			this.setPosition(new Position(intX, intY));

			eat();

			this.setDestroyed(outOfMap());

			countDelayMove = 0;

		}

		countDelayFrame += 1;
		if (countDelayFrame == maxDelayFrame) {
			countFrame += 1;
			countFrame %= maxFrame;

			countDelayFrame = 0;
		}

		countDelayRotate += 1;
		if (countDelayRotate == maxDelayRotate) {
			findNewAngle();
			countDelayRotate = 0;
		}

	}

	public void hit() {
		step += 8.0 / this.word.length();
		findNewAngle();
		super.hit();
	}
}
