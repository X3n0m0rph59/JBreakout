package org.x3n0m0rph59.breakout;

import org.newdawn.slick.geom.Rectangle;


public abstract class GameObject {
	
	public abstract void render();
	public abstract void step();
	
	public abstract Rectangle getBoundingBox();
}
