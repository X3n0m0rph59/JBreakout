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
import org.newdawn.slick.geom.Rectangle;

public final class Scene {
	enum State { GREETING, WAITING_FOR_BALL, RUNNING, STAGE_CLEARED, RESTART, PAUSED, TERMINATED };
	private State state = State.GREETING;
	
	private ScoreBoard scoreBoard = new ScoreBoard();	
	
	private int level = 0;
	private int ballsLeft = 3;
	private int score = 0; 
	
	private Paddle paddle = new Paddle();
	private List<Ball> balls = new ArrayList<Ball>();
	private List<Brick> bricks = new ArrayList<Brick>();
	private List<Powerup> powerups = new ArrayList<Powerup>();
	private List<Particle> particles = new ArrayList<Particle>();
	private List<Projectile> projectiles = new ArrayList<Projectile>();	
	private List<TextAnimation> textAnimations = new ArrayList<TextAnimation>();
		
	private TrueTypeFont font;
	
	private int frameCounter = 0;
	
	public Scene() {
		font = FontLoader.getInstance().getFont("Verdana", Font.BOLD, 44);
		
		initLevel(0);						
	}
	
	private void initLevel(int level) {	
		powerups.clear();
		projectiles.clear();
		
		EffectManager.getInstance().clearEffects();
		
		bricks = LevelLoader.loadLevel(level);
		
		balls.clear();
		balls.add(new Ball(Config.BALL_SPAWN_X, Config.BALL_SPAWN_Y));		
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
		
		for (Projectile p : projectiles) {
			p.render();
		}

		for (Ball b : balls) {
			b.render();
		}
		
		// Draw a wall on the bottom of the screen?
		if (EffectManager.getInstance().isEffectActive(EffectType.CLOSED_BOTTOM)) {
			for (int i = 0; i <= 22; i++) {
				float x = (i * 45) + 0;
				float y = 750;
				
				final float width = 40;
				final float height = 25;
				
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glColor3f(1.0f, 0.0f, 0.0f);			
					GL11.glVertex2f(x, y);			
					GL11.glVertex2f(x + width, y);			
					GL11.glVertex2f(x + width, y + height);			
					GL11.glVertex2f(x, y + height);
				GL11.glEnd();
			}
		}
		
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
			font.drawString(190, 400, "Press mouse button to start", Color.white);
			
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
			font.drawString(180, 400, "Press mouse button to resume", Color.white);
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
			font.drawString(100, 400, "Press mouse button for a new ball", Color.white);
			GL11.glDisable(GL11.GL_BLEND);
			break;
			
		case STAGE_CLEARED:
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
			font.drawString(350, 300, "Stage " + (level + 1) + " cleared!", Color.white);
			font.drawString(180, 400, "Press Mouse Button to continue", Color.white);
			GL11.glDisable(GL11.GL_BLEND);
			break;
						
		case TERMINATED:
			break;
			
		default:
			throw new RuntimeException("Unsupported state: " + state);			
		}
		
