package me.jamboxman5.abnpgame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.ScreenInfo;
import me.jamboxman5.drop.Drop;
import me.jamboxman5.galaga.GalagaGame;
import me.jamboxman5.legacyofgoku.LegacyGame;
import me.jamboxman5.pathfinder.PathfinderGame;
import me.jamboxman5.statemachine.StateMachineGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(ScreenInfo.WIDTH,ScreenInfo.HEIGHT);
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("GuardGame");
		new Lwjgl3Application(new ABNPGame(), config);
	}
}
