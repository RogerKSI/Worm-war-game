package utility;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class DrawingUtility {
	// font
	public static Font font = getFontWithSize(30);
	public static Font fontH = getFontWithSize(55);

	// panel
	public static BufferedImage rankButton = getImage("res/button/rank.png");
	public static BufferedImage volumeOn = getImage("res/button/volumeOn.png");
	public static BufferedImage volumeOff = getImage("res/button/volumeOff.png");
	public static BufferedImage backButton = getImage("res/button/back.png");

	// Background menu
	public static BufferedImage bgGameTitle = getImage("res/background/gametitle.png");

	// button state menu
	public static BufferedImage onePlayerButton = getImage("res/button/onePlayer.png");
	public static BufferedImage twoPlayerButton = getImage("res/button/twoPlayer.png");
	public static BufferedImage howToButton = getImage("res/button/howto.png");
	public static BufferedImage creditButton = getImage("res/button/credit.png");

	// text
	public static BufferedImage roleText = getImage("res/text/chooserole.png");
	public static BufferedImage screenText = getImage("res/text/choosescreen.png");

	// rank
	public static BufferedImage bgRank = getImage("res/background/bg_rank.png");

	// credit
	public static BufferedImage credit = getImage("res/background/bg_credit.png");

	// how to play
	public static BufferedImage howtoImage = getImage("res/text/rule.png");
	public static BufferedImage bgHowto = getImage("res/background/bg_howto.png");

	// choose role
	public static BufferedImage farmerButton = getImage("res/button/farmer.png");
	public static BufferedImage wormButton = getImage("res/button/worm.png");

	// choose screen
	public static BufferedImage[] bgGameButton = getArrayImage("button", "screen", 0, 4);

	// background screen
	public static BufferedImage bgBoss = getImage("res/background/bg_boss.png");
	public static BufferedImage[] bgGameScreen = getArrayImage("background", "screen", 0, 4);

	// worm
	public static BufferedImage orangeWormSprite = getImage("res/worm/orangeSprite.png");
	public static BufferedImage greenWormSprite = getImage("res/worm/greenSprite.png");
	public static BufferedImage yellowWormSprite = getImage("res/worm/yellowSprite.png");

	// end game
	public static BufferedImage farmerWin = getImage("res/background/farmerwin.png");
	public static BufferedImage farmerWinBG = getImage("res/background/farmerwinbg.png");
	public static BufferedImage wormWin = getImage("res/background/wormwin.png");
	public static BufferedImage wormWinBG = getImage("res/background/wormwinbg.png");
	public static BufferedImage confirmButton = getImage("res/button/confirm.png");

	// game
	public static BufferedImage pauseButton = getImage("res/button/pauseButton.png");
	public static BufferedImage bgBorder = getImage("res/background/bg_border.png");

	// pause
	public static BufferedImage pause = getImage("res/utility/pause.png");
	public static BufferedImage[] timePause = getArrayImage("utility", "time", 1, 4);

	// cursor
	public static Cursor defaultCursor = getCursor("res/cursor/default.png");

	// screen play
	public static BufferedImage currentGameScreen;

	private static Cursor getCursor(String dir) {
		return Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(getImage(dir)).getImage(), new Point(0, 0),
				"cursor");
	}

	private static Font getFontWithSize(int size) {
		ClassLoader loader = DrawingUtility.class.getClassLoader();
		try {
			return Font.createFont(Font.TRUETYPE_FONT, loader.getResourceAsStream("res/font/2005_iannnnnGMO.ttf"))
					.deriveFont(Font.BOLD, size);
		} catch (FontFormatException e) {
		} catch (IOException e) {
		}
		return null;
	}

	private static BufferedImage getImage(String dir) {
		ClassLoader loader = DrawingUtility.class.getClassLoader();
		try {
			return ImageIO.read(loader.getResource(dir));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static BufferedImage[] getArrayImage(String type, String prefix, int st, int en) {
		BufferedImage[] bg = new BufferedImage[4];
		for (int i = st; i < en; i++)
			bg[i] = getImage("res/" + type + "/" + prefix + "_" + i + ".png");
		return bg;
	}

	public static BufferedImage copyImage(BufferedImage source) {
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}
}
