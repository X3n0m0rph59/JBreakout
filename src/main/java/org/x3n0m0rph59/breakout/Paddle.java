package org.x3n0m0rph59.breakout;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Paddle extends GameObject {
	protected float x,y,width = 100,height = 20;
	
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
		setCenteredPosition(Mouse.getX(), Mouse.getY());
	}
	
	public void setCenteredPosition(float x, float y) {
		this.x = x - width / 2;		
		this.y = 700;
	}

	@Override
	public Rectangle getBoundingBox() {		
		return new Rectangle(x, y, width, height);
	}
}
