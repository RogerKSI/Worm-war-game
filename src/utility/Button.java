package utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ui.GameManager;
import ui.GameManager.ROLE;
import ui.GameManager.STATE;

public class Button extends Picture {

	private boolean hover = false;
	private STATE nextstate;
	private ROLE role = ROLE.nochange;
	private int stage = -1;

	public Button(String name, BufferedImage image, Position pos, STATE thisstate, STATE nextstate, boolean shadows) {
		super(name, image, pos, thisstate, shadows);
		this.nextstate = nextstate;
	}

	public Button(String name, BufferedImage image, Position pos, STATE thisstate, STATE nextstate, boolean shadows,
			ROLE role) {
		super(name, image, pos, thisstate, shadows);
		this.nextstate = nextstate;
		this.role = role;
	}

	public Button(String name, BufferedImage image, Position pos, STATE thisstate, STATE nextstate, boolean shadows,
			int stage) {
		super(name, image, pos, thisstate, shadows);
		this.nextstate = nextstate;
		this.stage = stage;
	}

	@Override
	public boolean collapse(Position p) {
		if (p.getX() >= pos.getX() && p.getY() >= pos.getY() && p.getX() <= pos.getX() + image.getWidth()
				&& p.getY() <= pos.getY() + image.getHeight())
			return true;
		return false;
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
		if (hover) {
			g2.setComposite(transcluentWhite);
			g2.setColor(Color.WHITE);
			if (this.name.equalsIgnoreCase("menu"))
				g2.fillRoundRect(pos.getX(), pos.getY(), image.getWidth(), image.getHeight(), 100, 200);
			else if (this.name.equalsIgnoreCase("back")) {
				g2.fillPolygon(new int[] { 15, 72, 69 }, new int[] { 55, 20, 93 }, 3);
				g2.fillPolygon(new int[] { 71, 70, 135, 130 }, new int[] { 40, 75, 75, 33 }, 4);
			} else
				g2.fillRect(pos.getX(), pos.getY(), image.getWidth(), image.getHeight());
			g2.setComposite(opaque);
		}
	}

	public ROLE getRole() {
		if (this.role == ROLE.nochange)
			return GameManager.role;
		return this.role;
	}

	public int getStage() {
		if (this.stage == -1)
			return GameManager.screen;
		return stage;
	}

	public void setHover(boolean hover) {
		this.hover = hover;
	}

	public STATE getNextstate() {
		if (this.name.equals("rank") && GameManager.state == STATE.rank)
			return STATE.menu;
		return nextstate;
	}

}
