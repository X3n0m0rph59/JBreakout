package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Ball extends GameObject {
	private float x,y,radius = 10;
	private float velX = 0, velY = 0;
	private float speed = 6.0f;
	
	private boolean destroyed = false; 
	
	public Ball(float x, float y) {
		this.x = x;
		this.y = y;
		
		this.velX = (float) Math.sin(-175) * speed;
		this.velY = (float) Math.sin(-175) * speed;
	}
	
	@Override
	public void render() {
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);			
			GL11.glColor3f(1.0f, 0.5f, 0.5f);			
			GL11.glVertex2f(x, y);
		  
			int numSegments = 100;
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
		x += velX;
		y += velY;
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, 15f, 15f);
	}
	
	public void setAngleOfReflection(float angle) {	
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
}
