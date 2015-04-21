package hu.bp.gdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BrickGame extends Game {
	public SpriteBatch batch;
	public Texture img;
	public BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("brick_wall_single_perfect.png");
		font = new BitmapFont();
		this.setScreen(new BrickScreen(this));
	}

	@Override
	public void render () {
		super.render();
		/*
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for (int x = 0; x < this.)
		batch.draw(img, 0, 0);
		batch.end();
		*/
	}

	public void dispose() {
		batch.dispose();
		img.dispose();
		font.dispose();
	}
}
