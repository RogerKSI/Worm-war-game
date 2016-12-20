package render;

import java.awt.Graphics2D;
import utility.Position;

public interface IRenderable {

	public boolean collapse(Position p);

	public Position getPosition();

	public void draw(Graphics2D g2);

	public boolean isVisible();

	public void update();

	public boolean isDestroyed();

	public void setDestroyed(boolean val);

}
