package org.x3n0m0rph59.breakout;

import org.lwjgl.input.Mouse;

public class Paddle extends GameObject {	
	private float lastX = 0.0f, lastY = 0.0f;
	
	boolean drawFlash = false;
	
	private final ParticleSystem leftEngine = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("sprites/fire.png", 198.0f, 197.0f, 198, 197)}, 
			    new Point(0.0f, 0.0f), -1.0f, 15.0f, 0.0f, 25.0f, 0.0f, 15.0f, 10.0f, 8.5f);
	
	private final ParticleSystem rightEngine = new ParticleSystem(new SpriteTuple[]{new SpriteTuple("sprites/fire.png", 198.0f, 197.0f, 198, 197)}, 
				new Point(0.0f, 0.0f), -1.0f, 15.0f, 0.0f, 25.0f, 0.0f, 15.0f, 10.0f, 8.5f);

	private GrapplingHook grapplingHook;
	
	public Paddle() {
		super(new Sprite("sprites/paddle.png", Config.PADDLE_DEFAULT_WIDTH, Config.PADDLE_HEIGHT, 600, 150), 
			  new Point((Config.getInstance().getClientWidth() / 2) - (Config.PADDLE_DEFAULT_WIDTH / 2), 
					    (Config.getInstance().getScreenHeight() - Config.PADDLE_BOTTOM_SPACING)), 
			  Config.PADDLE_DEFAULT_WIDTH, Config.PADDLE_HEIGHT, 0.0f, 0.0f, 0.0f, 0.0f);
		
		grapplingHook = new GrapplingHook(new Point(0.0f, 0.0f));
	}
	
	@Override
	public void render() {
		leftEngine.render();
		rightEngine.render();
		
		getGrapplingHook().render();
		
		final boolean inGracePeriod = EffectManager.getInstance().isEffectInGracePeriod(EffectType.ENLARGE_PADDLE) || 
									  EffectManager.getInstance().isEffectInGracePeriod(EffectType.SHRINK_PADDLE);
		
		if (inGracePeriod && drawFlash)
			getSprite().setFlashed(true);
		else
			getSprite().setFlashed(false);
		
		super.render();
	}
	
	@Override
	public void step() {
		// Clamp mouse to client rect to avoid 
		// choppy paddle movement
		if (!Config.getInstance().isWindowed()) {
			// TODO: fix this
			int new_x = Mouse.getX(), 
				new_y = Mouse.getY();
			
			if (new_x > Config.getInstance().getClientWidth())
				new_x = (int) (Config.getInstance().getClientWidth());
			
			Mouse.setCursorPosition(new_x, new_y);
			
			setCenteredPosition(new Point(Mouse.getX(), Mouse.getY()));
		}
		
		leftEngine.step();
		rightEngine.step();
		
		grapplingHook.step();
		
		if ((frameCounter % (Config.SYNC_FPS * Config.GRACE_PERIOD_BLINK_RATE)) == 0)
			drawFlash = !drawFlash;
		
		super.step();
	}
	
	public void setCenteredPosition(Point position) {
		setPosition(new Point(position.getX() - (getWidth() / 2), Config.getInstance().getScreenHeight() - Config.PADDLE_BOTTOM_SPACING));
		
		if (getX() < 0) 
			setPosition(new Point(0.0f, Config.getInstance().getScreenHeight() - Config.PADDLE_BOTTOM_SPACING));
		
		if (getX() > Config.getInstance().getClientWidth() - getWidth()) 
			setPosition(new Point(Config.getInstance().getClientWidth() - getWidth(), 
								  Config.getInstance().getScreenHeight() - Config.PADDLE_BOTTOM_SPACING));
	}

	private void updateEnginePosition() {
		leftEngine.setPositionAndAngle(new Point(getX() + Config.PADDLE_ENGINE_OFFSET, getY() + getHeight()), 140);
		rightEngine.setPositionAndAngle(new Point((getX() + getWidth()) - Config.PADDLE_ENGINE_OFFSET, getY() + getHeight()), 140);
	}
	
	private void updateGrapplingHookPosition() {
		grapplingHook.setPosition(new Point(getX() + getWidth() / 2, getY()));
	}
	
	public void expand() {
		this.width += Config.PADDLE_EXPANSION;
	}
	
	public void shrink() {
		this.width -= Config.PADDLE_EXPANSION;
	}

	@Override
	public void setPosition(Point position) {
		super.setPosition(position);
		
		updateEnginePosition();
		updateGrapplingHookPosition();
	}

	public float getdX() {
		float result = getX() - lastX;		
		lastX = getX();
		
		return result;
	}
	
	public float getdY() {
		float result = getY() - lastY;		
		lastY = getY();
		
		return result;
	}

	public Point getCenterPoint() {
		return new Point(getBoundingBox().getCenterX(), getBoundingBox().getCenterY());
	}

	public GrapplingHook getGrapplingHook() {
		return grapplingHook;
	}
}
