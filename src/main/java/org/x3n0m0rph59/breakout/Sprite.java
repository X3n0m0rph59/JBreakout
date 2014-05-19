package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Sprite {
	private SpriteSheet spriteSheet;
	private int frameCounter = 0;
	
	private float width, height, alpha = 1.0f, angle = 0.0f;
	
	private boolean hasAlphaChannel;
	
	public Sprite(String filename, float width, float height, int tw, int th) {
		this(filename, width, height, tw, th, false);
	}
	
	public Sprite(String filename, float width, float height, int tw, int th, boolean hasAlphaChannel) {
		this.width = width;
		this.height = height;
		
		this.hasAlphaChannel = hasAlphaChannel;
		
		spriteSheet = SpriteLoader.getInstance().getSpriteSheet(filename, tw, th);
	}
	
	public void render(float x, float y, float width, float height) {		
		if (!hasAlphaChannel) {
			GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.0f);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glEnable(GL11.GL_BLEND);
		} else {
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_BLEND);
		}
				
		Image img = spriteSheet.getSprite(frameCounter, 0);
		img.setAlpha(alpha);
						
		img.rotate(angle);
		img.draw(x, y, width, height);
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void render(float x, float y) {
		render(x, y, width, height);
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
	
	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
}