		// Debugging:
		// Draw bounding boxes around the paddle 
//		Rectangle r1 = balls.get(0).getBoundingBox();
//		Rectangle r2 = paddle.getBoundingBox();		
//
//		Rectangle left 			= new Rectangle(r2.getX() - r1.getWidth(), r2.getY(), r1.getWidth(), r2.getHeight());
//		Rectangle top_left 		= new Rectangle(r2.getX() - r1.getWidth(), r2.getY() - r1.getHeight(), r1.getWidth(), r1.getHeight());
//		Rectangle top 			= new Rectangle(r2.getX(), r2.getY() - r1.getHeight(), r2.getWidth(), r1.getHeight());
//		Rectangle top_right 	= new Rectangle(r2.getX() + r2.getWidth(), r2.getY() - r1.getHeight(), r1.getWidth(), r1.getHeight());
//		Rectangle right 		= new Rectangle(r2.getX() + r2.getWidth(), r2.getY(), r1.getWidth(), r2.getHeight());
//		Rectangle bottom_right 	= new Rectangle(r2.getX() + r2.getWidth(), r2.getY() + r2.getHeight(), r1.getWidth(), r1.getHeight());
//		Rectangle bottom 		= new Rectangle(r2.getX(), r2.getY() + r2.getHeight(), r2.getWidth(), r1.getHeight());
//		Rectangle bottom_left 	= new Rectangle(r2.getX() - r1.getWidth(), r2.getY() + r2.getHeight(), r1.getWidth(), r1.getHeight());
//		
//		drawRect(left);
//		drawRect(top_left);
//		drawRect(top);
//		drawRect(top_right);
//		drawRect(right);
//		drawRect(bottom_right);
//		drawRect(bottom);
//		drawRect(bottom_left);
	}
	
	public void drawRect(Rectangle r) {
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.8f);
			GL11.glVertex2f(r.getX(), r.getY());			
			GL11.glVertex2f(r.getX() + r.getWidth(), r.getY());			
			GL11.glVertex2f(r.getX() + r.getWidth(), r.getY() + r.getHeight());			
			GL11.glVertex2f(r.getX(), r.getY() + r.getHeight());
		GL11.glEnd();
	}
	
	public void step() {
		frameCounter++;
		
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
			
		case STAGE_CLEARED:
			if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
				initLevel(++level);
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
			
			for (Projectile p : projectiles) {
				p.step();
			}
			
			for (TextAnimation a : textAnimations) {
				a.step();
			}
			
			for (Ball b : balls) {
				b.step();
			}
			
			paddle.step();
			
			doCollisionDetection();
			doCleanup();
			
			// Spawn new particles
			for (int i = 0; i < 2; i++) {
				particles.add(new Particle(Util.random(0, 1024), 0f, Util.random(5, 15)));
			}
			
			// Spawn new projectiles?
			if (EffectManager.getInstance().isEffectActive(EffectType.PADDLE_GUN)) {
				if (Mouse.isButtonDown(0) && (frameCounter % 4 == 0)) {
					for (int i = 0; i < 2; i++) {
						float x = (frameCounter % 8 == 0) ? paddle.getX() : 
															paddle.getX() + paddle.getWidth() - 10; 
						projectiles.add(new Projectile(x, paddle.getY()));
					}
				}
			}
			
			// Stage cleared?			
			if (isStageCleared()) {
				setState(State.STAGE_CLEARED);
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
				balls.clear();
				spawnBall();
				
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
		
	public boolean isStageCleared() {
		return getDestructibleBricksLeft() == 0;
	}

	public int getDestructibleBricksLeft() {
		if (bricks.isEmpty())
			return 0;
		
		int destructibleBricks = 0;
		
		for (Brick b : bricks)
			if (b.getType() != Brick.Type.SOLID)
				destructibleBricks++;
		
		return destructibleBricks;
	}

	public void doCollisionDetection() {
		// Ball vs. Edges
		for (Ball ball : balls) {
			if (ball.getBoundingBox().getX() <= 0 || ball.getBoundingBox().getX() >= 1024) {
				ball.invertXVelocity();
				
				SoundLayer.playSound(Sounds.WALL_HIT);
			}
		}

		for (Ball ball : balls) {
			if (ball.getBoundingBox().getY() <= 0 || 
				(EffectManager.getInstance().isEffectActive(EffectType.CLOSED_BOTTOM)) && 
				 ball.getBoundingBox().getY() >= 768 - 25) {
				ball.invertYVelocity();
				
				SoundLayer.playSound(Sounds.WALL_HIT);
			}
		}
		
		// Ball lost?
		Iterator<Ball> bi = balls.iterator();
		while (bi.hasNext()) {			
			Ball ball = bi.next();
			if (ball.getBoundingBox().getY() >= 768 && 
				!EffectManager.getInstance().isEffectActive(EffectType.CLOSED_BOTTOM)) {
				ballLost(bi);
			}
		}
		
		// Ball vs. Paddle
		for (Ball ball : balls) {
			if (Util.collisionTest(paddle.getBoundingBox(), ball.getBoundingBox())) {
				switch (Util.getCollisionEdge(ball.getBoundingBox(), paddle.getBoundingBox())) {
				case LEFT:
					ball.setAngleOfReflection(180);
					break;
					
				case TOP_LEFT:
					ball.setAngleOfReflection(320);
					break;
					
				case TOP:
					// TODO: calculate fractional angle of reflection 
					//		 based on paddle surface impact point to 
					//		 simulate a curved surface
					float angle = 180 * Util.sign(ball.getVelX()) * -1;
					ball.setAngleOfReflection(angle);
					break;
					
				case TOP_RIGHT:
					ball.setAngleOfReflection(45);
					break;
					
				case RIGHT:
					ball.setAngleOfReflection(180);
					break;									
					
				case BOTTOM_RIGHT:
					ball.setAngleOfReflection(225);
					break;
					
				case BOTTOM:
					ball.setAngleOfReflection(180);
					break;
					
				case BOTTOM_LEFT:
					ball.setAngleOfReflection(135);
					break;

				default:
					throw new RuntimeException("Invalid egde type");				
				}				
				
				SoundLayer.playSound(Sounds.PADDLE_HIT);
			}
		}
		
		// Ball vs. Bricks
		for (Ball ball : balls) {
			for (Brick b : bricks) {
				if (Util.collisionTest(ball.getBoundingBox(), b.getBoundingBox())) {
					brickHit(b, ball, false);
				}
			}
		}
		
		// Caught a powerup?
		for (Powerup p : powerups) {
			if (Util.collisionTest(paddle.getBoundingBox(), p.getBoundingBox())) {
				EffectManager.getInstance().addEffect(p.getType());
				p.setDestroyed(true);
			}
		}
		
		// Projectile vs. Bricks
		for (Brick b : bricks) {
			for (Projectile p : projectiles) {
				if (Util.collisionTest(p.getBoundingBox(), b.getBoundingBox())) {
					brickHit(b, null, true);
					p.setDestroyed(true);
				}
			}
		}
	}

	private void brickHit(Brick b, Ball ball, boolean hitByProjectile) {
		b.hit();
		
		if (hitByProjectile) {
			if (b.getType() != Brick.Type.SOLID) {
				score += 100;		
			}
			
			if (b.getType() == Brick.Type.POWERUP) {
				score += 1000;			
				spawnPowerup(b.getX(), b.getY(), 
							 EffectType.values()[Util.random(0, EffectType.values().length - 1)]);
			}
		}
		else {
			// Reflect the ball?			
			if (!EffectManager.getInstance().isEffectActive(EffectType.FIREBALL) &&
				b.getType() != Brick.Type.WEAK) {
				
				switch (Util.getCollisionEdge(ball.getBoundingBox(), b.getBoundingBox())) {
				case LEFT:
					ball.invertXVelocity();					
					break;
					
				case TOP_LEFT:
					ball.invertXVelocity();
					ball.invertYVelocity();
					break;
					
				case TOP:
					ball.invertYVelocity();
					break;
					
				case TOP_RIGHT:
					ball.invertXVelocity();
					ball.invertYVelocity();
					break;
					
				case RIGHT:
					ball.invertXVelocity();
					break;									
					
				case BOTTOM_RIGHT:
					ball.invertXVelocity();
					ball.invertYVelocity();
					break;
					
				case BOTTOM:
					ball.invertYVelocity();
					break;
					
				case BOTTOM_LEFT:
					ball.invertXVelocity();
					ball.invertYVelocity();
					break;

				default:
					throw new RuntimeException("Invalid egde type");				
				}				
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
	}

	public void spawnBall() {
		balls.add(new Ball(Config.BALL_SPAWN_X, Config.BALL_SPAWN_Y));
	}
	
	private void ballLost(Iterator<Ball> bi) {
		ballsLeft--;
		score -= 1000;
		
		bi.remove();

		if (balls.isEmpty()) {
			EffectManager.getInstance().clearEffects();
			setState(State.WAITING_FOR_BALL);
		}			
		
		SoundLayer.playSound(Sounds.BALL_LOST);
	}
	
	public void doCleanup() {		
		Iterator<Ball> bai = balls.iterator();		
		while (bai.hasNext()) {
			Ball ball = bai.next();
			
			if (ball.isDestroyed()) {
				bai.remove();
			}
		}
		
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
		
		// Remove projectiles
		Iterator<Projectile> pri = projectiles.iterator();		
		while (pri.hasNext()) {
			Projectile p = pri.next();
			
			if (p.isDestroyed() || p.getY() <= 0) {
				pri.remove();
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
