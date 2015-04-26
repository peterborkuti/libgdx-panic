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
public class Bomb  {
	private float x = 0;
	private float y = Const.TILE_SIZE;
	private float stateTime = 0;
	private boolean active = false;

	private Animation animation;

	private TextureRegion currentFrame;

	public static final float aspectRatio = 1.5f; //sprite was too small

	public static final float animSpeed = 0.4f; // second / frame
	public static final float animVelocity = 2.0f * aspectRatio; // pixel moving / frame

	public Bomb(BrickGame game) {
		TextureRegion[][] tmp = TextureRegion.split(game.bombSheet,
				game.bombSheet.getWidth() / 9, game.bombSheet.getHeight() / 1);
		TextureRegion[] frames = new TextureRegion[9];
		for (int i = 0; i < 9; i++) {
			frames[i] = tmp[0][i];
		}

		animation = new Animation(animSpeed, frames);
	}

	public void reset(float _x, float _y) {
		x = _x;
		y = _y;
		stateTime = 0;
		active = true;
	}

	public float getWidth() {
		return currentFrame.getRegionWidth() * aspectRatio;
	}

	public float getHeight() {
		return currentFrame.getRegionHeight() * aspectRatio;
	}

	public boolean isActive() {
		return active;
	}

	public TextureRegion getFrame() {
		currentFrame = animation.getKeyFrame(stateTime, false);
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

	public void setX(float _x) {
		x = _x;
	}

	public void setY(float _y) {
		y = _y;
	}

	public boolean getLoop() {
		return false;
	}

	public void move(float deltaTime) {
		stateTime += deltaTime;
	}

}
