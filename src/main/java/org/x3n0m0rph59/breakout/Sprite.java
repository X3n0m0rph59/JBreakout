package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public class Sprite {
	private SpriteSheet spriteSheet;
	
	private int frameCounter = 0;
	
	private float width, height, alpha = 1.0f, angleInDegrees = 0.0f;
	
	private boolean hasAlphaChannel;
	private Point centerOfRotation = new Point(0.0f, 0.0f);
	
	private boolean flashed = false;
	
	public Sprite(String filename, float width, float height, int tw, int th) {
		this(filename, width, height, tw, th, false);
	}
	
	public Sprite(String filename, float width, float height, int tw, int th, boolean hasAlphaChannel) {
		this.width = width;
		this.height = height;
		
		this.hasAlphaChannel = hasAlphaChannel;
		
		spriteSheet = SpriteLoader.getInstance().getSpriteSheet(filename, tw, th);
	}
	
	public void render(Point position, float width, float height) {		
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
		
		img.setCenterOfRotation(centerOfRotation.getX(), centerOfRotation.getY());		
		img.rotate(angleInDegrees);
		
		if (flashed)
			img.drawFlash(position.getX(), position.getY(), width, height);
		else
			img.draw(position.getX(), position.getY(), width, height);
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void render(Point position) {
		this.render(position, width, height);
	}
	
	public void render(Rectangle r) {
		this.render(new Point(r.getX(), r.getY()), r.getWidth(), r.getHeight());
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
		return angleInDegrees;
	}

	public void setAngle(float angleInDegrees) {
		this.angleInDegrees = angleInDegrees;
	}

	public void setCenterOfRotation(Point position) {
		centerOfRotation = position;
	}

	public boolean isFlashed() {
		return flashed;
	}

	public void setFlashed(boolean flashed) {
		this.flashed = flashed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(alpha);
		result = prime * result + Float.floatToIntBits(angleInDegrees);
		result = prime
				* result
				+ ((centerOfRotation == null) ? 0 : centerOfRotation.hashCode());
		result = prime * result + frameCounter;
		result = prime * result + (hasAlphaChannel ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result
				+ ((spriteSheet == null) ? 0 : spriteSheet.hashCode());
		result = prime * result + Float.floatToIntBits(width);
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
		Sprite other = (Sprite) obj;
		if (Float.floatToIntBits(alpha) != Float.floatToIntBits(other.alpha))
			return false;
		if (Float.floatToIntBits(angleInDegrees) != Float
				.floatToIntBits(other.angleInDegrees))
			return false;
		if (centerOfRotation == null) {
			if (other.centerOfRotation != null)
				return false;
		} else if (!centerOfRotation.equals(other.centerOfRotation))
			return false;
		if (frameCounter != other.frameCounter)
			return false;
		if (hasAlphaChannel != other.hasAlphaChannel)
			return false;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
			return false;
		if (spriteSheet == null) {
			if (other.spriteSheet != null)
				return false;
		} else if (!spriteSheet.equals(other.spriteSheet))
			return false;
		if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width))
			return false;
		return true;
	}
}
