package hu.bp.gdx.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class BrickInput extends InputAdapter {

	public static boolean left = false;
	public static boolean right = false;
	public static boolean up = false;
	public static boolean down = false;

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.LEFT:
			left = true;
			break;
		case Keys.RIGHT:
			right = true;
			break;
		case Keys.UP:
			up = true;
			break;
		case Keys.DOWN:
			down = true;
			break;
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.LEFT:
			left = false;
			break;
		case Keys.RIGHT:
			right = false;
			break;
		case Keys.UP:
			up = false;
			break;
		case Keys.DOWN:
			down = false;
			break;
		}

		return true;
	}

}
