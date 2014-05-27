package org.x3n0m0rph59.breakout;

import org.newdawn.slick.geom.Rectangle;

public class Ball extends GameObject {
	private static final float throwAngle = -45.0f;
	
	private float speed = 1.0f;
	
	boolean drawFlash = false;
	
	public enum State {ROLLING, STUCK_TO_PADDLE}
	private State state = State.ROLLING;
	
	private boolean multiball = false;
		
	private final Sprite spriteNormalBall = new Sprite("sprites/ball.png", Config.BALL_RADIUS * 2, 
												Config.BALL_RADIUS * 2, 200, 200);
	private final Sprite spriteFireBall = new Sprite("sprites/fireball.png", Config.BALL_RADIUS * 2, 
												Config.BALL_RADIUS * 2, 200, 200);
	
	private final ParticleSystem trail = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("sprites/Star1.png", 255.0f, 255.0f, 255, 255), 
																	  new SpriteTuple("sprites/Star2.png", 345.0f, 342.0f, 345, 342), 
																	  new SpriteTuple("sprites/Star3.png", 270.0f, 261.0f, 270, 261), 
																	  new SpriteTuple("sprites/Star4.png", 264.0f, 285.0f, 264, 285)}, 
															new Point(0.0f, 0.0f), -1.0f, 5.0f, 0.0f, 45.0f, 2.0f, 15.0f, 15.0f, 5.0f);
	
	private final ParticleSystem fireBallTrail = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("sprites/fire.png", 198.0f, 197.0f, 198, 197)}, 
															new Point(0.0f, 0.0f), -1.0f, 25.0f, 0.0f, 25.0f, 2.0f, 15.0f, 25.0f, 25.0f);
	
	
	public Ball(Point position) {
		this(position, false);
	}
	
	public Ball(Point position, boolean multiball) {
		super(null, position, Config.BALL_RADIUS * 2, Config.BALL_RADIUS * 2, 
			  throwAngle, 0.0f, 
			  (float) Math.cos(Math.toRadians(throwAngle)) * +Config.BALL_SPEED, 
			  (float) Math.sin(Math.toRadians(throwAngle)) * -Config.BALL_SPEED);
		
		this.multiball = multiball;
	}
	
	@Override
	public void render() {		
		if (EffectManager.getInstance().isEffectActive(Effect.Type.FIREBALL)) {
			
			fireBallTrail.render();			
			setSprite(spriteFireBall);							
		} else {
			if (EffectManager.getInstance().isEffectActive(Effect.Type.STICKY_BALL))
				trail.render();
			
			setSprite(spriteNormalBall);
		}
		
		final boolean inGracePeriod = EffectManager.getInstance().isEffectInGracePeriod(Effect.Type.STICKY_BALL) || 
				  					  EffectManager.getInstance().isEffectInGracePeriod(Effect.Type.FIREBALL);

		if (inGracePeriod && drawFlash)
			getSprite().setFlashed(true);
		else
			getSprite().setFlashed(false);
		
		super.render();
				
//		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
//			if (EffectManager.getInstance().isEffectActive(EffectType.FIREBALL))
//				GL11.glColor4f(1.0f, 0.15f, 0.15f, 1.0f);
//			else
//				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//			
//			GL11.glVertex2f(x, y);
//		  
//			int numSegments = 16;
//			float angle;
//		  
//			for (int i = 0; i <= numSegments; i++) {
//				angle = (float) (i * 2.0f * Math.PI / numSegments);
//				GL11.glVertex2f((float) (Math.cos(angle) * radius) + x, 
//								(float) (Math.sin(angle) * radius) + y);
//			}
//		GL11.glEnd();
	}

	@Override
	public void step() {
		if (state != State.STUCK_TO_PADDLE) {						
			super.step();	
		}
		
		spriteNormalBall.step();
		spriteFireBall.step();
		
		updateTrailPosition();

		trail.step();
		fireBallTrail.step();
		
		if ((frameCounter % (Config.SYNC_FPS * Config.GRACE_PERIOD_BLINK_RATE)) == 0)
			drawFlash = !drawFlash;
	}

	private void updateTrailPosition() {
		trail.setPositionAndAngle(this.getPosition(), getMovementAngleInDegrees());
		fireBallTrail.setPositionAndAngle(this.getPosition(), getMovementAngleInDegrees());
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(position.getX(), position.getY(), Config.BALL_RADIUS, Config.BALL_RADIUS);
	}
		
	public void invertXVelocity() {
		setDeltaX(getDeltaX() * -1);
		
		updateTrailPosition();
	}

	public void invertYVelocity() {
		setDeltaY(getDeltaY() * -1);
		
		updateTrailPosition();
	}

	@Override
	public void setPosition(Point position) {
		super.setPosition(position);
		
		updateTrailPosition();
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
		
		setDeltaX((float) Math.cos(Math.toRadians(getMovementAngleInDegrees())) * speed);
		setDeltaY((float) Math.sin(Math.toRadians(getMovementAngleInDegrees())) * -speed);
		
		updateTrailPosition();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public void changePosition(float deltaX, float deltaY) {
		super.changePosition(deltaX, deltaY);
		
		updateTrailPosition();
	}
	
	public void changeMovementAngle(float delta) {
		setMovementAngle(getMovementAngleInDegrees() + delta);		
		
		setDeltaX((float) Math.cos(Math.toRadians(getMovementAngleInDegrees())) * speed);
		setDeltaY((float) Math.sin(Math.toRadians(getMovementAngleInDegrees())) * -speed);
		
		updateTrailPosition();
	}

	public boolean isMultiball() {
		return multiball;
	}
	
	
	public void setMovementAngle(float movementAngleInDegrees) {
		setDeltaX((float) Math.cos(Math.toRadians(getMovementAngleInDegrees())) * speed);
		setDeltaY((float) Math.sin(Math.toRadians(getMovementAngleInDegrees())) * -speed);
		
		updateTrailPosition();
	}

	public void reflect() {
		final float movementAngleInDegrees = getMovementAngleInDegrees() * -1;
		
		setDeltaX((float) Math.cos(Math.toRadians(movementAngleInDegrees)) * speed);
		setDeltaY((float) Math.sin(Math.toRadians(movementAngleInDegrees)) * -speed);
		
		updateTrailPosition();
	}
	
	public float getMovementAngleInDegrees() {
		return (float) Math.toDegrees(Math.atan2(-getY(), getX()));
	}
}
