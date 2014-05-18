package org.x3n0m0rph59.breakout;

import junit.framework.TestCase;

public class BallTest extends TestCase {

	public void testChangeAngle() {
		Ball b = new Ball(100.0f, 100.0f);
		
		b.setAngle(180.0f);
		b.reflect();
		
		assertEquals(-180.0f, b.getAngle());
		
	}

	public void testGetAngle() {

	}

	public void testReflect() {

	}

}
