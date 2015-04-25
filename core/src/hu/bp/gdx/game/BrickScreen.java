package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;

public class BrickScreen implements Screen {
	final BrickGame game;

	private OrthographicCamera camera;

	public static final int NUM_OF_FLOORS = 5;
	public static final int TILE_SIZE = 32; // measured in pixel, n * n
	public static final int SCREEN_SIZE = 10; // measured in tiles, n * n
	public static final int WORLD_SIZE = 100; // measured in tiles, n * n

	public static final int FLOOR_HEIGHT = 4; // measure in tiles, height of one

	private TiledMap map;
	private TiledMapRenderer renderer;

	private BitmapFont font;
	private SpriteBatch batch;

	private void _setupCamera() {
		float aspectRatio = Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		int pixelSize = TILE_SIZE * SCREEN_SIZE;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, aspectRatio * pixelSize, pixelSize);
		camera.update();
	}

	public BrickScreen(final BrickGame bGame) {
		game = bGame;

		_setupCamera();

		font = new BitmapFont();
		batch = new SpriteBatch();

		_createField();

	}

	@Override
	public void show() {

	}

	private void _handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, 3, 0);
		}

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(100f / 255f, 100f / 255f, 250f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_handleInput();
		camera.update();
		renderer.setView(camera);
		renderer.render();
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		_setupCamera();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_createField();

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		game.dispose();
		map.dispose();
		font.dispose();
		batch.dispose();
	}

	private void _createLadder(TiledMapTileLayer layer, int x1, int x2,
			int floorY) {
		for (int y = floorY + 1; y < floorY + FLOOR_HEIGHT + 1; y++) {
			_setCell(layer, game.ladder, x1, y);
			_setCell(layer, game.ladder, x2, y);
		}
	}

	private void _createLadders(TiledMapTileLayer layer) {
		for (int floorY = 0; floorY < WORLD_SIZE; floorY += FLOOR_HEIGHT) {
			int x1 = MathUtils.random(0, 2 * WORLD_SIZE / 3);
			int x2 = MathUtils.random(x1 + 1, WORLD_SIZE);
			_createLadder(layer, x1, x2, floorY);
		}
	}

	private void _setCell(TiledMapTileLayer layer, Texture t, int x, int y) {
		Cell cell = new Cell();
		cell.setTile(new StaticTiledMapTile(new TextureRegion(t)));
		layer.setCell(x, y, cell);
	}

	private void createFloors(TiledMapTileLayer layer) {
		for (int y = 0; y < WORLD_SIZE; y += FLOOR_HEIGHT) {
			for (int x = 0; x < WORLD_SIZE; x++) {
				_setCell(layer, game.brick, x, y);
			}
		}
	}

	private void _createField() {
		TiledMapTileLayer layer = new TiledMapTileLayer(WORLD_SIZE, WORLD_SIZE,
				TILE_SIZE, TILE_SIZE);

		createFloors(layer);
		_createLadders(layer);

		map = new TiledMap();
		map.getLayers().add(layer);

		renderer = new OrthogonalTiledMapRenderer(map);
	}

}
