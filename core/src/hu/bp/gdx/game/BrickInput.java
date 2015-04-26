package hu.bp.gdx.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class BrickInput extends InputAdapter {

	public static boolean up = false;
	public static boolean down = false;
	public static boolean plus = false;
	public static boolean minus = false;
	public static boolean space = false;

	private Movable movable; 

	public BrickInput(Movable _movable) {
		movable = _movable;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.LEFT:
			movable.setState(Movable.STATE.LEFT);
			break;
		case Keys.RIGHT:
			movable.setState(Movable.STATE.RIGHT);
			break;
		case Keys.UP:
			up = true;
			break;
		case Keys.DOWN:
			down = true;
			break;
		case Keys.PLUS:
			plus = true;
			break;
		case Keys.MINUS:
			minus = true;
			break;
		case Keys.SPACE:
			space = true;
			break;

		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.LEFT:
			movable.setState(Movable.STATE.STOP);
			movable.setLastState(Movable.STATE.LEFT);
			break;
		case Keys.RIGHT:
			movable.setState(Movable.STATE.STOP);
			movable.setLastState(Movable.STATE.RIGHT);
			break;
		case Keys.UP:
			up = false;
			break;
		case Keys.DOWN:
			down = false;
			break;
		case Keys.PLUS:
			plus = false;
			break;
		case Keys.MINUS:
			minus = false;
			break;
		case Keys.SPACE:
			space = false;
			break;

		}

		return true;
	}

}
