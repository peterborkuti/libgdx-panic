package hu.bp.gdx.game;

public class Const {
	/**
	 * Number of ladders on a floor. Do not change this from 2, because
	 * TiledForeGround has hard-coded 2
	 */
	public static final int LADDERS_ON_A_FLOOR = 2; // Number of ladders on a floor
	/**
	 * The size of the tiles (bricks, ladders) in the game in word-units.
	 * Also the size ff the enemies, but use it with care for sprites,
	 * because sprites can be resized
	 */
	public static final int TILE_SIZE = 32; // measured in pixel, n * n
	public static final int SCREEN_SIZE = 20; // measured in tiles, n x n
	public static final int WORLD_HEIGHT = 60; // measured in tiles
	public static final int WORLD_WIDTH = 20; // measured in tiles
	public static final int WORLD_WIDTH_UNIT = WORLD_WIDTH * TILE_SIZE;
	/**
	 * The height of a floor in tiles (not in units).
	 * The first tile from the down is a brick, so the empty space on
	 * a floor is FLOOR_HEIGHT - 1 
	 */
	public static final int FLOOR_HEIGHT = 4; // measure in tiles, height of one
	/**
	 * Number of floors in the world. The first floor is the 0th, 
	 * the last is NUM_OF_FLOORS - 1
	 */
	public static final int NUM_OF_FLOORS = WORLD_HEIGHT / FLOOR_HEIGHT;
	public static final int ENEMY_NUM = 5; // Number of enemies
}
