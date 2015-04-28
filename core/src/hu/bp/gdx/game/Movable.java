package hu.bp.gdx.game;

public interface Movable {
	public static enum STATE { STOP, LEFT, RIGHT, UP, DOWN, NONE};
	public void setState(STATE state);
	public void setLastState(STATE state);
	public void move(float deltaTime);
	public float getX();
	public float getY();
	public void setX(float x);
	public void setY(float y);
}
