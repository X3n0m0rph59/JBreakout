package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;
import org.x3n0m0rph59.breakout.SoundLayer.Sounds;

public class Brick extends GameObject {	
	public enum Type {NORMAL, WEAK, HARD, SOLID, POWERUP};
	public enum Action {REFLECT, PASS};
	
	private float x, y, width, height;
	private boolean destroyed = false;
	private Type type;
	private int hitCounter = 0;
		
	public Brick(Type type, float x, float y, float width, float height) {
		this.type = type;
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void render() {
		switch (type) {		
		case NORMAL:
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor3f(0.0f, 1.0f, 0.0f);			
				GL11.glVertex2f(x, y);
				GL11.glColor3f(0.0f, 1.0f, 0.0f);
				GL11.glVertex2f(x + width, y);
				GL11.glColor3f(0.0f, 1.0f, 0.0f);
				GL11.glVertex2f(x + width, y + height);
				GL11.glColor3f(0.0f, 1.0f, 0.0f);
				GL11.glVertex2f(x, y + height);
			GL11.glEnd();
			break;
			
		case WEAK:
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor3f(0.5f, 1.0f, 0.5f);			
				GL11.glVertex2f(x, y);
				GL11.glColor3f(0.5f, 1.0f, 0.5f);
				GL11.glVertex2f(x + width, y);
				GL11.glColor3f(0.5f, 1.0f, 0.5f);
				GL11.glVertex2f(x + width, y + height);
				GL11.glColor3f(0.5f, 1.0f, 0.5f);
				GL11.glVertex2f(x, y + height);
			GL11.glEnd();
			break;
			
		case HARD:
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor3f(1.0f, 0.0f, 0.0f);			
				GL11.glVertex2f(x, y);			
				GL11.glVertex2f(x + width, y);			
				GL11.glVertex2f(x + width, y + height);			
				GL11.glVertex2f(x, y + height);
			GL11.glEnd();
			break;
			
		case POWERUP:
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
			break;
			
		case SOLID:
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glColor3f(0.0f, 0.0f, 0.25f);			
				GL11.glVertex2f(x, y);			
				GL11.glVertex2f(x + width, y);			
				GL11.glVertex2f(x + width, y + height);			
				GL11.glVertex2f(x, y + height);
			GL11.glEnd();
			break;
			
		default:
			throw new RuntimeException("Unsupported brick type");			
		}
	}
	
	@Override
	public void step() {
		
	}
	
	@Override
	public Rectangle getBoundingBox() {		
		return new Rectangle(x, y, width, height);
	}
	
	public void hit() {
		switch (type) {
		case NORMAL:
			destroyed = true;
			SoundLayer.playSound(Sounds.BRICK_DESTROYED);
			break;
			
		case WEAK:
			destroyed = true;
			SoundLayer.playSound(Sounds.BRICK_DESTROYED);
			break;
			
		case HARD:
			hitCounter++;
			if (hitCounter >= 3)
			{
				SoundLayer.playSound(Sounds.BRICK_DESTROYED);
				destroyed = true;
			}
			else
				SoundLayer.playSound(Sounds.BRICK_HIT);
			break;
			
		case POWERUP:
			destroyed = true;
			break;
			
		case SOLID:
			// Solid bricks are indestructible
			SoundLayer.playSound(Sounds.SOLID_BRICK_HIT);
			break;
			
		default:
			throw new RuntimeException("Unsupported brick type");
		}
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
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
