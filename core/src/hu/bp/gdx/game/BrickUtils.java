package hu.bp.gdx.game;

public class BrickUtils {
	public static float getYCoordOfFloor(int floor) {
		return floor * Const.FLOOR_HEIGHT * Const.TILE_SIZE + Const.TILE_SIZE;
	}

	public static int getFloorOfCoord(float y) {
		y -= Const.TILE_SIZE;
		return (int) Math.ceil(y / Const.FLOOR_HEIGHT / Const.TILE_SIZE);
	}

}
