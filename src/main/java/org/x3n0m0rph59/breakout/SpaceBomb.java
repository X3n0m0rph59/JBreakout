package org.x3n0m0rph59.breakout;

public class SpaceBomb extends GameObject {
	public enum Type {USER_FIRED, BONUS};
	public enum State {FLOATING, EXPLODING, EXPLODED, STUCK_TO_GRAPPLING_HOOK}
	
	private Type type;
	private State state = State.FLOATING;
	
	private int ttl = 4 * Config.SYNC_FPS;
	
	private final ParticleSystem trail = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("sprites/fire.png", 198.0f, 197.0f, 198, 197)}, 
											  				new Point(0.0f, 0.0f), -1.0f, 10.0f, 180.0f, 45.0f, 0.0f, 15.0f, 55.0f, 2.0f);
	
	private final ParticleSystem explosion = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("sprites/fire.png", 198.0f, 197.0f, 198, 197)},
											   					new Point(0.0f, 0.0f), -1.0f, 25.0f, 0.0f, 360.0f, 2.0f, 55.0f, 55.0f, 25.0f);
	private int explosionframeCounter = 0; 
	
	public SpaceBomb(Point position, Type type) {
		super(new Sprite("sprites/spacebomb.png", Config.SPACEBOMB_WIDTH, Config.SPACEBOMB_HEIGHT, 150, 150), position, 
			  Config.SPACEBOMB_WIDTH, Config.SPACEBOMB_HEIGHT, 0.0f, -5.0f, 0.0f, 0.0f);
		
		this.type = type;
		
		switch (type) {
		case BONUS:
			setDeltaY(+Config.SPACEBOMB_LURKING_SPEED);
			break;
			
		case USER_FIRED:
			setDeltaY(-Config.SPACEBOMB_SPEED);
			break;
			
		default:
			throw new RuntimeException("Invalid state");		
		}
	}

	@Override
	public void render() {
		switch (state) {
		case FLOATING:
			trail.render();			
			super.render();
			break;
			
		case STUCK_TO_GRAPPLING_HOOK:
			trail.render();			
			super.render();
			break;
			
		case EXPLODING:
			explosion.render();
			break;
			
		case EXPLODED:
			break;		
			
		default:
			throw new RuntimeException("Invalid state");
		}
	}

	@Override
	public void step() {
		if (type == Type.USER_FIRED) {
			switch (state) {
			case FLOATING:
				super.step();
				
				trail.setPositionAndAngle(getCenterPosition(), getAngleInDegrees());
				trail.step();
						
				if (ttl-- <= 0 || getY() <= 0) {
					setState(State.EXPLODING);
				}
				break;
						
			case EXPLODING:
				super.step();
				
				explosion.setPositionAndAngle(getCenterPosition(), getAngleInDegrees());
				explosion.step();
				
				if (explosionframeCounter++ >= Config.SPACEBOMB_EXPLOSION_DURATION + Config.SYNC_FPS) {
					setState(State.EXPLODED);
				}
				break;
				
			case EXPLODED:
				setDestroyed(true);
				break;
				
			case STUCK_TO_GRAPPLING_HOOK:
				super.step();
				
				trail.setPositionAndAngle(getCenterPosition(), getAngleInDegrees());
				trail.step();
						
				if (ttl-- <= 0 || getY() <= 0) {
					setState(State.EXPLODING);
				}
				break;
				
			default:
				throw new RuntimeException("Invalid state");
			}
		} else if (type == Type.BONUS) {
			if (state != State.STUCK_TO_GRAPPLING_HOOK)
				super.step();
		} else
			throw new RuntimeException("Invalid type: " + type);			
	}
	
	@Override
	public boolean isExcemptFromSpeedFactorChange() {
		return true;
	}

	public Point getCenterOfExplosion() {
		return getCenterPosition();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		if (this.state == State.FLOATING && state == State.EXPLODING)
			SoundLayer.playSound(Sounds.SPACEBOMB_EXPLOSION);
		
		this.state = state;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
}
