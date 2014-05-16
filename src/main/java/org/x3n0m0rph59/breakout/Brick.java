package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;
import org.x3n0m0rph59.breakout.SoundLayer;

public class Brick extends GameObject {	
	public enum Type {NORMAL, WEAK, HARD, SOLID, POWERUP};
	public enum Behaviour {NORMAL, MOVE_LEFT, MOVE_RIGHT};

	private float x, y, width, height;
	private boolean destroyed = false;
	
	private Type type;
	private Behaviour behaviour;
	private float speed;
	
	private int hitCounter = 0;
		
	public Brick(Type type, Behaviour behaviour, float speed, 
				 float x, float y, float width, float height) {
		this.type = type;
		this.behaviour = behaviour;
		
		this.speed = speed;
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void render() {
		// clamp moving bricks to the beginning and end of the column
		// so that they don't draw over into the offset space
		float x = this.x;
		float width = this.width;
		
		if (behaviour != Behaviour.NORMAL) {
			// left edge
			if (this.x <= 0 + Config.BRICK_OFFSET_X) {
				x = 0 + Config.BRICK_OFFSET_X;
				width -= (0 + Config.BRICK_OFFSET_X) - this.x; 
			}
				
			// right edge
			if (this.x + this.width >= Config.CLIENT_WIDTH - Config.BRICK_OFFSET_X) {
				width -= (this.x + this.width + Config.BRICK_SPACING_X) - (Config.CLIENT_WIDTH - Config.BRICK_OFFSET_X);
			}
		}
		
		
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
		switch (behaviour) {
		case MOVE_LEFT:
			x -= speed;
			
			if (x + width <= 0 + Config.BRICK_OFFSET_X)
				x = Config.CLIENT_WIDTH - Config.BRICK_OFFSET_X;
			break;
			
		case MOVE_RIGHT:
			x += speed;
			
			if (x >= Config.CLIENT_WIDTH - Config.BRICK_OFFSET_X)
				x = Config.BRICK_OFFSET_X - width;
			break;
			
		case NORMAL:
			// do nothing
			break;
		}
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
			if (hitCounter >= Config.HARD_BRICK_HITS_NEEDED)
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
