package hu.bp.gdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BrickGame extends Game {
	public SpriteBatch batch;
	public Texture brick;
	public Texture ladder;
	public BitmapFont font;
	public Texture nerdSheet;
	public Texture bombSheet;
	public Texture enemySheet;

	/**
	 * Shows how many floors the enemy has to fall to die
	 */
	public int level;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		brick = new Texture("brick_wall_single_perfect.png");
		ladder = new Texture("ladder.png");
		bombSheet = new Texture("bomb_1x9_32x32.png");
		nerdSheet = new Texture("nerd_sprite_12x8tiles_24x32pixels.png");
		enemySheet = new Texture("cyborgpig_1x5_32x32.png");
		font = new BitmapFont();
		level = 2;
		this.setScreen(new BrickScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		brick.dispose();
		ladder.dispose();
		font.dispose();
	}
}
