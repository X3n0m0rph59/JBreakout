package org.x3n0m0rph59.breakout;

public class SpriteTuple {
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