package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;

public class LadderManager {

	public static final int NUM_OF_LADDERS = 2; // on every floor

	private int[][] ladders =  new int[Const.NUM_OF_FLOORS][NUM_OF_LADDERS];

	public Ladder getLadder(int floor, int num) {
		return new Ladder(ladders[floor][num], floor);
	}

	public void setLadder(int floor, int num, int x) {
		Gdx.app.log("LadderManager", floor + "," + num + "," + x);
		ladders[floor][num] = x;
	}

	public boolean canGoDown(float x, float y, int tolerance) {
		int floor = BrickUtils.getFloorOfCoord(y) - 1;

		return (floor > 0) && isLadder(floor, x, tolerance);
	}

	public Ladder getLadder(float x, int floor, int tolerance) {
		Ladder l = null;

		if (floor >= 0) {
			for (int i = 0; i < NUM_OF_LADDERS; i++) {
				int x0 = ladders[floor][i];
				if ((x >= x0 && (x - x0) <= tolerance)) {
					l = new Ladder(x0, floor);
					break;
				}
			}
		}

		return l;
	}

	public Ladder getLadderDown(float x, float y, int tolerance) {
		int floor = BrickUtils.getFloorOfCoord(y) - 1;

		return getLadder(x, floor, tolerance);
	}

	public Ladder getLadderUp(float x, float y, int tolerance) {
		int floor = BrickUtils.getFloorOfCoord(y);

		return getLadder(x, floor, tolerance);
	}

	public boolean canGoUp(float x, float y, int tolerance) {
		int floor = BrickUtils.getFloorOfCoord(y);

		return isLadder(floor, x, tolerance);
	}

	private boolean isLadder(int floor, float x, int tolerance) {

		return 
			checkX(ladders[floor][0], x, tolerance) ||
			checkX(ladders[floor][1], x, tolerance);
	}

	private boolean checkX(int x0, float x, int tolerance) {

		return 
			(x >= x0 && (x - x0) < tolerance); /*&&
			(x < (x0 + Const.TILE_SIZE / 4));*/
	}


}
