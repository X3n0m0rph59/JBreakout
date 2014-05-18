package org.x3n0m0rph59.breakout;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Sprite {
	private SpriteSheet spriteSheet;
	private int frameCounter = 0;
	
	private float width, height, alpha = 1.0f;
	
	public Sprite(String filename, float width, float height, int tw, int th) {
		this.width = width;
		this.height = height;
		
		spriteSheet = SpriteLoader.getInstance().getSpriteSheet(filename, tw, th);
	}
	
	public void render(float x, float y, float width, float height) {
		Image img = spriteSheet.getSprite(frameCounter, 0);
		
		img.setAlpha(alpha);		
		img.draw(x, y, width, height);		
	}
	
	public void render(float x, float y) {
		Image img = spriteSheet.getSprite(frameCounter, 0);
		
		img.setAlpha(alpha);		
		img.draw(x, y, width, height);
	}
	
	public void step() {
		if (++frameCounter >= spriteSheet.getHorizontalCount());
			frameCounter = 0;
	}

	public int getFrameCounter() {
		return frameCounter;
	}

	public void setFrameCounter(int frameCounter) {
		this.frameCounter = frameCounter;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}	
}
