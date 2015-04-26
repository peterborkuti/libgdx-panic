/**
 * 
 */
package hu.bp.gdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author peter
 *
 */
public class Enemy extends CanCollide implements Movable {

	private STATE state = STATE.RIGHT;
	private static final int LEFT_MARGIN = 3; // empty margin in pixels
	private static final int RIGHT_MARGIN = 2; // empty margin in pixels
	
	private float stateTime = 0;
	private boolean active = false;

	private Animation animation;

	private TextureRegion currentFrame;

	public static final float aspectRatio = 1.5f; //sprite was too small

	public static final float animSpeed = 0.1f; // second / frame
	public static final float animVelocity = 5.0f * aspectRatio; // pixel moving / frame

	public Enemy(BrickGame game) {
		super(3, 2, aspectRatio, Const.TILE_SIZE, Const.TILE_SIZE);

		TextureRegion[][] tmp = TextureRegion.split(game.enemySheet,
				game.enemySheet.getWidth() / 5, game.enemySheet.getHeight() / 1);
		TextureRegion[] frames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			frames[i] = tmp[0][i];
		}

		animation = new Animation(animSpeed, frames);
		reset();
	}


	public void reset(int floor) {
		x = MathUtils.random(0, Const.WORLD_WIDTH_UNIT - Const.TILE_SIZE * aspectRatio);

		y = floor * Const.FLOOR_HEIGHT * Const.TILE_SIZE + Const.TILE_SIZE;

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

	public void move(float deltaTime) {
		stateTime += deltaTime;

		float delta = deltaTime / animSpeed * animVelocity;

		if (state == STATE.LEFT) {
			x -= delta;
			if (x < - LEFT_MARGIN * aspectRatio) {
				x = - LEFT_MARGIN * aspectRatio;
				state = STATE.RIGHT;
			}
		}
		else if (state == STATE.RIGHT) {
			x += delta;
			if (x > Const.WORLD_WIDTH_UNIT - (Const.TILE_SIZE - RIGHT_MARGIN)* aspectRatio) {
				x = Const.WORLD_WIDTH_UNIT - (Const.TILE_SIZE - RIGHT_MARGIN) * aspectRatio;
				state = STATE.LEFT;
			}
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
		// TODO Auto-generated method stub
		return false;
	}

}
