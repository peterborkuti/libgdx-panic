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
        Vector2 v = getRandomPlace(game.brick);
        game.batch.draw(game.brick, v.x, v.y);
        v = getRandomPlace(game.ladder);
        game.batch.draw(game.ladder, v.x, v.y);
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

}
