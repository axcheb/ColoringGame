package ru.axcheb.coloringgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.axcheb.coloringgame.ColoringGame;
import ru.axcheb.coloringgame.desktop.model.DesktopImageState;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 1280;

		new LwjglApplication(new ColoringGame(new DesktopImageState()), config);
	}
}
