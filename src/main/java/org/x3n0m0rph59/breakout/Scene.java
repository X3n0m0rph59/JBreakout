package org.x3n0m0rph59.breakout;

import org.x3n0m0rph59.breakout.SoundLayer;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;


public final class Scene {
	public enum State {LOADING, NEW_STAGE, WAITING_FOR_BALL, RUNNING, STAGE_CLEARED, RESTART, PAUSED, GAME_OVER, LEVEL_SET_COMPLETED, TERMINATED};
	private enum ParticleEffect {BRICK_EXPLOSION, BALL_LOST};
	private State state = State.LOADING;
	
	private ScoreBoard scoreBoard = new ScoreBoard();
	
	private HashMap<String, String> levelMetadata;
	private int level = 0;
	private int ballsLeft = Config.INITIAL_BALLS_LEFT;
	private int score = 0; 
	
	private Paddle paddle = new Paddle();
	private List<Ball> balls = new ArrayList<Ball>();
	private List<Brick> bricks = new ArrayList<Brick>();
	private List<Powerup> powerups = new ArrayList<Powerup>();
	private List<Star> stars = new ArrayList<Star>();
	private List<Projectile> projectiles = new ArrayList<Projectile>();	
	private List<TextAnimation> textAnimations = new ArrayList<TextAnimation>();
	private List<ParticleSystem> particleEffects = new ArrayList<ParticleSystem>();
	private List<Background> backgrounds = new ArrayList<>();
		
	private TrueTypeFont font;
	
	private int frameCounter = 0;
	
	public Scene() {
		font = FontLoader.getInstance().getFont("Verdana", Font.BOLD, Config.TOAST_FONT_SIZE);
		
		initLevel(0);						
	}
	
	private void initLevel(int level) {		
		EffectManager.getInstance().clearEffects();
		
		paddle.setWidth(Config.PADDLE_DEFAULT_WIDTH);
		
		balls.clear();
		balls.add(new Ball((Config.getInstance().getScreenWidth() - Config.SCOREBOARD_WIDTH) / 2, 
							Config.getInstance().getScreenHeight() / 2));
		
		levelMetadata = LevelLoader.getLevelMetaData(level);
		bricks = LevelLoader.loadLevel(level);
		
		powerups.clear();
		projectiles.clear();
		textAnimations.clear();
				
		// Spawn initial set of particles
		stars.clear();
		for (int i = 0; i < Config.SYNC_FPS * Config.STAR_DENSITY; i++) {
			stars.add(new Star(Util.random(0, (int) Config.getInstance().getClientWidth()), 
									   Util.random(0, (int) Config.getInstance().getScreenHeight()), 
									   Util.random((int) Config.STAR_MIN_SPEED, 
											   	   (int) Config.STAR_MAX_SPEED)));
		}
		
		backgrounds.clear();
		backgrounds.add(BackgroundFactory.getRandomBackground());
	}
	
	private String getLevelMetaData(String key) {
		String result = levelMetadata.get(key);
		
		if (result == null)
			result = "<no data>";
		
		return result;
	}
	
	private void drawCenteredText(String[] lines, boolean eraseBackground) {
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		
		if (eraseBackground)
		{			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.85f);
				GL11.glVertex2f(0, 0);			
				GL11.glVertex2f(Config.getInstance().getClientWidth(), 0);			
				GL11.glVertex2f(Config.getInstance().getClientWidth(), 
								Config.getInstance().getScreenHeight());			
				GL11.glVertex2f(0, Config.getInstance().getScreenHeight());
			GL11.glEnd();
		}
												
		int cnt = 0;
		for (String line : lines) {
			int width = font.getWidth(line);
			int height = font.getLineHeight() + 5;
			
			font.drawString(Config.getInstance().getClientWidth() / 2 - width / 2, 
							(height * cnt) + Config.getInstance().getScreenHeight() / 2, 
							line, Color.white);
			cnt++;
		}
		
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void render() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL11.glPushMatrix();		
		
		for (Background b : backgrounds) {
			b.render();
		}
		
