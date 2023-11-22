package me.jamboxman5.abnpgame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import me.jamboxman5.abnpgame.ABNPGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(800,480);
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("ABNPGame");
		new Lwjgl3Application(new ABNPGame(), config);
	}
}
