package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class BrickScreen implements Screen {
	final BrickGame game;

	private ShapeRenderer shapeRenderer = new ShapeRenderer();

	private LadderManager ladders;

	private Nerd nerd;
	private Bomb bomb;
	private Enemy enemy[] = new Enemy[Const.ENEMY_NUM];

	private TiledForeGround foreground;

	private OrthographicCamera camera;

	private BitmapFont font;
	private SpriteBatch batch;

	private BrickInput iProcessor;

	private CameraPosition center;

	private void _setupCamera() {
		float aspectRatio = Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		int pixelSize = Const.TILE_SIZE * Const.SCREEN_SIZE;

		camera.setToOrtho(false, aspectRatio * pixelSize, pixelSize);
		center.update();
		camera.update();
	}

	public BrickScreen(final BrickGame bGame) {
		game = bGame;

		font = new BitmapFont();
		batch = new SpriteBatch();

		ladders = new LadderManager();

		foreground = new TiledForeGround(game.brick, game.ladder, ladders);

		nerd = new Nerd(game, ladders, foreground);
		iProcessor = new BrickInput(nerd);
		Gdx.input.setInputProcessor(iProcessor);

		bomb = new Bomb(game, nerd, foreground);

		for (int i = 0; i < Const.ENEMY_NUM; i++) {
			enemy[i] = new Enemy(game, ladders, foreground, nerd);
		}

		camera = new OrthographicCamera();
		center = new CameraPosition(camera, nerd, Const.TILE_SIZE * Const.SCREEN_SIZE);

		_setupCamera();

		newScreen(1);

	}

	private void newScreen(int level) {
		game.level = level;
		bomb.setBombs(game.level + 1);
		foreground.createField();
		nerd.init();
		for (int i = 0; i < Const.ENEMY_NUM; i++) {
			enemy[i].reset(i);
			Gdx.app.log("Screen", "Enemy(" + enemy[i].getX() + "," + enemy[i].getY() + ")");
		}
		_setupCamera();

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
		if (BrickInput.space && !bomb.isAlive()) {
			bomb.reset(nerd.getX(), nerd.getY());
		}
	}

	private void scoreBoard(int enemies) {
		Vector3 stickyText = camera.unproject(new Vector3(10, 20, 0));
		font.draw(
			batch,
			"FPS:  " + Gdx.graphics.getFramesPerSecond() + ", " +
			"Lives:" + nerd.getLives() + ", " +
			"Bombs:" + bomb.getBombs() + ", " +
			"Level:" + game.level + ", " +
			"Enemies:" + enemies,
			stickyText.x, stickyText.y);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(100f / 255f, 100f / 255f, 250f / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_handleInput();

		center.update();
		//camera.update();
		foreground.getRenderer().setView(camera);
		foreground.getRenderer().render();

		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		nerd.move(Gdx.graphics.getDeltaTime());

		int enemiesLeft = 0;
		for (int i = 0; i < Const.ENEMY_NUM; i++) {
			if (enemy[i].isAlive()) {
				enemiesLeft++;
				enemy[i].move(Gdx.graphics.getDeltaTime());
			}
		}

		scoreBoard(enemiesLeft);

		if (bomb.isAlive()) {
			bomb.move(Gdx.graphics.getDeltaTime());
			batch.draw(
				bomb.getFrame(), bomb.getX(), bomb.getY(), bomb.getWidth(),
				bomb.getHeight());
		}

		batch.draw(
			nerd.getFrame(), nerd.getX(), nerd.getY(), nerd.getWidth(),
			nerd.getHeight());

		for (int i = 0; i < Const.ENEMY_NUM; i++) {
			if (enemy[i].isAlive()) {
				batch.draw(
					enemy[i].getFrame(), enemy[i].getX(), enemy[i].getY(), enemy[i].getWidth(),
					enemy[i].getHeight());
			}
		}

		batch.end();

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.rect(nerd.getX(), nerd.getY(), nerd.getWidth(), nerd.getHeight());
		shapeRenderer.end();

		if (enemiesLeft == 0) {
			newScreen(game.level + 1);
		}

		if (bomb.getBombs() < 0) {
			newScreen(game.level);
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.update();
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
