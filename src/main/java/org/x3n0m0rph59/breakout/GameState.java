package org.x3n0m0rph59.breakout;

public final class GameState {
	private static int level = 0;
	private static int ballsLeft = Config.INITIAL_BALLS_LEFT;
	private static int spaceBombsLeft = Config.INITIAL_SPACEBOMBS_LEFT;
	private static int score = 0;
	
	public static int getLevel() {
		return level;
	}
	
	public static void setLevel(int level) {
		GameState.level = level;
	}
	
	public static int getBallsLeft() {
		return ballsLeft;
	}
	
	public static void setBallsLeft(int ballsLeft) {
		GameState.ballsLeft = ballsLeft;
	}
	
	public static int getSpaceBombsLeft() {
		return spaceBombsLeft;
	}
	
	public static void setSpaceBombsLeft(int spaceBombsLeft) {
		GameState.spaceBombsLeft = spaceBombsLeft;
	}
	
	public static int getScore() {
		return score;
	}
	
	public static void setScore(int score) {
		GameState.score = score;
	}
	
	public static void changeScore(int delta) {
		GameState.score += delta;
		
		checkForBonusBall();
	}
	
	public static void decrementBallsLeft() {
		GameState.ballsLeft--;		
	}
	
	public static void incrementBallsLeft() {
		GameState.ballsLeft++;		
	}	
	
	public static void decrementSpaceBombsLeft() {
		GameState.spaceBombsLeft--;
	}

	public static void incrementSpaceBombsLeft() {
		GameState.spaceBombsLeft++;
	}
	
	private static void checkForBonusBall() {
		if (GameState.score > 0 && 
			(GameState.score % Config.BONUS_BALL_SCORE) == 0) {
			
			GameState.ballsLeft++;
			
			TextAnimationManager.getInstance().add("Bonus Ball!");
			SoundLayer.playSound(Sounds.BONUS_BALL);
		}		
	}
}
