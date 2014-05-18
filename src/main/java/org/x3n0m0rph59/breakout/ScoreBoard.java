package org.x3n0m0rph59.breakout;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

public class ScoreBoard {
	private List<Brick> bricks = new ArrayList<>();
	
	public ScoreBoard() {
		final float y_start = 285.0f;
		final float line_height = 78.5f;
		
		bricks.add(new Brick(Brick.Type.NORMAL, Brick.Behaviour.NORMAL, 0.0f, 
							 Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, 
							 y_start + line_height * 1, 
							 65.0f, Config.BRICK_HEIGHT));
		
		bricks.add(new Brick(Brick.Type.WEAK, Brick.Behaviour.NORMAL, 0.0f, 
							 Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, 
							 y_start + line_height * 2, 
							 65.0f, Config.BRICK_HEIGHT));
		
		bricks.add(new Brick(Brick.Type.HARD, Brick.Behaviour.NORMAL, 0.0f, 
							 Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, 
							 y_start + line_height * 3, 
							 65.0f, Config.BRICK_HEIGHT));
		
		bricks.add(new Brick(Brick.Type.SOLID, Brick.Behaviour.NORMAL, 0.0f, 
							 Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, 
							 y_start + line_height * 4, 
							 65.0f, Config.BRICK_HEIGHT));
		
		bricks.add(new Brick(Brick.Type.POWERUP, Brick.Behaviour.NORMAL, 0.0f, 
							 Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, 
							 y_start + line_height * 5, 
							 65.0f, Config.BRICK_HEIGHT));
	}
	
	public void render() {
		Scene currentScene = App.getMainWindow().getScene();
		TrueTypeFont font = FontLoader.getInstance().getFont("Comic Sans", Font.BOLD, 28);
		TrueTypeFont smallFont = FontLoader.getInstance().getFont("Comic Sans", Font.BOLD, 24);
		
		int score = currentScene.getScore();
		int level = currentScene.getLevel();
		int ballsLeft = currentScene.getBallsLeft();
		
		// Draw separator bar
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1.0f, 1.0f, 0.0f);			
			GL11.glVertex2f(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH, 0);
			GL11.glColor3f(0.0f, 1.0f, 0.0f);
			GL11.glVertex2f(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 10, 0);
			GL11.glColor3f(0.0f, 0.0f, 1.0f);
			GL11.glVertex2f(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 10, Config.SCREEN_HEIGHT);
			GL11.glColor3f(1.0f, 1.0f, 0.0f);
			GL11.glVertex2f(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH, Config.SCREEN_HEIGHT);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		font.drawString(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, 50,  "Score: " + score);
		font.drawString(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, 90,  "Level: " + (level + 1));
		font.drawString(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, 130, "Balls: " + ballsLeft);
		
		font.drawString(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, 270, "Brick Types");
		
		final int lineHeight = smallFont.getLineHeight() + (int) Config.BRICK_HEIGHT + 30;
		final int y_start = 250;
		smallFont.drawString(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, y_start + lineHeight * 1, "NORMAL");
		smallFont.drawString(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, y_start + lineHeight * 2, "WEAK");
		smallFont.drawString(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, y_start + lineHeight * 3, "HARD");
		smallFont.drawString(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, y_start + lineHeight * 4, "SOLID");
		smallFont.drawString(Config.SCREEN_WIDTH - Config.SCOREBOARD_WIDTH + 25, y_start + lineHeight * 5, "POWERUP");		
		GL11.glDisable(GL11.GL_BLEND);
		
		for (Brick b : bricks)
			b.render();
	}
}
