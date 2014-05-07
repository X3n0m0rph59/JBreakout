package org.x3n0m0rph59.breakout;

import junit.framework.TestCase;

public class UtilTest extends TestCase {

	public void testRandom() {
		for (int i = 0; i < 100; i++) {
			int rnd = Util.random(0, 100);
			
			assert(rnd < i);
		}		
	}

}
