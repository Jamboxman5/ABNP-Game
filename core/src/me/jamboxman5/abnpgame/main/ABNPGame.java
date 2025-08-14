package me.jamboxman5.abnpgame.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
    private float zoom = 1;
    public boolean debugMode = false;

    DiscreteServer server;
    Client client;

    Array<Screen> disposal;

    Array<String> connectedPlayers;
    HashMap<String, String> connectedPlayerNames;

    public void create() {
        instance = this;
        canvas = new SpriteBatch();
        uiCanvas = new SpriteBatch();
        uiShapeRenderer = new ShapeRenderer();
        shapeRenderer = new ShapeRenderer();
        mapManager = new MapManager(this);

        this.setScreen(new LoadingScreen(this));

        loadAssets();
        Sounds.updateVolumes();
        generatePlayer();

        disposal = new Array<>();

        Screen old = getScreen();
        this.setScreen(new MainMenuScreen(this));
        old.dispose();
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
        canvas .dispose();
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

    public void sendPacketTCP(Packet packet) {
        client.sendTCP(packet);
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

        connectedPlayers = new Array<>();
        connectedPlayerNames = new HashMap<>();

        String name = JOptionPane.showInputDialog("Input Gamertag: ");
        String address;
        if (!hosting) address = JOptionPane.showInputDialog("Input Server Address: ");
        else address = "localhost";
        if (name == null) name = "";
        if (name.equalsIgnoreCase("")) name = "Spare Brains";

        player.setName(name);
        client = new Client();
        Kryo kryo = client.getKryo();

        NetUtil.registerPackets(kryo);
        NetUtil.registerListeners(client);

        client.start();

        try {
            client.connect(5000, address, 13331, 13331);
            sendLogin();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendLogin() {
        PacketLogin login = new PacketLogin();
        login.username = player.getUsername();
        login.uuid = player.getID();
        client.sendTCP(login);
        connectedPlayers.add(player.getID());
        connectedPlayerNames.put(player.getID(), player.getUsername());
    }

    public void disconnectPlayer(PacketDisconnect disconnect) {
        connectedPlayers.removeValue(disconnect.uuid, false);
        connectedPlayerNames.remove(disconnect.uuid);
        mapManager.removeOnlinePlayer(disconnect);
    }

    public void connectPlayer(OnlinePlayer joining) {
        mapManager.addOnlinePlayer(joining);
        connectedPlayers.add(joining.getID());
        connectedPlayerNames.put(joining.getID(), joining.getName());
    }

    public void loadAssets() {
        Zombie.initSprites();
        RifleM4A1.initSounds();
        RifleM4A1.initSprites();
        Pistol1911.initSounds();
        Pistol1911.initSprites();
        ShotgunWinchester12.initSounds();
        ShotgunWinchester12.initSprites();
        PickupWeapon.initSprites();
        Pickup.initSprites();
        Fonts.initFonts();
        Sounds.initSounds();
        Map.loadMaps();
    }

    public void closeMultiplayerGame() throws IOException {
        if (client != null) client.stop();
        if (server != null) server.stop();
        client = null;
        server = null;
        connectedPlayers = null;
        connectedPlayerNames = null;
        mapManager.removeOnlinePlayers();
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

    public boolean isMultiplayer() {
        return (server != null || client != null);
    }
    public boolean isHosting() {
        return (server != null);
    }

    public void sendPacketUDP(Packet p) { client.sendUDP(p);
    }

    public Array<String> getConnectedPlayers() {
        if (connectedPlayers == null) return new Array<>();
        else return connectedPlayers;
    }

    public String getConnectedPlayerName(String uuid) {
        if (connectedPlayerNames.containsKey(uuid)) return connectedPlayerNames.get(uuid);
        else return "PLAYER NOT FOUND";
    }
}
