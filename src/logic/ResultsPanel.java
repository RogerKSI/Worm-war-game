package logic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utility.DrawingUtility;

public class ResultsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static class Result implements Comparable<Result> {

		private String name = "";
		private int win = 0;
		private int lose = 0;

		public Result(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getWin() {
			return win;
		}

		public int getLose() {
			return lose;
		}

		public void increaseWin() {
			win++;
		}

		public void increaseLose() {
			lose++;
		}

		@Override
		public int compareTo(Result o) {
			if (o.getWin() < this.getWin())
				return -1;
			else if (o.getWin() > this.getWin() || o.getLose() < this.getLose())
				return 1;
			else
				return -1;
		}
	}

	private static ArrayList<Result> results = new ArrayList<Result>();
	private static final String fileName = "results";

	public ResultsPanel() {
		this.setOpaque(false);
	}

	public void updateResult() throws ResultsException {
		loadHighScore();
		createPanel();
		this.validate();
	}

	private void createJLabel(String[] arrStr, String color) {
		for (String str : arrStr) {
			JLabel temp = new JLabel(str);
			temp.setForeground(Color.decode(color));
			temp.setFont(DrawingUtility.fontH);
			this.add(temp);
		}
	}

	private void createPanel() {

		this.removeAll();
		this.setLayout(new GridBagLayout());

		if (results.size() == 0)
			createJLabel(new String[] { "No record results" }, "#F05954");
		else {
			this.setLayout(new GridLayout(results.size() < 3 ? results.size() + 1 : 4, 4));

			createJLabel(new String[] { "", "Name", "Win", "Lose" }, "#4CBBFB");

			int count = 0;
			for (Result result : results) {
				if (++count == 4)
					break;

				createJLabel(new String[] { "      " + count, result.getName(), "" + result.getWin(),
						"" + result.getLose() }, "#32CCD4");
			}
		}
		this.setPreferredSize(new Dimension(810, 350));
	}

	private static Result getResultWithName(String str) {
		for (Result result : results)
			if (result.getName().equalsIgnoreCase(str))
				return result;
		return null;
	}

	private static boolean onlyOneInString(String str, String w) {
		if (str.indexOf(w) >= 0 && str.lastIndexOf(w) == str.indexOf(w))
			return true;
		return false;
	}

	private static boolean checkResult(String str) {
		return (onlyOneInString(str, ">") ^ onlyOneInString(str, "<"));
	}

	public static void recordResult(String worm, String farmer, int type) throws ResultsException {
		if (worm.equals("") || farmer.equals(""))
			throw new ResultsException(0);
		if (worm.indexOf(">") >= 0 || worm.indexOf("<") >= 0 || farmer.indexOf(">") >= 0 || farmer.indexOf("<") >= 0)
			throw new ResultsException(1);
		if (worm.equalsIgnoreCase(farmer))
			throw new ResultsException(2);

		FileWriter out;
		try {
			out = new FileWriter(fileName, true);
			if (type == 1)
				out.write(worm + ">" + farmer + "\n");
			else
				out.write(worm + "<" + farmer + "\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadHighScore() throws ResultsException {

		File file = new File(fileName);

		if (!file.exists()) {
			PrintWriter out;
			try {
				out = new PrintWriter(fileName);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		Scanner kb = null;
		try {
			results.clear();
			kb = new Scanner(new File(fileName));
			while (kb.hasNextLine()) {
				String str = kb.nextLine();

				if (checkResult(str)) {

					String winStr, loseStr;
					int index = str.indexOf(">");

					try {
						Result result;
						if (index == -1) {
							index = str.indexOf("<");
							winStr = str.substring(index + 1);
							loseStr = str.substring(0, index);
						} else {
							loseStr = str.substring(index + 1);
							winStr = str.substring(0, index);
						}

						result = getResultWithName(winStr);
						if (result == null) {
							result = new Result(winStr);
							results.add(result);
						}
						result.increaseWin();

						result = getResultWithName(loseStr);
						if (result == null) {
							result = new Result(loseStr);
							results.add(result);
						}
						result.increaseLose();

					} catch (IndexOutOfBoundsException e) {
						results.clear();
						throw new ResultsException(3);
					}
				} else {
					results.clear();
					throw new ResultsException(3);
				}
			}
			Collections.sort(results);
		} catch (FileNotFoundException e1) {

		} finally {
			if (kb != null)
				kb.close();
		}
	}
}
