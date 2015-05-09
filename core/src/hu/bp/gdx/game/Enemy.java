/**
 * 
 */
package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author peter
 *
 */
public class Enemy extends CanCollide implements Movable {

	public static final float animSpeed = 0.1f; // second / frame
	public static final float aspectRatio = 1f; // sprite was too small
	public static final float animVelocity = 5.0f * aspectRatio; // pixel moving
																// / frame
	private static int LADDER_TOLERANCE = 8; // pixels
	private static final int LAST_STATE_COUNTER_MAX = 10;
	private static final int LEFT_MARGIN = 3; // empty margin in pixels
	private static final int RIGHT_MARGIN = 2; // empty margin in pixels
	// Last X coordinate of the enemy
	private static final float RIGHT_WORLD_BOUNDARY = Const.WORLD_WIDTH_UNIT
			- (Const.TILE_SIZE - RIGHT_MARGIN) * aspectRatio;
	private static final float LEFT_WORLD_BOUNDARY = - LEFT_MARGIN * aspectRatio;
	private boolean active = false;

	private Animation animation;
	private TextureRegion currentFrame;

	/**
	 * How many levels I falled in one soar If it is >= game.level
	 */
	private int falledLevels;
	private TiledForeGround foreGround;

	private BrickGame game;

	private LadderManager ladders;
	private STATE lastState = STATE.LEFT;

	private int lastStateCounter = 0;

	private STATE lastStateSaver = STATE.NONE;
	private Nerd nerd;
	private STATE state = STATE.RIGHT;
	private float stateTime = 0;

	public Enemy(BrickGame game, LadderManager ladders,
			TiledForeGround foreGround, Nerd nerd) {
		super(3, 2, aspectRatio, Const.TILE_SIZE, Const.TILE_SIZE);

		TextureRegion[][] tmp = TextureRegion
				.split(game.enemySheet, game.enemySheet.getWidth() / 5,
						game.enemySheet.getHeight() / 1);
		TextureRegion[] frames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			frames[i] = tmp[0][i];
		}

