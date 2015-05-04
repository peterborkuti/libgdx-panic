/**
 * 
 */
package hu.bp.gdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * @author Peter Borkuti
 *
 */
public class Nerd extends CanCollide implements Movable {

	private STATE lastState = STATE.LEFT;
	private STATE state = STATE.STOP;
	private float stateTime = 0;
	/**
	 * seconds when shield active
	 */
	private static final int MAX_SHIELD = 3;

	private LadderManager ladders;
	private TiledForeGround foreGround;

	private Animation walkRight;
	private Animation walkLeft;
	private Animation standRight;
	private Animation standLeft;

	private int lives = 5;

	private TextureRegion currentFrame;

	private static final int UP_MARGIN = 3;
	private static final int LEFT_MARGIN = 5; // empty margin in pixels
	private static final int RIGHT_MARGIN = 5; // empty margin in pixels

	public static final float aspectRatio = 1f; //sprite was too small

	private static final float LADDER_TOLERANCE = 4 * aspectRatio; // for horizontal tolerance
	private static final float FLOOR_TOLERANCE = 8 * aspectRatio; // for vertical tolerance

	public static final float animSpeed = 0.05f; // second / frame
	public static final float animVelocity = 7.0f * aspectRatio; // pixel moving / frame

	private static final float RIGHT_WORLD_BOUNDARY =
			Const.WORLD_WIDTH_UNIT - (Const.TILE_SIZE - RIGHT_MARGIN) * aspectRatio;

	/**
	 * when shield is > 0, bob can not be killed
	 * counts down when shield is active
	 */
	private float shield = MAX_SHIELD;

	public Nerd(BrickGame game, LadderManager ladders, TiledForeGround foreGround) {
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
		this.foreGround = foreGround;
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

	public boolean isInSpace(float x, float y) {
		boolean onLadder = ladders.isOnLadder(this);
		boolean onFloor = BrickUtils.isOnFloor(y, FLOOR_TOLERANCE);

		return !onLadder && !onFloor;
	}

	@Override
	public void setState(STATE _state) {
		/*
		HashSet<STATE> allowedStates = new HashSet<STATE>();

		if (ladders.getLadderDown(this) != null) {
			allowedStates.add(STATE.DOWN);
		}

		if (ladders.getLadderUp(this) != null) {
			allowedStates.add(STATE.UP);
		}

		if (ladders.isOnLadder(this)) {
			allowedStates.add(STATE.DOWN);
			allowedStates.add(STATE.UP);
			allowedStates.add(STATE.LEFT);
			allowedStates.add(STATE.RIGHT);
			allowedStates.add(STATE.STOP);
		}

		if (BrickUtils.isOnFloor(y, FLOOR_TOLERANCE)) {
			allowedStates.add(STATE.LEFT);
			allowedStates.add(STATE.RIGHT);
			allowedStates.add(STATE.STOP);
		}
		else {
			allowedStates.add(STATE.FALL);
		}

		if (allowedStates.contains(_state)) {
			state = _state;;
		}
		*/
		if (_state != STATE.FALL) {
			state = _state;
		}
	}

	@Override
	public void setLastState(STATE state) {
		lastState = state;
	}

	private void doMove(float delta) {
		if (state == STATE.LEFT) {
			x -= delta;
			y = BrickUtils.alignIfOnFloor(y, FLOOR_TOLERANCE);
			if (x < - LEFT_MARGIN * aspectRatio) {
				x = - LEFT_MARGIN * aspectRatio;
			}
		}
		else if (state == STATE.RIGHT) {
			x += delta;
			y = BrickUtils.alignIfOnFloor(y, FLOOR_TOLERANCE);
			if ( x > RIGHT_WORLD_BOUNDARY) {
				x = RIGHT_WORLD_BOUNDARY;
			}
		}
		else if (state == STATE.UP) {
			y += delta;
		}
		else if (state == STATE.DOWN || state == STATE.FALL) {
			y -= delta;
		}

		if (state == STATE.FALL && BrickUtils.isOnFloor(y, FLOOR_TOLERANCE)) {
			y = BrickUtils.alignIfOnFloor(y, FLOOR_TOLERANCE);
			state = STATE.STOP;
		}
	}

	private boolean badMove(STATE oldState) {
		Tile cell = null;

		// left, down, falling
		cell = foreGround.getCell(x, y);

		if (oldState == STATE.RIGHT) {
			cell = foreGround.getCell(x + width - LEFT_MARGIN, y);
		}
		else if (oldState == STATE.UP) {
			cell = foreGround.getCell(x, y + height - UP_MARGIN);
		}

		if ((cell.getType() == TiledForeGround.TYPE.brick)) {
			return true;
		}

		return false;
	}

	@Override
	public void move(float deltaTime) {
		if (shield >= 0) {
			shield -= deltaTime;
		}

		stateTime += deltaTime;

		float delta = deltaTime / animSpeed * animVelocity;

		float oldX = x;
		float oldY = y;
		STATE oldState = state;

		doMove(delta);

		if (badMove(oldState)) {
			x = oldX;
			y = oldY;
			state = STATE.STOP;
			return;
		}

		if (isInSpace(x, y)) {
			state = STATE.FALL;
			delta *= 2;
		}

		countBoundary();
	}

	@Override
	public void die() {
		if (shield <= 0) {
		 lives--;
		}
		y = BrickUtils.getYCoordOfFloor(0);
		x = 50;
		shield = MAX_SHIELD;
	}

	public int getLives() {
		return lives;
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public float getLadderTolerance() {
		return LADDER_TOLERANCE;
	}

	@Override
	public float getFloorTolerance() {
		return FLOOR_TOLERANCE;
	}

}
