package org.x3n0m0rph59.breakout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

	public static List<Brick> loadLevel(int level) {
		List<Brick> bricks = new ArrayList<Brick>();
		
		FileReader file = null;
		BufferedReader reader = null;
		try {
			
			file = new FileReader("./data/levels/level" + String.format("%02d", level) + ".lvl");
			reader = new BufferedReader(file);

			int lindex = 0;
			String line;
			while ((line = reader.readLine()) != null) {
				Brick.Type type = null;
				Brick.Behaviour behaviour = Brick.Behaviour.NORMAL;
				int multiplier = 1;
				
				for (int i = 0; i < line.length() - 1; i++) {
					char c = line.charAt(i);

					switch (c) {
					// Behavioural modifiers apply linewise
					case '<':
						behaviour = Brick.Behaviour.MOVE_LEFT;
						multiplier *= Config.BRICK_MOVEMENT_MULTIPLIER;
						continue;
						
					case '>':
						behaviour = Brick.Behaviour.MOVE_RIGHT;
						multiplier *= Config.BRICK_MOVEMENT_MULTIPLIER;
						continue;
					
					// Brick types
					case ' ':												
					case 'X':
						// Whitespace and 'X' means "no brick at that position"
						continue;
						
					case 'N':
						type = Brick.Type.NORMAL;
						break;
						
					case 'W':
						type = Brick.Type.WEAK;
						break;
						
					case 'H':
						type = Brick.Type.HARD;
						break;
						
					case 'S':
						type = Brick.Type.SOLID;
						break;
						
					case 'P':
						type = Brick.Type.POWERUP;
						break;
					}	
					
					final float BRICK_WIDTH = ((Config.CLIENT_WIDTH - Config.BRICK_OFFSET_X) / line.length()) - Config.BRICK_SPACING_X;
					final float BRICK_HEIGHT = Config.BRICK_HEIGHT;
					
					final float BRICK_SPEED = Config.BRICK_MOVEMENT_SPEED * multiplier;
					
					bricks.add(new Brick(type, behaviour, BRICK_SPEED, 
										 (i * (BRICK_WIDTH + Config.BRICK_SPACING_X)) + Config.BRICK_OFFSET_X, 
										 (lindex * (BRICK_HEIGHT  + Config.BRICK_SPACING_Y)) + Config.BRICK_OFFSET_Y, 
									     BRICK_WIDTH, BRICK_HEIGHT));
				}
				
				lindex++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		finally {
			try {
				reader.close();
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
				
		return bricks;
	}

}
