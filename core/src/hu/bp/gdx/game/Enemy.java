/**
 * 
 */
package hu.bp.gdx.game;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author peter
 *
 */
public class Enemy extends CanCollide implements Movable {

	private static int LADDER_TOLERANCE = 8; // pixels

	private static final int LAST_STATE_COUNTER_MAX = 10;
	private STATE lastStateSaver = STATE.NONE;
	private int lastStateCounter = 0;
	private STATE lastState = STATE.LEFT;
	private STATE state = STATE.RIGHT;
	private static final int LEFT_MARGIN = 3; // empty margin in pixels
	private static final int RIGHT_MARGIN = 2; // empty margin in pixels

	private float goalY = 0;

	private float stateTime = 0;
	private boolean active = false;

	private Animation animation;
	private LadderManager ladders;

	private TextureRegion currentFrame;

	public static final float aspectRatio = 1f; //sprite was too small
	public static final float animSpeed = 0.1f; // second / frame
	public static final float animVelocity = 5.0f * aspectRatio; // pixel moving / frame

	// Last X coordinate of the enemy
	private static final float RIGHT_WORLD_BOUNDARY =
			Const.WORLD_WIDTH_UNIT - (Const.TILE_SIZE - RIGHT_MARGIN) * aspectRatio;

	private TiledForeGround foreGround;
	private Nerd nerd;
	
	public Enemy(BrickGame game, LadderManager ladders, TiledForeGround foreGround, Nerd nerd) {
		super(3, 2, aspectRatio, Const.TILE_SIZE, Const.TILE_SIZE);

		TextureRegion[][] tmp = TextureRegion.split(game.enemySheet,
				game.enemySheet.getWidth() / 5, game.enemySheet.getHeight() / 1);
		TextureRegion[] frames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			frames[i] = tmp[0][i];
		}

		animation = new Animation(animSpeed, frames);
		this.ladders = ladders;
		this.foreGround = foreGround;
		this.nerd = nerd;
		reset(0);
	}


	public void reset(int floor) {
		x = MathUtils.random(
			0, Const.WORLD_WIDTH_UNIT - Const.TILE_SIZE * aspectRatio);

		y = BrickUtils.getYCoordOfFloor(floor);

		stateTime = 0;
		active = true;

		if (x < Const.WORLD_WIDTH_UNIT / 2) {
			state = STATE.RIGHT;
		} else {
			state = STATE.LEFT;
		}
		countBoundary();
	}

	public void reset (int floor1, int floor2) {
		int floor = MathUtils.random(floor1, floor2);
		reset(floor);
	}

	public void reset() {
		reset(1, Const.NUM_OF_FLOORS - 1);
	}

	public float getWidth() {
		return currentFrame.getRegionWidth() * aspectRatio;
	}

	public float getHeight() {
		return currentFrame.getRegionHeight() * aspectRatio;
	}

	public void die() {
		active = false;
	}

	public boolean isActive() {
		return active;
	}

	public TextureRegion getFrame() {
		currentFrame = animation.getKeyFrame(stateTime, true);
		if (animation.isAnimationFinished(stateTime)) {
			active = false;
		};
		return currentFrame;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	private void startClimb(Ladder ladder, STATE state) {
		if (state == STATE.DOWN) {
			goalY = BrickUtils.getYCoordOfFloor(ladder.getFloor());
		}
		else {
			goalY = BrickUtils.getYCoordOfFloor(ladder.getFloor() + 1);
		}
		x = ladder.getX();
		lastStateCounter = 0;
		lastState = state;
		this.state = state;
	}

	private void decideRandomClimb() {
		Ladder up = null;
		int floor = BrickUtils.getFloorOfCoord(y);

		if (lastState != STATE.DOWN && floor < Const.NUM_OF_FLOORS) {
			up = ladders.getLadderUp(this);
		}

		Ladder down = null;
		if (lastState != STATE.UP) {
			down = ladders.getLadderDown(this);
		}

		if ((up != null) && (down != null)) {
			if (Math.random() < 0.5) {
				up = null;
			}
			else {
				down = null;
			}
		}

		if (down != null) {
			startClimb(down, STATE.DOWN);
		}
		else if (up != null) {
			startClimb(up, STATE.UP);
		}
	}

	private void decideClimb() {
		if (nerd.y < y) {
			Ladder down = ladders.getLadderDown(this);
			if (down != null) startClimb(down, STATE.DOWN);
		}
		else if (nerd.y > y) {
			Ladder up = ladders.getLadderUp(this);
			if (up != null) startClimb(up, STATE.UP);
		}
	}

	private void doMove(float delta) {
		if (state == STATE.DOWN || state == STATE.FALL) {
			y -= delta;
			if (y <= goalY) {
				y = goalY;
				lastState = state;
				lastStateCounter = 0;
				state = (Math.random() < 0.5) ? STATE.RIGHT : STATE.LEFT;
			}
		}
		else if (state == STATE.UP) {
			y += delta;
			if (y >= goalY) {
				y = goalY;
				lastState = state;
				lastStateCounter = 0;
				state = (Math.random() < 0.5) ? STATE.RIGHT : STATE.LEFT;
			}
		}
		else if (state == STATE.LEFT) {
			x -= delta;
			if (x < - LEFT_MARGIN * aspectRatio) {
				x = - LEFT_MARGIN * aspectRatio;
				lastState = state;
				lastStateCounter = 0;
				state = STATE.RIGHT;
			}
		}
		else if (state == STATE.RIGHT) {
			x += delta;
			if (x > RIGHT_WORLD_BOUNDARY) {
				x = RIGHT_WORLD_BOUNDARY;
				lastState = state;
				lastStateCounter = 0;
				state = STATE.LEFT;
			}
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

		Tile tile = foreGround.getCell(x + LEFT_MARGIN, y - Const.TILE_SIZE);

		if (TiledForeGround.TYPE.none == tile.getType()) {
			state = STATE.FALL;
			goalY = BrickUtils.getYCoordOfFloor(BrickUtils.getFloorOfCoord(y) - 1);
		}
		else if ((state == STATE.LEFT) || (state == STATE.RIGHT)) {
			decideClimb();
		}

		countBoundary();
	}

	@Override
	public void setState(STATE state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastState(STATE state) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isAlive() {
		return true;
	}


	@Override
	public float getLadderTolerance() {
		return LADDER_TOLERANCE;
	}


	@Override
	public float getFloorTolerance() {
		return 0;
	}


}
