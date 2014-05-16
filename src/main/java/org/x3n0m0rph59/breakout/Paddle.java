package org.x3n0m0rph59.breakout;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Paddle extends GameObject {
	private float x, y, width = Config.PADDLE_DEFAULT_WIDTH, height = Config.PADDLE_HEIGHT;	
	private float lastX = 0, lastY = 0;
	
	@Override
	public void render() {
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1.0f, 0.0f, 0.0f);			
			GL11.glVertex2f(x, y);
			GL11.glColor3f(0.0f, 1.0f, 0.0f);
			GL11.glVertex2f(x + width, y);
			GL11.glColor3f(0.0f, 0.0f, 1.0f);
			GL11.glVertex2f(x + width, y + height);
			GL11.glColor3f(1.0f, 1.0f, 0.0f);
			GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}
	
	@Override
	public void step() {
		// Clamp mouse to client rect to avoid 
		// choppy paddle movement
		int new_x = Mouse.getX(), 
			new_y = Mouse.getY();
		
		if (new_x > Config.CLIENT_WIDTH)
			new_x = (int) (Config.CLIENT_WIDTH);
		
		Mouse.setCursorPosition(new_x, new_y);
		
		setCenteredPosition(Mouse.getX(), Mouse.getY());
	}
	
	public void setCenteredPosition(float x, float y) {
		this.x = x - width / 2;		
		this.y = Config.PADDLE_DEFAULT_Y;
				
		if (this.x < 0) 
			this.x = 0;
		
		if (this.x > Config.CLIENT_WIDTH - this.width) 
			this.x = Config.CLIENT_WIDTH - this.width;
	}

	@Override
	public Rectangle getBoundingBox() {		
		return new Rectangle(x, y, width, height);
	}
	
	public void expand() {
		this.width += Config.PADDLE_EXPANSION;
	}
	
	public void shrink() {
		this.width -= Config.PADDLE_EXPANSION;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getdX() {
		float result = x - lastX;		
		lastX = x;
		
		return result;
	}
	
	public float getdY() {
		float result = y - lastY;		
		lastY = y;		
		
		return result;
	}
}