		for (Star p : stars) {
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
		if (EffectManager.getInstance().isEffectActive(EffectType.BOTTOM_WALL)) {
			drawBottomWall();
		}
		
		for (ParticleSystem p : particleEffects) {
			p.render();
		}
		
		paddle.render();
		
		scoreBoard.render();
				
//		for (TextAnimation a : textAnimations) {
//			a.render();
//		}
		
		if (textAnimations.size() > 0)
			textAnimations.get(0).render();
		
		switch (state) {
		case LOADING:
			drawCenteredText(new String[] {"Loading, please wait..."}, true);
			break;
			
		case NEW_STAGE:
			drawCenteredText(new String[] {"Level Set: " + getLevelMetaData("Level Set"),
										   "Level: " + getLevelMetaData("Level"), 
										   getLevelMetaData("Name"),
										   "",
										   "Press mouse button to start"}, true);
			break;
			
		case RUNNING:			
			break;
			
		case RESTART:
			drawCenteredText(new String[] {"Restarting stage"}, true);
			break;
			
		case PAUSED:
			drawCenteredText(new String[] {"*GAME PAUSED*",
										   "",
										   "Level Set: " + getLevelMetaData("Level Set"),
										   "Level: " + getLevelMetaData("Level"), 
										   getLevelMetaData("Name"),
										   "",
										   "Press mouse button to resume"}, true);
			break;
			
		case WAITING_FOR_BALL:
			drawCenteredText(new String[] {"*BALL LOST*", "", "Press mouse button for a new ball"}, true);
			break;
			
		case STAGE_CLEARED:
			drawCenteredText(new String[] {"*STAGE CLEARED*", "",
										   "Level Set: " + getLevelMetaData("Level Set"),
										   "Level: " + getLevelMetaData("Level"), 
										   getLevelMetaData("Name"),
										   "","Press mouse button to continue"}, true);
			break;
			
		case GAME_OVER:
			drawCenteredText(new String[] {"*GAME OVER!*", "", "Press F2 to restart or Q to quit"}, true);
			break;
						
		case TERMINATED:
			drawCenteredText(new String[] {"Good bye!"}, true);
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
		
		GL11.glPopMatrix();
	}

	private void drawBottomWall() {
		for (int i = 0; i <= Config.getInstance().getClientWidth() / (Config.BOTTOM_WALL_SEGMENT_WIDTH + Config.BOTTOM_WALL_SEGMENT_SPACING); i++) {
			float x = i * (Config.BOTTOM_WALL_SEGMENT_WIDTH + Config.BOTTOM_WALL_SEGMENT_SPACING);
			float y = Config.getInstance().getScreenHeight() - Config.BOTTOM_WALL_HEIGHT;
			
			final float width = Config.BOTTOM_WALL_SEGMENT_WIDTH;
			final float height = Config.BOTTOM_WALL_SEGMENT_HEIGHT;
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor3f(1.0f, 0.0f, 0.0f);			
				GL11.glVertex2f(x, y);			
				GL11.glVertex2f(x + width, y);			
				GL11.glVertex2f(x + width, y + height);			
				GL11.glVertex2f(x, y + height);
			GL11.glEnd();
		}
	}
	
//	private void drawRect(Rectangle r) {
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL11.glBegin(GL11.GL_QUADS);
//			GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.5f);
//			GL11.glVertex2f(r.getX(), r.getY());			
//			GL11.glVertex2f(r.getX() + r.getWidth(), r.getY());			
//			GL11.glVertex2f(r.getX() + r.getWidth(), r.getY() + r.getHeight());			
//			GL11.glVertex2f(r.getX(), r.getY() + r.getHeight());
//		GL11.glEnd();
//	}
	
	public void step() {
		frameCounter++;		
		
		float mdx = (int) Mouse.getDX();
		// float mdy = (int) Mouse.getDY();

		// Process keyboard events
		int lastKeyID = 0;
		while (Keyboard.next()) {
			if ((lastKeyID = Keyboard.getEventKey()) == Keyboard.KEY_ADD && 
				Keyboard.getEventKeyState()) {
				initLevel(++level);
				
				Logger.log("Cheating to next level");
			}
		}

		// Process mouse events
		boolean leftMouseButtonPressed = false;
		boolean rightMouseButtonPressed = false;
		while (Mouse.next()) {
			if (Mouse.getEventButton() == 0) {
				leftMouseButtonPressed = Mouse.getEventButtonState();
			} else if (Mouse.getEventButton() == 1) {
				rightMouseButtonPressed = Mouse.getEventButtonState();
			}
		}
		
		switch (state) {
		case LOADING:
			break;
			
		case NEW_STAGE:
			if (leftMouseButtonPressed || rightMouseButtonPressed) {
				setState(State.RUNNING);
			}
			
			if (lastKeyID == Keyboard.KEY_Q) {
				setState(State.TERMINATED);
			}
			break;
			
		case PAUSED:
			if (leftMouseButtonPressed || rightMouseButtonPressed) {
				setState(State.RUNNING);
			}
			
			if (lastKeyID == Keyboard.KEY_Q) {
				setState(State.TERMINATED);
			}
			break;
			
		case STAGE_CLEARED:
			if (leftMouseButtonPressed || rightMouseButtonPressed) {
				initLevel(++level);
				setState(State.NEW_STAGE);
			}
			
			if (lastKeyID == Keyboard.KEY_Q) {
				setState(State.TERMINATED);
			}
			break;
			
		case RUNNING:
			if (lastKeyID == Keyboard.KEY_P) {
				setState(State.PAUSED);
			}
			
			if (lastKeyID == Keyboard.KEY_Q) {
				setState(State.TERMINATED);
			}
			
			if (lastKeyID == Keyboard.KEY_F2) {
				setState(State.RESTART);
			}
			
			EffectManager.getInstance().step();
			
			for (Background b : backgrounds) {
				b.step();
			}
			
			for (Star p : stars) {
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

//			for (TextAnimation a : textAnimations) {
//				a.step();
//			}
			
			if (textAnimations.size() > 0)
				textAnimations.get(0).step();
			
			
			for (ParticleSystem p : particleEffects) {
				p.step();
			}
			
			
			for (Ball b : balls) {
				b.step();
			}
			
			paddle.step();
			
			doCollisionDetection();
			doCleanup();
			
			// Spawn new particles
			for (int i = 0; i < Config.STAR_DENSITY; i++) {
				stars.add(new Star(Util.random(0, (int) Config.getInstance().getClientWidth()), 0f, 
													   Util.random((int) Config.STAR_MIN_SPEED, 
															       (int) Config.STAR_MAX_SPEED)));
			}
			
			// Spawn a new background?
			if ((frameCounter % Config.BACKGROUND_DENSITY) == 0) {
				backgrounds.add(BackgroundFactory.getRandomBackground());
			}
			
			// Spawn new projectiles?
			if (EffectManager.getInstance().isEffectActive(EffectType.PADDLE_GUN)) {
				if (Mouse.isButtonDown(0) && (frameCounter % Config.PROJECTILE_FIRE_RATE == 0)) {
					for (int i = 0; i < 2; i++) {
						float x = (frameCounter % (Config.PROJECTILE_FIRE_RATE * 2) == 0) ? 
								paddle.getX() : paddle.getX() + paddle.getWidth() - Config.PROJECTILE_WIDTH; 
						
						projectiles.add(new Projectile(x, paddle.getY()));
						
						SoundLayer.playSound(Sounds.BULLET_FIRED);
					}
				}
			}
			
			// Move sticky balls with the paddle and
			// release them if a mouse button is pushed
			for (Ball ball : balls)
			{
				if (ball.getState() == Ball.State.STUCK_TO_PADDLE) {
					if ((Mouse.getX() + mdx >= 0) && (Mouse.getX() + mdx <= Config.getInstance().getClientWidth())) {						
						ball.moveBy(mdx, 0);
					}
					
					if (leftMouseButtonPressed || rightMouseButtonPressed) {
						// ball.setAngleOfReflection(180);
						ball.setState(Ball.State.ROLLING);						
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
			setState(State.NEW_STAGE);
			break;
			
		case TERMINATED:
			System.exit(0);
			break;
			
		case WAITING_FOR_BALL:
			if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
				balls.clear();
				spawnBall(false);
				
				setState(State.RUNNING);
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				setState(State.TERMINATED);
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
				setState(State.RESTART);
			}
			break;
			
		case GAME_OVER:
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
		float pdx = paddle.getdX();		
		
		// Ball vs. Edges
		for (Ball ball : balls) {			
			// sanity check ball coordinates
			if ((ball.getBoundingBox().getX() + ball.getBoundingBox().getWidth()) >= Config.getInstance().getClientWidth()) {
				final float newX = Config.getInstance().getClientWidth() - ball.getBoundingBox().getWidth();				
				final float newY = ball.getBoundingBox().getY(); 
				
				ball.setPosition(newX, newY);
			}
			
			if (ball.getBoundingBox().getX() <= 0) {
				final float newX = 0;				
				final float newY = ball.getBoundingBox().getY(); 
				
				ball.setPosition(newX, newY);
			}	
			
			// Reflect the ball if it's not stuck to the paddle	
			if (ball.getState() != Ball.State.STUCK_TO_PADDLE) {
				if (ball.getBoundingBox().getX() <= 0 || 
					ball.getBoundingBox().getX() >= Config.getInstance().getClientWidth() - 
													ball.getBoundingBox().getWidth()) {
					
					ball.invertXVelocity();
					
					SoundLayer.playSound(Sounds.WALL_HIT);
				}
			}
		}

		for (Ball ball : balls) {
			if (ball.getState() != Ball.State.STUCK_TO_PADDLE) {
				if (ball.getBoundingBox().getY() <= 0 || 
					(EffectManager.getInstance().isEffectActive(EffectType.BOTTOM_WALL)) && 
					 ball.getBoundingBox().getY() >= (Config.getInstance().getScreenHeight() - Config.BOTTOM_WALL_HEIGHT) - 
					 								  ball.getBoundingBox().getHeight()) {
					ball.invertYVelocity();
					
					SoundLayer.playSound(Sounds.WALL_HIT);
				}
			}
		}
		
		// Ball lost?
		Iterator<Ball> bi = balls.iterator();
		while (bi.hasNext()) {			
			Ball ball = bi.next();
			if (ball.getBoundingBox().getY() >= Config.getInstance().getScreenHeight() && 
				!EffectManager.getInstance().isEffectActive(EffectType.BOTTOM_WALL)) {
				ballLost(ball, bi);
			}
		}
		
		// Ball vs. Paddle
		for (Ball ball : balls) {
			if (ball.getState() != Ball.State.STUCK_TO_PADDLE) {
				if (Util.collisionTest(paddle.getBoundingBox(), ball.getBoundingBox())) {					
//					final Edge edge = Util.getCollisionEdge(ball.getBoundingBox(), paddle.getBoundingBox());
													
					Vector2f ballVector = new Vector2f();
					ballVector.set(ball.getVelX(), ball.getVelY());
					ballVector.setTheta(ball.getAngle());					
					
					Vector2f paddleVector = new Vector2f();
					paddleVector.set(pdx, 1.0f);
					paddleVector.setTheta(0.0f);
					
					Vector2f result = ballVector.add(paddleVector);
					result = result.set(result.getX(), result.getY() > 0 ? result.getY() : 
																		   result.getY() * -1);

					float newBallSpeed = result.length();
					
					// compromise realistic physics 
					// for a better gameplay
					if (newBallSpeed > Config.BALL_SPEED_MAX)
						newBallSpeed = Config.BALL_SPEED_MAX;
					else if (newBallSpeed < Config.BALL_SPEED_MIN)
						newBallSpeed = Config.BALL_SPEED_MIN;
					
					ball.setAngle((float) result.getTheta());
					ball.setSpeed(newBallSpeed);
										
						
					// avoid double collisions by placing the ball above the paddle
					ball.setPosition(ball.getBoundingBox().getX(), 
									 paddle.getBoundingBox().getY() - 
									 (ball.getBoundingBox().getHeight() + 2.0f));
															
					
					// Sticky ball?
					if (EffectManager.getInstance().isEffectActive(EffectType.STICKY_BALL))
						ball.setState(Ball.State.STUCK_TO_PADDLE);
					
					SoundLayer.playSound(Sounds.PADDLE_HIT);
				}
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
		
		if (b.isDestroyed()) {
			addParticleEffect(b.getBoundingBox().getCenterX(), 
							  b.getBoundingBox().getCenterY(), 
							  ParticleEffect.BRICK_EXPLOSION);
		}
		
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

	public void spawnBall(boolean isMultiball) {
		balls.add(new Ball((Config.getInstance().getScreenWidth() - Config.SCOREBOARD_WIDTH) / 2, 
							Config.getInstance().getScreenHeight() / 2, isMultiball));
	}
	
	private void ballLost(Ball ball, Iterator<Ball> bi) {
		if (!ball.isMultiball()) {
			ballsLeft--;
			score -= 1000;
		}
		
		bi.remove();

		if (balls.isEmpty()) {
			EffectManager.getInstance().clearEffects();
			setState(State.WAITING_FOR_BALL);
		}			
		
		SoundLayer.playSound(Sounds.BALL_LOST);
		
		if (ballsLeft <= 0) {
			setState(State.GAME_OVER);
		}
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
				
		// Remove excess stars
		Iterator<Star> pi = stars.iterator();		
		while (pi.hasNext()) {
			Star p = pi.next();
			
			if (p.getY() >= Config.getInstance().getScreenHeight()) {
				pi.remove();
			}
		}
		
		// Remove excess backgrounds
		Iterator<Background> bgi = backgrounds.iterator();		
		while (bgi.hasNext()) {
			Background b = bgi.next();
			
			if (b.isDestroyed()) {
				bgi.remove();
			}
		}
		
		// Remove excess and/or consumed powerups
		Iterator<Powerup> pui = powerups.iterator();		
		while (pui.hasNext()) {
			Powerup p = pui.next();
			
			if (p.isDestroyed() || p.getY() >= Config.getInstance().getScreenHeight()) {
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
		
		// Remove expired particle effects
		Iterator<ParticleSystem> psi = particleEffects.iterator();		
		while (psi.hasNext()) {
			ParticleSystem ps = psi.next();
			
			if (ps.isDestroyed()) {
				psi.remove();
			}
		}
	}
	
	public void spawnPowerup(float x, float y, EffectType effectType) {
		Logger.log("Spawned powerup: " + effectType);
		
		powerups.add(new Powerup(x, y, effectType));
				
		SoundLayer.playSound(Sounds.POWERUP_SPAWNED);
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
	
	public int getLevel() {		
		return level;
	}

	public Paddle getPaddle() {
		return paddle;		
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
		
		switch (state) {					
		case NEW_STAGE:
			SoundLayer.playMusic(Musics.BACKGROUND);
			break;
			
		case RESTART:
			level = 0;
			score = 0;
			ballsLeft = Config.INITIAL_BALLS_LEFT;			
			break;
			
		case GAME_OVER:
			break;
			
		case LOADING:
			break;
			
		case PAUSED:
			break;
			
		case RUNNING:
			break;
			
		case STAGE_CLEARED:
			break;
			
		case TERMINATED:
			break;
			
		case WAITING_FOR_BALL:
			break;
			
		default:
			throw new RuntimeException("Invalid state: " + state);
		}
	}
	
	private void addParticleEffect(float x, float y, ParticleEffect effect) {		
		switch (effect) {
		case BRICK_EXPLOSION:		
			particleEffects.add(new ParticleSystem(new SpriteTuple[]{new SpriteTuple("sprites/fire.png", 198.0f, 197.0f, 198, 197)}, 
					x, y, 20.0f, 10.0f, 0.0f, 360.0f, 0.0f, 15.0f, 55.0f, 2.0f));
			break;
			
		case BALL_LOST:		
//			particleEffects.add(new ParticleSystem(new SpriteTuple[]{new SpriteTuple("sprites/Star1.png", 255.0f, 255.0f, 255, 255), 
//					  new SpriteTuple("sprites/Star2.png", 345.0f, 342.0f, 345, 342), 
//					  new SpriteTuple("sprites/Star3.png", 270.0f, 261.0f, 270, 261), 
//					  new SpriteTuple("sprites/Star4.png", 264.0f, 285.0f, 264, 285)}, 
//			x, y, 150.0f, 5.0f, 0.0f, 180.0f, 0.0f, 15.0f, 15.0f, 5.0f));
			break;
		}
	}
}
