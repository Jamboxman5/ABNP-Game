package me.jamboxman5.abnpgame.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.data.DataManager;
import me.jamboxman5.abnpgame.entity.player.Player;
import me.jamboxman5.abnpgame.managers.MapManager;
import me.jamboxman5.abnpgame.net.GameClient;
import me.jamboxman5.abnpgame.screen.ui.screens.GameOverScreen;
import me.jamboxman5.abnpgame.screen.ui.screens.MainMenuScreen;
import me.jamboxman5.abnpgame.screen.ScreenInfo;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Sounds;

public class ABNPGame extends Game {

    private static ABNPGame instance;
    public SpriteBatch canvas;
    public SpriteBatch uiCanvas;
    public ShapeRenderer uiShapeRenderer;
    public ShapeRenderer shapeRenderer;
    private Player player;
    private MapManager mapManager;
    private float zoom = 1;
    public boolean debugMode = false;

    private GameClient socketClient;

    public void create() {
        instance = this;
        canvas = new SpriteBatch();
        uiCanvas = new SpriteBatch();
        uiShapeRenderer = new ShapeRenderer();
        shapeRenderer = new ShapeRenderer();
        mapManager = new MapManager(this);
        Fonts.initFonts();
        Sounds.initSounds();
        generatePlayer();

        this.setScreen(new MainMenuScreen(this));

    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        canvas .dispose();
        uiCanvas.dispose();
        shapeRenderer.dispose();
        uiShapeRenderer.dispose();
        Sounds.dispose();
        Fonts.dispose();

    }

    public void generatePlayer() {
        player = DataManager.loadLocalPlayer();
    }

    public static ABNPGame getInstance() { return instance; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player newPlayer) { player = newPlayer; }

    public MapManager getMapManager() { return mapManager; }

    public Vector2 getMousePointer() {
        return new Vector2(Gdx.input.getX(), ScreenInfo.HEIGHT - Gdx.input.getY());
    }

    public Vector2 getWorldMousePointer() {
        return new Vector2((float) (player.getWorldX() - (Gdx.graphics.getWidth()/2) + Gdx.input.getX()), (float) (player.getWorldY() - (Gdx.graphics.getHeight()/2) + (ScreenInfo.HEIGHT - Gdx.input.getY())));
//        return new Vector2(Gdx.input.getX(), ScreenInfo.HEIGHT - Gdx.input.getY());
    }


    public GameClient getClient() { return socketClient; }


    public void gameOver() {
        DataManager.save(getPlayer());
        this.getScreen().dispose();
        setScreen(new GameOverScreen(this));
//        generatePlayer();
    }
}
