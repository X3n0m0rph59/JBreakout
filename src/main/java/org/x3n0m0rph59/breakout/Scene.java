package org.x3n0m0rph59.breakout;

import org.x3n0m0rph59.breakout.SoundLayer;

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
	public enum State {LOADING, GREETING, WAITING_FOR_BALL, RUNNING, STAGE_CLEARED, RESTART, PAUSED, GAME_OVER, TERMINATED};
	private State state = State.LOADING;
	
	private ScoreBoard scoreBoard = new ScoreBoard();	
	
	private int level = 0;
	private int ballsLeft = Config.INITIAL_BALLS_LEFT;
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
		font = FontLoader.getInstance().getFont("Verdana", Font.BOLD, Config.TOAST_FONT_SIZE);
		
		initLevel(0);						
	}
	
	private void initLevel(int level) {		
		EffectManager.getInstance().clearEffects();
		
		balls.clear();
		balls.add(new Ball(Config.BALL_SPAWN_X, Config.BALL_SPAWN_Y));
		
		bricks = LevelLoader.loadLevel(level);
		
		powerups.clear();
		projectiles.clear();
		textAnimations.clear();
				
		// Spawn initial set of particles
		particles.clear();
		for (int i = 0; i < Config.SYNC_FPS * Config.PARTICLE_DENSITY ; i++) {
			particles.add(new Particle(Util.random(0, (int) Config.CLIENT_WIDTH), 
									   Util.random(0, (int) Config.SCREEN_HEIGHT), 
									   Util.random((int) Config.PARTICLE_MIN_SPEED, 
											   	   (int) Config.PARTICLE_MAX_SPEED)));
		}
	}
	
	private void drawCenteredText(String[] lines, boolean eraseBackground) {
		if (eraseBackground)
		{			
			GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);
			GL11.glEnable(GL11.GL_BLEND);
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.25f);
				GL11.glVertex2f(0, 0);			
				GL11.glVertex2f(Config.CLIENT_WIDTH, 0);			
				GL11.glVertex2f(Config.CLIENT_WIDTH, Config.SCREEN_HEIGHT);			
				GL11.glVertex2f(0, Config.SCREEN_HEIGHT);
			GL11.glEnd();
		}
								
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		
		int cnt = 0;
		for (String line : lines) {
			int width = font.getWidth(line);
			int height = font.getLineHeight() + 5;
			
			font.drawString(Config.CLIENT_WIDTH / 2 - width / 2, 
							(height * cnt) + Config.SCREEN_HEIGHT / 2, 
							line, Color.white);
			cnt++;
		}
		
		GL11.glDisable(GL11.GL_BLEND);
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
		if (EffectManager.getInstance().isEffectActive(EffectType.BOTTOM_WALL)) {
			drawBottomWall();
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
			
		case GREETING:
			drawCenteredText(new String[] {"Press mouse button to start"}, true);
			break;
			
		case RUNNING:			
			break;
			
		case RESTART:
			drawCenteredText(new String[] {"Restarting stage"}, true);
			break;
			
		case PAUSED:
			drawCenteredText(new String[] {"GAME PAUSED", "Press mouse button to resume"}, true);
			break;
			
		case WAITING_FOR_BALL:
			drawCenteredText(new String[] {"BALL LOST", "Press mouse button for a new ball"}, true);
			break;
			
		case STAGE_CLEARED:
			drawCenteredText(new String[] {"Stage " + (level + 1) + " cleared", "Press mouse button to continue"}, true);
			break;
			
		case GAME_OVER:
			drawCenteredText(new String[] {"GAME OVER!", "Press F2 to restart or Q to quit"}, true);
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
	}

	private void drawBottomWall() {
		for (int i = 0; i <= Config.CLIENT_WIDTH / (Config.BOTTOM_WALL_SEGMENT_WIDTH + Config.BOTTOM_WALL_SEGMENT_SPACING); i++) {
			float x = i * (Config.BOTTOM_WALL_SEGMENT_WIDTH + Config.BOTTOM_WALL_SEGMENT_SPACING);
			float y = Config.SCREEN_HEIGHT - Config.BOTTOM_WALL_HEIGHT;
			
			final float width = Config.BOTTOM_WALL_SEGMENT_WIDTH;
			final float height = Config.BOTTOM_WALL_SEGMENT_HEIGHT;
			
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
//		GL11.glBegin(GL11.GL_QUADS);
//			GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.8f);
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
				
		processKeyboardEvents();
		
		switch (state) {
		case LOADING:
			break;
			
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

//			for (TextAnimation a : textAnimations) {
//				a.step();
//			}
			
			if (textAnimations.size() > 0)
				textAnimations.get(0).step();
			
			
			for (Ball b : balls) {
				b.step();
			}
			
			paddle.step();
			
			doCollisionDetection();
			doCleanup();
			
			// Spawn new particles
			for (int i = 0; i < Config.PARTICLE_DENSITY; i++) {
				particles.add(new Particle(Util.random(0, (int) Config.CLIENT_WIDTH), 0f, 
													   Util.random((int) Config.PARTICLE_MIN_SPEED, 
															       (int) Config.PARTICLE_MAX_SPEED)));
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
					if ((Mouse.getX() + mdx >= 0) && (Mouse.getX() + mdx <= Config.CLIENT_WIDTH)) {						
						ball.moveBy(mdx, 0);
					}
					
					if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
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
			setState(State.GREETING);
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
			if ((ball.getBoundingBox().getX() + ball.getBoundingBox().getWidth()) >= Config.CLIENT_WIDTH) {
				final float newX = Config.CLIENT_WIDTH - ball.getBoundingBox().getWidth();				
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
					ball.getBoundingBox().getX() >= Config.CLIENT_WIDTH - 
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
					 ball.getBoundingBox().getY() >= (Config.SCREEN_HEIGHT - Config.BOTTOM_WALL_HEIGHT) - 
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
			if (ball.getBoundingBox().getY() >= Config.SCREEN_HEIGHT && 
				!EffectManager.getInstance().isEffectActive(EffectType.BOTTOM_WALL)) {
				ballLost(ball, bi);
			}
		}
		
		// Ball vs. Paddle
		for (Ball ball : balls) {
			if (ball.getState() != Ball.State.STUCK_TO_PADDLE) {
				if (Util.collisionTest(paddle.getBoundingBox(), ball.getBoundingBox())) {
					float angle; 
					
					switch (Util.getCollisionEdge(ball.getBoundingBox(), paddle.getBoundingBox())) {
					case LEFT:
						angle = ball.getAngle() * -1;
						ball.setAngleOfReflection(angle);
						break;
						
					case TOP_LEFT:
						angle = ball.getAngle() * -1;
						ball.setAngleOfReflection(angle);
						break;
						
					case TOP:
						// TODO: calculate fractional angle of reflection 
						// 		 based on paddle surface impact point to 
						// 		 simulate a curved surface
						final float ballX = ball.getBoundingBox().getCenterX();												
						final float paddleCenter = paddle.getBoundingBox().getCenterX();						
						final float deviationFromCenter = ballX - paddleCenter;						
						final float fractionalAngle = deviationFromCenter * 0.25f;
									
						angle = ball.getAngle() + (360 - Math.abs(ball.getAngle()));						
						
						Logger.log("Deviation from center: " + deviationFromCenter +
								   " Fractional angle: " + fractionalAngle + 
								   " angle: " + ball.getAngle() +  
								   " final angle: " + angle);
						
						ball.setAngleOfReflection(angle);												
						break;
						
					case TOP_RIGHT:
						angle = ball.getAngle() * -1;
						ball.setAngleOfReflection(angle);
						break;
						
					case RIGHT:
						angle = ball.getAngle() * -1;
						ball.setAngleOfReflection(angle);
						break;									
						
					case BOTTOM_RIGHT:
						angle = ball.getAngle() * -1;
						ball.setAngleOfReflection(angle);
						break;
						
					case BOTTOM:
						angle = ball.getAngle() * -1;
						ball.setAngleOfReflection(angle);
						break;
						
					case BOTTOM_LEFT:
						angle = ball.getAngle() * -1;
						ball.setAngleOfReflection(angle);
						break;
	
					default:
						throw new RuntimeException("Invalid egde type");				
					}
					
					
					// Compute ball speed change due to paddle velocity
					// Parameters for linear conversion:
					final float minBallSpeedDelta = 1.0f; 
					final float maxBallSpeedDelta = 1.5f;
					
					final float minPaddleDeltaX =  0.0f;
					final float maxPaddleDeltaX = 10.0f;
					
					float velD = ((Math.abs(pdx) - minPaddleDeltaX) * (maxBallSpeedDelta - minBallSpeedDelta) / 
									(maxPaddleDeltaX - minPaddleDeltaX)) + minBallSpeedDelta;
					ball.changeSpeed(velD);
					
					
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
		balls.add(new Ball(Config.BALL_SPAWN_X, Config.BALL_SPAWN_Y, isMultiball));
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
				
		// Remove excess particles
		Iterator<Particle> pi = particles.iterator();		
		while (pi.hasNext()) {
			Particle p = pi.next();
			
			if (p.getY() >= Config.SCREEN_HEIGHT) {
				pi.remove();
			}
		}
		
		// Remove excess and/or consumed powerups
		Iterator<Powerup> pui = powerups.iterator();		
		while (pui.hasNext()) {
			Powerup p = pui.next();
			
			if (p.isDestroyed() || p.getY() >= Config.SCREEN_HEIGHT) {
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
		case GREETING:
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
}
