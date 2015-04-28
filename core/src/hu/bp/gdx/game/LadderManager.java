package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;

public class LadderManager {

	private int[][] ladders =  new int[Const.NUM_OF_FLOORS][2];

	public void setLadder(int floor, int num, int x) {
		Gdx.app.log("LadderManager", floor + "," + num + "," + x);
		ladders[floor][num] = x;
	}

	public boolean canGoDown(float x, float y) {
		int floor = BrickUtils.getFloorOfCoord(y) - 1;

		return (floor > 0) && isLadder(floor, x);
	}

	public boolean canGoUp(float x, float y) {
		int floor = BrickUtils.getFloorOfCoord(y);

		return isLadder(floor, x);
	}

	private boolean isLadder(int floor, float x) {

		return checkX(ladders[floor][0], x) || checkX(ladders[floor][1], x);
	}

	private boolean checkX(int x0, float x) {

		return 
			(x > (x0 + Const.TILE_SIZE / 4)) &&
			(x < (x0 + 3 * Const.TILE_SIZE / 4));
	}


}
