package logic;

import java.awt.Color;
import utility.DrawingUtility;
import utility.Helper;
import utility.Position;
import utility.RandomUtility;

public class OrangeWorm extends Worm {

	private Position finalPoint;
	private double finalAngle;
	private int son;

	public OrangeWorm(String word) {
		super(word, new Position(RandomUtility.rand(405, 981), RandomUtility.rand(60, 680)), word.length(), true);

		maxFrame = this.life * 4;

		maxDelayMove = 4;
		maxDelayFrame = 2;
		step = 7.0;
		son = RandomUtility.rand(1, 4);

		finalPoint = new Position(700, 400);
		finalAngle = findNewAngle(finalPoint);

		sprite = DrawingUtility.orangeWormSprite;
	}

	public int getSon() {
		return son;
	}

	public void setSon(int son) {
		this.son = son;
	}

	@Override
	public void update() {

		countDelayMove++;
		if (countDelayMove == maxDelayMove) {

			if (finalPoint != null && DrawingUtility.currentGameScreen.getRGB(finalPoint.getX() - 410 + 24,
					finalPoint.getY() - 64 + 24) == Color.BLACK.getRGB()) {
				finalPoint = findNewPosition();
			}
			if (finalPoint != null)
				finalAngle = findNewAngle(finalPoint);

			if (this.rotate - finalAngle < -(0.27 * 2 / this.life))
				rotate += 0.27 * 2 / this.life;
			else if (this.rotate - finalAngle > 0.27 * 2 / this.life)
				rotate -= 0.27 * 2 / this.life;

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
		step += 9.0 / this.word.length();
		super.hit();
	}
}
