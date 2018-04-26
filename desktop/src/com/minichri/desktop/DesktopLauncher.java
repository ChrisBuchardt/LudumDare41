package com.minichri.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.minichri.MainGame;
import com.minichri.helpers.GameInfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = GameInfo.SCREEN_WIDTH;
		config.height = GameInfo.SCREEN_HEIGHT;

		new LwjglApplication(new MainGame(), config);
	}
}
