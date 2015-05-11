package hu.bp.gdx.game.desktop;

import hu.bp.gdx.game.BrickGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 600;
		config.width = 600;
		
		new LwjglApplication(new BrickGame(), config);
	}
}
