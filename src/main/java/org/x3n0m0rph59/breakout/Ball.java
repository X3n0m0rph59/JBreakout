package org.x3n0m0rph59.breakout;

//import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Ball extends GameObject {
	private float x,y,radius = Config.BALL_RADIUS;
	private float velX = 0, velY = 0;
	private float speed = Config.BALL_SPEED;
	
	public enum State {ROLLING, STUCK_TO_PADDLE}
	private State state = State.ROLLING;
	
	private boolean multiball = false;
	private boolean destroyed = false;
	
	private Sprite spriteNormalBall = new Sprite("data/sprites/ball.png", Config.BALL_RADIUS * 2, 
												Config.BALL_RADIUS * 2, 200, 200);
	private Sprite spriteFireBall = new Sprite("data/sprites/fireball.png", Config.BALL_RADIUS * 2, 
												Config.BALL_RADIUS * 2, 200, 200);
	
	private ParticleSystem trail = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("data/sprites/Star1.png", 255.0f, 255.0f, 255, 255), 
																	  new SpriteTuple("data/sprites/Star2.png", 345.0f, 342.0f, 345, 342), 
																	  new SpriteTuple("data/sprites/Star3.png", 270.0f, 261.0f, 270, 261), 
																	  new SpriteTuple("data/sprites/Star4.png", 264.0f, 285.0f, 264, 285)}, 
															x, y, 1.0f, 5.0f, 0.0f, 45.0f, 2.0f, 15.0f, 15.0f, 5.0f);
	
	private ParticleSystem fireBallTrail = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("data/sprites/fire.png", 198.0f, 197.0f, 198, 197)}, 
															x, y, 1.0f, 25.0f, 0.0f, 25.0f, 2.0f, 15.0f, 25.0f, 25.0f);
	
	
	public Ball(float x, float y) {
		this(x, y, false);
	}
	
	public Ball(float x, float y, boolean multiball) {
		this.x = x;
		this.y = y;
		
		this.multiball = multiball;
		
		final float angle = (float) Math.toRadians(45.0f);		
		this.velX = (float) Math.cos(angle) * speed;
		this.velY = (float) Math.sin(angle) * -speed;
	}
	
	@Override
	public void render() {		
		if (EffectManager.getInstance().isEffectActive(EffectType.FIREBALL)) {
			fireBallTrail.render();
			spriteFireBall.render(x, y);			
		} else {
			if (EffectManager.getInstance().isEffectActive(EffectType.STICKY_BALL))
				trail.render();
			
			spriteNormalBall.render(x, y);
		}
				
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
			x += velX * Config.getInstance().getSpeedFactor();
			y += velY * Config.getInstance().getSpeedFactor();
		}
		
		spriteNormalBall.step();
		spriteFireBall.step();
		
		updateTrailPosition();
		
		trail.step();
		fireBallTrail.step();
	}

	private void updateTrailPosition() {
		trail.setPosition(x, y, (float) Math.toRadians(getAngle() + 90.0f));
		fireBallTrail.setPosition(x, y, (float) Math.toRadians(getAngle() + 90.0f));
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, radius * 2, radius * 2);
	}
		
	public void invertXVelocity() {
		velX *= -1;
		
		updateTrailPosition();
	}

	public void invertYVelocity() {
		velY *= -1;
		
		updateTrailPosition();
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		
		updateTrailPosition();
	}

	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void setDestroyed(boolean b) {
		destroyed = b;
	}

	public float getVelX() {
		return velX;
	}
	
	public float getVelY() {
		return velY;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
		
		velX = (float) Math.cos(Math.toRadians(getAngle())) * speed;
		velY = (float) Math.sin(Math.toRadians(getAngle())) * -speed;
		
		updateTrailPosition();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void moveBy(float dX, float dY) {
		this.x += dX;
		this.y += dY;
		
		updateTrailPosition();
	}

	public void changeSpeed(float v) {
		speed *= v;
		
		final float angle = getAngle();
		velX = (float) Math.cos(Math.toRadians(angle)) * speed;
		velY = (float) Math.sin(Math.toRadians(angle)) * -speed;
		
		updateTrailPosition();
	}
	
	public void changeAngle(float delta) {
		float angle = getAngle();				
		angle += delta;		
		
		velX = (float) Math.cos(Math.toRadians(angle)) * speed;
		velY = (float) Math.sin(Math.toRadians(angle)) * -speed;
		
		updateTrailPosition();
	}

	public boolean isMultiball() {
		return multiball;
	}
	
	public void setAngle(float angle) {		
		velX = (float) Math.cos(Math.toRadians(angle)) * speed;
		velY = (float) Math.sin(Math.toRadians(angle)) * -speed;
		
		updateTrailPosition();
	}

	public float getAngle() {		
		return (float) Math.toDegrees(Math.atan2(-velY, velX));
	}

	public void reflect() {
		final float angle = getAngle() * -1;
		
		velX = (float) Math.cos(Math.toRadians(angle)) * speed;
		velY = (float) Math.sin(Math.toRadians(angle)) * -speed;
		
		updateTrailPosition();
	}
}
