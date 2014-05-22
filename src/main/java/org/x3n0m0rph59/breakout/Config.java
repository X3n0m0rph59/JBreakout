package org.x3n0m0rph59.breakout;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public final class Config {	
	// Global configuration parameters
	
	/** The name of the application */
	public static final String APP_NAME = "JBreakout";
	
	/** The version of the application */
	public static final String APP_VERSION = "0.0.1";
	
	/** Synchronize to this framerate */
	public static final int SYNC_FPS = 60;
	
	/** The width of the scoreboard on the right edge of the screen */
	public static final float SCOREBOARD_WIDTH = 300;
	
	// DEBUGGING
	/** Force fullscreen mode */
//	public static final boolean FULLSCREEN = false;
	
	/** Force a video mode with this width */
	public static final float FORCE_SCREEN_WIDTH = 1280;
	
	/** Force a video mode with this height */
	public static final float FORCE_SCREEN_HEIGHT = 1024;
	
	// RELEASE
	/** Force fullscreen mode */
	public static final boolean FULLSCREEN = true;
	
	/** Size of the font used to render messages on the screen */
	public static final int TOAST_FONT_SIZE = 44;
	
	/** How long a message is being displayed */
	public static final float TOAST_DELAY = 4.0f;
	
	
	/** Bias of pitch when in accelerated or decelerated mode */
	public static final float MUSIC_PITCH_BIAS = 1.5f;
	
	
	/** Initial number of lives left */
	public static final int INITIAL_BALLS_LEFT = 4;
	
	/** Default ball speed */
	public static final float BALL_SPEED = 8.0f;
	
	/** The maximum amount at which the ball speed is capped */
	public static final float BALL_SPEED_MAX = 16.0f;
	
	/** The minimum amount at which the ball speed is capped */
	public static final float BALL_SPEED_MIN = 2.0f;
	
	/** Ball radius */
	public static final float BALL_RADIUS = 10.0f;
		
	
	/** Speed up the game by this factor when a speedup powerup is caught */
	public static final float POWERUP_SPEEDUP_FACTOR = 2.0f;
	
	/** Slow down the game by this factor when a speed powerup is caught */
	public static final float POWERUP_SLOWDOWN_FACTOR = 2.0f;
	
	
	/** Time in seconds before an active effect is expired */
	public static final float EFFECT_DURATION = 15.0f;
	
	/** Lower end of the range of randomized scroll speeds for background sprites */
	public static final float BACKGROUND_MIN_SPEED = 1.0f;
	
	/** Upper end of the range of randomized scroll speeds for background sprites */
	public static final float BACKGROUND_MAX_SPEED = 4.0f;
	
	/** Density of background sprites (the lower the value the more backgrounds are generated per time unit) */
	public static final int BACKGROUND_DENSITY = 400;
	
	/** Transparency level of background sprites */
	public static final float BACKGROUND_ALPHA = 0.45f;
	
	
	/** Force width of bricks */
//	public static final float BRICK_WIDTH = 33.5f;
	
	/** Height of bricks */
	public static final float BRICK_HEIGHT = 18.0f;
	
	/** Left and right free space between wall and bricks */
	public static final float BRICK_OFFSET_X = 50.0f;
	
	/** Top free space between wall and bricks */
	public static final float BRICK_OFFSET_Y = 50.0f;
	
	/** Horizontal spacing between bricks */
	public static final float BRICK_SPACING_X = 5.0f;
	
	/** Vertical spacing between bricks */
	public static final float BRICK_SPACING_Y = 5.0f;
	
	/** Movement speed of animated bricks */
	public static final float BRICK_MOVEMENT_SPEED = 0.5f;
	
	/** Multiplier used to compute bricks with multiple movement specifiers */
	public static final float BRICK_MOVEMENT_MULTIPLIER = 2.0f;
	
	/** Specifies how many hits are needed to destroy a hard brick */
	public static final int HARD_BRICK_HITS_NEEDED = 3;
	
	
	/** Distance of the paddle from the bottom of the screen */
	public static final float PADDLE_BOTTOM_SPACING = 100.0f;
	
	/** Default width of the paddle */
	public static final float PADDLE_DEFAULT_WIDTH = 100.0f;
	
	/** Specifies how much the paddle grows when an extender bonus has been caught */	
	public static final float PADDLE_EXPANSION = 40.0f;
	
	/** The height of the paddle */
	public static final float PADDLE_HEIGHT = 25.0f;
	
	/** Horizontal offset of the "Engine" ParticelSystem */
	public static final float PADDLE_ENGINE_OFFSET = 5.0f;
	
	public static final int STAR_DENSITY = 2;
	public static final float STAR_WIDTH = 2.5f;
	public static final float STAR_HEIGHT = 2.5f;
	public static final float STAR_MIN_SPEED = 5.0f;
	public static final float STAR_MAX_SPEED = 20.0f;
	
	public static final float POWERUP_WIDTH = 50.0f;
	public static final float POWERUP_HEIGHT = 50.0f;
	
	public static final float PROJECTILE_WIDTH = 7.0f;
	public static final float PROJECTILE_HEIGHT = 18.0f;
	public static final float PROJECTILE_SPEED = 12.0f;
	public static final int PROJECTILE_FIRE_RATE = 4;
	
	public static final float BOTTOM_WALL_HEIGHT = 15.0f;	
	public static final float BOTTOM_WALL_SEGMENT_WIDTH = 45.0f;
	public static final float BOTTOM_WALL_SEGMENT_HEIGHT = 25.0f;
	public static final float BOTTOM_WALL_SEGMENT_SPACING = 5.0f;
	
	
	private static final Config instance = new Config();
	
	private float screenWidth = 0.0f;
	private float screenHeight = 0.0f;
	
	private float speedFactor = 1.0f;

	private boolean debugging;
	
	public Config() {
		
	}
	
	public static Config getInstance() {
		return instance;
	}
	
	public void parseCommandLine(String[] args) {
		ArgumentParser parser = ArgumentParsers.newArgumentParser(Config.APP_NAME).
								description("Java and OpenGL based Breakout Game");

		parser.addArgument("--debug").type(Boolean.class).action(Arguments.storeTrue()).help("Set the debugging flag");
		
		try {
			Namespace namespace = parser.parseArgs(args);
			
			this.debugging = namespace.getBoolean("debug");
			
		} catch (ArgumentParserException e) {
			e.printStackTrace();
		}
		
	}
	
	public float getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(float screenWidth) {
		this.screenWidth = screenWidth;
	}

	public float getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(float screenHeight) {
		this.screenHeight = screenHeight;
	}
	
	public float getClientWidth() {
		return screenWidth - SCOREBOARD_WIDTH;
	}

	public float getSpeedFactor() {				
		return speedFactor;
	}
	
	public void setSpeedFactor(float f) {				
		speedFactor = f;
		
		changeMusicPitch();
	}
	
	public void increaseGameSpeed(float factor) {
		speedFactor *= factor;
		
		changeMusicPitch();
	}
	
	public void decreaseGameSpeed(float factor) {
		speedFactor /= factor;
		
		changeMusicPitch();
	}
	
	private void changeMusicPitch() { 
		if (speedFactor == 1.0f)
			SoundLayer.getInstance().setCurrentMusicPitch(speedFactor);
		else if (speedFactor < 1.0f)			
			SoundLayer.getInstance().setCurrentMusicPitch(speedFactor * Config.MUSIC_PITCH_BIAS);
		else if (speedFactor > 1.0f)			
			SoundLayer.getInstance().setCurrentMusicPitch(speedFactor / Config.MUSIC_PITCH_BIAS);
	}

	public boolean isDebugging() {		
		return debugging;
	}
}
