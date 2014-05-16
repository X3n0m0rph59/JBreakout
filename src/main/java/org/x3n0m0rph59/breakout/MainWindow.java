package org.x3n0m0rph59.breakout;

import org.lwjgl.input.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class MainWindow {
	
	private Scene scene;
	
	public MainWindow() {
		Display.setTitle(Config.APP_NAME + " " + Config.APP_VERSION);
		
		this.initOpenGL();
				
		// Try to hide the cursor 
		// not a problem if it fails
		try {
			Cursor emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
			Mouse.setNativeCursor(emptyCursor);
			
			Mouse.setGrabbed(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}		
	}
	
	public void show() {
		scene = new Scene();
		scene.setState(Scene.State.GREETING);
		
		while (!Display.isCloseRequested()) {									
			scene.step();
			scene.render();
			
			Display.update();
			Display.sync(Config.SYNC_FPS);
		}
						
		Display.destroy();
	}
	
	private void initOpenGL() {
		try {
			DisplayMode displayMode = null;
			
			if (Config.FULLSCREEN) {				
		        DisplayMode[] modes = Display.getAvailableDisplayModes();
	
				for (int i = 0; i < modes.length; i++) {
					 if (modes[i].getWidth() == Config.SCREEN_WIDTH && 
					     modes[i].getHeight() == Config.SCREEN_HEIGHT && 
					     modes[i].isFullscreenCapable()) {
						 
					        displayMode = modes[i];
					 }
				}
			} else {
				displayMode = new DisplayMode((int) Config.SCREEN_WIDTH, (int) Config.SCREEN_HEIGHT);
			}
			
			Display.setDisplayModeAndFullscreen(displayMode);	
			Display.create();			
			Display.setVSyncEnabled(true);
			
			//GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);       
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GL11.glDisable(GL11.GL_CULL_FACE);
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glViewport(0, 0, (int) Config.SCREEN_WIDTH, (int) Config.SCREEN_HEIGHT);
			GL11.glOrtho(0, (int) Config.SCREEN_WIDTH, (int) Config.SCREEN_HEIGHT, 0, 0, 128);					
			
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
