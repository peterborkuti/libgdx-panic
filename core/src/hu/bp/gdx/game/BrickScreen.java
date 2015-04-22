package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class BrickScreen implements Screen {
	final BrickGame game;

	OrthographicCamera camera;

	public static final int NUM_OF_FLOORS = 5;

	public BrickScreen(final BrickGame bGame) {
		game = bGame;
		camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.disableBlending();
        game.batch.begin();
        createField();
        game.batch.end();

        /*
        if (Gdx.input.isTouched()) {
            game.setScreen(new BrickScreen(game));
            dispose();
        }
        */

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


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


	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		game.dispose();
	}

	private static Vector2 getRandomPlace(Texture image) {
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		int ih = image.getHeight();
		int iw = image.getWidth();

		double x = Math.floor(Math.random() * (w / iw)) * iw; 
		double y = Math.floor(Math.random() * (h / ih)) * ih;
		
		return new Vector2((float)x, (float)y);

		
	}

	private void createFloors() {
		int floorHeight = Gdx.graphics.getHeight() / NUM_OF_FLOORS;

		int places = Gdx.graphics.getWidth() / game.ladder.getWidth();

		for (int floor = 0; floor < NUM_OF_FLOORS; floor++) {
			double x = Math.floor(Math.random() * places)
					* game.ladder.getWidth();
			for (int y = floor * floorHeight; y < (floor + 1) * floorHeight; y += game.ladder
					.getHeight()) {
				game.batch.draw(game.ladder, (float) x, (float) y);
			}
		}
	}

	private void createLadders() {
		int floorHeight = Gdx.graphics.getHeight() / NUM_OF_FLOORS;

		for (int floor = 0; floor < NUM_OF_FLOORS; floor++ ) {
			for (int x = 0; x < Gdx.graphics.getWidth(); x += game.brick.getWidth()) {
				game.batch.draw(game.brick, x, floor * floorHeight);
			}
		}
	}

	private void createField() {
		createFloors();
		createLadders();
		
	}

}
