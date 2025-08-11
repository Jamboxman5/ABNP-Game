package me.jamboxman5.abnpgame.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.jamboxman5.abnpgame.data.DataManager;
import me.jamboxman5.abnpgame.entity.mob.player.Player;
import me.jamboxman5.abnpgame.managers.MapManager;
import me.jamboxman5.abnpgame.net.p2p.DiscreteServer;
import me.jamboxman5.abnpgame.net.packets.Login;
import me.jamboxman5.abnpgame.net.packets.Move;
import me.jamboxman5.abnpgame.screen.ui.screens.GameOverScreen;
import me.jamboxman5.abnpgame.screen.ui.screens.MainMenuScreen;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;

import javax.swing.*;
import java.io.IOException;

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

    DiscreteServer server;
    Client client;

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

        if (hosting) {
            server = new DiscreteServer();
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String name = JOptionPane.showInputDialog("Input Gamertag: ");
        if (name == null) name = "";
        if (name.equalsIgnoreCase("")) name = "Spare Brains";

        client = new Client();
        Kryo kryo = client.getKryo();

        kryo.register(Login.class);
        kryo.register(Move.class);

        client.addListener(new Listener() {
            public void received(Connection conn, Object obj) {
                System.out.println("Received: " + obj);
            }
        });

        client.start();
        try {
            client.connect(5000, "localhost", 54555, 54777);

            Login login = new Login();
            login.username = name;
            client.sendTCP(login);

            Move move = new Move();
            move.x = 69;
            move.y = 420;
            move.rotation = 13.37f;
            client.sendUDP(move);

        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void gameOver() {
        DataManager.save(getPlayer());
        this.getScreen().dispose();
        setScreen(new GameOverScreen(this));
//        generatePlayer();
    }
}
