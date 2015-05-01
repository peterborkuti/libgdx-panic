/**
 * 
 */
package hu.bp.gdx.game;

import java.util.HashSet;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author peter
 *
 */
public class Nerd extends CanCollide implements Movable {

	private static final int LADDER_TOLERANCE = 4; // for horizontal tolerance
	private static final int FLOOR_TOLERANCE = 4; // for vertical tolerance
	private STATE lastState = STATE.LEFT;
	private STATE state = STATE.STOP;
	private float stateTime = 0;

	private LadderManager ladders;

	private Animation walkRight;
	private Animation walkLeft;
	private Animation standRight;
	private Animation standLeft;

	private int lives = 5;

	private TextureRegion currentFrame;

	public static final float aspectRatio = 1f; //sprite was too small

	public static final float animSpeed = 0.05f; // second / frame
	public static final float animVelocity = 2.0f * aspectRatio; // pixel moving / frame

	public Nerd(BrickGame game, LadderManager ladders) {
		super(4, 4, aspectRatio, 24, 32);
		TextureRegion[][] tmp = TextureRegion.split(game.nerdSheet,
				game.nerdSheet.getWidth() / 12, game.nerdSheet.getHeight() / 8);
		TextureRegion[] walkFrames = new TextureRegion[6];
		for (int i = 0; i < 6; i++) {
			walkFrames[i] = tmp[1][i];
		}

		walkRight = new Animation(animSpeed, walkFrames);

		walkFrames = new TextureRegion[6];
		for (int i = 0; i < 6; i++) {
			walkFrames[i] = tmp[3][i];
		}

		walkLeft = new Animation(animSpeed, walkFrames);
		walkFrames = new TextureRegion[1];
		walkFrames[0] = tmp[1][1];
		standRight = new Animation(0, walkFrames);

		walkFrames = new TextureRegion[1];
		walkFrames[0] = tmp[3][1];
		standLeft = new Animation(0, walkFrames);

		setXY(0, Const.TILE_SIZE);

		this.ladders = ladders;
	}

	public float getWidth() {
		return currentFrame.getRegionWidth() * aspectRatio;
	}

	public float getHeight() {
		return currentFrame.getRegionHeight() * aspectRatio;
	}

	public TextureRegion getFrame() {
		currentFrame = standRight.getKeyFrame(stateTime, true);
		if (state == STATE.LEFT) {
			currentFrame = walkLeft.getKeyFrame(stateTime, true);
		}
		else if (state == STATE.RIGHT) {
			currentFrame = walkRight.getKeyFrame(stateTime, true);
		}
		else if (lastState == STATE.LEFT) {
			currentFrame = standLeft.getKeyFrame(stateTime, true);
		}

		return currentFrame;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	public void isOnFloor() {
		
	}

	@Override
	public void setState(STATE _state) {
	
		HashSet<STATE> allowedStates = new HashSet<STATE>();

		if (ladders.isOnLadder(x, y, LADDER_TOLERANCE)) {
			allowedStates.add(STATE.DOWN);
			allowedStates.add(STATE.UP);
			allowedStates.add(STATE.LEFT);
			allowedStates.add(STATE.RIGHT);
			allowedStates.add(STATE.STOP);
		}
		else if (BrickUtils.isOnFloor(y, FLOOR_TOLERANCE)) {
			allowedStates.add(STATE.LEFT);
			allowedStates.add(STATE.RIGHT);
			allowedStates.add(STATE.STOP);
		}
		else {
			//allowedStates.add(STATE.FALL);
		}

		if (!allowedStates.contains(_state)) {
			return;
		}

		/*
		if (_state == STATE.DOWN) {
			Ladder down = ladders.getLadderDown(x, y, LADDER_TOLERANCE);
			if (down != null) {
				state = _state;
				x = down.getX();
	
				return;
			}
		}

		if (_state == STATE.UP) {
			Ladder up = ladders.getLadderUp(x, y, LADDER_TOLERANCE);

			if (up != null) {
				state = _state;
				x = up.getX();
			}

			return;
		}
		*/

		state = _state;
	}

	@Override
	public void setLastState(STATE state) {
		lastState = state;
	}

	@Override
	public void move(float deltaTime) {
		stateTime += deltaTime;

		float delta = deltaTime / animSpeed * animVelocity;

		if (state == STATE.LEFT) {
			x -= delta;
			y = BrickUtils.alignIfOnFloor(y, FLOOR_TOLERANCE);
		}
		else if (state == STATE.RIGHT) {
			x += delta;
			y = BrickUtils.alignIfOnFloor(y, FLOOR_TOLERANCE);
		}
		else if (state == STATE.UP) {
			y += delta;
		}
		else if (state == STATE.DOWN) {
			y -= delta;
		}

		countBoundary();
	}

	@Override
	public void die() {
		lives--;
	}

	public int getLives() {
		return lives;
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return true;
	}

}
