package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class BrickScreen implements Screen {
	final BrickGame game;

	private Array<CanCollide> ladders =
		new Array<CanCollide>(Const.NUM_OF_FLOORS * Const.LADDERS_ON_A_FLOOR);

	private Nerd nerd;
	private Bomb bomb;
	private Enemy enemy[] = new Enemy[Const.ENEMY_NUM];

	private TiledForeGround foreground;

	private OrthographicCamera camera;

	private BitmapFont font;
	private SpriteBatch batch;

	private BrickInput iProcessor;

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

		foreground = new TiledForeGround(game.brick, game.ladder, ladders);

		nerd = new Nerd(game);
		iProcessor = new BrickInput(nerd);
		Gdx.input.setInputProcessor(iProcessor);

		bomb = new Bomb(game, nerd);
		for (int i = 0; i < Const.ENEMY_NUM; i++) {
			enemy[i] = new Enemy(game, ladders);
			enemy[i].reset(i);
		}
	}

	@Override
	public void show() {

	}

	private void _handleInput() {
		if (iProcessor.down) {
			camera.translate(0, -3, 0);
		}
		if (iProcessor.up) {
			camera.translate(0, 3, 0);
		}
		if (iProcessor.plus) {
			camera.zoom += 0.02;
		}
		if (iProcessor.minus) {
			camera.zoom -= 0.02;
		}
		if (iProcessor.space && !bomb.isActive()) {
			bomb.reset(nerd.getX(), nerd.getY());
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

		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		Vector3 stickyText = camera.unproject(new Vector3(10, 20, 0));
		font.draw(
			batch, "FPS: " + Gdx.graphics.getFramesPerSecond() + ", lives:" + nerd.getLives(),
			stickyText.x, stickyText.y);

		nerd.move(Gdx.graphics.getDeltaTime());

		for (int i = 0; i < Const.ENEMY_NUM; i++) {
			enemy[i].move(Gdx.graphics.getDeltaTime());
		}

		if (bomb.isActive()) {
			bomb.move(Gdx.graphics.getDeltaTime());
			batch.draw(
				bomb.getFrame(), bomb.getX(), bomb.getY(), bomb.getWidth(),
				bomb.getHeight());
		}

		batch.draw(
			nerd.getFrame(), nerd.getX(), nerd.getY(), nerd.getWidth(),
			nerd.getHeight());

		for (int i = 0; i < Const.ENEMY_NUM; i++) {
			batch.draw(
				enemy[i].getFrame(), enemy[i].getX(), enemy[i].getY(), enemy[i].getWidth(),
				enemy[i].getHeight());
		}

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
