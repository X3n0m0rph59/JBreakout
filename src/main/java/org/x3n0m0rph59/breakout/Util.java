package org.x3n0m0rph59.breakout;

import org.newdawn.slick.geom.Rectangle;

public final class Util {
	public static boolean collisionTest(Rectangle r1, Rectangle r2) {
		return r1.intersects(r2);
	}
}
