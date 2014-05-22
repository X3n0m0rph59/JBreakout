package org.x3n0m0rph59.breakout;

import org.lwjgl.input.Mouse;
//import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Rectangle;

public class Paddle extends GameObject {
	private float x, y, width = Config.PADDLE_DEFAULT_WIDTH, height = Config.PADDLE_HEIGHT;	
	private float lastX = 0, lastY = 0;
	
	private Sprite sprite = new Sprite("data/sprites/paddle.png", width, height, 600, 150);
	
	private ParticleSystem leftEngine = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("data/sprites/fire.png", 198.0f, 197.0f, 198, 197)}, 
			   x, y, 10.0f, 5.0f, 0.0f, 25.0f, 0.0f, 15.0f, 10.0f, 8.5f);
	
	private ParticleSystem rightEngine = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("data/sprites/fire.png", 198.0f, 197.0f, 198, 197)}, 
			   x, y, 10.0f, 5.0f, 0.0f, 25.0f, 0.0f, 15.0f, 10.0f, 8.5f);
	
	@Override
	public void render() {
		leftEngine.render();
		rightEngine.render();
		
		sprite.render(x, y, width, height);
		
//		GL11.glBegin(GL11.GL_QUADS);
//			GL11.glColor3f(1.0f, 0.0f, 0.0f);			
//			GL11.glVertex2f(x, y);
//			GL11.glColor3f(0.0f, 1.0f, 0.0f);
//			GL11.glVertex2f(x + width, y);
//			GL11.glColor3f(0.0f, 0.0f, 1.0f);
//			GL11.glVertex2f(x + width, y + height);
//			GL11.glColor3f(1.0f, 1.0f, 0.0f);
//			GL11.glVertex2f(x, y + height);
//		GL11.glEnd();
	}
	
	@Override
	public void step() {
		// Clamp mouse to client rect to avoid 
		// choppy paddle movement
		int new_x = Mouse.getX(), 
			new_y = Mouse.getY();
		
		if (new_x > Config.getInstance().getClientWidth())
			new_x = (int) (Config.getInstance().getClientWidth());
		
		Mouse.setCursorPosition(new_x, new_y);
		
		setCenteredPosition(Mouse.getX(), Mouse.getY());
		
		sprite.step();
		leftEngine.step();
		rightEngine.step();
	}
	
	public void setCenteredPosition(float x, float y) {
		this.x = x - width / 2;		
		this.y = Config.getInstance().getScreenHeight() - Config.PADDLE_BOTTOM_SPACING;
				
		if (this.x < 0) 
			this.x = 0;
		
		if (this.x > Config.getInstance().getClientWidth() - this.width) 
			this.x = Config.getInstance().getClientWidth() - this.width;
		
		updateEnginePosition();
	}

	private void updateEnginePosition() {
		leftEngine.setPosition(this.x + Config.PADDLE_ENGINE_OFFSET, (this.y + this.height), (float) Math.toRadians(180.0f - 80.0f));
		rightEngine.setPosition((this.x + this.width) - Config.PADDLE_ENGINE_OFFSET, (this.y + this.height), (float) Math.toRadians(180.0f - 80.0f));
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
		
		updateEnginePosition();
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		
		updateEnginePosition();
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
		
		updateEnginePosition();
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
		
		updateEnginePosition();
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
