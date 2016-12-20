package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import logic.ResultsException;
import logic.ResultsPanel;
import logic.Word;
import logic.Worm;

import render.RenderManager;

import ui.GameManager.ROLE;
import ui.GameManager.STATE;

import utility.AudioUtility;
import utility.Button;
import utility.Picture;
import utility.Configuration;
import utility.DrawingUtility;
import utility.InputUtility;
import utility.Position;

public class GameScreen extends JComponent {

	private static final long serialVersionUID = 1L;

	private JTextField farmerName, wormName;

	public GameScreen() {
		super();

		this.setName("gamescreen");
		setPreferredSize(new Dimension(Configuration.getScreenWidth(), Configuration.getScreenHeight()));
		this.setDoubleBuffered(true);

		farmerName = new JTextField();
		farmerName.setBounds(650, 350, 150, 30);
		farmerName.setVisible(false);
		this.add(farmerName);

		wormName = new JTextField();
		wormName.setBounds(200, 350, 150, 30);
		wormName.setVisible(false);
		this.add(wormName);

		addListener();
	}

	public void addListener() {

		this.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent arg0) {

				int val = arg0.getKeyCode();
				if (!GameManager.isPaused && GameManager.state != STATE.gameend && GameManager.role != ROLE.farmer) {
					if (val == KeyEvent.VK_ENTER) {
						for (Word word : GameManager.wordsGame)
							if (word.isVisible())
								word.checkInput(InputUtility.getWord());
						InputUtility.setWord("");
					} else if (val >= KeyEvent.VK_A && val <= KeyEvent.VK_Z) {
						boolean chk = false;
						for (Word word : GameManager.wordsGame)
							if (word.isVisible())
								if (word.getWord().startsWith(InputUtility.getWord() + ((char) val)))
									chk = true;
						if (chk == false)
							InputUtility.setWord("" + ((char) val));
						else
							InputUtility.setWord(InputUtility.getWord() + ((char) val));
					}
				}
				if (GameManager.state == STATE.game && val == KeyEvent.VK_SPACE) {
					pausegame();
				}
			}

		});

		this.addMouseMotionListener(new MouseAdapter() {

			public void mouseMoved(MouseEvent e) {
				Position pm = new Position(e.getX(), e.getY());
				for (Picture pic : RenderManager.getInstance().getPictures()) {
					if (pic instanceof Button) {
						Button button = (Button) pic;
						if (button.isVisible() && button.collapse(pm))
							button.setHover(true);
						else
							button.setHover(false);
					}
				}
			}

		});

		this.addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {

				Position pm = new Position(e.getX(), e.getY());
				for (Picture pic : RenderManager.getInstance().getPictures()) {
					if (pic instanceof Button) {
						Button button = (Button) pic;
						if (button.isVisible() && button.collapse(pm)) {
							if (button.getName().equals("pause") && GameManager.state != STATE.gameend)
								pausegame();
							else if (button.getName().equals("confirm")) {
								try {
									ResultsPanel.recordResult(wormName.getText(), farmerName.getText(),
											GameManager.sideWin);

									GameManager.state = button.getNextstate();
									wormName.setText("");
									farmerName.setText("");
								} catch (ResultsException e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							} else {
								GameManager.state = STATE.menu;
								GameManager.role = ROLE.both;
							}
							break;
						}
					}
				}

				if (!GameManager.isPaused && GameManager.state != STATE.gameend && pm.getX() >= 410 && pm.getX() <= 976
						&& pm.getY() >= 64 && pm.getY() <= 674 && GameManager.role != ROLE.worm) {
					int n = GameManager.wormsGame.size();
					for (int i = n - 1; i >= 0; i--) {
						Worm worm = GameManager.wormsGame.get(i);
						if (worm.isVisible() && worm.collapse(pm)) {
							worm.hit();
							break;
						}
					}
				}

			}

		});
	}

	public void pausegame() {
		if (GameManager.isPaused) {
			if (!GameManager.unPaused) {
				GameManager.unPaused = true;
				GameManager.timePaused = 3;
			} else
				GameManager.unPaused = false;
		} else
			GameManager.isPaused = true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(DrawingUtility.currentGameScreen, null, 410, 64);

		RenderManager.getInstance().drawWorms(g2);
		g2.drawImage(DrawingUtility.bgBoss, null, 27, 64);
		RenderManager.getInstance().drawWords(g2);

		g2.drawImage(DrawingUtility.bgBorder, null, 0, 0);

		g2.setFont(DrawingUtility.fontH);
		g2.setColor(Color.white);
		g2.drawString(GameManager.timeGame.getTime() + "", 310, 43);

		if (GameManager.lostGame.getLost() >= 80)
			g2.setColor(Color.RED);
		else if (GameManager.lostGame.getLost() >= 50)
			g2.setColor(Color.ORANGE);
		g2.drawString(GameManager.lostGame.getLost() + "", 850, 43);

		if (GameManager.isPaused) {
			if (GameManager.unPaused) {
				g2.drawImage(DrawingUtility.timePause[GameManager.timePaused], null, 430, 140);
				AudioUtility.playAudio(AudioUtility.count[GameManager.timePaused], "play");
			} else
				g2.drawImage(DrawingUtility.pause, null, 300, 240);
		}

		if (GameManager.state == STATE.gameend) {
			if (GameManager.role == ROLE.worm) {
				farmerName.setText("John_Bot");
				farmerName.setEditable(false);
				wormName.setEditable(true);
			} else if (GameManager.role == ROLE.farmer) {
				wormName.setText("Billy_BOT");
				wormName.setEditable(false);
				farmerName.setEditable(true);
			} else {
				wormName.setEditable(true);
				farmerName.setEditable(true);

			}
			if (GameManager.sideWin == 1)
				g.drawImage(DrawingUtility.wormWinBG, 30, 70, null);
			else
				g.drawImage(DrawingUtility.farmerWinBG, 30, 70, null);
			farmerName.setVisible(true);
			wormName.setVisible(true);
		} else {
			farmerName.setVisible(false);
			wormName.setVisible(false);
		}

		RenderManager.getInstance().drawButtons(g2);

	}
}
