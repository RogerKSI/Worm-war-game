package ui;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import logic.BotFarmer;
import logic.BotWorm;
import logic.GreenWorm;
import logic.LostCount;
import logic.OrangeWorm;
import logic.TimeCount;
import logic.Word;
import logic.Worm;

import render.RenderManager;

import utility.AudioUtility;
import utility.Configuration;
import utility.DrawingUtility;
import utility.Helper;
import utility.InputUtility;
import utility.Position;
import utility.RandomUtility;

public class GameManager {
	
	// Thread
	private static Thread time;
	private static Thread lost;
	
	// frame
	public static GameWindow gamewindow;

	// panel
	public static GameTitle gametitle;
	public static GameScreen gamescreen;

	// pause
	public static boolean isPaused;
	public static boolean unPaused;
	public static int timePaused;

	// =1 worm win , other farmer win
	public static int sideWin;

	// runnable
	public static LostCount lostGame;
	public static TimeCount timeGame;
	public static Runnable botGame;

	// state of game (start at menu)
	public enum STATE {
		menu, chooserole, choosestage, howto, credit, game, gameend, rank;
	}

	public static STATE state = STATE.menu;

	// role of player (start at both)
	public enum ROLE {
		farmer, worm, both, nochange;
	}

	public static ROLE role = ROLE.both;

	// game screen
	public static int screen = 0;

	// keep words and worms
	public static CopyOnWriteArrayList<Word> wordsGame = new CopyOnWriteArrayList<Word>();
	public static CopyOnWriteArrayList<Worm> wormsGame = new CopyOnWriteArrayList<Worm>();

	public static void runGame() {

		// initial frame and panel
		gametitle = new GameTitle();
		gamewindow = new GameWindow(gametitle);
		gamescreen = new GameScreen();

		// loop game
		while (true) {

			gamewindow.setCurrentscene(gametitle);

			AudioUtility.playAudio(AudioUtility.bgGameSound, "stop");
			AudioUtility.playAudio(AudioUtility.bgMenuSound, "loop");

			RenderManager.getInstance().getEntities().clear();
			wordsGame.clear();
			wormsGame.clear();
			InputUtility.setWord("");

			// loop menu
			while (true) {
				sleep(25);

				if (state == STATE.game || state == STATE.gameend) {
					DrawingUtility.currentGameScreen = DrawingUtility
							.copyImage(DrawingUtility.bgGameScreen[GameManager.screen]);
					break;
				}
				gamewindow.getCurrentscene().repaint();
			}

			newGame();
		}
	}

	public static void newGame() {

		settingStart();

		while (true) {
			sleep(20);
			if (state == STATE.menu)
				break;

			gamewindow.getCurrentscene().repaint();

			if (!GameManager.isPaused && GameManager.state != STATE.gameend)
				logicupdate();
			else if (GameManager.unPaused) {
				sleep(980);
				GameManager.timePaused--;

				if (GameManager.timePaused == 0) {
					GameManager.isPaused = false;
					GameManager.unPaused = false;
					synchronized (timeGame) {
						timeGame.notifyAll();
					}
					synchronized (lostGame) {
						lostGame.notifyAll();
					}
					if (role != ROLE.both)
						synchronized (botGame) {
							botGame.notifyAll();
						}
				}
			}

			if ((!time.isAlive() || !lost.isAlive()) && GameManager.state != STATE.gameend) {
				GameManager.state = STATE.gameend;

				if (!time.isAlive())
					sideWin = 2;
				else
					sideWin = 1;
			}

		}
		GameManager.role = ROLE.both;
		GameManager.screen = 0;

	}

	public static void settingStart() {

		isPaused = true;
		unPaused = true;
		timePaused = 3;

		sideWin = 0;

		timeGame = new TimeCount(Configuration.getTimePerGame());
		lostGame = new LostCount(0);

		generateWord();

		time = new Thread(timeGame);
		lost = new Thread(lostGame);
		botGame = null;

		if (role == ROLE.worm)
			botGame = new BotFarmer();
		if (role == ROLE.farmer)
			botGame = new BotWorm();

		Thread bot = new Thread(botGame);
		bot.start();
		time.start();
		lost.start();

		gamewindow.setCurrentscene(gamescreen);

		AudioUtility.isWarning = false;
		AudioUtility.playAudio(AudioUtility.bgMenuSound, "stop");
		AudioUtility.playAudio(AudioUtility.bgGameSound, "loop");
	}

	public static void generateWord() {

		ArrayList<String> words = new ArrayList<String>();
		ArrayList<Word> allWord = new ArrayList<Word>();

		Scanner kb = new Scanner(GameManager.class.getClassLoader().getResourceAsStream("res/words.txt"));
		while (kb.hasNextLine())
			words.add(kb.nextLine());
		kb.close();

		double pivot = -150000, range = 0.025;
		for (int i = 0; i <= 2500; i++) {
			pivot += range * i;

			int index = RandomUtility.rand(0, words.size() - 1);
			String word = words.get(index);

			if (word.length() > 27)
				continue;
			Position p = new Position(RandomUtility.rand(27, 392 - word.length() * 13),
					Helper.round(RandomUtility.rand(pivot - range * i / 2, pivot + range * i / 2)));
			int speed = Helper.round(RandomUtility.rand(3.0, 9.0 - i * 3 / 2500.0));
			allWord.add(new Word(word, p, speed));
		}
		int slide = Helper.round(-10 - (allWord.get(allWord.size() - 1).getPosition().getY()));
		for (Word word : allWord) {
			word.getPosition().setY(word.getPosition().getY() + slide);
			RenderManager.getInstance().getEntities().add(word);
			wordsGame.add(word);
		}

	}

	public static void logicupdate() {

		// clear word
		for (Word word : wordsGame) {
			if (word.isDestroyed() || word.getPosition().getY() > Configuration.getScreenHeight()) {
				if (word.isDestroyed()) {

					RenderManager.getInstance().getEntities().add(word.getWorm());
					wormsGame.add(word.getWorm());
				}
				RenderManager.getInstance().getEntities().remove(word);
				wordsGame.remove(word);

			}

		}

		// clear worm
		for (Worm worm : wormsGame) {
			if (worm.isDestroyed() || worm.getLife() <= 0) {
				if (worm.getLife() <= 0 && worm instanceof OrangeWorm) {
					int son = ((OrangeWorm) worm).getSon();
					for (int j = 1; j <= son; j++) {
						GreenWorm gWorm = new GreenWorm(worm.getWord(), worm.getPosition(), son);
						RenderManager.getInstance().getEntities().add(gWorm);
						wormsGame.add(gWorm);
					}
				}

				wormsGame.remove(worm);
				RenderManager.getInstance().getEntities().remove(worm);
			}
		}

		// update word
		for (Word word : wordsGame)
			word.update();

		// update worm
		for (Worm worm : wormsGame)
			worm.update();

	}

	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
