package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Particle extends GameObject {
	private float x,y, speed;
	private final float width = Config.PARTICLE_WIDTH, height = Config.PARTICLE_HEIGHT;
	
	public Particle(float x, float y, float speed) {
		this.x = x;
		this.y = y;
		
		this.speed = speed;
	}
	
	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
			GL11.glVertex2f(x, y);			
			GL11.glVertex2f(x + width, y);			
			GL11.glVertex2f(x + width, y + height);			
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}

	@Override
	public void step() {
		y += speed;
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, 1, 1);
	}

	public float getY() {
		return y;
	}

}
