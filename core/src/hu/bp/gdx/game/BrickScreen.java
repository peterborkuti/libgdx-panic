package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BrickScreen implements Screen {
	final BrickGame game;

	private TiledForeGround foreground;

	private OrthographicCamera camera;

	private BitmapFont font;
	private SpriteBatch batch;

	private BrickInput iProcessor= new BrickInput();

	private void _setupCamera() {
		float aspectRatio = Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		int pixelSize = Const.TILE_SIZE * Const.SCREEN_SIZE;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, aspectRatio * pixelSize, pixelSize);
		camera.update();
	}

	public BrickScreen(final BrickGame bGame) {
		game = bGame;

		_setupCamera();

		font = new BitmapFont();
		batch = new SpriteBatch();

		foreground = new TiledForeGround(game.brick, game.ladder);

		Gdx.input.setInputProcessor(iProcessor);

	}

	@Override
	public void show() {

	}

	private void _handleInput() {
		if (iProcessor.left) {
			camera.translate(-3, 0, 0);
		}
		if (iProcessor.right) {
			camera.translate(3, 0, 0);
		}
		if (iProcessor.down) {
			camera.translate(0, -3, 0);
		}
		if (iProcessor.up) {
			camera.translate(0, 3, 0);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(100f / 255f, 100f / 255f, 250f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_handleInput();
		camera.update();
		foreground.getRenderer().setView(camera);
		foreground.getRenderer().render();
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
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		game.dispose();
		font.dispose();
		batch.dispose();
	}

}
