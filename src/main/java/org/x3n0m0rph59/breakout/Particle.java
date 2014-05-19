package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Particle extends GameObject {	
	private float x, y, dx, dy, width, height, initialttl, ttl, size, sizeIncrease;
	private float angularMomentum, angle;
	private boolean destroyed = false;
	
	private Sprite sprite;
	
	public Particle(Sprite sprite, float x, float y, float dx, float dy, float angularMomentum, float ttl, float sizeIncrease) {
		this.x = x;
		this.y = y;
		
		this.dx = dx;
		this.dy = dy;
		
		this.angularMomentum = (float) Util.random((int) -angularMomentum, (int) +angularMomentum);

		this.initialttl = ttl;
		this.ttl = ttl;
		
		this.size = 1.0f;
		this.sizeIncrease = sizeIncrease;
		
		this.angle = 0.0f;
		
		this.sprite = sprite;
	}

	@Override
	public void render() {
		sprite.setAlpha((ttl / initialttl) * 1.5f /* * (ttl / initialttl) */);
		sprite.setAngle(angle);
		sprite.render(x, y, width, height);
	}

	@Override
	public void step() {
		if ((ttl -= 1.0f) <= 0)
			setDestroyed(true);
		
		angle += angularMomentum;
		size += sizeIncrease;
		
		x += dx;
		y += dy;
		
		width  = (getAge() + 1.0f) + size;
		height = (getAge() + 1.0f) + size;
		
		sprite.step();
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
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

	public float getTtl() {
		return ttl;
	}

	public void setTtl(float ttl) {
		this.ttl = ttl;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
	
	public float getAge() {
		return initialttl - ttl;
	}

}
