package hu.bp.gdx.game;

import com.badlogic.gdx.math.Rectangle;

public abstract class CanCollide {

	protected Rectangle boundary = new Rectangle(0,0,0,0);
	protected float x = 0;
	protected float y = 0;
	protected int leftMargin = 0;
	protected int rightMargin = 0;
	protected float aspectRatio = 1f;
	protected int width = 0;
	protected int height = 0;

	public abstract void die();
	public abstract boolean isAlive();

	public void setX(float _x) {
		x = _x;
		countBoundary();
	}

	public void setY(float _y) {
		y = _y;
		countBoundary();
	}


	public void setXY(float _x, float _y) {
		x = _x;
		y = _y;
		countBoundary();
	}
	public CanCollide(int _leftMargin, int _rightMargin, float _aspectRatio, int _width, int _height) {
		leftMargin = _leftMargin;
		rightMargin = _rightMargin;
		aspectRatio = _aspectRatio;
		width = _width;
		height = _height;
		x = 0;
		y = 0;
		countBoundary();
	}

	public CanCollide(int x, int y, int _width, int _height) {
		leftMargin = 0;
		rightMargin = 0;
		aspectRatio = 1f;
		width = _width;
		height = _height;
		this.x = x;
		this.y = y;
		countBoundary();
	}

	public Rectangle getBoundary() {
		return boundary;
	}

	protected void countBoundary() {
		boundary.x = x + leftMargin * aspectRatio;
		boundary.y = y;
		boundary.width = (width - leftMargin - rightMargin) * aspectRatio;
		boundary.height = height * aspectRatio;
	}


}
