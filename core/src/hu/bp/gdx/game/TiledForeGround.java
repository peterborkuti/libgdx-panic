package hu.bp.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class TiledForeGround {

	public enum TYPE {brick, ladder, hole, none};

	private TiledMap map;
	private TiledMapRenderer renderer;

	private Texture brick;
	private Texture ladder;

	private LadderManager ladders;

	private TYPE[][] types = new TYPE[Const.WORLD_WIDTH + 1][Const.WORLD_HEIGHT + 1];

	public TiledForeGround(Texture _brick, Texture _ladder, LadderManager ladders) {
		brick = _brick;
		ladder = _ladder;
		this.ladders = ladders;

		_createField();
	}

	public TiledMapRenderer getRenderer() {
		return renderer;
	}

	private void _createLadder(TiledMapTileLayer layer, int x1, int x2,
			int floorY) {

		for (int y = floorY + 1; y < floorY + Const.FLOOR_HEIGHT + 1; y++) {
			Gdx.app.log("TiledForeground", "createLadder " + x1 + "," + x2 + "," + y);
			types[x1][y] = TYPE.ladder;
			types[x2][y] = TYPE.ladder;
			_setCell(layer, ladder, x1, y);
			_setCell(layer, ladder, x2, y);
		}
	}

	private void _createLadders(TiledMapTileLayer layer) {
		for (int floorY = 0, floor = 0; floorY < Const.WORLD_HEIGHT;
				floorY += Const.FLOOR_HEIGHT, floor++) {

			int x1 = MathUtils.random(0, 2 * Const.WORLD_WIDTH / 3);
			int x2 = MathUtils.random(x1 + 2, Const.WORLD_WIDTH - 1);
			ladders.setLadder(floor, 0, x1 * Const.TILE_SIZE);
			ladders.setLadder(floor, 1, x2 * Const.TILE_SIZE);
			_createLadder(layer, x1, x2, floorY);
		}
	}

	private void _setCell(TiledMapTileLayer layer, Texture t, int x, int y) {
		Cell cell = new Cell();
		cell.setTile(new StaticTiledMapTile(new TextureRegion(t)));
		layer.setCell(x, y, cell);
	}

	private void _createFloors(TiledMapTileLayer layer) {
		for (int y = 0; y < Const.WORLD_HEIGHT; y += Const.FLOOR_HEIGHT) {
			for (int x = 0; x < Const.WORLD_WIDTH; x++) {
				types[x][y] = TYPE.brick;
				_setCell(layer, brick, x, y);
			}
		}
	}

	private void _createField() {
		TiledMapTileLayer layer =
			new TiledMapTileLayer(
				Const.WORLD_WIDTH, Const.WORLD_HEIGHT,
				Const.TILE_SIZE, Const.TILE_SIZE);

		_createFloors(layer);
		_createLadders(layer);

		map = new TiledMap();
		map.getLayers().add(layer);

		renderer = new OrthogonalTiledMapRenderer(map);
	}

	public void setCellToEmpty(float x, float y) {
		int r = (int) Math.floor(y / Const.TILE_SIZE);
		int c = (int) Math.floor(x / Const.TILE_SIZE);

		TYPE type = TYPE.none;

		if (r < Const.WORLD_HEIGHT && r >= 0 && c < Const.WORLD_WIDTH
			&& c >= 0) {
			types[c][r] = type;
		}

		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		layer.setCell(c, r, null);
	}

	public Tile getCell(float x, float y) {
		int r = (int) Math.floor(y / Const.TILE_SIZE);
		int c = (int) Math.floor(x / Const.TILE_SIZE);

		TYPE type = TYPE.none;

		if (r < Const.WORLD_HEIGHT && r >= 0 && c < Const.WORLD_WIDTH && c >= 0) {
			if (types[c][r] != null) type = types[c][r];
		}

		Rectangle rec = new Rectangle(c * Const.TILE_SIZE, r * Const.TILE_SIZE, Const.TILE_SIZE, Const.TILE_SIZE);

		return (new Tile(rec, type));
	}
}
