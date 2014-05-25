package org.x3n0m0rph59.breakout;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.TrueTypeFont;

public class FontLoader {
	private static final FontLoader instance = new FontLoader();
	private Map<FontTuple, TrueTypeFont> map = new HashMap<>();
	
	public FontLoader() {
		primeCache();
	}
	
	public static FontLoader getInstance() {
		return instance;
	}
	
	public TrueTypeFont getFont(String name, int style, int size) {
		TrueTypeFont font;
		if ((font = getFontFromCache(name, style, size)) == null) {
			Font f = new Font(name, style, size);
			font = new TrueTypeFont(f, true);
			
			addFontToCache(name, style, size, font);
			
			return font;
		}
		else {
			return font;
		}
	}
	
	private void addFontToCache(String name, int style, int size, TrueTypeFont font) {
		map.put(new FontTuple(name, style, size), font);
	}
	
	private TrueTypeFont getFontFromCache(String name, int style, int size) {
		return map.get(new FontTuple(name, style, size));		
	}
	
	private void primeCache() {
				
	}
}
