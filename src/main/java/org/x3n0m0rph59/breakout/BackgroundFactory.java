package org.x3n0m0rph59.breakout;


public class BackgroundFactory {
	
	public static Background getRandomBackground() {
		Sprite sprite;
		
		switch (Util.random(0, 4))
		{
		case 0:
			sprite = new Sprite("backgrounds/00.png", 256, 256, 256, 256, true);
			break;
			
		case 1:				
			sprite = new Sprite("backgrounds/01.png", 256, 256, 256, 256, true);
			break;
			
		case 2:
			sprite = new Sprite("backgrounds/02.png", 256, 256, 256, 256, true);
			break;
			
		case 3:
			sprite = new Sprite("backgrounds/03.png", 256, 256, 256, 256, true);
			break;
			
		case 4:
			sprite = new Sprite("backgrounds/04.png", 256, 256, 256, 256, true);
			break;
			
		default:
			throw new RuntimeException("Invalid background requested");
		}
		
		
		final float width  = (float) Util.random(512, (int) Config.getInstance().getClientWidth());
		final float height = (float) Util.random(512, (int) Config.getInstance().getScreenHeight());
		final float angle  = (float) Util.random(0, 360);
		final float speed  = (float) Util.random((int) Config.BACKGROUND_MIN_SPEED, 
												 (int) Config.BACKGROUND_MAX_SPEED);
		
		return new Background(sprite, new Point((float) Util.random(0, (int) Config.getInstance().getClientWidth() - (int) width), -height), 
							  width, height, angle, speed);
	}
}
