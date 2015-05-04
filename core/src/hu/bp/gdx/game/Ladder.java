package hu.bp.gdx.game;

public class Ladder extends CanCollide {

	int floor = 0;

	public Ladder(float x, int floor) {
		super(
			x, BrickUtils.getYCoordOfFloor(floor),
			Const.TILE_SIZE, Const.TILE_SIZE * Const.FLOOR_HEIGHT);
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

	@Override
	public void die() {
	}

	@Override
	public boolean isAlive() {
		return true;
	}

	@Override
	public float getLadderTolerance() {
		return 0;
	}

	@Override
	public float getFloorTolerance() {
		return 0;
	}
}
