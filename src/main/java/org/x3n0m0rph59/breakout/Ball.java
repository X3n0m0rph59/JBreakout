package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Ball extends GameObject {
	private float x,y,radius = Config.BALL_RADIUS;
	private float velX = 0, velY = 0, angle = 0;
	private float speed = Config.BALL_SPEED;
	
	public enum State { ROLLING, STUCK_TO_PADDLE }
	private State state = State.ROLLING;
	
	private boolean multiball = false;
	private boolean destroyed = false;
	
	public Ball(float x, float y) {
		this(x, y, false);
	}
	
	public Ball(float x, float y, boolean multiball) {
		this.x = x;
		this.y = y;
		
		this.multiball = multiball;
		
		this.angle = -(180.0f + 45.0f);
		
		this.velX = (float) Math.sin(angle) * speed;
		this.velY = (float) Math.cos(angle) * speed;
	}
	
	@Override
	public void render() {
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			if (EffectManager.getInstance().isEffectActive(EffectType.FIREBALL))
				GL11.glColor4f(1.0f, 0.15f, 0.15f, 1.0f);
			else
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			
			GL11.glVertex2f(x, y);
		  
			int numSegments = 16;
			float angle;
		  
			for (int i = 0; i <= numSegments; i++) {
				angle = (float) (i * 2.0f * Math.PI / numSegments);
				GL11.glVertex2f((float) (Math.cos(angle) * radius) + x, 
								(float) (Math.sin(angle) * radius) + y);
			}
		GL11.glEnd();		   
	}

	@Override
	public void step() {		
		if (state != State.STUCK_TO_PADDLE) {
			x += velX * Config.getInstance().getSpeedFactor();
			y += velY * Config.getInstance().getSpeedFactor();
		}
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, radius, radius);
	}
	
	public void setAngleOfReflection(float angle) {
		this.angle = angle;
		
		velX = (float) Math.sin(angle) * speed;
		velY = (float) Math.cos(angle) * speed;
	}
	
	public void invertXVelocity() {
		velX *= -1;
	}

	public void invertYVelocity() {
		velY *= -1;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void moveBy(float dX, float dY) {
		this.x += dX;
		this.y += dY;
	}

	public void changeSpeed(float v) {
		speed *= v;
		
		velX = (float) Math.sin(angle) * speed;
		velY = (float) Math.cos(angle) * speed;
	}

	public boolean isMultiball() {
		return multiball;
	}

	public float getAngle() {
		return angle;
	}
}
