package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Ball extends GameObject {
	private float x,y,radius = 10;
	private float velX = 1.5f, velY = 5.5f;
	
	@Override
	public void render() {
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);			
			GL11.glColor3f(1.0f, 0.5f, 0.5f);			
			GL11.glVertex2f(x, y);
		  
			int numSegments = 100;
			float angle;
		  
			for (int i = 0; i <= numSegments; i++) { // Last vertex same as first vertex
				angle = (float) (i * 2.0f * Math.PI / numSegments);  // 360 deg for all segments
				GL11.glVertex2f((float) (Math.cos(angle) * radius) + x, (float) (Math.sin(angle) * radius) + y);
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
}
