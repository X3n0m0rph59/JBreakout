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
		if (!Config.getInstance().isDebugging()) {
			try {
				Cursor emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
				Mouse.setNativeCursor(emptyCursor);
				
				Mouse.setGrabbed(true);
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void show() {
		scene = new Scene();
		scene.setState(Scene.State.NEW_STAGE);
		
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

			if (Config.getInstance().isDebugging()) {
				displayMode = new DisplayMode(
						(int) Config.FORCE_SCREEN_WIDTH,
						(int) Config.FORCE_SCREEN_HEIGHT);				
			} else if (Config.getInstance().isWindowed()) {				
				if (Config.getInstance().useUserSpecifiedVideoMode()) {
					DisplayMode[] modes = Display.getAvailableDisplayModes();
					
					// use matching windowed (non-fullscreen) mode
					final int xres = Config.getInstance().getRequestedXres(), 
							  yres = Config.getInstance().getRequestedYres();
					for (int i = 0; i < modes.length; i++) {					
						if (modes[i].getWidth() == xres && 
							modes[i].getHeight() == yres) {
							
							if (!modes[i].isFullscreenCapable()) {
								displayMode = modes[i];							
								break;
							}
						}
					}	
					
					// try harder if we failed until here
					if (displayMode == null) {
						Logger.log("Display mode enumeration failed, forcing mode", 1);
						displayMode = new DisplayMode(xres, yres);
					}
				} else {
					DisplayMode[] modes = Display.getAvailableDisplayModes();
	
					// use largest windowed (non-fullscreen) mode
					int xres = 0, yres = 0;
					for (int i = 0; i < modes.length; i++) {					
						if (modes[i].getWidth() > xres && 
							modes[i].getHeight() > yres) {
							
//							if (!modes[i].isFullscreenCapable()) {
								displayMode = modes[i];
//							}
							
							xres = displayMode.getWidth();
							yres = displayMode.getHeight();
						}
					}
					
					// try harder if we failed until here
					if (displayMode == null) {
						Logger.log("Display mode enumeration failed, forcing mode", 1);
						displayMode = new DisplayMode(xres, yres);
					}
				}
			} else {
				if (Config.getInstance().useUserSpecifiedVideoMode()) {
					DisplayMode[] modes = Display.getAvailableDisplayModes();
					
					// use matching fullscreen mode
					final int xres = Config.getInstance().getRequestedXres(), 
							  yres = Config.getInstance().getRequestedYres();
					for (int i = 0; i < modes.length; i++) {					
						if (modes[i].getWidth() == xres && 
							modes[i].getHeight() == yres) {
							
							if (modes[i].isFullscreenCapable()) {
								displayMode = modes[i];							
								break;
							}
						}
					}										
				} else {
					DisplayMode[] modes = Display.getAvailableDisplayModes();
	
					// use largest fullscreen mode
					int xres = 0, yres = 0;
					for (int i = 0; i < modes.length; i++) {
						if (modes[i].isFullscreenCapable()) {
							if (modes[i].getWidth() > xres && 
								modes[i].getHeight() > yres) {
								
								displayMode = modes[i];
	
								xres = displayMode.getWidth();
								yres = displayMode.getHeight();
							}
						}
					}
				}
			}
			
			if (displayMode == null) {
				System.err.println("Unable to set video mode");
				System.exit(1);
			}
			
			Config.getInstance().setScreenHeight(displayMode.getHeight());
			Config.getInstance().setScreenWidth(displayMode.getWidth());

			Logger.log("Using mode: "
					+ (int) Config.getInstance().getScreenWidth() + "x"
					+ (int) Config.getInstance().getScreenHeight(), 1);
			
			Display.setDisplayModeAndFullscreen(displayMode);	
			Display.create();			
			Display.setVSyncEnabled(true);
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);       
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.0f);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);			
			
			GL11.glDisable(GL11.GL_CULL_FACE);
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glViewport(0, 0, (int) Config.getInstance().getScreenWidth(), (int) Config.getInstance().getScreenHeight());
			GL11.glOrtho(0, (int) Config.getInstance().getScreenWidth(), (int) Config.getInstance().getScreenHeight(), 0, 0, 128);					
			
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
