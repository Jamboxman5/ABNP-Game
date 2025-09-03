package me.jamboxman5.abnpgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.data.SettingsData;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Settings;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main(String[] arg) {
		boolean generateNewSettings = SettingsData.loadSettings();

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		Graphics.DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		Vector2 displayRes = new Vector2(displayMode.width, displayMode.height);
		if (displayRes.x == Settings.screenWidth && displayRes.y == Settings.screenHeight) config.setDecorated(false);

		config.setWindowedMode(Settings.screenWidth,Settings.screenHeight);
		config.useVsync(true);

		config.setForegroundFPS(60);
		config.setResizable(false);
		config.setTitle("ABNPGame");

		config.setWindowIcon("icons/GameIcon.png");

		new Lwjgl3Application(new ABNPGame(), config);

		Settings.setResolution(new Vector2(Settings.getResolution()));

		if (generateNewSettings) {
			SettingsData.loadSettings();
		}
	}

}
