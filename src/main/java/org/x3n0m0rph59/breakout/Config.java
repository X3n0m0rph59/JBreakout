package org.x3n0m0rph59.breakout;

public final class Config {	
	// Global configuration parameters
	public static final String APP_NAME = "JBreakout";
	public static final String APP_VERSION = "0.0.1";
	
	public static final int SYNC_FPS = 60;
	
	public static final float SCOREBOARD_WIDTH = 300;
	
	// DEBUGGING
	public static final boolean FULLSCREEN = false;
	public static final float SCREEN_WIDTH = 1280;
	public static final float SCREEN_HEIGHT = 1024;
	
	// RELEASE
//	public static final boolean FULLSCREEN = true;
//	public static final float SCREEN_WIDTH = 2048;
//	public static final float SCREEN_HEIGHT = 1152;
	
	public static final float CLIENT_WIDTH = SCREEN_WIDTH - SCOREBOARD_WIDTH;
		
	public static final int TOAST_FONT_SIZE = 44;
	public static final float TOAST_DELAY = 4.0f;
	
	public static final int INITIAL_BALLS_LEFT = 3;
	
	public static final float BALL_SPEED = 8.0f;
	public static final float BALL_RADIUS = 10;
	public static final float BALL_SPAWN_X = CLIENT_WIDTH / 2 - 50.0f;
	public static final float BALL_SPAWN_Y = SCREEN_HEIGHT / 2;
	
	public static final float POWERUP_SPEEDUP_FACTOR = 1.5f;
	public static final float POWERUP_SLOWDOWN_FACTOR = 1.5f;
	
	public static final int EFFECT_DURATION = 15;
	
//	public static final float BRICK_WIDTH = 33.5f;
	public static final float BRICK_HEIGHT = 18.0f;
	public static final float BRICK_OFFSET_X = 50.0f;
	public static final float BRICK_OFFSET_Y = 50.0f;
	public static final float BRICK_SPACING_X = 5.0f;
	public static final float BRICK_SPACING_Y = 5.0f;	
	
	public static final float BRICK_MOVEMENT_SPEED = 0.5f;
	public static final float BRICK_MOVEMENT_MULTIPLIER = 2.0f;
	
	public static final int HARD_BRICK_HITS_NEEDED = 2;
	
	public static final float PADDLE_DEFAULT_Y = SCREEN_HEIGHT - 70.0f;
	public static final float PADDLE_DEFAULT_WIDTH = 75.0f;
	public static final float PADDLE_EXPANSION = 40.0f;
	public static final float PADDLE_HEIGHT = 15.0f;
	
	public static final int PARTICLE_DENSITY = 2;
	public static final float PARTICLE_WIDTH = 2.5f;
	public static final float PARTICLE_HEIGHT = 2.5f;
	public static final float PARTICLE_MIN_SPEED = 5.0f;
	public static final float PARTICLE_MAX_SPEED = 20.0f;
	
	public static final float POWERUP_WIDTH = 20.0f;
	public static final float POWERUP_HEIGHT = 20.0f;
	
	public static final float PROJECTILE_WIDTH = 2.5f;
	public static final float PROJECTILE_HEIGHT = 12.0f;
	public static final float PROJECTILE_SPEED = 12.0f;
	public static final int PROJECTILE_FIRE_RATE = 4;
	
	public static final float BOTTOM_WALL_HEIGHT = 15.0f;	
	public static final float BOTTOM_WALL_SEGMENT_WIDTH = 45.0f;
	public static final float BOTTOM_WALL_SEGMENT_HEIGHT = 25.0f;
	public static final float BOTTOM_WALL_SEGMENT_SPACING = 5.0f;
	
	
	private static final Config instance = new Config();	
	private float speedFactor = 1.0f;
	
	public Config() {
		
	}
	
	public static Config getInstance() {
		return instance;
	}
	
	public float getSpeedFactor() {				
		return speedFactor;
	}
	
	public void setSpeedFactor(float f) {				
		speedFactor = f;
	}
	
	public void increaseGameSpeed(float factor) {
		speedFactor *= factor;
	}
	
	public void decreaseGameSpeed(float factor) {
		speedFactor /= factor;
	}
}
