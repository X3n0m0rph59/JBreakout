package org.x3n0m0rph59.breakout;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.TrueTypeFont;

class FontTuple {
	private String name;
	private int style;
	private int size;
	
	public FontTuple(String name, int style, int size) {
		this.name = name;
		this.style = style;
		this.size = size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + size;
		result = prime * result + style;
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
		FontTuple other = (FontTuple) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (size != other.size)
			return false;
		if (style != other.style)
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}

public class FontLoader {
	private static final FontLoader instance = new FontLoader();
	private Map<FontTuple, TrueTypeFont> map = new HashMap<FontTuple, TrueTypeFont>();
	
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
