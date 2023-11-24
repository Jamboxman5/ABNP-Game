package me.jamboxman5.abnpgame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.ScreenInfo;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(ScreenInfo.WIDTH,ScreenInfo.HEIGHT);
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("ABNPGame");
		new Lwjgl3Application(new ABNPGame(), config);
	}
}
