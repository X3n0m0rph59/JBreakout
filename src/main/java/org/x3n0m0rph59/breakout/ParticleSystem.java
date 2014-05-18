package org.x3n0m0rph59.breakout;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;

class SpriteTuple {
	private String fileName;
	private float width, height;
	private int tw, th;
		
	public SpriteTuple(String fileName, float width, float height, int tw, int th) {		
		this.fileName = fileName;
		this.width = width;
		this.height = height;
		this.tw = tw;
		this.th = th;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public int getTw() {
		return tw;
	}
	public void setTw(int tw) {
		this.tw = tw;
	}
	public int getTh() {
		return th;
	}
	public void setTh(int th) {
		this.th = th;
	}
}

public class ParticleSystem extends GameObject {	
	private float x, y, lifeTime, particleDensity, angle, angularDeviation, ttl, speed, sizeFactor;
	private int frameCounter = 0;	
	private boolean destroyed = false;
	
	private List<Particle> particles = new LinkedList<>();	
	private List<Sprite> spriteList = new LinkedList<>();

	public ParticleSystem(SpriteTuple[] sprites, float x, float y, float lifeTime, float particleDensity, 
						  float angle, float angularDeviation,  float ttl, float speed, float sizeFactor) {
		this.x = x;
		this.y = y;
		
		this.lifeTime = lifeTime;
		
		this.particleDensity = particleDensity; 
		
		this.angle = angle;
		this.angularDeviation = angularDeviation;
		
		this.ttl = ttl;
		
		this.speed = speed;
		
		this.sizeFactor = sizeFactor;
		
		loadSprites(sprites);
	}
		
	private void loadSprites(SpriteTuple[] sprites) {		
		for (SpriteTuple st : sprites) {
			spriteList.add(new Sprite(st.getFileName(), st.getWidth(), st.getHeight(), st.getTw(), st.getTh()));
		}			
	}

	@Override
	public void render() {
		for (Particle p : particles)
			p.render();
	}

	@Override
	public void step() {
		lifeTime -= 1.0f;
		if (lifeTime <= 0.0f)
			setDestroyed(true);
		
		if ((frameCounter++ % 1) == 0)
			for (int i = 0; i < (int) particleDensity; i++)
				addNewParticle();
		
		Iterator<Particle> pi = particles.iterator();
		while (pi.hasNext()) {
			Particle p = pi.next();
			p.step();
			
			if (p.isDestroyed())
				pi.remove();
		}
	}

	@Override
	public Rectangle getBoundingBox() {
		return null;
	}
	
	private void addNewParticle() {
		final float angularDeviation = (float) Util.random(0, (int) this.angularDeviation);

		final float speed = Util.random((int) 1.0f, (int) this.speed);
		
		final float dx = ((float) Math.cos((angle + Math.toRadians(90)) + Math.toRadians(angularDeviation)) * speed);
		final float dy = ((float) Math.sin((angle - Math.toRadians(90)) + Math.toRadians(angularDeviation)) * speed);
		
		final float ttl = (float) Util.random(0, (int) this.ttl);
		
		Sprite sprite = spriteList.get(Util.random(0, spriteList.size() - 1));
															  
		particles.add(new Particle(sprite, this.x, this.y, dx, dy, ttl, this.sizeFactor));
	}
	
	public void setPosition(float x, float y, float angle) {
		this.x = x;
		this.y = y;
		
		this.angle = angle;
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

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getParticleDensity() {
		return particleDensity;
	}

	public void setParticleDensity(float particleDensity) {
		this.particleDensity = particleDensity;
	}

	public float getAngularDeviation() {
		return angularDeviation;
	}

	public void setAngularDeviation(float angularDeviation) {
		this.angularDeviation = angularDeviation;
	}

	public float getTtl() {
		return ttl;
	}

	public void setTtl(float ttl) {
		this.ttl = ttl;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSizeFactor() {
		return sizeFactor;
	}

	public void setSizeFactor(float sizeFactor) {
		this.sizeFactor = sizeFactor;
	}

	public int getFrameCounter() {
		return frameCounter;
	}

	public void setFrameCounter(int frameCounter) {
		this.frameCounter = frameCounter;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
}
