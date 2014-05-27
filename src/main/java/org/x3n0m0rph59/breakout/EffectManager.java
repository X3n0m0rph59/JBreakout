package org.x3n0m0rph59.breakout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class EffectManager {
	private static final EffectManager instance = new EffectManager();	
	private List<Effect> effectList = new ArrayList<Effect>();
	
		
	public void addEffect(Effect.Type type) {
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
				
		Logger.log("New active effect: " + type, 1);
	}
	
	public void expireEffect(Effect e) {		
		switch (e.getType()) {
		case BOTTOM_WALL:
			// Test if we really are the last active effect of this type
			if (!isEffectActive(Effect.Type.BOTTOM_WALL))
				App.getMainWindow().getScene().addTextAnimation("No more Bottom Wall!");
			break;
			
		case ENLARGE_PADDLE:
			App.getMainWindow().getScene().addTextAnimation("Shrink again!");
			break;
			
		case FIREBALL:
			// Test if we really are the last active effect of this type
			if (!isEffectActive(Effect.Type.FIREBALL))
				App.getMainWindow().getScene().addTextAnimation("Fireball vanished!");
			break;
			
		case MULTIBALL:			
			break;
			
		case PADDLE_GUN:
			// Test if we really are the last active effect of this type
			if (!isEffectActive(Effect.Type.PADDLE_GUN))
				App.getMainWindow().getScene().addTextAnimation("Guns jammed!");
			break;
			
		case SHRINK_PADDLE:
			App.getMainWindow().getScene().addTextAnimation("Grow back!");
			break;	
			
		case STICKY_BALL:
			// Test if we really are the last active effect of this type
			if (!isEffectActive(Effect.Type.STICKY_BALL))
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
		
		Logger.log("Effect expired: " + e.getType(), 1);
	}
	
	public boolean isEffectActive(Effect.Type effect) {
		for (Effect e : effectList) {
			if (e.getType() == effect)
				return true;			
		}
		
		return false;
	}
	
	public boolean isEffectInGracePeriod(Effect.Type effect) {
		List<Effect> candidates = new ArrayList<>();
		
		for (Effect e : effectList) {
			if (e.getType() == effect) {
				candidates.add(e);
			}
		}
		
		candidates.sort(new Comparator<Effect>() {
							public int compare(Effect o1, Effect o2) { 
								return (int) (o2.getEffectDuration() - o1.getEffectDuration());
							}
		});
		
		if (candidates.size() > 0)
			if (candidates.get(0).getEffectDuration() <= Config.EFFECT_GRACE_PERIOD)
				return true;
		
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
