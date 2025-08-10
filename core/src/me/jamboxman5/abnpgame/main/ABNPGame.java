package me.jamboxman5.abnpgame.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.data.DataManager;
import me.jamboxman5.abnpgame.entity.mob.player.OnlinePlayer;
import me.jamboxman5.abnpgame.entity.mob.player.Player;
import me.jamboxman5.abnpgame.managers.MapManager;
import me.jamboxman5.abnpgame.net.GameClient;
import me.jamboxman5.abnpgame.net.GameServer;
import me.jamboxman5.abnpgame.net.packets.Packet00Login;
import me.jamboxman5.abnpgame.net.packets.Packet02Move;
import me.jamboxman5.abnpgame.screen.ui.screens.GameOverScreen;
import me.jamboxman5.abnpgame.screen.ui.screens.MainMenuScreen;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;

import javax.swing.*;

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
    private GameServer socketServer;

    public void create() {
        instance = this;
        canvas = new SpriteBatch();
        uiCanvas = new SpriteBatch();
        uiShapeRenderer = new ShapeRenderer();
        shapeRenderer = new ShapeRenderer();
        mapManager = new MapManager(this);
        Fonts.initFonts();
        Sounds.initSounds();
        Sounds.updateVolumes();
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

    public void setupMultiplayerGame(boolean hosting) {

        String name = JOptionPane.showInputDialog("Input Gamertag: ");
        if (name == null) name = "";
        if (name.equalsIgnoreCase("")) name = "Spare Brains";

        if (hosting && socketServer == null) {
            socketServer = new GameServer(this);
            socketServer.start();
        }

        setClient(new GameClient(this, "192.168.1.24"));
        getClient().start();

        Packet00Login loginPacket = new Packet00Login(name, player.getWorldX(), player.getWorldY());

        if (socketServer != null) {
            socketServer.addConnection(new OnlinePlayer(this, player, null, -1), loginPacket);
        }

        loginPacket.writeData(getClient());

    }

    public void setClient(GameClient socketClient) {
        this.socketClient = socketClient;
    }

    public void generatePlayer() {
        player = DataManager.loadLocalPlayer();
    }

    public static ABNPGame getInstance() { return instance; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player newPlayer) { player = newPlayer; }

    public MapManager getMapManager() { return mapManager; }

    public Vector2 getMousePointer() {
        return new Vector2(Gdx.input.getX(), Settings.screenHeight - Gdx.input.getY());
    }

    public Vector2 getWorldMousePointer() {
        return new Vector2((float) (player.getWorldX() - (Gdx.graphics.getWidth()/2) + Gdx.input.getX()), (float) (player.getWorldY() - (Gdx.graphics.getHeight()/2) + (Settings.screenHeight - Gdx.input.getY())));
//        return new Vector2(Gdx.input.getX(), ScreenInfo.HEIGHT - Gdx.input.getY());
    }


    public GameClient getClient() { return socketClient; }
    public GameServer getServer() { return socketServer; }


    public void gameOver() {
        DataManager.save(getPlayer());
        this.getScreen().dispose();
        setScreen(new GameOverScreen(this));
//        generatePlayer();
    }
}
