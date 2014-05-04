package org.x3n0m0rph59.breakout;

import org.x3n0m0rph59.breakout.SoundLayer.Sounds;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

public final class Scene {
	private ScoreBoard scoreBoard = new ScoreBoard();
	private Ball ball = new Ball();
	private Paddle paddle = new Paddle();
	
	private int ballsLeft = 3;
	private int score = 0; 
	
	private List<Brick> bricks = new ArrayList<Brick>();	
	
	public Scene() {
		for (int x = 0; x <= 35; x++)
			for (int y = 0; y <= 10; y++) {
				bricks.add(new Brick((float) x + (25 * x) + 40, 
									 (float) y + (25 * y) + 40, 
									 20f, 10f));
			}
	}
	
	public void render() {
		GL11.glClearColor(0.15f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		for (Brick b : bricks) {
			b.render();
		}
		
		ball.render();
		paddle.render();
		
		scoreBoard.render();
	}
	
	public void step() {
		for (Brick b : bricks) {
			b.step();
		}
		
		ball.step();
		paddle.step();
		
		doCollisionDetection();
		doCleanup();
	}
		
	public void doCollisionDetection() {
		// Ball vs. Edges
		if (ball.getBoundingBox().getX() <= 0 || ball.getBoundingBox().getX() >= 1024) {
			ball.invertXVelocity();
			
			SoundLayer.playSound(Sounds.WALL_HIT);
		}
		
		if (ball.getBoundingBox().getY() <= 0) {
			ball.invertYVelocity();
			
			SoundLayer.playSound(Sounds.WALL_HIT);
		}
		
		// Ball lost?
		if (ball.getBoundingBox().getY() >= 768) {
			ballLost();
		}
		
		// Ball vs. Paddle
		if (Util.collisionTest(paddle.getBoundingBox(), ball.getBoundingBox())) {
			ball.invertYVelocity();
			
			SoundLayer.playSound(Sounds.PADDLE_HIT);
		}
		
		// Ball vs. Bricks
		for (Brick b : bricks) {
			if (Util.collisionTest(ball.getBoundingBox(), b.getBoundingBox())) {
				brickDestroyed(b);
			}
		}
		
	}

	private void brickDestroyed(Brick b) {
		b.explode();
		
		score += 100;
		
		SoundLayer.playSound(Sounds.BRICK_HIT);
	}

	private void ballLost() {
		ballsLeft--;
		score -= 1000;
		
		ball.setPosition(0f, 0f);
		
		SoundLayer.playSound(Sounds.BALL_LOST);
	}
	
	public void doCleanup() {
		Iterator<Brick> i = bricks.iterator();		
		while (i.hasNext()) {
			Brick b = i.next();
			
			if (b.isDestroyed()) {
					i.remove();
			}
		}		
	}

	public int getBallsLeft() {
		return ballsLeft;
	}

	public void setBallsLeft(int ballsLeft) {
		this.ballsLeft = ballsLeft;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
