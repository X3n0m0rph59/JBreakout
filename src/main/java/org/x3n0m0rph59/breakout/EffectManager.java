package org.x3n0m0rph59.breakout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

enum EffectType {
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
					
					// Bonuses
					// NEW_BALL,
					// SCORE_BOOST,
					
					// TODO:
					// 2d movement of paddle
					// multi paddle
				};

class Effect {
	private EffectType type;
	private int effectDuration;
	private boolean expired = false;
	
	public Effect(EffectType type, int effectDuration) {
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

	public int getEffectDuration() {
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
		
		Logger.log("Expired effect: " + this.type);
		
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

public final class EffectManager {
	private static final EffectManager instance = new EffectManager();	
	private List<Effect> effectList = new ArrayList<Effect>();
	
	public EffectManager() {
		
	}
	
	public void addEffect(EffectType type) {
		effectList.add(new Effect(type, Config.SYNC_FPS * Config.EFFECT_DURATION));
		
		switch (type) {
		case BOTTOM_WALL:
			App.getMainWindow().getScene().addTextAnimation("Bottom Wall!");
			break;
			
		case ENLARGE_PADDLE:
			App.getMainWindow().getScene().addTextAnimation("Enlarge!");
			break;
			
		case FIREBALL:
			App.getMainWindow().getScene().addTextAnimation("Fireball!");
			break;
			
		case MULTIBALL:
			App.getMainWindow().getScene().addTextAnimation("Multiball");
			break;
			
		case PADDLE_GUN:
			App.getMainWindow().getScene().addTextAnimation("Guns!");
			break;
			
		case SHRINK_PADDLE:
			App.getMainWindow().getScene().addTextAnimation("Shrink!");
			break;	
			
		case STICKY_BALL:
			App.getMainWindow().getScene().addTextAnimation("Sticky Ball!");
			break;
			
		case SPEED_UP:
			App.getMainWindow().getScene().addTextAnimation("Speed Up!");
			break;	
			
		case SLOW_DOWN:
			App.getMainWindow().getScene().addTextAnimation("Slow Down!");
			break;
			
		default:
			throw new RuntimeException("Unsupported type: " + type);		
		}
				
		Logger.log("New active effect: " + type);
	}
	
	public void expireEffect(Effect e) {		
		switch (e.getType()) {
		case BOTTOM_WALL:
			App.getMainWindow().getScene().addTextAnimation("No more Bottom Wall!");
			break;
			
		case ENLARGE_PADDLE:
			App.getMainWindow().getScene().addTextAnimation("Shrink again!");
			break;
			
		case FIREBALL:
			App.getMainWindow().getScene().addTextAnimation("Fireball vanished!");
			break;
			
		case MULTIBALL:			
			break;
			
		case PADDLE_GUN:
			App.getMainWindow().getScene().addTextAnimation("Guns jammed!");
			break;
			
		case SHRINK_PADDLE:
			App.getMainWindow().getScene().addTextAnimation("Grow back!");
			break;	
			
		case STICKY_BALL:
			App.getMainWindow().getScene().addTextAnimation("No more Sticky Ball!");
			break;
			
		case SPEED_UP:
			App.getMainWindow().getScene().addTextAnimation("Slow down again!");
			break;	
			
		case SLOW_DOWN:
			App.getMainWindow().getScene().addTextAnimation("Speed up again!");
			break;
			
		default:
			throw new RuntimeException("Unsupported type: " + e.getType());		
		}
		
		Logger.log("Effect expired: " + e.getType());
	}
	
	public boolean isEffectActive(EffectType effect) {
		for (Effect e : effectList) {
			if (e.getType() == effect)
				return true;			
		}
		
		return false;
	}
	
	public void step() {		
		Iterator<Effect> i = effectList.iterator();				
		while (i.hasNext()) {
			Effect e = i.next();
			e.step();
			
			if (e.isExpired()) {
				expireEffect(e);		
				i.remove();
			}
		}
	}
	
	public void clearEffects() {
		for (Effect e : effectList)
			e.expire();
		
		effectList.clear();
	}
	
	public static EffectManager getInstance() {
		return instance;
	}	
}
