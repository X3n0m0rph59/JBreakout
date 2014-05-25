package org.x3n0m0rph59.breakout;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import org.x3n0m0rph59.breakout.SoundLayer;

public class Brick extends GameObject {	
	public enum Type {NORMAL, WEAK, HARD, SOLID, POWERUP};
	public enum Behavior {MOVE_LEFT, MOVE_RIGHT, ROTATE_LEFT, ROTATE_RIGHT};
	
	private Type type;
	private EnumSet<Behavior> behavior;
	private float speed;
	
	private int hitCounter = 0;
	
	private Map<Type, Sprite> sprites = new EnumMap<Type, Sprite>(Type.class);
		
	public Brick(Type type, EnumSet<Behavior> behavior, float speed, float angularVelocity, 
				 Point position, float width, float height) {
		
		super(null, position, width, height, 0.0f, angularVelocity, 0.0f, 0.0f);
		
		this.type = type;
		this.behavior = behavior;
		
		this.speed = speed;
		
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
		float x = this.getX();
		float width = this.getWidth();
		
		if (behavior.contains(Behavior.MOVE_LEFT) || behavior.contains(Behavior.MOVE_RIGHT)) {
			// left edge
			if (this.getX() <= 0 + Config.BRICK_OFFSET_X) {
				x = 0 + Config.BRICK_OFFSET_X;
				width -= (0 + Config.BRICK_OFFSET_X) - this.getX(); 
			}
				
			// right edge
			if (this.getX() + this.getWidth() >= Config.getInstance().getClientWidth() - Config.BRICK_OFFSET_X) {
				width -= (this.getX() + this.getWidth() + Config.BRICK_SPACING_X) - 
						 (Config.getInstance().getClientWidth() - Config.BRICK_OFFSET_X);
			}
		}
		
		
		Sprite sprite = sprites.get(type);
		if (sprite != null) {
			sprite.setAngle(angleInDegrees);
			sprite.setCenterOfRotation(new Point((sprite.getWidth() - 1) / 16, (sprite.getHeight() - 1) / 16));
			sprite.render(new Point(x, getY()), width, height);
		}
	}
	
	@Override
	public void step() {
		if(behavior.contains(Behavior.MOVE_LEFT)) {
			setPosition(new Point(getX() - speed * Config.getInstance().getSpeedFactor(), getY()));
			
			if (getX() + getWidth() <= 0 + Config.BRICK_OFFSET_X)
				setPosition(new Point(Config.getInstance().getClientWidth() - Config.BRICK_OFFSET_X, getY()));
			
		} else if(behavior.contains(Behavior.MOVE_RIGHT)) {
			setPosition(new Point(getX() + speed * Config.getInstance().getSpeedFactor(), getY()));
			
			if (getX() >= Config.getInstance().getClientWidth() - Config.BRICK_OFFSET_X)
				setPosition(new Point(Config.BRICK_OFFSET_X - width, getY()));
		}
			
		if(behavior.contains(Behavior.ROTATE_LEFT)) {
			setAngularVelocity(getAngularVelocity() * speed * Config.getInstance().getSpeedFactor());
			setAngleInDegrees(getAngleInDegrees() - angularVelocity);
			
		} else if(behavior.contains(Behavior.ROTATE_RIGHT)) {
			setAngularVelocity(getAngularVelocity() * speed * Config.getInstance().getSpeedFactor());
			setAngleInDegrees(getAngleInDegrees() + angularVelocity);
		}
		
		
		for (Sprite s : sprites.values())
			s.step();
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
			
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
