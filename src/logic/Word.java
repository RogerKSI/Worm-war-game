package logic;

import java.awt.Color;
import java.awt.Graphics2D;

import render.IRenderable;
import utility.DrawingUtility;
import utility.InputUtility;
import utility.Position;
import utility.RandomUtility;

public class Word implements IRenderable {

	private static final int maxDelay = 2;

	private int countDelay;
	private String word;
	private int type;
	private Position position;
	private int speed;
	private boolean destroyed;
	private Worm worm;

	public Word(String word, Position p, int speed) {
		super();
		this.word = word;
		this.position = p;
		this.speed = speed;
		this.destroyed = false;
		this.countDelay = 0;

		int temp = RandomUtility.rand(0, 20);
		if (temp <= 10) {
			this.worm = new GreenWorm(word);
			type = 0;
		} else if (temp <= 16) {
			this.worm = new YellowWorm(word);
			type = 1;
		} else {
			this.worm = new OrangeWorm(word);
			type = 2;

		}
	}

	public void checkInput(String input) {
		this.setDestroyed(this.word.equalsIgnoreCase(input));
	}

	@Override
	public void draw(Graphics2D g2) {

		g2.setFont(DrawingUtility.font);

		if (type == 0)
			g2.setColor(Color.GREEN);
		else if (type == 1)
			g2.setColor(Color.yellow);
		else
			g2.setColor(Color.orange);
		g2.fillOval(position.getX() - 20, position.getY() - 28, g2.getFontMetrics().stringWidth(word) + 38, 41);

		int lastindex = 0;
		g2.setColor(Color.RED);
		if (word.startsWith(InputUtility.getWord())) {
			lastindex = InputUtility.getWord().length();
			g2.drawString(word.substring(0, lastindex), position.getX(), position.getY());

		}
		g2.setColor(Color.BLACK);
		g2.drawString(word.substring(lastindex),
				position.getX() + g2.getFontMetrics().stringWidth(word.substring(0, lastindex)), position.getY());

	}

	@Override
	public boolean isVisible() {
		return position.getY() >= 64;
	}

	@Override
	public void update() {

		this.countDelay += 1;
		if (this.countDelay == maxDelay) {
			if (this.position.getY() <= 0)
				this.position.setY(this.position.getY() + 2);
			else
				this.position.setY(this.position.getY() + this.speed);

			this.countDelay = 0;
		}
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void setDestroyed(boolean val) {
		this.destroyed = val;
	}

	@Override
	public boolean collapse(Position p) {
		return false;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Worm getWorm() {
		return worm;
	}

	public String getWord() {
		return word;
	}

}
