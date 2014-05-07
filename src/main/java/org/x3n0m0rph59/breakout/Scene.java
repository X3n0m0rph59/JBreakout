package org.x3n0m0rph59.breakout;

import org.x3n0m0rph59.breakout.SoundLayer.Sounds;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public final class Scene {
	enum State {GREETING, WAITING_FOR_BALL, RUNNING, RESTART, PAUSED, TERMINATED};
	private State state = State.GREETING;
	
	private ScoreBoard scoreBoard = new ScoreBoard();
	private Ball ball = new Ball();
	private Paddle paddle = new Paddle();
	
	private int level = 0;
	private int ballsLeft = 0;
	private int score; 
	
	private List<Brick> bricks = new ArrayList<Brick>();
	private List<Powerup> powerups = new ArrayList<Powerup>();
	private List<Particle> particles = new ArrayList<Particle>();
	private List<TextAnimation> textAnimations = new ArrayList<TextAnimation>();
		
	private TrueTypeFont font;
	
	public Scene() {
		Font awtFont = new Font("Arial", Font.BOLD, 44);
		font = new TrueTypeFont(awtFont, true);
		
		initLevel(0);						
	}
	
	private void initLevel(int level) {
		ballsLeft += 3;
		score = 0;
		
		bricks = LevelLoader.loadLevel(level);
		
		ball.setPosition(Config.BALL_SPAWN_X, Config.BALL_SPAWN_Y);
	}

	public void render() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		for (Particle p : particles) {
			p.render();
		}
		
		for (Brick b : bricks) {
			b.render();
		}
		
		for (Powerup p : powerups) {
			p.render();
		}
		
		ball.render();
		paddle.render();
		
		scoreBoard.render();
		
		for (TextAnimation a : textAnimations) {
			a.render();
		}
				
		switch (state) {
		case GREETING:
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.25f);
				GL11.glVertex2f(0, 0);			
				GL11.glVertex2f(1024, 0);			
				GL11.glVertex2f(1024, 768);			
				GL11.glVertex2f(0, 768);
			GL11.glEnd();
									
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			font.drawString(240, 300, "Press mouse button to start", Color.white);
			
			GL11.glDisable(GL11.GL_BLEND);
			break;
			
		case RUNNING:			
			break;
			
		case RESTART:			
			break;
			
		case PAUSED:
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.25f);
				GL11.glVertex2f(0, 0);			
				GL11.glVertex2f(1024, 0);			
				GL11.glVertex2f(1024, 768);			
				GL11.glVertex2f(0, 768);
			GL11.glEnd();
									
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			font.drawString(200, 300, "Press mouse button to resume", Color.white);
			GL11.glDisable(GL11.GL_BLEND);
			break;					
			
		case WAITING_FOR_BALL:
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.25f);
				GL11.glVertex2f(0, 0);			
				GL11.glVertex2f(1024, 0);			
				GL11.glVertex2f(1024, 768);			
				GL11.glVertex2f(0, 768);
			GL11.glEnd();
									
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			font.drawString(180, 300, "Press mouse button for a new ball", Color.white);
			GL11.glDisable(GL11.GL_BLEND);
			break;
			
		case TERMINATED:
			break;
			
		default:
			throw new RuntimeException("Unsupported state: " + state);			
		}		
	}
	
	public void step() {
		processKeyboardEvents();
		
		switch (state) {
		case GREETING:
			if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
				setState(State.RUNNING);
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				setState(State.TERMINATED);
			}
			break;
			
		case PAUSED:
			if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
				setState(State.RUNNING);
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				setState(State.TERMINATED);
			}
			break;
			
		case RUNNING:
			if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
				setState(State.PAUSED);
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				setState(State.TERMINATED);
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
				setState(State.RESTART);
			}
			
			EffectManager.getInstance().step();
			
			for (Particle p : particles) {
				p.step();
			}
			
			for (Brick b : bricks) {
				b.step();
			}
			
			for (Powerup p : powerups) {
				p.step();
			}
			
			for (TextAnimation a : textAnimations) {
				a.step();
			}
			
			ball.step();
			paddle.step();
			
			doCollisionDetection();
			doCleanup();
			
			// Spawn new particles
			for (int i = 0; i < 2; i++) {
				particles.add(new Particle(Util.random(0, 1024), 0f, Util.random(5, 15)));
			}
			break;
			
		case RESTART:
			initLevel(level);			
			setState(State.GREETING);
			break;
			
		case TERMINATED:
			System.exit(0);
			break;
			
		case WAITING_FOR_BALL:
			if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
				setState(State.RUNNING);
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				setState(State.TERMINATED);
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
				setState(State.RESTART);
			}
			break;
			
		default:
			throw new RuntimeException("Unsupported state: " + state);		
		}
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
				brickHit(b);
			}
		}
		
		// Caught a powerup?
		for (Powerup p : powerups) {
			if (Util.collisionTest(paddle.getBoundingBox(), p.getBoundingBox())) {
				EffectManager.getInstance().addEffect(p.getType());
				p.setDestroyed(true);
			}
		}
	}

	private void brickHit(Brick b) {
		b.hit();
		
		// Reflect the ball?
		if (!EffectManager.getInstance().isEffectActive(EffectType.FIREBALL) &&
			b.getType() != Brick.Type.WEAK) {
			
			ball.invertXVelocity();
			ball.invertYVelocity();
		}
		
		if (b.getType() != Brick.Type.SOLID) {
			score += 100;		
		}
		
		if (b.getType() == Brick.Type.POWERUP) {
			score += 1000;			
			spawnPowerup(b.getX(), b.getY(), 
						 EffectType.values()[Util.random(0, EffectType.values().length - 1)]);
		}
	}

	private void ballLost() {
		ballsLeft--;
		score -= 1000;
		
		ball.setPosition(Config.BALL_SPAWN_X, Config.BALL_SPAWN_Y);
		
		setState(State.WAITING_FOR_BALL);
		
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
				
		// Remove excess particles
		Iterator<Particle> pi = particles.iterator();		
		while (pi.hasNext()) {
			Particle p = pi.next();
			
			if (p.getY() >= 768) {
				pi.remove();
			}
		}
		
		// Remove excess and/or consumed powerups
		Iterator<Powerup> pui = powerups.iterator();		
		while (pui.hasNext()) {
			Powerup p = pui.next();
			
			if (p.isDestroyed() || p.getY() >= 768) {
				pui.remove();
			}
		}	
		
		// Remove expired TextAnimations
		Iterator<TextAnimation> tai = textAnimations.iterator();		
		while (tai.hasNext()) {
			TextAnimation a = tai.next();
			
			if (a.isDestroyed()) {
				tai.remove();
			}
		}
	}
	
	public void spawnPowerup(float x, float y, EffectType effectType) {
		Logger.log("Spawned powerup: " + effectType);
		
		powerups.add(new Powerup(x, y, effectType));
				
		SoundLayer.playSound(Sounds.POWERUP_SPAWNED);
	}
	
	public void processKeyboardEvents() {		
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_ADD && 
				Keyboard.getEventKeyState()) {
				initLevel(++level);
				
				Logger.log("Cheating to next level");
			}
		}
	}
	
	public void addTextAnimation(String text) {
		textAnimations.add(new TextAnimation(text));
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

	public Paddle getPaddle() {
		return paddle;		
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
