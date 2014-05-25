package org.x3n0m0rph59.breakout;

/**
 * A background sprite that scrolls through the scene from top to bottom at a constant speed
 * @author user
 */
public class Background extends GameObject {	
	/**
	 * Create a new Background
	 * 
	 * @param sprite	The Sprite used to draw the background
	 * @param position	The initial position
	 * @param width		The initial width
	 * @param height	The initial height
	 * @param angle		The initial angle of rotation in radians
	 * @param speed		The initial scroll speed
	 */
	public Background(Sprite sprite, Point position, float width, float height, float angle, float deltaY) {
		super(sprite, position, width, height, angle, 0.0f, 0.0f, deltaY);
		
		getSprite().setAlpha(Config.BACKGROUND_ALPHA);
	}
	
	/**
	 * Step one frame forward, animate our sprite and advance our 
	 * position by speed units 
	 */
	@Override
	public void step() {
		super.step();
		
		if (getBoundingBox().getMinY() >= Config.getInstance().getScreenHeight())
			setDestroyed(true);
	}
}
