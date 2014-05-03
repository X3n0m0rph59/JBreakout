package org.x3n0m0rph59.breakout;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class MainWindow {
	
	private Scene scene;
	
	public MainWindow() {
		this.initOpenGL();
		
		this.scene = new Scene();
	}
	
	public void show() {
		while (!Display.isCloseRequested()) {
			this.render();
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
	
	private void initOpenGL() {
		try {
			Display.setDisplayModeAndFullscreen(new DisplayMode(1024, 768));
			Display.create();
			Display.setVSyncEnabled(true);			
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		GL11.glClearColor(0.2f, 0.0f, 0.0f, 1.0f);
	}
	
	private void render() {
		scene.render();
	}

}
