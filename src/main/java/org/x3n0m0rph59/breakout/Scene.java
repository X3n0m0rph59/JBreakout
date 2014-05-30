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
import org.newdawn.slick.geom.Circle;


public class Scene {
	public enum State {LOADING, NEW_STAGE, WAITING_FOR_BALL, RUNNING, STAGE_CLEARED, 
					   RESTART, PAUSED, GAME_OVER, LEVEL_SET_COMPLETED, TERMINATED};
	private enum ParticleEffect {BRICK_EXPLOSION, BALL_LOST};
	
	private State state = State.LOADING;
	
	private ScoreBoard scoreBoard = new ScoreBoard();
	private BottomWall bottomWall = new BottomWall();
	
	private HashMap<String, String> levelMetadata;	
	
	private Paddle paddle = new Paddle();
	
	private List<Ball> balls = new ArrayList<>();
	private List<Brick> bricks = new ArrayList<>();
	private List<Powerup> powerups = new ArrayList<>();
	private List<Star> stars = new ArrayList<>();
	private List<Projectile> projectiles = new ArrayList<>();		
	private List<ParticleSystem> particleEffects = new ArrayList<>();
	private List<Background> backgrounds = new ArrayList<>();
	private List<SpaceBomb> spaceBombs = new ArrayList<>();
		
	private TrueTypeFont font;
	
	private int frameCounter = 0;
	
	private int spaceBombCoolDownTime = 0;
	
	public Scene() {
		font = FontLoader.getInstance().getFont("Verdana", Font.BOLD, Config.TOAST_FONT_SIZE);
		
		initLevel(0);						
	}
	
