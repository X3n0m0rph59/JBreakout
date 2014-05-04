package org.x3n0m0rph59.breakout;

import org.x3n0m0rph59.breakout.SoundLayer.Sounds;

import org.lwjgl.input.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class MainWindow {
	
	private Scene scene;
	
	public MainWindow() {
		this.initOpenGL();
				
		// Try to hide the cursor 
		// not a problem if it fails
		try {
			Cursor emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
			Mouse.setNativeCursor(emptyCursor);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}		
		
		this.scene = new Scene();
	}
	
	public void show() {
		SoundLayer.playSound(Sounds.WELCOME);		
		
		while (!Display.isCloseRequested()) {									
			scene.step();
			scene.render();
			
			Display.update();
			Display.sync(60);
		}
		
		SoundLayer.playSound(Sounds.QUIT);
		
		Display.destroy();
	}
	
	private void initOpenGL() {
		try {
			Display.setDisplayModeAndFullscreen(new DisplayMode(1024, 768));
			Display.create();
			Display.setVSyncEnabled(true);
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glViewport(0, 0, 1024, 768);
			GL11.glOrtho(0, 1024, 768, 0, 0, 128);					
			
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();					
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}			
	}
	
	public Scene getScene() {
		return scene;
	}
}
