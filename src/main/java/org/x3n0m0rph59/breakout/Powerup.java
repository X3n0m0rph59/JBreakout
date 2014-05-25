package org.x3n0m0rph59.breakout;

import java.util.EnumMap;
import java.util.Map;

import org.x3n0m0rph59.breakout.SpaceBomb.State;

public class Powerup extends GameObject {
	public enum State {FLOATING, STUCK_TO_GRAPPLING_HOOK}
	private State state = State.FLOATING;
	
	private EffectType type;
	
	private Map<EffectType, Sprite> sprites = new EnumMap<>(EffectType.class);
	
	private final ParticleSystem trail = new ParticleSystem(new SpriteTuple[]{
										 new SpriteTuple("sprites/Star1.png", 255.0f, 255.0f, 255, 255), 
										 new SpriteTuple("sprites/Star2.png", 345.0f, 342.0f, 345, 342), 
										 new SpriteTuple("sprites/Star3.png", 270.0f, 261.0f, 270, 261), 
										 new SpriteTuple("sprites/Star4.png", 264.0f, 285.0f, 264, 285)}, 
										 position, -1.0f, 10.0f, 0.0f, 360.0f, 10.0f,  95.0f, 150.0f, 3.5f);
	
	public Powerup(Point position, EffectType type) {
		super(null, position, Config.POWERUP_WIDTH, Config.POWERUP_HEIGHT, 0.0f, 0.0f, 0.0f, Config.POWERUP_SPEED);
		
		this.type = type;
		
		
		sprites.put(EffectType.BOTTOM_WALL, new Sprite(
				"sprites/powerup_bottom_wall.png", Config.POWERUP_WIDTH,
				Config.POWERUP_HEIGHT, 100, 100));
		sprites.put(EffectType.PADDLE_GUN, new Sprite(
				"sprites/powerup_paddle_gun.png", Config.POWERUP_WIDTH,
				Config.POWERUP_HEIGHT, 100, 100));

		sprites.put(EffectType.SHRINK_PADDLE, new Sprite(
				"sprites/powerup_shrink.png", Config.POWERUP_WIDTH,
				Config.POWERUP_HEIGHT, 100, 100));
		sprites.put(EffectType.ENLARGE_PADDLE, new Sprite(
				"sprites/powerup_enlarge.png", Config.POWERUP_WIDTH,
				Config.POWERUP_HEIGHT, 100, 100));

		sprites.put(EffectType.FIREBALL, new Sprite(
				"sprites/powerup_fireball.png", Config.POWERUP_WIDTH,
				Config.POWERUP_HEIGHT, 100, 100));
		sprites.put(EffectType.MULTIBALL, new Sprite(
				"sprites/powerup_multiball.png", Config.POWERUP_WIDTH,
				Config.POWERUP_HEIGHT, 100, 100));

		sprites.put(EffectType.SLOW_DOWN, new Sprite(
				"sprites/powerup_slow_down.png", Config.POWERUP_WIDTH,
				Config.POWERUP_HEIGHT, 100, 100));
		sprites.put(EffectType.SPEED_UP, new Sprite(
				"sprites/powerup_speed_up.png", Config.POWERUP_WIDTH,
				Config.POWERUP_HEIGHT, 100, 100));

		sprites.put(EffectType.STICKY_BALL, new Sprite(
				"sprites/powerup_sticky_ball.png", Config.POWERUP_WIDTH,
				Config.POWERUP_HEIGHT, 100, 100));
	}
	
	@Override
	public void render() {
		trail.render();
		
		Sprite sprite = sprites.get(type);
		if (sprite != null)
			setSprite(sprite);
		
		super.render();
	}

	@Override
	public void step() {		
		trail.setPositionAndAngle(new Point(getBoundingBox().getCenterX(), getBoundingBox().getCenterY()), 180.0f);
		trail.step();
		
		for (Sprite s : sprites.values())
			s.step();
		
		if (state != State.STUCK_TO_GRAPPLING_HOOK)
			super.step();		
		
		if (getY() >= Config.getInstance().getScreenHeight())
				setDestroyed(true);
	}
	
	public EffectType getType() {
		return type;
	}

	@Override
	public void setPosition(Point position) {
		super.setPosition(position);
		
		trail.setPositionAndAngle(new Point(getBoundingBox().getCenterX(), 
				  					getBoundingBox().getCenterY()), 
				  			        (float) Math.toRadians(180.0f));
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
}
