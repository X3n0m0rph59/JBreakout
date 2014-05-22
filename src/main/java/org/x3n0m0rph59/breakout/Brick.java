package org.x3n0m0rph59.breakout;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;


//import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;
import org.x3n0m0rph59.breakout.SoundLayer;

public class Brick extends GameObject {	
	public enum Type {NORMAL, WEAK, HARD, SOLID, POWERUP};
	public enum Behavior {MOVE_LEFT, MOVE_RIGHT, ROTATE_LEFT, ROTATE_RIGHT};

	private float x, y, width, height;
	
	private boolean destroyed = false;
	
	private Type type;
	private EnumSet<Behavior> behavior;
	private float speed;
	
	private float angle;
	private float angularVelocity = 1.0f;
	
	private int hitCounter = 0;
	
	private Map<Type, Sprite> sprites = new EnumMap<Type, Sprite>(Type.class);
		
	public Brick(Type type, EnumSet<Behavior> behavior, float speed, 
				 float x, float y, float width, float height) {
		this.type = type;
		this.behavior = behavior;
		
		this.speed = speed;
		
		this.angle = 0.0f;
		
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		sprites.put(Type.NORMAL, new Sprite("sprites/brick_normal.png", 255, 159, 255, 159));
		sprites.put(Type.WEAK, new Sprite("sprites/brick_weak.png", 255, 159, 255, 159));
		sprites.put(Type.HARD, new Sprite("sprites/brick_hard.png", 255, 159, 255, 159));
		sprites.put(Type.SOLID, new Sprite("sprites/brick_solid.png", 255, 159, 255, 159));
		sprites.put(Type.POWERUP, new Sprite("sprites/brick_powerup.png", 255, 159, 255, 159));
	}
	
	@Override
	public void render() {
		// clamp moving bricks to the beginning and end of the column
		// so that they don't draw over into the offset space
		float x = this.x;
		float width = this.width;
		
		if (behavior.contains(Behavior.MOVE_LEFT) || behavior.contains(Behavior.MOVE_RIGHT)) {
			// left edge
			if (this.x <= 0 + Config.BRICK_OFFSET_X) {
				x = 0 + Config.BRICK_OFFSET_X;
				width -= (0 + Config.BRICK_OFFSET_X) - this.x; 
			}
				
			// right edge
			if (this.x + this.width >= Config.getInstance().getClientWidth() - Config.BRICK_OFFSET_X) {
				width -= (this.x + this.width + Config.BRICK_SPACING_X) - 
						 (Config.getInstance().getClientWidth() - Config.BRICK_OFFSET_X);
			}
		}
		
		
		Sprite sprite = sprites.get(type);
		if (sprite != null) {
			sprite.setAngle(angle);
			sprite.render(x, y, width, height);
		}
			
		
		
//		switch (type) {		
//		case NORMAL:
//			GL11.glBegin(GL11.GL_QUADS);
//				GL11.glColor3f(0.0f, 1.0f, 0.0f);
//				GL11.glVertex2f(x, y);
//				GL11.glColor3f(0.0f, 1.0f, 0.0f);
//				GL11.glVertex2f(x + width, y);
//				GL11.glColor3f(0.0f, 1.0f, 0.0f);
//				GL11.glVertex2f(x + width, y + height);
//				GL11.glColor3f(0.0f, 1.0f, 0.0f);
//				GL11.glVertex2f(x, y + height);
//			GL11.glEnd();
//			break;
//			
//		case WEAK:
//			GL11.glBegin(GL11.GL_QUADS);
//				GL11.glColor3f(0.5f, 1.0f, 0.5f);			
//				GL11.glVertex2f(x, y);
//				GL11.glColor3f(0.5f, 1.0f, 0.5f);
//				GL11.glVertex2f(x + width, y);
//				GL11.glColor3f(0.5f, 1.0f, 0.5f);
//				GL11.glVertex2f(x + width, y + height);
//				GL11.glColor3f(0.5f, 1.0f, 0.5f);
//				GL11.glVertex2f(x, y + height);
//			GL11.glEnd();
//			break;
//			
//		case HARD:
//			GL11.glBegin(GL11.GL_QUADS);
//				GL11.glColor3f(1.0f, 0.0f, 0.0f);			
//				GL11.glVertex2f(x, y);			
//				GL11.glVertex2f(x + width, y);			
//				GL11.glVertex2f(x + width, y + height);			
//				GL11.glVertex2f(x, y + height);
//			GL11.glEnd();
//			break;
//			
//		case POWERUP:
//			GL11.glBegin(GL11.GL_QUADS);
//				GL11.glColor3f(1.0f, 0.0f, 0.0f);			
//				GL11.glVertex2f(x, y);
//				GL11.glColor3f(0.0f, 1.0f, 0.0f);
//				GL11.glVertex2f(x + width, y);
//				GL11.glColor3f(0.0f, 0.0f, 1.0f);
//				GL11.glVertex2f(x + width, y + height);
//				GL11.glColor3f(1.0f, 1.0f, 0.0f);
//				GL11.glVertex2f(x, y + height);
//			GL11.glEnd();
//			break;
//			
//		case SOLID:
//			GL11.glBegin(GL11.GL_QUADS);
//				GL11.glColor3f(0.0f, 0.0f, 0.25f);			
//				GL11.glVertex2f(x, y);			
//				GL11.glVertex2f(x + width, y);			
//				GL11.glVertex2f(x + width, y + height);			
//				GL11.glVertex2f(x, y + height);
//			GL11.glEnd();
//			break;
//			
//		default:
//			throw new RuntimeException("Unsupported brick type");			
//		}
	}
	
	@Override
	public void step() {
		if(behavior.contains(Behavior.MOVE_LEFT)) {
			x -= speed * Config.getInstance().getSpeedFactor();
			
			if (x + width <= 0 + Config.BRICK_OFFSET_X)
				x = Config.getInstance().getClientWidth() - Config.BRICK_OFFSET_X;
		} else if(behavior.contains(Behavior.MOVE_RIGHT)) {
			x += speed * Config.getInstance().getSpeedFactor();
			
			if (x >= Config.getInstance().getClientWidth() - Config.BRICK_OFFSET_X)
				x = Config.BRICK_OFFSET_X - width;
		}
			
		if(behavior.contains(Behavior.ROTATE_LEFT)) {
			angle -= angularVelocity * speed * Config.getInstance().getSpeedFactor();
			
		} else if(behavior.contains(Behavior.ROTATE_RIGHT)) {
			angle += angularVelocity * speed * Config.getInstance().getSpeedFactor();
		}
		
		
		for (Sprite s : sprites.values())
			s.step();
	}
	
	@Override
	public Rectangle getBoundingBox() {
		final Transform t = Transform.createRotateTransform(angle, x + width / 2, y + height / 2);
		final Shape boundingShape = new Rectangle(x, y, width, height).transform(t);
		final Rectangle boundingBox = Util.getBoundingBoxFromShape(boundingShape);
		
		return boundingBox;
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
