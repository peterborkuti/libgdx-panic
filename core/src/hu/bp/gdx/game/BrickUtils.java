package hu.bp.gdx.game;

public class BrickUtils {
	public static float getYCoordOfFloor(int floor) {
		return floor * Const.FLOOR_HEIGHT * Const.TILE_SIZE + Const.TILE_SIZE;
	}

	public static int getFloorOfCoord(float y) {
		y -= Const.TILE_SIZE;
		return (int) Math.floor(y / Const.TILE_SIZE / Const.FLOOR_HEIGHT);
	}

	public static boolean isOnFloor(float y, int tolerance) {
		float floorY = getYCoordOfFloor(getFloorOfCoord(y));

		return (Math.abs(floorY - y) <= tolerance);
	}

	public static float alignIfOnFloor(float y, int tolerance) {
		float floorY = getYCoordOfFloor(getFloorOfCoord(y));

		if (Math.abs(floorY - y) <= tolerance) {
			y = floorY;
		}

		return y;
	}

}
