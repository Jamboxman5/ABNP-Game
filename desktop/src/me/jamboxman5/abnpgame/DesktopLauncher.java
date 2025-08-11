package me.jamboxman5.abnpgame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Settings;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(Settings.screenWidth,Settings.screenHeight);
		config.useVsync(true);

		config.setForegroundFPS(60);
		config.setResizable(false);
		config.setTitle("ABNPGame");
		new Lwjgl3Application(new ABNPGame(), config);
	}
}
