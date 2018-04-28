package com.minichri.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.minichri.MainGame;
import com.minichri.helpers.GameInfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = GameInfo.SCREEN_WIDTH;
		config.height = GameInfo.SCREEN_HEIGHT;
		config.title = "LudumDare41 - Minichri project";
		config.addIcon("escape_pod.png", Files.FileType.Internal);
		config.foregroundFPS = 60;

		new LwjglApplication(new MainGame(), config);
	}
}
