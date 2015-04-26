/**
 * 
 */
package hu.bp.gdx.game;

import hu.bp.gdx.game.Movable.STATE;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author peter
 *
 */
public class Enemy implements Movable {

	private STATE state = STATE.RIGHT;
	
	private float x = 0;
	private float y = Const.TILE_SIZE + Const.FLOOR_HEIGHT;
	private float stateTime = 0;
	private boolean active = false;

	private Animation animation;

	private TextureRegion currentFrame;

	public static final float aspectRatio = 1.5f; //sprite was too small

	public static final float animSpeed = 0.1f; // second / frame
	public static final float animVelocity = 5.0f * aspectRatio; // pixel moving / frame

	public Enemy(BrickGame game) {
		TextureRegion[][] tmp = TextureRegion.split(game.enemySheet,
				game.enemySheet.getWidth() / 5, game.enemySheet.getHeight() / 1);
		TextureRegion[] frames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
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
		float delta = deltaTime / animSpeed * animVelocity;

		if (state == STATE.LEFT) {
			x -= delta;
			if (x < animVelocity) {
				state = STATE.RIGHT;
			}
		}
		else if (state == STATE.RIGHT) {
			x += delta;
			if (x > (Const.WORLD_WIDTH - 1) * Const.TILE_SIZE) {
				state = STATE.LEFT;
			}
		}
	}

	@Override
	public void setState(STATE state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastState(STATE state) {
		// TODO Auto-generated method stub
		
	}

}
