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
public class Bomb extends CanCollide {
	private float stateTime = 0;
	private boolean active = false;

	private Animation animation;

	private CanCollide bob;

	private TextureRegion currentFrame;

	public static final float aspectRatio = 1.5f; //sprite was too small

	public static final float animSpeed = 0.4f; // second / frame
	public static final float animVelocity = 2.0f * aspectRatio; // pixel moving / frame

	public Bomb(BrickGame game, CanCollide _bob) {
		super(0, 0, aspectRatio, Const.TILE_SIZE, Const.TILE_SIZE);

		TextureRegion[][] tmp = TextureRegion.split(game.bombSheet,
				game.bombSheet.getWidth() / 9, game.bombSheet.getHeight() / 1);
		TextureRegion[] frames = new TextureRegion[9];
		for (int i = 0; i < 9; i++) {
			frames[i] = tmp[0][i];
		}

		animation = new Animation(animSpeed, frames);

		bob = _bob;
	}

	public void reset(float _x, float _y) {
		x = _x;
		y = _y;
		stateTime = 0;
		active = true;
		countBoundary();
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
		if (!animation.isAnimationFinished(stateTime)) {
			if (animation.getKeyFrameIndex(stateTime) > 5) {
				if (bob.getBoundary().overlaps(boundary)) {
					bob.die();
				}
			}
		}
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

	public boolean getLoop() {
		return false;
	}

	public void move(float deltaTime) {
		stateTime += deltaTime;
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return false;
	}

}
