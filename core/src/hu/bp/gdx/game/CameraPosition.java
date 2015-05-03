package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * @author peter
 *
 * Translate the camera to always shows Bob with smooth vertical scrolling
 */
public class CameraPosition {

	private CanCollide bob;
	private OrthographicCamera cam;
	/**
	 * Maximum Y coordinate of camera position
	 */
	private int maxY;
	/**
	 * Minimum Y coordinate of camera position
	 */
	private int minY;
	/**
	 * Camera is centered, so it's Y coordinate is the viewport's center
	 */
	private int camHeightDiv2;

	/**
	 * Hysteresis. Camera does not follow right away Bob's moving, because that
	 * would led to flickering. If Bob is moving on the middle-third of the
	 * screen, the camera will not follow it.
	 */
	private int toleranceY;
	
	/**
	 * When camera is moving and camera is very near to the desired position,
	 * it will stop moving
	 */
	private int minDeltaY;
	/**
	 * To sign if camera is moving. If camera is already moving, it will go to
	 * the desired position regardless of the toleranceY.
	 * However, if camera is not moving, it wont be start moving if the desired
	 * position is not farther than the tolerance.
	 */
	private boolean cameraMoving;

	public CameraPosition(OrthographicCamera cam, CanCollide bob,int cameraHeight) {
		this.bob = bob;
		this.cam = cam;
		this.camHeightDiv2 = cameraHeight / 2;
		toleranceY = cameraHeight / 3;
		maxY = Const.WORLD_HEIGHT * Const.TILE_SIZE - camHeightDiv2;
		minY = camHeightDiv2;
		minDeltaY = toleranceY / 10;
	}

	/**
	 * Centers the camera in the World with a nice, smooth and intelligent
	 * vertical scrolling to position the camera as to the Bob will be at 
	 * the center of the screen.
	 * It updates the camera only when is necessary.
	 */
	public void update() {
		Vector3 position = cam.position;
		Gdx.app.log("position", "pos:" + position);
		float newY = position.y;
		float delta = bob.y - position.y;
		float absDelta = Math.abs(delta);

		if (absDelta < minDeltaY) {
			//the difference from center is so subtle, that camera will not
			//follow it
			cameraMoving = false;
			return;
		}

		newY += Math.signum(delta);

		// camera reached the top/bottom, so it will not moving
		if ((newY > maxY) || (newY < minY)) {
			cameraMoving = false;

			return;
		}

		if ((Math.abs(delta) > toleranceY) || cameraMoving) {
			cameraMoving = true;
			cam.position.set(position.x, newY, position.z);
			cam.update();
		}
	}

}
