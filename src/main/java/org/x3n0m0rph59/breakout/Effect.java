package org.x3n0m0rph59.breakout;

public class Effect {
	private EffectType type;
	private float effectDuration;
	private boolean expired = false;
	
	public Effect(EffectType type, float effectDuration) {
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
	
	public void step() {
		if (--effectDuration <= 0) {
			expire();
		}		
	}
	
	public EffectType getType() {
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
}