package utility;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import render.IRenderable;

import ui.GameManager;
import ui.GameManager.STATE;

public class Picture implements IRenderable {

	protected static final AlphaComposite transcluentWhite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	protected static final AlphaComposite shadow = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
	protected static final AlphaComposite opaque = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);

	protected String name = "";
	protected BufferedImage image;
	protected Position pos;
	protected STATE thisstate;
	protected boolean shadows = false;
	protected boolean visible = true;

	public Picture(String name, BufferedImage image, Position pos, STATE thisstate) {
		super();

		this.name = name;
		this.image = image;
		this.pos = pos;
		this.thisstate = thisstate;
		this.visible = true;
	}

	public Picture(String name, BufferedImage image, Position pos, STATE thisstate, boolean shadows) {
		super();

		this.name = name;
		this.image = image;
		this.pos = pos;
		this.thisstate = thisstate;
		this.shadows = shadows;
		this.visible = true;
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	@Override
	public void draw(Graphics2D g2) {
		if (this.shadows) {
			g2.setComposite(shadow);
			g2.setColor(Color.lightGray);
			if (this.name.equalsIgnoreCase("menu"))
				g2.fillRoundRect(pos.getX() + 3, pos.getY() + 3, image.getWidth(), image.getHeight(), 100, 200);
			else
				g2.fillRect(pos.getX() + 5, pos.getY() + 5, image.getWidth(), image.getHeight());
		}
		g2.setComposite(opaque);
		g2.drawImage(image, null, pos.getX(), pos.getY());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVisible() {

		boolean chk = false;
		if (this.name.equals("rank") || this.name.equals("muteOff") || this.name.equals("muteOn")) {
			if (GameManager.state != STATE.game && GameManager.state != STATE.gameend)
				chk = true;
		}
		if (this.name.equals("back") && GameManager.state != STATE.menu && GameManager.state != STATE.gameend)
			chk = true;
		return this.visible && ((GameManager.state == this.thisstate) || chk);

	}

	public boolean collapse(Position p) {
		return false;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}

	@Override
	public void setDestroyed(boolean val) {

	}

	@Override
	public void update() {

	}

}
