package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class LadderManager {

	public static final int NUM_OF_LADDERS = 2; // on every floor

	private Ladder[][] ladders =
		new Ladder[Const.NUM_OF_FLOORS][NUM_OF_LADDERS];

	/**
	 * @param object
	 * @return with a ladder which can be reached upwards or null
	 * if there is no such ladder
	 */
	public Ladder getLadderUp(CanCollide object) {
		int floor = BrickUtils.getFloorOfCoord(object.y);

		Rectangle rec = object.getBoundary();
		rec.height += object.getLadderTolerance();

		return getLadder(rec, floor);
	}

	/**
	 * @param object
	 * @return with a ladder which can be reached downwards or null
	 * if there is no such ladder
	 */
	public Ladder getLadderDown(CanCollide object) {
		int floor = BrickUtils.getFloorOfCoord(object.y) - 1;

		Rectangle rec = object.getBoundary();
		rec.y -= object.getLadderTolerance();

		return getLadder(rec, floor);
	}

	/**
	 * @param rec
	 * @return arrays of ladders which overlaps with the rectangle
	 * no matter on which floor is it (up or down ladder)
	 */
	private Array<Ladder> getLadders(Rectangle rec) {
		Array<Ladder> l = new Array<Ladder>(false, Const.LADDERS_ON_A_FLOOR * 2);
		int floor = BrickUtils.getFloorOfCoord(rec.y);
		Ladder ll = getLadder(rec, floor);
		if (ll != null) l.add(ll);
		ll = getLadder(rec, floor - 1);
		if (ll != null) l.add(ll);

		return l;
	}

	/**
	 * @param rec
	 * @return a ladder which overlaps with the rectangle and is on the
	 * given floor or null
	 */
	private Ladder getLadder(Rectangle rec, int floor) {
		Ladder l = null;

		if (floor >= 0) {
			for (int i = 0; i < NUM_OF_LADDERS; i++) {
				if (ladders[floor][i].getBoundary().overlaps(rec)) {
					l = ladders[floor][i];
					break;
				}
			}
		}

		return l;
	}

	public void setLadder(int floor, int num, int x)
			throws ArrayIndexOutOfBoundsException {
		Gdx.app.log("LadderManager", floor + "," + num + "," + x);
		ladders[floor][num] = new Ladder(x, floor);
	}

	public boolean isOnLadder(CanCollide object) {
		return getLadders(object.getBoundary()).size > 0;
	}


}