package de.pogs.rl;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Rocket Launch");
		config.setWindowIcon("assets/images/icon.png");
		config.setWindowedMode(1000, 700);
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 2);
		new Lwjgl3Application(new RocketLauncher(), config);
	}
}
