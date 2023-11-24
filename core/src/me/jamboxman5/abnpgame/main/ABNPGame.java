package me.jamboxman5.abnpgame.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.jamboxman5.abnpgame.data.DataManager;
import me.jamboxman5.abnpgame.entity.player.Player;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.managers.MapManager;
import me.jamboxman5.abnpgame.screen.MainMenuScreen;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Sounds;

import java.awt.*;

public class ABNPGame extends Game {

    private static ABNPGame instance;
    public SpriteBatch batch;
    private Player player;
    private final MapManager mapManager = new MapManager(this);
    private float zoom = 1;

    public void create() {
        instance = this;
        batch = new SpriteBatch();
        Fonts.initFonts();
        Sounds.initSounds();
        loadPlayerData();

        Sounds.AMBIENCE.setLooping(true);
        Sounds.AMBIENCE.play();

        this.setScreen(new MainMenuScreen(this));

    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        Sounds.dispose();
        Fonts.dispose();
    }

    private void loadPlayerData() {
        player = DataManager.loadLocalPlayer();
    }

    public static ABNPGame getInstance() { return instance; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player newPlayer) { player = newPlayer; }

    public MapManager getMapManager() { return mapManager; }

    public void setZoom(float newZoom) { zoom = newZoom; }
    public float getZoom() { return zoom; }

    public void zoomIn() {
        setZoom(getZoom()+.002f);
        for (Entity e : mapManager.getEntities()) {
            e.setWorldX(e.getWorldX()*(getZoom()/(getZoom()-.00206)));
            e.setWorldY(e.getWorldY()*(getZoom()/(getZoom()-.00206)));
        }
        player.setWorldX(player.getWorldX()*(getZoom()/(getZoom()-.00206)));
        player.setWorldY(player.getWorldY()*(getZoom()/(getZoom()-.00206)));
    }
    public void zoomOut() {
        setZoom(getZoom()-.005f);
        for (Entity e : mapManager.getEntities()) {
            e.setWorldX(e.getWorldX()*(getZoom()/(getZoom()+.00506)));
            e.setWorldY(e.getWorldY()*(getZoom()/(getZoom()+.00506)));
        }
        player.setWorldX(player.getWorldX()*(getZoom()/(getZoom()+.00506)));
        player.setWorldY(player.getWorldY()*(getZoom()/(getZoom()+.00506)));
    }
    public Point getMousePointer() {
        return null;
    }
}
