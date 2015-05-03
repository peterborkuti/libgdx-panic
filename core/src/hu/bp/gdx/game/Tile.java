package hu.bp.gdx.game;

import hu.bp.gdx.game.TiledForeGround.TYPE;

import com.badlogic.gdx.math.Rectangle;

public class Tile {
	public Rectangle getRec() {
		return rec;
	}
	public void setRec(Rectangle rec) {
		this.rec = rec;
	}
	public TiledForeGround.TYPE getType() {
		return type;
	}
	public void setType(TiledForeGround.TYPE type) {
		this.type = type;
	}
	public Tile(Rectangle rec, TYPE type) {
		super();
		this.rec = rec;
		this.type = type;
	}
	private Rectangle rec;
	private TiledForeGround.TYPE type;

}
