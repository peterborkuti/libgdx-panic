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

	private int floor;

	private CanCollide bob;

	private TextureRegion currentFrame;

	private TiledForeGround foreGround;
	public static final float aspectRatio = 1f; //sprite was too small

	public static final float animSpeed = 0.4f; // second / frame
	public static final float animVelocity = 2.0f * aspectRatio; // pixel moving / frame

	private int bombs = 0;

	public int getBombs() {
		return bombs;
	}

	public void setBombs(int bombs) {
		this.bombs = bombs;
	}

	public Bomb(BrickGame game, CanCollide _bob, TiledForeGround foreGround) {
		super(0, 0, aspectRatio, Const.TILE_SIZE, Const.TILE_SIZE);

		TextureRegion[][] tmp = TextureRegion.split(game.bombSheet,
				game.bombSheet.getWidth() / 9, game.bombSheet.getHeight() / 1);
		TextureRegion[] frames = new TextureRegion[9];
		for (int i = 0; i < 9; i++) {
			frames[i] = tmp[0][i];
		}

		animation = new Animation(animSpeed, frames);

		bob = _bob;

		this.foreGround = foreGround;
	}

	

	public void reset(float x, float y) {
		if (bombs > 0) {
			floor = BrickUtils.getFloorOfCoord(y);
			this.y = BrickUtils.getYCoordOfFloor(floor);
			this.x = (float)Math.floor(x / Const.TILE_SIZE) * Const.TILE_SIZE;
			stateTime = 0;
			active = true;
			countBoundary();
		}
		else {// BrickScreen checks -1 and exits
			bombs--;
		}
	}

	public float getWidth() {
		return currentFrame.getRegionWidth() * aspectRatio;
	}

	public float getHeight() {
		return currentFrame.getRegionHeight() * aspectRatio;
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
			die();
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
		bombs--;
		active = false;
		int floor = BrickUtils.getFloorOfCoord(y);
		if (floor > 0) {
			foreGround.setCellToEmpty(x, y - Const.TILE_SIZE);
		}
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return active;
	}

	@Override
	public float getLadderTolerance() {
		return 0;
	}

	@Override
	public float getFloorTolerance() {
		return 0;
	}

}
