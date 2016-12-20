package render;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import logic.Word;
import logic.Worm;
import ui.GameManager.ROLE;
import ui.GameManager.STATE;
import utility.Picture;
import utility.Button;
import utility.DrawingUtility;
import utility.Position;

public class RenderManager {
	public static final RenderManager instance = new RenderManager();
	private CopyOnWriteArrayList<IRenderable> entities;
	private ArrayList<Picture> pictures;

	public RenderManager() {
		entities = new CopyOnWriteArrayList<IRenderable>();

		pictures = new ArrayList<Picture>();

		// panel menu
		pictures.add(
				new Button("rank", DrawingUtility.rankButton, new Position(936, 353), STATE.menu, STATE.rank, false));
		pictures.add(
				new Button("muteOn", DrawingUtility.volumeOn, new Position(936, 413), STATE.menu, STATE.menu, false));
		pictures.add(
				new Button("muteOff", DrawingUtility.volumeOff, new Position(936, 413), STATE.menu, STATE.menu, false));

		// state menu
		pictures.add(new Button("menu", DrawingUtility.onePlayerButton, new Position(200, 300), STATE.menu,
				STATE.chooserole, true));
		pictures.add(new Button("menu", DrawingUtility.twoPlayerButton, new Position(517, 300), STATE.menu,
				STATE.choosestage, true));
		pictures.add(
				new Button("menu", DrawingUtility.howToButton, new Position(200, 390), STATE.menu, STATE.howto, true));
		pictures.add(new Button("menu", DrawingUtility.creditButton, new Position(517, 390), STATE.menu, STATE.credit,
				true));

		// choose role
		pictures.add(new Picture("roletext", DrawingUtility.roleText, new Position(90, 260), STATE.chooserole, false));
		pictures.add(new Button("menu", DrawingUtility.farmerButton, new Position(358, 300), STATE.chooserole,
				STATE.choosestage, true, ROLE.farmer));
		pictures.add(new Button("menu", DrawingUtility.wormButton, new Position(358, 390), STATE.chooserole,
				STATE.choosestage, true, ROLE.worm));

		// choose screen
		pictures.add(
				new Picture("screentext", DrawingUtility.screenText, new Position(90, 260), STATE.choosestage, false));
		for (int i = 0; i <= 3; i++)
			pictures.add(new Button("bg_button" + i, DrawingUtility.bgGameButton[i],
					new Position(340 + (240 * (i % 2)), 260 + (180 * (i / 2))), STATE.choosestage, STATE.game, true,
					i));

		// stage game
		pictures.add(
				new Button("pause", DrawingUtility.pauseButton, new Position(470, 10), STATE.game, STATE.game, true));

		// state game end
		pictures.add(new Button("confirm", DrawingUtility.confirmButton, new Position(379, 440), STATE.gameend,
				STATE.menu, true));

		// state how to
		pictures.add(new Picture("howto", DrawingUtility.bgHowto, new Position(-110, 25), STATE.howto));

		// state rank
		pictures.add(new Picture("bgrank", DrawingUtility.bgRank, new Position(-145, 35), STATE.rank));

		// state credit
		pictures.add(new Picture("bgcredit", DrawingUtility.credit, new Position(-110, 30), STATE.credit));

		// all except menu
		pictures.add(new Button("back", DrawingUtility.backButton, new Position(5, 5), STATE.howto, STATE.menu, false,
				ROLE.both));

	}

	public static RenderManager getInstance() {
		return instance;
	}

	public void drawWords(Graphics2D g2) {
		synchronized (RenderManager.getInstance()) {
			for (IRenderable e : entities)
				if (e instanceof Word && e.isVisible() && !e.isDestroyed())
					e.draw(g2);
		}
	}

	public void drawWorms(Graphics2D g2) {
		synchronized (RenderManager.getInstance()) {
			for (IRenderable e : entities)
				if (e instanceof Worm && e.isVisible() && !e.isDestroyed())
					e.draw(g2);
		}
	}

	public void drawButtons(Graphics2D g2) {
		synchronized (RenderManager.getInstance()) {
			for (IRenderable e : pictures)
				if (e.isVisible())
					e.draw(g2);
		}
	}

	public void drawAll(Graphics2D g2) {
		drawWords(g2);
		drawWorms(g2);
		drawButtons(g2);
	}

	public ArrayList<Picture> getPictures() {
		return pictures;
	}

	public CopyOnWriteArrayList<IRenderable> getEntities() {
		return entities;
	}

	public void setEntities(CopyOnWriteArrayList<IRenderable> entities) {
		this.entities = entities;
	}
}
