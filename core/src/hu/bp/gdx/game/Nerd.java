/**
 * 
 */
package hu.bp.gdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author peter
 *
 */
public class Nerd implements Movable {
	private STATE lastState = STATE.LEFT;
	private STATE state = STATE.STOP;
	private float x = 0;
	private float y = Const.TILE_SIZE;
	private float stateTime = 0;

	private Animation walkRight;
	private Animation walkLeft;
	private Animation standRight;
	private Animation standLeft;

	private TextureRegion currentFrame;

	public static final float aspectRatio = 2f; //sprite was too small

	public static final float animSpeed = 0.05f; // second / frame
	public static final float animVelocity = 2.0f * aspectRatio; // pixel moving / frame

	public Nerd(BrickGame game) {
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

	@Override
	public void setX(float _x) {
		x = _x;
	}

	@Override
	public void setY(float _y) {
		y = _y;
	}

	@Override
	public void setState(STATE _state) {
		state = _state;
	}

	@Override
	public void setLastState(STATE state) {
		lastState = state;
	}

	public boolean getLoop() {
		return true;
	}

	@Override
	public void move(float deltaTime) {
		stateTime += deltaTime;

		float delta = deltaTime / animSpeed * animVelocity;

		if (state == STATE.LEFT) {
			x -= delta;
		}
		else if (state == STATE.RIGHT) {
			x += delta;
		}
	}

}
