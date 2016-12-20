package ui;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import logic.ResultsException;
import logic.ResultsPanel;
import render.RenderManager;

import ui.GameManager.STATE;
import utility.AudioUtility;
import utility.Button;
import utility.Picture;
import utility.Configuration;
import utility.DrawingUtility;
import utility.Position;

public class GameTitle extends JComponent {

	private static final long serialVersionUID = 1L;

	private JScrollPane insideScroll;
	private ResultsPanel resultsPanel;

	public GameTitle() {
		super();
		this.setName("gamemenu");

		setPreferredSize(new Dimension(Configuration.getScreenWidth(), Configuration.getScreenHeight()));
		this.setDoubleBuffered(true);

		addListener();
		addHowToPanel();
		addRankPanel();
	}

	private void addHowToPanel() {
		JLabel insidePanel = new JLabel(new ImageIcon(DrawingUtility.howtoImage));
		insidePanel.setPreferredSize(new Dimension(600, 1016));
		insideScroll = new JScrollPane(insidePanel);
		insideScroll.setSize(new Dimension(650, 350));
		insideScroll.setLocation(220, 210);
		insideScroll.setVisible(false);
		this.add(insideScroll);
	}

	private void addRankPanel() {
		resultsPanel = new ResultsPanel();
		resultsPanel.setBounds(100, 220, 810, 400);
		resultsPanel.setVisible(false);
		this.add(resultsPanel);
	}

	private void addListener() {

		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {

				Position pm = new Position(e.getX(), e.getY());
				for (Picture pic : RenderManager.getInstance().getPictures())
					if (pic instanceof Button) {
						Button button = (Button) pic;
						if (button.isVisible() && button.collapse(pm))
							button.setHover(true);
						else
							button.setHover(false);
					}

			}
		});

		this.addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				Position pm = new Position(e.getX(), e.getY());
				for (Picture pic : RenderManager.getInstance().getPictures())
					if (pic instanceof Button) {

						Button button = (Button) pic;
						if (button.isVisible() && button.collapse(pm)) {

							AudioUtility.playAudio(AudioUtility.clickButton, "play");
							if (button.getName().equalsIgnoreCase("muteOn")) {
								AudioUtility.setMute(true);
								AudioUtility.playAudio(AudioUtility.bgMenuSound, "stop");
							} else if (button.getName().equalsIgnoreCase("muteOff")) {
								AudioUtility.setMute(false);
								AudioUtility.playAudio(AudioUtility.bgMenuSound, "loop");
							} else {
								GameManager.screen = button.getStage();
								GameManager.role = button.getRole();
								GameManager.state = button.getNextstate();
							}
							break;
						}
					}
			}

		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.clearRect(0, 0, Configuration.getScreenWidth(), Configuration.getScreenHeight());

		g2.drawImage(DrawingUtility.bgGameTitle, null, 0, 0);

		if (AudioUtility.isMute()) {
			for (Picture b : RenderManager.getInstance().getPictures()) {
				if (b.getName().equalsIgnoreCase("muteOff")) {
					b.setVisible(true);
				} else if (b.getName().equalsIgnoreCase("muteOn")) {
					b.setVisible(false);
				}
			}
		} else {
			for (Picture b : RenderManager.getInstance().getPictures()) {
				if (b.getName().equalsIgnoreCase("muteOff")) {
					b.setVisible(false);
				} else if (b.getName().equalsIgnoreCase("muteOn")) {
					b.setVisible(true);
				}
			}
		}

		RenderManager.getInstance().drawAll(g2);

		if (GameManager.state == STATE.howto)
			insideScroll.setVisible(true);
		else
			insideScroll.setVisible(false);

		if (GameManager.state == STATE.rank) {
			if (!resultsPanel.isVisible()) {
				try {
					resultsPanel.setVisible(true);
					resultsPanel.updateResult();
				} catch (ResultsException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

				}
			}
		} else
			resultsPanel.setVisible(false);

	}

}