	private void initLevel(int level) {
		GameState.setLevel(level);
		
//		don't reset the frame counter for now (game balance)
//		it influences whether new space bombs are spawned etc. 
//		frameCounter = 0;
		
		// reset cooldown timers
		spaceBombCoolDownTime = 0;
		
		EffectManager.getInstance().clearEffects();
		TextAnimationManager.getInstance().clear();
		
		paddle.getGrapplingHook().resetState();
		paddle.setWidth(Config.PADDLE_DEFAULT_WIDTH);
		
		balls.clear();
		balls.add(new Ball(new Point((Config.getInstance().getScreenWidth() - Config.SCOREBOARD_WIDTH) / 2, 
									  Config.getInstance().getScreenHeight() / 2)));
						
		powerups.clear();
		projectiles.clear();
		spaceBombs.clear();		
				
		// Spawn initial set of particles
		stars.clear();
		for (int i = 0; i < Config.SYNC_FPS * Config.STAR_DENSITY; i++) {
			stars.add(new Star(new Point(Util.random(0, (int) Config.getInstance().getClientWidth()), 
									   	 Util.random(0, (int) Config.getInstance().getScreenHeight())), 
									   	 Util.random((int) Config.STAR_MIN_SPEED, 
											   	   	 (int) Config.STAR_MAX_SPEED)));
		}
		
		backgrounds.clear();
		backgrounds.add(BackgroundFactory.getRandomBackground());
		
		levelMetadata = LevelLoader.getLevelMetaData(level);
		bricks = LevelLoader.loadLevel(level);
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
		
		for (SpaceBomb b : spaceBombs) {
			b.render();
		}

		for (Ball b : balls) {
			b.render();
		}
		
		// Draw a wall on the bottom of the screen?
		if (EffectManager.getInstance().isEffectActive(Effect.Type.BOTTOM_WALL)) {
			bottomWall.render();
		}
		
		for (ParticleSystem p : particleEffects) {
			p.render();
		}
		
		paddle.render();
		
		scoreBoard.render();

		TextAnimationManager.getInstance().render();
		
		switch (state) {
		case LOADING:
			drawCenteredText(new String[] {"Loading, please wait..."}, true);
			break;
			
		case NEW_STAGE:
			drawCenteredText(new String[] {"Level Set: " + getLevelMetaData("Level Set"),
										   "Level: " + getLevelMetaData("Level"), 
										   "\"" + getLevelMetaData("Name") + "\"",
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
										   "\"" + getLevelMetaData("Name") + "\"",
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
										   "\"" + getLevelMetaData("Name") + "\"",
										   "","Press mouse button to continue"}, true);
			break;
			
		case GAME_OVER:
			drawCenteredText(new String[] {"*GAME OVER!*", "", "Press F2 to restart, or Q to quit"}, true);
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
		
		// cooldown timers
		spaceBombCoolDownTime--;
		
		
		float mdx = (int) Mouse.getDX();
		// float mdy = (int) Mouse.getDY();

		// Process keyboard events
		int lastKeyID = 0;
		while (Keyboard.next()) {
			if ((lastKeyID = Keyboard.getEventKey()) == Keyboard.KEY_ADD && 
				Keyboard.getEventKeyState()) {
				initLevel(GameState.getLevel() + 1);
				
				Logger.log("Cheating to next level", 1);
			}
		}

		// Process mouse events
		boolean leftMouseButtonPressed = false;
		boolean rightMouseButtonPressed = false;
		boolean middleMouseButtonPressed = false;
		
		while (Mouse.next()) {
			if (Mouse.getEventButton() == 0) {
				leftMouseButtonPressed = Mouse.getEventButtonState();
			} else if (Mouse.getEventButton() == 1) {
				rightMouseButtonPressed = Mouse.getEventButtonState();
			} else if (Mouse.getEventButton() == 2) {
				middleMouseButtonPressed = Mouse.getEventButtonState();
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
				initLevel(GameState.getLevel() + 1);
				setState(State.NEW_STAGE);
			}
			
			if (lastKeyID == Keyboard.KEY_Q) {
				setState(State.TERMINATED);
			}
			break;
			
		case RUNNING:
			if (middleMouseButtonPressed) {
				paddle.getGrapplingHook().toggleSwitch();
			}				
				
			if (lastKeyID == Keyboard.KEY_P) {
				setState(State.PAUSED);
			}
			
			if (lastKeyID == Keyboard.KEY_Q) {
				setState(State.TERMINATED);
			}
			
			if (lastKeyID == Keyboard.KEY_F2) {
				setState(State.RESTART);
			}
			
			if (lastKeyID == Keyboard.KEY_B) {				
				if (Keyboard.getEventKeyState())
					releaseSpaceBomb();
			}
						
			EffectManager.getInstance().step();
			TextAnimationManager.getInstance().step();
			
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
			
			for (SpaceBomb b : spaceBombs) {
				b.step();
			}			
			
			for (ParticleSystem p : particleEffects) {
				p.step();
			}
			
			
			for (Ball b : balls) {
				b.step();
			}
			
			paddle.step();
			
			bottomWall.step();
			
			// TODO: Make this work
//			if (lastKeyID == Keyboard.KEY_LEFT) {				
//				paddle.changeX(-50.0f);
//			}
//			
//			if (lastKeyID == Keyboard.KEY_RIGHT) {				
//				paddle.changeX(+50.0f);
//			}
			
			doCollisionDetection();
			doCleanup();
			
			// check for exploding space bombs
			// and apply effect to bricks
			for (SpaceBomb b : spaceBombs) {
				if (b.getState() == SpaceBomb.State.EXPLODING) {
					final Point centerOfExplosion = b.getCenterOfExplosion();
					final Circle explosionCircle = new Circle(centerOfExplosion.getX(), 
															  centerOfExplosion.getY(), 
															  Config.SPACEBOMB_EXPLOSION_RADIUS);					
									
					for (Brick brick : bricks) {
						if (explosionCircle.intersects(brick.getBoundingBox())) {
//							brickHit(brick, null, true);
							
							if (brick.getType() != Brick.Type.SOLID) {
								GameState.changeScore(100);
							}
							
							if (brick.getType() == Brick.Type.POWERUP) {
								GameState.changeScore(1000);			
								spawnPowerup(b.getPosition(), Effect.Type.values()[Util.random(0, Effect.Type.values().length - 1)]);
							}
							
							brick.setDestroyed(true);
						}
					}
				}
			}
			
			// Spawn new particles
			for (int i = 0; i < Config.STAR_DENSITY; i++) {
				stars.add(new Star(new Point(Util.random(0, (int) Config.getInstance().getClientWidth()), 0.0f), 
								   Util.random((int) Config.STAR_MIN_SPEED, 
										       (int) Config.STAR_MAX_SPEED)));
			}
			
			// Spawn a new background?
			if ((frameCounter % Config.BACKGROUND_DENSITY) == 0) {
				backgrounds.add(BackgroundFactory.getRandomBackground());
			}
			
			// Fire projectiles?
			if (EffectManager.getInstance().isEffectActive(Effect.Type.PADDLE_GUN)) {
				if ((Mouse.isButtonDown(0) || lastKeyID == Keyboard.KEY_SPACE) && 
					(frameCounter % Config.PROJECTILE_FIRE_RATE == 0)) {
					for (int i = 0; i < 2; i++) {
						float x = (frameCounter % (Config.PROJECTILE_FIRE_RATE * 2) == 0) ? 
								paddle.getX() : paddle.getX() + paddle.getWidth() - Config.PROJECTILE_WIDTH; 
						
						projectiles.add(new Projectile(new Point(x, paddle.getY())));
						
						SoundLayer.playSound(Sounds.BULLET_FIRED);
					}
				}
			}
			
			// Spawn a new bonus SpaceBomb?
			if ((frameCounter % Config.SPACEBOMB_DENSITY) == 0) {
				spaceBombs.add(new SpaceBomb(new Point((float) Util.random(0, (int) Config.getInstance().getClientWidth()), -50.0f), 
											 SpaceBomb.Type.BONUS));
			}
			
			// Move sticky balls with the paddle and
			// release them if a mouse button is pushed
			for (Ball ball : balls) {
				if (ball.getState() == Ball.State.STUCK_TO_PADDLE) {
					if ((Mouse.getX() + mdx + (paddle.getWidth() / 2) >= 0) && 
						(Mouse.getX() + mdx + (paddle.getWidth() / 2) <= Config.getInstance().getClientWidth())) {						
						
						// TODO BUG: 
						// ball may be moved relative to the paddle!
						ball.changePosition(mdx, 0);
					}
					
					if (leftMouseButtonPressed || rightMouseButtonPressed) {
						
						ball.setState(Ball.State.ROLLING);						
					}
				}
			}
			
			// Move caught power ups with the paddle
			for (Powerup p : powerups) {
				if (p.getState() == Powerup.State.STUCK_TO_GRAPPLING_HOOK) {
					if ((Mouse.getX() + mdx + (paddle.getWidth() / 2) >= 0) && 
						(Mouse.getX() + mdx + (paddle.getWidth() / 2) <= Config.getInstance().getClientWidth())) {						
						
						// TODO BUG: 
						// bomb may be moved relative to the paddle!
						p.setCenterPosition(paddle.getGrapplingHook().getHookCenterPoint());
					}										
				}
			}
			
			// Move caught space bombs with the paddle
			for (SpaceBomb bomb : spaceBombs) {
				if (bomb.getState() == SpaceBomb.State.STUCK_TO_GRAPPLING_HOOK) {
					if ((Mouse.getX() + mdx + (paddle.getWidth() / 2) >= 0) && 
						(Mouse.getX() + mdx + (paddle.getWidth() / 2) <= Config.getInstance().getClientWidth())) {						
						
						// TODO BUG: 
						// bomb may be moved relative to the paddle!
						bomb.setCenterPosition(paddle.getGrapplingHook().getHookCenterPoint());
					}										
				}
			}
			
			// Stage cleared?			
			if (isStageCleared()) {
				setState(State.STAGE_CLEARED);
			}
			break;
			
		case RESTART:
			initLevel(GameState.getLevel());			
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
		
	public void releaseSpaceBomb() {			
		if (GameState.getSpaceBombsLeft() > 0) {
			if (spaceBombCoolDownTime > 0)
			{
				TextAnimationManager.getInstance().add("Bomb not ready yet!");
				
				SoundLayer.playSound(Sounds.ACTION_DENIED);
				
			} else {
				spaceBombs.add(new SpaceBomb(new Point(paddle.getCenterPoint().getX(), 
													   paddle.getCenterPoint().getY() - 10.0f), 
													   SpaceBomb.Type.USER_FIRED));
				
				GameState.decrementSpaceBombsLeft();				
				spaceBombCoolDownTime = Config.SPACEBOMB_COOLDOWN_TIME;
				
				SoundLayer.playSound(Sounds.SPACEBOMB_LAUNCH);
			}
		} else {
			TextAnimationManager.getInstance().add("No bomb available!");
			
			SoundLayer.playSound(Sounds.ACTION_DENIED);
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
			// clamp ball to client area
			if ((ball.getX() + ball.getWidth()) >= Config.getInstance().getClientWidth()) {
				final float newX = (Config.getInstance().getClientWidth() - (ball.getWidth()));				
				final float newY = ball.getY(); 
				
				ball.setPosition(new Point(newX, newY));
			}
			
			if (ball.getX() <= 0) {
				final float newX = 0;				
				final float newY = ball.getY(); 
				
				ball.setPosition(new Point(newX, newY));
			}
			
			if (ball.getY() <= 0) {
				final float newX = ball.getX();				
				final float newY = 0.0f; 
				
				ball.setPosition(new Point(newX, newY));
			}	
			
			// Reflect the ball if it's not stuck to the paddle	
			if (ball.getState() != Ball.State.STUCK_TO_PADDLE) {
				if (ball.getX() <= 0 || 
					ball.getX() >= Config.getInstance().getClientWidth() - ball.getWidth()) {
					
					ball.invertXVelocity();
					
					SoundLayer.playSound(Sounds.WALL_HIT);
				}
			}
		}

		for (Ball ball : balls) {
			if (ball.getState() != Ball.State.STUCK_TO_PADDLE) {
				if (ball.getY() <= 0 || 
					(EffectManager.getInstance().isEffectActive(Effect.Type.BOTTOM_WALL)) && 
					 ball.getY() >= (Config.getInstance().getScreenHeight() - Config.BOTTOM_WALL_HEIGHT) - 
					 								  ball.getHeight()) {
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
				!EffectManager.getInstance().isEffectActive(Effect.Type.BOTTOM_WALL)) {					
				ballLost(ball, bi);
			}
		}
		
		// Ball vs. Paddle
		for (Ball ball : balls) {
			if (ball.getState() != Ball.State.STUCK_TO_PADDLE) {
				if (Util.collisionTest(paddle.getBoundingBox(), ball.getBoundingBox())) {					
//					final Edge edge = Util.getCollisionEdge(ball.getBoundingBox(), paddle.getBoundingBox());
					
					// Calculate reflection vector based on the formula
					// V - Velocity Vector
					// N - The Normal Vector of the plane
					// Vnew = -2*(V dot N)*N + V
													
					Vector ballVector = new Vector(ball.getDeltaX(), ball.getDeltaY(), ball.getMovementAngleInDegrees());					
					Vector paddleVector =  new Vector(pdx, 1.0f, 0.0f);
					Vector surfaceNormal = paddleVector.cross(ballVector).normalize();
					
					Vector result = surfaceNormal.mult(-2 * (ballVector.dot(surfaceNormal))).add(ballVector);
					result = result.mult(Config.PADDLE_DAMPENING_FACTOR);					
					result = result.add(paddleVector);
					
					ball.setDeltaX(result.getX());
					ball.setDeltaY(-result.getY());
						
					// avoid double collisions by placing the ball above the paddle
					ball.setPosition(new Point(ball.getX(), paddle.getY() - (ball.getHeight() + 1.0f)));
															
					
					// Sticky ball?
					if (EffectManager.getInstance().isEffectActive(Effect.Type.STICKY_BALL))
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
		
		// Caught a powerup with the paddle?
		for (Powerup p : powerups) {
			if (Util.collisionTest(paddle.getBoundingBox(), p.getBoundingBox())) {
				EffectManager.getInstance().addEffect(p.getType());
				p.setDestroyed(true);
			}
		}
		
		// Projectile vs. Bricks		
		for (Projectile p : projectiles) {
			for (Brick b : bricks) {
				if (Util.collisionTest(p.getBoundingBox(), b.getBoundingBox())) {
					brickHit(b, null, true);
					p.setDestroyed(true);
				}
			}
		}
		
		// Caught a power up with the grappling hook?
		if (paddle.getGrapplingHook().getState() != GrapplingHook.State.IDLE) {
			for (Powerup p : powerups) {
				if (paddle.getGrapplingHook().collisionTest(p.getBoundingBox())) {
					
					p.setState(Powerup.State.STUCK_TO_GRAPPLING_HOOK);
					
					Logger.log("Caught a Power up!", 1);
				}
			}
		}
		
		// Consume caught power ups if they are 
		// drawn in with the grappling hook
		for (Powerup p : powerups) {
			if (p.getState() == Powerup.State.STUCK_TO_GRAPPLING_HOOK) {
				if (Util.collisionTest(paddle.getBoundingBox(), p.getBoundingBox())) {
					EffectManager.getInstance().addEffect(p.getType());
					p.setDestroyed(true);
				}
			}
		}
		
		// Caught a space bomb with the grappling hook?
		if (paddle.getGrapplingHook().getState() != GrapplingHook.State.IDLE) {
			for (SpaceBomb b : spaceBombs) {
				//if (b.getType() == SpaceBomb.Type.BONUS) {
					if (paddle.getGrapplingHook().collisionTest(b.getBoundingBox())) {
						
						b.setState(SpaceBomb.State.STUCK_TO_GRAPPLING_HOOK);
						
						Logger.log("Caught a Space Bomb!", 1);
					}
				//}
			}
		}
		
		// Consume caught space bombs if they are 
		// drawn in with the grappling hook
		for (SpaceBomb b : spaceBombs) {
			if (b.getState() == SpaceBomb.State.STUCK_TO_GRAPPLING_HOOK) {
				if (Util.collisionTest(paddle.getBoundingBox(), b.getBoundingBox())) {
					GameState.incrementSpaceBombsLeft();
					b.setDestroyed(true);
				}
			}
		}
	}

	private void brickHit(Brick b, Ball ball, boolean hitByProjectile) {
		b.hit();
		
		if (b.isDestroyed()) {
			addParticleEffect(new Point(b.getBoundingBox().getCenterX(), b.getBoundingBox().getCenterY()), 
							  ParticleEffect.BRICK_EXPLOSION);
		}
		
		if (hitByProjectile) {
			if (b.getType() != Brick.Type.SOLID) {
				GameState.changeScore(100);
			}
			
			if (b.getType() == Brick.Type.POWERUP) {
				GameState.changeScore(1000);			
				spawnPowerup(b.getPosition(), Effect.Type.values()[Util.random(0, Effect.Type.values().length - 1)]);
			}
		}
		else {
			// Reflect the ball?			
			if (!EffectManager.getInstance().isEffectActive(Effect.Type.FIREBALL) &&
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
				GameState.changeScore(100);		
			}
			
			if (b.getType() == Brick.Type.POWERUP) {
				GameState.changeScore(1000);			
				spawnPowerup(b.getPosition(), Effect.Type.values()[Util.random(0, Effect.Type.values().length - 1)]);
			}
		}
	}

	public void spawnBall(boolean isMultiball) {
		balls.add(new Ball(new Point((Config.getInstance().getScreenWidth() - Config.SCOREBOARD_WIDTH) / 2, 
									  Config.getInstance().getScreenHeight() / 2), isMultiball));
	}
	
	private void ballLost(Ball ball, Iterator<Ball> bi) {
		if (!ball.isMultiball()) {
			GameState.decrementBallsLeft();
			GameState.changeScore(-1000);
		}
		
		bi.remove();

		if (balls.isEmpty()) {
			paddle.getGrapplingHook().resetState();
			
			EffectManager.getInstance().clearEffects();
			
			setState(State.WAITING_FOR_BALL);
		}			
		
		SoundLayer.playSound(Sounds.BALL_LOST);
		
		if (GameState.getBallsLeft() <= 0) {
			setState(State.GAME_OVER);
		}
	}
	
	private <T extends Destroyable> void cleanupList(List<T> list) {
		Iterator<T> i = list.iterator();		
		while (i.hasNext()) {
			T t = i.next();
			
			if (t.isDestroyed()) {
				i.remove();
			}
		}
	}
	
	public void doCleanup() {
		cleanupList(balls);
		cleanupList(bricks);
		cleanupList(stars);
		cleanupList(backgrounds);
		cleanupList(powerups);
		cleanupList(projectiles);
		cleanupList(spaceBombs);
		cleanupList(stars);
		cleanupList(particleEffects);
	}
	
	public void spawnPowerup(Point position, Effect.Type effectType) {
		Logger.log("Spawned powerup: " + effectType, 1);
		
		powerups.add(new Powerup(position, effectType));
				
		SoundLayer.playSound(Sounds.POWERUP_SPAWNED);
	}
	
	public void addTextAnimation(String text) {		
		TextAnimationManager.getInstance().add(text);
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
			GameState.setLevel(0);
			GameState.setScore(0);
			GameState.setBallsLeft(Config.INITIAL_BALLS_LEFT);
			GameState.setSpaceBombsLeft(Config.INITIAL_SPACEBOMBS_LEFT);
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
	
	private void addParticleEffect(Point position, ParticleEffect effect) {		
		switch (effect) {
		case BRICK_EXPLOSION:		
			particleEffects.add(new ParticleSystem(new SpriteTuple[]{new SpriteTuple("sprites/fire.png", 198.0f, 197.0f, 198, 197)}, 
					position, 10.0f, 15.0f, 0.0f, 360.0f, 0.0f, 15.0f, 155.0f, 4.5f));
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
