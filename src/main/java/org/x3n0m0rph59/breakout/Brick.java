package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Brick extends GameObject {
	private float x, y, width, height;
	private boolean destroyed = false;
	
	public Brick(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1.0f, 0.0f, 0.0f);			
			GL11.glVertex2f(x, y);
			GL11.glColor3f(0.0f, 1.0f, 0.0f);
			GL11.glVertex2f(x + width, y);
			GL11.glColor3f(0.0f, 0.0f, 1.0f);
			GL11.glVertex2f(x + width, y + height);
			GL11.glColor3f(1.0f, 1.0f, 0.0f);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	
	@Override
	public void step() {
		
	}
	
	public void explode() {
		destroyed = true;		
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

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public Rectangle getBoundingBox() {		
		return new Rectangle(x, y, width, height);
	}
}
