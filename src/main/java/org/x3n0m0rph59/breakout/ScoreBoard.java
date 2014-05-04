package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.Display;

public class ScoreBoard {
	public void render() {
		Scene currentScene = App.getMainWindow().getScene();
		
		int ballsLeft = currentScene.getBallsLeft();
		int score = currentScene.getScore();
		
		String s = "Score: " + score + " - Balls left: " + ballsLeft;
		Display.setTitle(s);
	}
}
