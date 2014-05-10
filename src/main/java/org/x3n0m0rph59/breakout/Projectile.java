package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Projectile extends GameObject {
	private float x,y;
	private final float width = 5.0f, height = 10.0f;
	
	private boolean destroyed = false;
	private int frameCounter = 0;
	
	public Projectile(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1.0f, 0.25f, 0.25f);
			GL11.glVertex2f(x, y);			
			GL11.glVertex2f(x + width, y);			
			GL11.glVertex2f(x + width, y + height);			
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}

	@Override
	public void step() {
		frameCounter++;
		
		y -= 10.0f;
	}

	@Override
	public Rectangle getBoundingBox() {		
		return new Rectangle(x, y, width, height);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

}