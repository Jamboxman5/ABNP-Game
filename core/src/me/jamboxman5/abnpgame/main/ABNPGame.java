package me.jamboxman5.abnpgame.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.jamboxman5.abnpgame.data.DataManager;
import me.jamboxman5.abnpgame.entity.mob.player.OnlinePlayer;
import me.jamboxman5.abnpgame.entity.mob.player.Player;
import me.jamboxman5.abnpgame.entity.mob.zombie.Zombie;
import me.jamboxman5.abnpgame.entity.prop.pickup.Pickup;
import me.jamboxman5.abnpgame.entity.prop.pickup.PickupWeapon;
import me.jamboxman5.abnpgame.managers.MapManager;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.maps.*;
import me.jamboxman5.abnpgame.net.ClientManager;
import me.jamboxman5.abnpgame.net.listeners.*;
import me.jamboxman5.abnpgame.net.p2p.DiscreteServer;
import me.jamboxman5.abnpgame.net.packets.*;
import me.jamboxman5.abnpgame.screen.GameScreen;
import me.jamboxman5.abnpgame.screen.ui.screens.GameOverScreen;
import me.jamboxman5.abnpgame.screen.ui.screens.LoadingScreen;
import me.jamboxman5.abnpgame.screen.ui.screens.MainMenuScreen;
import me.jamboxman5.abnpgame.script.BasicScript;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.NetUtil;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ABNPGame extends Game {

    private static ABNPGame instance;
    public SpriteBatch canvas;
    public SpriteBatch uiCanvas;
    public ShapeRenderer uiShapeRenderer;
    public ShapeRenderer shapeRenderer;
    private Player player;
    private MapManager mapManager;
    private AssetManager assetManager;
    private float zoom = 1;
    public boolean debugMode = false;

    DiscreteServer server;
    ClientManager clientManager;

    Array<Screen> disposal;



    public void create() {
        instance = this;
        canvas = new SpriteBatch();
        uiCanvas = new SpriteBatch();
        uiShapeRenderer = new ShapeRenderer();
        shapeRenderer = new ShapeRenderer();
        mapManager = new MapManager(this);
        clientManager = new ClientManager(this);
        assetManager = new AssetManager();

        Fonts.initFonts();

        this.setScreen(new LoadingScreen(this));

        loadAssets();
//        generatePlayer();

        disposal = new Array<>();


    }

    public void render() {

        super.render(); // important!
        for (Screen s : disposal) {
            s.dispose();
        }


    }

    public void disposeScreen(Screen s) {
        disposal.add(s);
    }

    public void dispose() {
        canvas.dispose();
        uiCanvas.dispose();
        shapeRenderer.dispose();
        uiShapeRenderer.dispose();
        try {
            closeMultiplayerGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        String address;
        if (!hosting) address = JOptionPane.showInputDialog("Input Server Address: ");
        else address = "localhost";
        if (name == null) name = "";
        if (name.equalsIgnoreCase("")) name = "Spare Brains";

        player.setUsername(name);

        clientManager.connect(player, address);

    }

    public void loadAssets() {
        Zombie.loadAssets(assetManager);
        RifleM4A1.loadAssets(assetManager);
        Pistol1911.loadAssets(assetManager);
        ShotgunWinchester12.loadAssets(assetManager);
        PickupWeapon.loadAssets(assetManager);
        Pickup.loadAssets(assetManager);
        Sounds.loadAssets(assetManager);
        Map.loadAssets(assetManager);
    }

    public void closeMultiplayerGame() throws IOException {
        clientManager.disconnect();
        if (server != null) server.stop();
        server = null;
        mapManager.removeOnlinePlayers();
    }

    public void generatePlayer() {
        player = DataManager.loadLocalPlayer();
    }

    public static ABNPGame getInstance() { return instance; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player newPlayer) { player = newPlayer; }

    public MapManager getMapManager() { return mapManager; }
    public ClientManager getClientManager() { return clientManager; }
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

    public boolean isMultiplayer() {
        return (server != null || clientManager.isConnected());
    }
    public boolean isHosting() {
        return (server != null);
    }

    public AssetManager getAssetManager() { return assetManager; }





}
