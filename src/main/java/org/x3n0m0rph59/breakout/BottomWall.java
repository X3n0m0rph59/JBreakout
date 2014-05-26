package org.x3n0m0rph59.breakout;

import org.lwjgl.opengl.GL11;

public class BottomWall implements Stepable, Renderable {
	private int frameCounter = 0;
	boolean drawFlash = false;

	@Override
	public void render() {		
		for (int i = 0; i <= Config.getInstance().getClientWidth() / (Config.BOTTOM_WALL_SEGMENT_WIDTH + Config.BOTTOM_WALL_SEGMENT_SPACING); i++) {
			float x = i * (Config.BOTTOM_WALL_SEGMENT_WIDTH + Config.BOTTOM_WALL_SEGMENT_SPACING);
			float y = Config.getInstance().getScreenHeight() - Config.BOTTOM_WALL_HEIGHT;
			
			final float width = Config.BOTTOM_WALL_SEGMENT_WIDTH;
			final float height = Config.BOTTOM_WALL_SEGMENT_HEIGHT;
			
			final boolean inGracePeriod = EffectManager.getInstance().isEffectInGracePeriod(EffectType.BOTTOM_WALL);			
			
			if ((frameCounter % (Config.SYNC_FPS * Config.GRACE_PERIOD_BLINK_RATE)) == 0)
				drawFlash = !drawFlash;
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			
			GL11.glBegin(GL11.GL_QUADS);
				if (inGracePeriod && drawFlash)
					GL11.glColor3f(0.0f, 0.0f, 0.0f);
				else
					GL11.glColor3f(1.0f, 0.0f, 0.0f);
				
				GL11.glVertex2f(x, y);			
				GL11.glVertex2f(x + width, y);			
				GL11.glVertex2f(x + width, y + height);			
				GL11.glVertex2f(x, y + height);
			GL11.glEnd();
		}		
	}

	@Override
	public void step() {
		frameCounter++;
	}
}
