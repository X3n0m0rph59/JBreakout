package org.x3n0m0rph59.breakout;

import org.newdawn.slick.geom.Rectangle;

public class Background extends GameObject {
	private float x, y, width, height, speed;
	private boolean destroyed = false;
	
	private Sprite image;
	
	public Background(Sprite image, float x, float y, float width, float height, float speed) {
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		this.speed = speed;
		
		this.image = image;
	}
	
	@Override
	public void render() {		
		image.render(x, y, width, height);
	}

	@Override
	public void step() {
		image.step();
		
		y += speed;
		
		if (y >= Config.SCREEN_HEIGHT)
			setDestroyed(true);
	}

	@Override
	public Rectangle getBoundingBox() {
		return null;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

}
