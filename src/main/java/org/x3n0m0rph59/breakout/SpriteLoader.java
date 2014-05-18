package org.x3n0m0rph59.breakout;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


public class SpriteLoader {
	private static final SpriteLoader instance = new SpriteLoader();
	private Map<String, SpriteSheet> map = new HashMap<>();
	
	public SpriteLoader() {
		primeCache();
	}
	
	public static SpriteLoader getInstance() {
		return instance;
	}
	
	public SpriteSheet getSpriteSheet(String filename, int tw, int th) {
		SpriteSheet spriteSheet;
		if ((spriteSheet = getSpriteSheetFromCache(filename)) == null) {
			try {
				spriteSheet = new SpriteSheet(filename, tw, th);
			} catch (SlickException e) {
				e.printStackTrace();
			}			
			
			addSpriteSheetToCache(filename, spriteSheet);
			
			return spriteSheet;
		}
		else {
			return spriteSheet;
		}
	}
	
	private void addSpriteSheetToCache(String filename, SpriteSheet spriteSheet) {
		map.put(filename, spriteSheet);
	}
	
	private SpriteSheet getSpriteSheetFromCache(String filename) {
		return map.get(filename);		
	}
	
	private void primeCache() {
				
	}
}