		animation = new Animation(animSpeed, frames);
		this.ladders = ladders;
		this.foreGround = foreGround;
		this.nerd = nerd;
		this.game = game;
		reset(0);
	}

	private void decideClimb() {
		if (Math.abs(nerd.y - y) <= 1) {
			return;
		}

		if (nerd.y < y) {
			Ladder down = ladders.getNearestDownLadder(this);
			if (down != null)
				startClimb(down, STATE.DOWN);
		} else if (nerd.y > y) {
			Ladder up = ladders.getNearestUpLadder(this);
			if (up != null)
				startClimb(up, STATE.UP);
		}
	}

	public void die() {
		active = false;
	}

	@Override
	public float getFloorTolerance() {
		return 0;
	}

	public TextureRegion getFrame() {
		currentFrame = animation.getKeyFrame(stateTime, true);

		return currentFrame;
	}

	public float getHeight() {
		return currentFrame.getRegionHeight() * aspectRatio;
	}

	@Override
	public float getLadderTolerance() {
		return LADDER_TOLERANCE;
	}

	public float getWidth() {
		return currentFrame.getRegionWidth() * aspectRatio;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	@Override
	public boolean isAlive() {
		return active;
	}

	private void setRandomHorizontalAutoMove() {
		if ((RIGHT_WORLD_BOUNDARY - x) < Const.TILE_SIZE) {
			state = STATE.LEFT;
		}
		else if ((LEFT_WORLD_BOUNDARY - x) < Const.TILE_SIZE) {
			state = STATE.RIGHT;
		}
		else {
			state = (Math.random() < 0.5) ? STATE.RIGHT : STATE.LEFT;
		}

		if (state == STATE.RIGHT) {
			startAutoMove(RIGHT_WORLD_BOUNDARY, y);
		}
		else {
			startAutoMove(LEFT_WORLD_BOUNDARY, y);
		}
	}

	private void doIfGoalReached() {
		if (state == STATE.FALL) {
			falledLevels++;
			Tile tile = foreGround.getCell(x + LEFT_MARGIN, y
					- Const.TILE_SIZE / 2);

			if (TiledForeGround.TYPE.none == tile.getType()) {
				float goalY = BrickUtils.getYCoordOfFloor(BrickUtils
						.getFloorOfCoord(y) - 1);

				startAutoMove(tile.getRec().x, goalY);
			} else {
				if (falledLevels >= game.level) {
					die();
				} else {
					falledLevels = 0;
				}

				setRandomHorizontalAutoMove();
			}
		}
		else if (state == STATE.UP || state == STATE.DOWN) {
			setRandomHorizontalAutoMove();
		}
		else if (state == STATE.RIGHT) {
			state = STATE.LEFT;
			startAutoMove(LEFT_WORLD_BOUNDARY, y);
		}
		else if (state == STATE.LEFT) {
			state = STATE.RIGHT;
			startAutoMove(RIGHT_WORLD_BOUNDARY, y);
		}
		else {
			Gdx.app.log("Enemy", "bad state:" + state);
		}
	}

	private void doMove(float delta) {
		autoMove(delta);

		if (goalReached()) {
			doIfGoalReached();
		}
	}

	public void move(float deltaTime) {
		if (lastStateSaver == lastState) {
			lastStateCounter++;
		}
		if (lastStateCounter > LAST_STATE_COUNTER_MAX) {
			lastStateCounter = 0;
			lastStateSaver = STATE.STOP;
			lastState = STATE.NONE;
		}

		stateTime += deltaTime;

		float delta = deltaTime / animSpeed * animVelocity;

		doMove(delta);

		if (!isAlive()) {
			return;
		}

		float checkX = (state == STATE.LEFT) ? x + LEFT_MARGIN : x
				+ Const.TILE_SIZE - RIGHT_MARGIN;

		Tile tile = foreGround.getCell(checkX, y - Const.TILE_SIZE / 2);

		if (state != STATE.FALL && TiledForeGround.TYPE.none == tile.getType()) {
			state = STATE.FALL;
			falledLevels = 0;
			float goalY = BrickUtils
					.getYCoordOfFloor(BrickUtils.getFloorOfCoord(y) - 1);

			startAutoMove(tile.getRec().x, goalY);
		} else if ((state == STATE.LEFT) || (state == STATE.RIGHT)) {
			decideClimb();
		}

		countBoundary();
	}

	public void reset() {
		reset(1, Const.NUM_OF_FLOORS - 1);
	}

	public void reset(int floor) {
		x = MathUtils.random(0, Const.WORLD_WIDTH_UNIT - Const.TILE_SIZE
				* aspectRatio);

		y = BrickUtils.getYCoordOfFloor(floor);

		stateTime = 0;
		active = true;
		falledLevels = 0;

		if (x < Const.WORLD_WIDTH_UNIT / 2) {
			state = STATE.RIGHT;
			startAutoMove(RIGHT_WORLD_BOUNDARY, y);
		} else {
			state = STATE.LEFT;
			startAutoMove(LEFT_WORLD_BOUNDARY, y);
		}
		countBoundary();
	}

	public void reset(int floor1, int floor2) {
		int floor = MathUtils.random(floor1, floor2);
		reset(floor);
	}

	@Override
	public void setLastState(STATE state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setState(STATE state) {
		// TODO Auto-generated method stub

	}

	private void startClimb(Ladder ladder, STATE state) {
		float goalY;
		if (state == STATE.DOWN) {
			goalY = BrickUtils.getYCoordOfFloor(ladder.getFloor());
		} else {
			goalY = BrickUtils.getYCoordOfFloor(ladder.getFloor() + 1);
		}
		startAutoMove(ladder.getX(), goalY);
		lastStateCounter = 0;
		lastState = state;
		this.state = state;
	}

}
