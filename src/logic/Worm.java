package logic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import render.IRenderable;
import utility.AudioUtility;
import utility.DrawingUtility;
import utility.Helper;
import utility.Position;
import utility.RandomUtility;

public abstract class Worm implements IRenderable {

	protected BufferedImage sprite;

	protected int maxFrame;
	protected int maxDelayMove;
	protected int maxDelayFrame;

	protected int countFrame = 0;
	protected int countDelayMove = 0;
	protected int countDelayFrame = 0;

	protected int life;

	protected double step;
	protected double rotate;

	protected boolean destroyed, visible;

	protected Position position;
	protected String word;

	public abstract void update();

	public Worm(String word, Position position, int life, boolean set) {
		this.position = position;
		this.life = life;
		this.word = word;
		this.destroyed = false;
		this.visible = true;

		if (set) {
			int side = RandomUtility.rand(0, 3);

			if (side % 2 == 0)
				this.getPosition().setX(374 + 616 * (side / 2));
			else
				this.getPosition().setY(26 + 655 * (side / 2));

			this.rotate = (Math.PI / 2 * side);
		} else
			this.rotate = RandomUtility.rand(0.1, Math.PI * 2 - 0.1);
	}

	private int shift(int segment) {
		if (countFrame < maxFrame / 2) {
			int temp = maxFrame / 4 - Math.abs(maxFrame / 4 - countFrame);
			return -1 * (temp * 2 / this.life * (this.life / 2 - Math.abs(this.life / 2 - segment)));
		} else {
			int temp = maxFrame / 4 - Math.abs(maxFrame * 3 / 4 - countFrame);
			return 1 * (temp * 2 / this.life * (this.life / 2 - Math.abs(this.life / 2 - segment)));
		}

	}

	protected boolean outOfMap() {

		double cos = Math.cos(rotate), sin = Math.sin(rotate);

		for (int i = 0; i < this.life; i++) {

			int shift = shift(i);
			int x = Helper.round(this.getPosition().getX() - i * cos * 24 + shift * sin) + 17;
			int y = Helper.round(this.getPosition().getY() - i * sin * 24 - shift * cos) + 17;

			if (x >= 315 && x <= 1040 && y >= -25 && y <= 740)
				return false;
		}
		return true;
	}

	protected void eat() {

		double cos = Math.cos(rotate), sin = Math.sin(rotate);

		int px = this.getPosition().getX() + 17, py = this.getPosition().getY() + 17;

		for (int k = 0; k < life; k++) {
			for (int i = -17; i <= 17; i++)
				for (int j = -17; j <= 17; j++) {
					if (i * i + j * j < 17 * 17) {
						int x = Helper.round(px + i + cos - (k * cos * 24));
						int y = Helper.round(py + j + sin - (k * sin * 24));
						if (x >= 410 && x < 976 && y >= 64 && y < 674) {
							DrawingUtility.currentGameScreen.setRGB(x - 410, y - 64, Color.black.getRGB());
						}
					}
				}
		}

	}

	protected Position findNewPosition() {
		ArrayList<Position> pos = new ArrayList<Position>();
		for (int i = DrawingUtility.currentGameScreen.getWidth() - 1; i >= 0; i--) {
			for (int j = DrawingUtility.currentGameScreen.getHeight() - 1; j >= 0; j--)
				if (DrawingUtility.currentGameScreen.getRGB(i, j) != Color.black.getRGB()) {
					pos.add(new Position(i + 410 - 24, j + 64 - 24));
				}
		}

		if (pos.size() != 0)
			return pos.get(RandomUtility.rand(0, pos.size() - 1));
		else
			return null;
	}

	protected double findNewAngle(Position p) {
		double angle;

		angle = Math.atan(1.0 * (p.getY() - this.position.getY()) / (p.getX() - this.position.getX()));
		if (p.getX() - this.position.getX() < 0)
			angle += Math.PI;

		if (angle < 0)
			angle += Math.PI * 2;
		if (angle > Math.PI * 2)
			angle -= Math.PI * 2;

		return angle;
	}

	public void draw(Graphics2D g2) {
		double cos = Math.cos(rotate), sin = Math.sin(rotate);

		AffineTransform at = new AffineTransform();
		at.rotate(rotate, 17, 17);
		AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);

		g2.setFont(DrawingUtility.font);

		int x = 33, size = 66;
		for (int i = 0; i < this.life; i++) {
			if (i == 1) {
				x = 0;
				size = 33;
			}

			int shift = shift(i);

			g2.drawImage(sprite.getSubimage(x, (i % 2) * 33, size, 34), atop,
					Helper.round(this.getPosition().getX() - i * cos * 24 + shift * sin),
					Helper.round(this.getPosition().getY() - i * sin * 24 - shift * cos));

			g2.drawString(this.word.charAt(i) + "",
					Helper.round(this.getPosition().getX() - i * cos * 24 + 9 + shift * sin),
					Helper.round(this.getPosition().getY() - i * sin * 24 + 25 - shift * cos));
		}

	}

	@Override
	public boolean collapse(Position p) {
		double cos = Math.cos(rotate), sin = Math.sin(rotate);

		for (int i = 0; i < this.life; i++) {

			int shift = shift(i);

			int deltax = p.getX() - Helper.round(this.getPosition().getX() - i * cos * 24 + shift * sin);
			int deltay = p.getY() - Helper.round(this.getPosition().getY() - i * sin * 24 - shift * cos);
			if (deltax >= 0 && deltax <= 33 && deltay >= 0 && deltay <= 33)
				return true;
		}
		return false;
	}

	public void hit() {
		this.life = this.getLife() - 1;
		AudioUtility.playAudio(AudioUtility.clickWormSound, "play");
	}

	public String getWord() {
		return word;
	}

	public int getLife() {
		return life;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean val) {
		this.destroyed = val;
	}
}
