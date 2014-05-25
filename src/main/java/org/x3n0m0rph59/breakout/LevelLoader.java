package org.x3n0m0rph59.breakout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;


public class LevelLoader {
	
	public static HashMap<String, String> getLevelMetaData(int level) {
		HashMap<String, String> data = new HashMap<>();
		
		BufferedReader reader = null;
		
		try {			
			InputStream in = LevelLoader.class.getClassLoader().getResourceAsStream("levels/level" + String.format("%02d", level) + ".lvl");			
			reader = new BufferedReader(new InputStreamReader(in));			

			String line;
			
			while ((line = reader.readLine()) != null) {
				boolean commentLine = false;
				int commentCharIndex = 0;
				
				charLoop:
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					
					switch (c) {
					case '#':
						// '#' comment char, ignore everything until next line
						commentLine = true;
						commentCharIndex = i;
						break charLoop;
					}
				}
				
				if (commentLine) {					
					String[] pair = new String(line.substring(commentCharIndex + 1)).split(":");
					
					if (pair.length == 2) {
						data.put(pair[0].trim(), pair[1].trim());
					} else {
						throw new RuntimeException("Malformed level metadata: " + line);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}		
		
		return data;
	}

	public static List<Brick> loadLevel(int level) {
		List<Brick> bricks = new ArrayList<Brick>();
		
		BufferedReader reader = null;
		
		try {			
			InputStream in = LevelLoader.class.getClassLoader().getResourceAsStream("levels/level" + String.format("%02d", level) + ".lvl");			
			reader = new BufferedReader(new InputStreamReader(in));			

			String line;
			int lindex = 0, cindex = 0;						
			
			while ((line = reader.readLine()) != null) {
				Brick.Type type = null;
				EnumSet<Brick.Behavior> behavior = EnumSet.noneOf(Brick.Behavior.class);
				
				final int numberOfBricksInLine = getNumberOfBrickDefs(line);
				
				char lastBehaviourModifier = '#';
				int multiplier = 1;
				boolean commentLine = false,
						modifierChar = false;
				
				charLoop:
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);

					modifierChar = false;
					
					switch (c) {
					case '#':
						// '#' comment char, ignore everything until next line
						commentLine = true;
						modifierChar = true;						
						break charLoop;
						
					case '\'':
						// ''' reset behavior to normal
						behavior = EnumSet.noneOf(Brick.Behavior.class);
						
						multiplier = 1;
						
						lastBehaviourModifier = c;
						modifierChar = true;
						break;
						
					case '<':
						// '<' move left
						if (behavior.contains(Brick.Behavior.MOVE_RIGHT))
							behavior.remove(Brick.Behavior.MOVE_RIGHT);
						
						behavior.add(Brick.Behavior.MOVE_LEFT);
						
						if (c == lastBehaviourModifier)
							multiplier *= Config.BRICK_MOVEMENT_MULTIPLIER;
						else
							multiplier = 1;
						
						lastBehaviourModifier = c;
						modifierChar = true;
						continue;
						
					case '>':
						// '>' move right
						if (behavior.contains(Brick.Behavior.MOVE_LEFT))
							behavior.remove(Brick.Behavior.MOVE_LEFT);
						
						behavior.add(Brick.Behavior.MOVE_RIGHT);
						
						if (c == lastBehaviourModifier)
							multiplier *= Config.BRICK_MOVEMENT_MULTIPLIER;
						else
							multiplier = 1;
						
						lastBehaviourModifier = c;
						modifierChar = true;
						continue;
						
					case '(':
						// '(' rotate right
						if (behavior.contains(Brick.Behavior.ROTATE_LEFT))
							behavior.remove(Brick.Behavior.ROTATE_LEFT);
						
						behavior.add(Brick.Behavior.ROTATE_RIGHT);
						
						if (c == lastBehaviourModifier)
							multiplier *= Config.BRICK_MOVEMENT_MULTIPLIER;
						else
							multiplier = 1;
						
						lastBehaviourModifier = c;
						modifierChar = true;
						continue;
						
					case ')':						
						// '(' rotate left
						if (behavior.contains(Brick.Behavior.ROTATE_RIGHT))
							behavior.remove(Brick.Behavior.ROTATE_RIGHT);
						
						behavior.add(Brick.Behavior.ROTATE_LEFT);
						
						if (c == lastBehaviourModifier)
							multiplier *= Config.BRICK_MOVEMENT_MULTIPLIER;
						else
							multiplier = 1;
						
						lastBehaviourModifier = c;
						modifierChar = true;
						continue;
					
					// Brick types
					case ' ':												
					case 'X':
						// Whitespace and 'X' means "no brick at that position"
						cindex++;
						modifierChar = false;
						continue;
						
					case 'N':
						type = Brick.Type.NORMAL;
						modifierChar = false;
						break;
						
					case 'W':
						type = Brick.Type.WEAK;
						modifierChar = false;
						break;
						
					case 'H':
						type = Brick.Type.HARD;
						modifierChar = false;
						break;
						
					case 'S':
						type = Brick.Type.SOLID;
						modifierChar = false;
						break;
						
					case 'P':
						type = Brick.Type.POWERUP;
						modifierChar = false;
						break;
					}
					
					final float BRICK_WIDTH = Math.abs(((Config.getInstance().getClientWidth() - 
											   (2 * Config.BRICK_OFFSET_X) - 
											   (numberOfBricksInLine * Config.BRICK_SPACING_X))) / 
											    numberOfBricksInLine);
					final float BRICK_HEIGHT = Config.BRICK_HEIGHT;
					
					final float BRICK_SPEED = Config.BRICK_MOVEMENT_SPEED * multiplier;
					
					float ANGULAR_VELOCITY = 0.0f;
					if (behavior.contains(Brick.Behavior.ROTATE_LEFT) || behavior.contains(Brick.Behavior.ROTATE_RIGHT))					
						ANGULAR_VELOCITY = Config.BRICK_MOVEMENT_SPEED * multiplier;
										
					bricks.add(new Brick(type, behavior, BRICK_SPEED, ANGULAR_VELOCITY,
										 new Point((cindex * (BRICK_WIDTH  + Config.BRICK_SPACING_X)) + Config.BRICK_OFFSET_X, 
												   (lindex * (BRICK_HEIGHT + Config.BRICK_SPACING_Y)) + Config.BRICK_OFFSET_Y), 
									     BRICK_WIDTH, BRICK_HEIGHT));
					
					if (!modifierChar)
						cindex++;			
				}
				
				if (!commentLine)
					lindex++;
				
				commentLine = false;	
				cindex = 0;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		return bricks;
	}

	private static int getNumberOfBrickDefs(String line) {
		int cnt = 0;
		
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			
			switch (c) {
			case ' ':
			case 'X':
				
			case 'N':
				
			case 'W':
				
			case 'H':
				
			case 'S':
				
			case 'P':
				cnt++;
				break;
			}
		}
		
		return cnt;
	}

//	private static float getNumberOfModifierChars(String line) {
//		int cnt = 0;
//		
//		for (int i = 0; i < line.length(); i++) {
//			char c = line.charAt(i);
//			
//			switch (c) {
//			case '#':
//	
//			case '\'':
//				
//			case '<':
//				
//			case '>':
//				
//			case '(':
//				
//			case ')':
//				cnt++;
//				break;
//			}
//		}
//		
//		return cnt;
//	}
}
