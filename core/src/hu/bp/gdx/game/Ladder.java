package hu.bp.gdx.game;

public class Ladder {
	float x = 0;
	int floor = 0;

	public Ladder(float x, int floor) {
		super();
		this.x = x;
		this.floor = floor;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}
}
