package hu.bp.gdx.game;

public class Const {
	public static final int NUM_OF_FLOORS = 5;
	//@TODO Dont change this, TiledForeGround has hard-coded 2
	public static final int LADDERS_ON_A_FLOOR = 2; // Number of ladders on a floor
	public static final int TILE_SIZE = 32; // measured in pixel, n * n
	public static final int SCREEN_SIZE = 20; // measured in tiles, n x n
	public static final int WORLD_HEIGHT = 60; // measured in tiles
	public static final int WORLD_WIDTH = 20; // measured in tiles
	public static final int WORLD_WIDTH_UNIT = WORLD_WIDTH * TILE_SIZE;

	public static final int FLOOR_HEIGHT = 4; // measure in tiles, height of one
	public static final int ENEMY_NUM = 5; // Number of enemies
}
