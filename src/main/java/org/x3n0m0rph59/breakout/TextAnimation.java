package org.x3n0m0rph59.breakout;

import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class TextAnimation implements Renderable, Stepable, Destroyable {
	private String text;
	private Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	
	private TrueTypeFont font;
	
	private int frameCounter = 0;
	private boolean destroyed = false;
	
	
	public TextAnimation(String text) {
		this.text = text;
		font = FontLoader.getInstance().getFont("Verdana", Font.BOLD, 44);
	}
	
	@Override
	public void render() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		int w = font.getWidth(text);		
		font.drawString(((int) Config.getInstance().getClientWidth() / 2) - (w / 2), 
						 (int) Config.getInstance().getScreenHeight() / 2, text, color);
			
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void step() {
		frameCounter++;

		color.a -= 0.015f;
		
		if (frameCounter > 60 * Config.TOAST_DELAY || color.a <= 0.0f) {
			setDestroyed(true);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
}
