package org.x3n0m0rph59.breakout;

public class Effect implements Stepable {
	public enum Type {
		// Special effects/abilities
		FIREBALL,
		MULTIBALL,
		ENLARGE_PADDLE, 
		SHRINK_PADDLE, 
		BOTTOM_WALL, 
		PADDLE_GUN,
		STICKY_BALL,
		SPEED_UP,
		SLOW_DOWN,	
		
		// TODO:
		// 2d movement of paddle
		// multi paddle
		// Ball movement influenced by mouse
		
		// Bonuses
		// NEW_BALL,
		// SCORE_BOOST,
	}
	
	private Type type;
	private float effectDuration;
	private boolean expired = false;
	
	public Effect(Type type, float effectDuration) {
		this.type = type;
		this.effectDuration = effectDuration;
		
		// Apply effect (if applicable)
		switch (type) {		
		case ENLARGE_PADDLE:
			App.getMainWindow().getScene().getPaddle().expand();
			break;		
		
		case MULTIBALL:
			App.getMainWindow().getScene().spawnBall(true);
			break;
			
		case BOTTOM_WALL:
			break;
			
		case FIREBALL:
			break;
			
		case PADDLE_GUN:
			break;
			
		case SHRINK_PADDLE:
			App.getMainWindow().getScene().getPaddle().shrink();
			break;
			
		case STICKY_BALL:
			break;
			
		case SPEED_UP:
			Config.getInstance().increaseGameSpeed(Config.POWERUP_SPEEDUP_FACTOR);			
			break;
			
		case SLOW_DOWN:
			Config.getInstance().decreaseGameSpeed(Config.POWERUP_SLOWDOWN_FACTOR);
			break;
			
		default:
			throw new RuntimeException("Unsupported EffectType: " + type);
		}
	}
	
	public void expire() {
		this.expired = true;
		
		Logger.log("Expired effect: " + this.type, 1);
		
		// Un-apply effect
		switch (type) {
		case BOTTOM_WALL:
			break;
			
		case ENLARGE_PADDLE:
			App.getMainWindow().getScene().getPaddle().shrink();
			break;
			
		case FIREBALL:
			break;
			
		case MULTIBALL:
			break;
			
		case PADDLE_GUN:
			break;
			
		case SHRINK_PADDLE:
			break;
			
		case STICKY_BALL:
			break;
			
		case SPEED_UP:
			Config.getInstance().decreaseGameSpeed(Config.POWERUP_SPEEDUP_FACTOR);
			break;
			
		case SLOW_DOWN:
			Config.getInstance().increaseGameSpeed(Config.POWERUP_SLOWDOWN_FACTOR);			
			break;
			
		default:
			throw new RuntimeException("Unsupported EffectType: " + type);		
		}
	}
	
	@Override
	public void step() {
		if (--effectDuration <= 0) {
			expire();
		}		
	}
	
	public Type getType() {
		return type;
	}

	public float getEffectDuration() {
		return effectDuration;
	}

	public void setEffectDuration(int effectDuration) {
		this.effectDuration = effectDuration;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setEffectDuration(float effectDuration) {
		this.effectDuration = effectDuration;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(effectDuration);
		result = prime * result + (expired ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Effect other = (Effect) obj;
		if (Float.floatToIntBits(effectDuration) != Float
				.floatToIntBits(other.effectDuration))
			return false;
		if (expired != other.expired)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Effect [type=" + type + ", effectDuration=" + effectDuration
				+ ", expired=" + expired + "]";
	}
}