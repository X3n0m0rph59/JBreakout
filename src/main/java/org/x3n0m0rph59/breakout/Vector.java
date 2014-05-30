package org.x3n0m0rph59.breakout;

public final class Vector {
	private final float x, y, theta;
	
	public Vector(float x, float y, float theta) {
		this.x = x;
		this.y = y;
		
		this.theta = theta;
	}
	
	public Vector add(Vector rhs) {		
		return new Vector(this.x + rhs.x, this.y + rhs.y, theta);
	}
	
	public Vector mult(float scalar) {		
		return new Vector(this.x * scalar, this.y * scalar, theta);
	}
			
	public float dot(Vector rhs) {	
		return (this.x * rhs.x) + (this.y * rhs.y);
	}
	
	public Vector cross(Vector rhs) {	
		return new Vector(rhs.y, -rhs.x, 0.0f);
	}
	
	public Vector normalize() {
		if (magnitude() == 0)
			return this;
		else
			return new Vector(x / magnitude(), y / magnitude(), theta);
	}
	
	public float magnitude() {	
		return (float) Math.sqrt((x * x) + (y * y));
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getTheta() {
		return theta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(theta);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		if (Float.floatToIntBits(theta) != Float.floatToIntBits(other.theta))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vector [x=" + x + ", y=" + y + ", theta=" + theta + "]";
	}
}
