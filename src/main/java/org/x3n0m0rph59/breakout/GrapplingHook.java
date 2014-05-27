package org.x3n0m0rph59.breakout;

import org.newdawn.slick.geom.Rectangle;

public class GrapplingHook extends GameObject {
	enum State {IDLE, EXTENDING, FULLY_EXTENDED, LOWERING}
	private State state = State.IDLE;
	
	private GrapplingHookSegment hook 	 = new GrapplingHookSegment(GrapplingHookSegment.Type.HOOK, new Point(0.0f,0.0f));
	private GrapplingHookSegment segment = new GrapplingHookSegment(GrapplingHookSegment.Type.SEGMENT, new Point(0.0f,0.0f));
	
	private float length = 0.0f;
	
	public GrapplingHook(Point position) {
		super(null, position, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		
		hook.setWidth(50.0f);
		hook.setHeight(20.0f);
		
		segment.setWidth(20.0f);
		segment.setHeight(20.0f);
	}
		
	public void toggleSwitch() {
		switch (state) {
		case IDLE:
		case LOWERING:
			setState(State.EXTENDING);
			break;
			
		case EXTENDING:
		case FULLY_EXTENDED:
			setState(State.LOWERING);
			break;
			
		default:
			throw new RuntimeException("Invalid state: " + state);
			
		}
	}
	
	public void resetState() {
		this.state = State.IDLE;
		length = 0.0f;
		SoundLayer.stopLoop(Sounds.GRAPPLING_HOOK_LOOP);
	}
	
	public boolean collisionTest(Rectangle r) {
		return Util.collisionTest(getBoundingBox(), r);			
	}
	
	@Override
	public void render() {		
		if (state != State.IDLE) {
			hook.render();
			
			for (int i = 0; i < (length / segment.getHeight()) - 1; i++) {
				final Point position = new Point(segment.getX(), (hook.getY() + hook.getHeight()) + 
																 (i * segment.getHeight()));
				segment.setPosition(position);
				segment.render();
			}
		}
	}
	
	@Override
	public void step() {
		switch (state) {
		case IDLE:
			// do nothing
			break;
			
		case EXTENDING:
			length += Config.GRAPPLING_HOOK_EXTEND_SPEED;
			
			if (length >= Config.GRAPPLING_HOOK_LENGTH)
				setState(State.FULLY_EXTENDED);
			break;
			
		case FULLY_EXTENDED:
			// do nothing
			break;		
			
		case LOWERING:
			length -= Config.GRAPPLING_HOOK_LOWER_SPEED;
			
			if (length <= 0)
				setState(State.IDLE);
			break;
			
		default:
			throw new RuntimeException("Invalid state: " + state);		
		}
	}
	
	@Override
	public void setPosition(Point position) {				
		final Point hookPos = new Point((position.getX() - (hook.getWidth() / 2)), position.getY() - length);
		final Point segmentPos = new Point(position.getX() - ((hook.getWidth() / 2) - (segment.getWidth() / 2) - 5.0f), 
										   position.getY() - length + hook.getHeight());
		
		hook.setPosition(hookPos);
		segment.setPosition(segmentPos);
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return hook.getBoundingBox();
	}
	
	@Override
	public boolean isExcemptFromSpeedFactorChange() {
		return true;
	}
	
	public Point getHookCenterPoint() {
		return hook.getCenterPosition();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		if (this.state == State.IDLE && state == State.EXTENDING)
			SoundLayer.loopSound(Sounds.GRAPPLING_HOOK_LOOP);
		
		if (this.state == State.FULLY_EXTENDED && state == State.LOWERING)
			SoundLayer.loopSound(Sounds.GRAPPLING_HOOK_LOOP);
		
		if (state == State.IDLE || state == State.FULLY_EXTENDED)
			SoundLayer.stopLoop(Sounds.GRAPPLING_HOOK_LOOP);
					
		this.state = state;
	}
}
