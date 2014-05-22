package org.x3n0m0rph59.breakout;

import java.util.EnumMap;
import java.util.Map;

//import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;
//import org.x3n0m0rph59.breakout.Brick.Type;

public class Powerup extends GameObject {
	private float x, y, width = Config.POWERUP_WIDTH, height = Config.POWERUP_HEIGHT;
	private EffectType type;
	private boolean destroyed = false;
	
	private Map<EffectType, Sprite> sprites = new EnumMap<>(EffectType.class);
	
	private ParticleSystem trail = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("sprites/Star1.png", 255.0f, 255.0f, 255, 255), 
																	  new SpriteTuple("sprites/Star2.png", 345.0f, 342.0f, 345, 342), 
																	  new SpriteTuple("sprites/Star3.png", 270.0f, 261.0f, 270, 261), 
																	  new SpriteTuple("sprites/Star4.png", 264.0f, 285.0f, 264, 285)}, 
														x, y, 0.0f, 10.0f, 0.0f, 360.0f, 10.0f,  95.0f, 150.0f, 3.5f);
	
	public Powerup(float x, float y, EffectType type) {
		this.x = x;
		this.y = y;
		
		this.type = type;
		
		sprites.put(EffectType.BOTTOM_WALL, new Sprite("sprites/powerup_bottom_wall.png", 100, 100, 100, 100));
		sprites.put(EffectType.PADDLE_GUN, new Sprite("sprites/powerup_paddle_gun.png", 100, 100, 100, 100));
		
		sprites.put(EffectType.SHRINK_PADDLE, new Sprite("sprites/powerup_shrink.png", 100, 100, 100, 100));
		sprites.put(EffectType.ENLARGE_PADDLE, new Sprite("sprites/powerup_enlarge.png", 100, 100, 100, 100));
		
		sprites.put(EffectType.FIREBALL, new Sprite("sprites/powerup_fireball.png", 100, 100, 100, 100));
		sprites.put(EffectType.MULTIBALL, new Sprite("sprites/powerup_multiball.png", 100, 100, 100, 100));						
		
		sprites.put(EffectType.SLOW_DOWN, new Sprite("sprites/powerup_slow_down.png", 100, 100, 100, 100));
		sprites.put(EffectType.SPEED_UP, new Sprite("sprites/powerup_speed_up.png", 100, 100, 100, 100));
		
		sprites.put(EffectType.STICKY_BALL, new Sprite("sprites/powerup_sticky_ball.png", 100, 100, 100, 100));
	}
	
	@Override
	public void render() {
		trail.render();
		
		Sprite sprite = sprites.get(type);
		if (sprite != null)
			sprite.render(x, y, width, height);
		
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL11.glDisable(GL11.GL_BLEND);
//		
//		GL11.glBegin(GL11.GL_QUADS);
//
//			switch (type) {
//			case BOTTOM_WALL:
//				GL11.glColor3f(0.0f, 0.0f, 0.0f);
//				break;
//			case ENLARGE_PADDLE:
//				GL11.glColor3f(0.0f, 1.0f, 0.0f);
//				break;
//			case FIREBALL:
//				GL11.glColor3f(1.0f, 0.0f, 0.0f);
//				break;
//			case MULTIBALL:
//				GL11.glColor3f(1.0f, 1.0f, 1.0f);
//				break;
//			case PADDLE_GUN:
//				GL11.glColor3f(1.0f, 0.5f, 0.5f);
//				break;
//			case SHRINK_PADDLE:
//				GL11.glColor3f(0.40f, 0.40f, 1.0f);
//				break;
//			case STICKY_BALL:
//				GL11.glColor3f(0.0f, 0.0f, 1.0f);
//				break;
//			case SPEED_UP:
//				GL11.glColor3f(0.0f, 1.0f, 1.0f);
//				break;			
//			case SLOW_DOWN:
//				GL11.glColor3f(0.0f, 1.0f, 0.5f);
//				break;
//				
//			default:
//				throw new RuntimeException("Unsupported EffectType: " + type);		
//			}
//					
//			GL11.glVertex2f(x, y);			
//			GL11.glVertex2f(x + width, y);			
//			GL11.glVertex2f(x + width, y + height);			
//			GL11.glVertex2f(x, y + height);
//		GL11.glEnd();
	}

	@Override
	public void step() {
		y += 10f;
		
		trail.setPosition(getBoundingBox().getCenterX(), 
						  getBoundingBox().getCenterY(), 
						  (float) 180.0f);
		trail.step();
		
		for (Sprite s : sprites.values())
			s.step();
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
		
		trail.setPosition(getBoundingBox().getCenterX(), 
				  getBoundingBox().getCenterY(), 
				  (float) Math.toRadians(180.0f));
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		
		trail.setPosition(getBoundingBox().getCenterX(), 
				  getBoundingBox().getCenterY(), 
				  (float) Math.toRadians(180.0f));
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
		
		trail.setPosition(getBoundingBox().getCenterX(), 
				  getBoundingBox().getCenterY(), 
				  (float) Math.toRadians(180.0f));
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
		
		trail.setPosition(getBoundingBox().getCenterX(), 
				  getBoundingBox().getCenterY(), 
				  (float) Math.toRadians(180.0f));
	}
}
