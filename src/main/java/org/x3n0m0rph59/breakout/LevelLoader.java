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
			file = new FileReader("./data/levels/level0" + level + ".lvl");
			reader = new BufferedReader(file);
			
			int lindex = 0;
			String line;
			while ((line = reader.readLine()) != null) {				
				for (int i = 0; i < line.length() - 1; i++) {
					char c = line.charAt(i);
					
					Brick.Type type = null;
					
					switch (c) {
					case ' ':
						// Whitespace means "no brick at that position"
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
					
					bricks.add(new Brick(type, (i * 40) + 40, (lindex * 30) + 40, 35f, 20f));
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
