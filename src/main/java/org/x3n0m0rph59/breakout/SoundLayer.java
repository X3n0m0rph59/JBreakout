package org.x3n0m0rph59.breakout;

public final class SoundLayer {
	public enum Sounds { 
							WELCOME, 
							BRICK_HIT, 
							SOLID_BRICK_HIT, 
							PADDLE_HIT, 
							WALL_HIT, 
							BALL_LOST, 
							POWERUP_SPAWNED, 
							BRICK_DESTROYED, 
							QUIT
						};
						
	public enum Music { 
						INTRO, 
						BACKGROUND, 
						OUTRO 
					  };
	
	public static void playSound(Sounds sound) {
		Logger.log("Playing sound: " + sound);
	}
	
	public static void playMusic(Music music) {
		Logger.log("Playing music: " + music);
	}
}
