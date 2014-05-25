package org.x3n0m0rph59.breakout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ParticleSystem extends GameObject {
	private final float initialLifeTime;
	private float lifeTime, particleDensity, angularDeviation, 
				  ttl, particleSpeed, sizeFactor;
	
	private final List<Particle> particles = new LinkedList<>();	
	private final List<Sprite> spriteList = new ArrayList<>();

	public ParticleSystem(final SpriteTuple[] sprites, Point position, float lifeTime, float particleDensity, 
						  float angleInDegrees, float angularDeviation, float angularVelocity, float ttl, float particleSpeed, 
						  float sizeFactor) {
		
		super(null, position, 0.0f, 0.0f, angleInDegrees, angularVelocity, 0.0f, 0.0f);
		
		this.position = position;
		this.angleInDegrees = angleInDegrees;
		this.angularVelocity = angularVelocity;
		
		this.initialLifeTime = lifeTime;
		this.lifeTime = lifeTime;
		
		this.particleDensity = particleDensity; 
		this.angularDeviation = angularDeviation;		
		
		this.ttl = ttl;
		
		this.particleSpeed = particleSpeed;
		
		this.sizeFactor = sizeFactor;
		
		loadSprites(sprites);
	}
		
	private void loadSprites(final SpriteTuple[] sprites) {		
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
		if (initialLifeTime >= 0.0f && lifeTime <= 0.0f) {
			setDestroyed(true);
		} else {		
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
	}
	
	private void addNewParticle() {
		final float angularDeviation = (float) Util.random(0, (int) this.angularDeviation);

		final float speed = Util.random((int) 1.0f, (int) this.particleSpeed);		
		final int 	ttl = Util.random(0, (int) this.ttl);
		
		Sprite sprite = spriteList.get(Util.random(0, spriteList.size() - 1));
															  
		particles.add(new Particle(sprite, position, angleInDegrees, angularDeviation, speed, angularVelocity, ttl, this.sizeFactor));
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

	public float getParticleSpeed() {
		return particleSpeed;
	}

	public void setParticleSpeed(float speed) {
		this.particleSpeed = speed;
	}

	public float getSizeFactor() {
		return sizeFactor;
	}

	public void setSizeFactor(float sizeFactor) {
		this.sizeFactor = sizeFactor;
	}

	public void setPositionAndAngle(final Point position, final float angleInDegrees) {
		this.setPosition(position);
		this.setAngleInDegrees(angleInDegrees);
	}
}
