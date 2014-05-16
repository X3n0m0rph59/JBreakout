package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Powerup extends GameObject {
	private float x,y, width = Config.POWERUP_WIDTH, height = Config.POWERUP_HEIGHT;
	private EffectType type;
	private boolean destroyed = false;		

	public Powerup(float x, float y, EffectType type) {
		this.x = x;
		this.y = y;
		
		this.type = type;
	}
	
	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);

			switch (type) {
			case BOTTOM_WALL:
				GL11.glColor3f(0.0f, 0.0f, 0.0f);
				break;
			case ENLARGE_PADDLE:
				GL11.glColor3f(0.0f, 1.0f, 0.0f);
				break;
			case FIREBALL:
				GL11.glColor3f(1.0f, 0.0f, 0.0f);
				break;
			case MULTIBALL:
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				break;
			case PADDLE_GUN:
				GL11.glColor3f(1.0f, 0.5f, 0.5f);
				break;
			case SHRINK_PADDLE:
				GL11.glColor3f(0.40f, 0.40f, 1.0f);
				break;
			case STICKY_BALL:
				GL11.glColor3f(0.0f, 0.0f, 1.0f);
				break;
			case SPEED_UP:
				GL11.glColor3f(0.0f, 1.0f, 1.0f);
				break;			
			case SLOW_DOWN:
				GL11.glColor3f(0.0f, 1.0f, 0.5f);
				break;
				
			default:
				throw new RuntimeException("Unsupported EffectType: " + type);		
			}
					
			GL11.glVertex2f(x, y);			
			GL11.glVertex2f(x + width, y);			
			GL11.glVertex2f(x + width, y + height);			
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}

	@Override
	public void step() {
		y += 10f;
	}

	@Override
	public Rectangle getBoundingBox() {		
		return new Rectangle(x, y, width, height);
	}
	
	public EffectType getType() {
		return type;
	}

	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void setDestroyed(boolean destroyed) {
		this.destroyed  = true;
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
}
