package org.x3n0m0rph59.breakout;

import org.newdawn.slick.geom.Rectangle;

/**
 * A background sprite that scrolls through the scene from top to bottom at a constant speed
 * @author user
 */
public class Background extends GameObject {
	private float x, y, width, height, speed;
	private boolean destroyed = false;
	
	/** Holds the actual sprite of the background*/
	private Sprite sprite;
	
	/**
	 * Create a new Background
	 * 
	 * @param sprite	The Sprite used to draw the background
	 * @param x			The initial coordinate on the X-axis
	 * @param y			The initial coordinate on the Y-axis
	 * @param width		The initial width
	 * @param height	The initial height
	 * @param speed		The initial scroll speed
	 */
	public Background(Sprite sprite, float x, float y, float width, float height, float speed) {
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		this.speed = speed;
		
		this.sprite = sprite;
		
		sprite.setAlpha(Config.BACKGROUND_ALPHA);
	}
	
	/**
	 * Render our sprite to the current OpenGL context
	 * Note: If the width or height is larger than the 
	 * width or height of the sprite image, the sprite 
	 * image will be rendered stretched and maybe distorted
	 * (if the aspect ratio is not kept)
	 */
	@Override
	public void render() {		
		sprite.render(x, y, width, height);
	}

	/**
	 * Step one frame forward, animate our sprite and advance our 
	 * position by speed units 
	 */
	@Override
	public void step() {
		sprite.step();
		
		y += speed;
		
		if (y >= Config.getInstance().getScreenHeight())
			setDestroyed(true);
	}

	/**
	 * Get the bounding box of our sprite
	 * @return The Bounding Box 
	 */
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Query whether we are destroyed. A Background is destroyed 
	 * when it scrolls below the bottom of the Screen 
	 * (outside the visible Range) or when 
	 * <code>setDestroyed(true)</code> is called
	 * @return True when we are destroyed, false otherwise
	 * @see void setDestroyed(boolean destroyed)
	 */
	public boolean isDestroyed() {
		return destroyed;
	}

	/**
	 * Set the destroyed State of the Background
	 * @param destroyed	The new destroyed State
	 * @see boolean isDestroyed()
	 */
	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

}
