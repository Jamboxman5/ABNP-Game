package me.jamboxman5.abnpgame.util;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.GameScreen;

public class ButtonUtil {

    public static Runnable getScreenSwitchButtonAction(final ABNPGame game, final Screen switchTo) {
        return new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(switchTo);
                game.disposeScreen(old);
            }
        };
    }

}
