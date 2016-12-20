package ui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;

import utility.Configuration;
import utility.DrawingUtility;

public class GameWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JComponent currentscene = null;

	public GameWindow(JComponent scene) {

		super(Configuration.getTitle());

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.getContentPane().setPreferredSize(
				new Dimension(Configuration.getScreenWidth(), Configuration.getScreenHeight()));
		this.setVisible(true);
		
		this.setCurrentscene(scene);
		
		this.setCursor(DrawingUtility.defaultCursor);

	}

	public JComponent getCurrentscene() {
		return currentscene;
	}

	public void setCurrentscene(JComponent scene) {

		this.getContentPane().removeAll();
		this.currentscene = scene;
		this.getContentPane().add(scene);

		this.getContentPane().validate();
		this.pack();
		this.currentscene.requestFocus();
	}

}
