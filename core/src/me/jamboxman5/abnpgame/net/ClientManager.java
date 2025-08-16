package me.jamboxman5.abnpgame.net;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import me.jamboxman5.abnpgame.entity.mob.player.OnlinePlayer;
import me.jamboxman5.abnpgame.entity.mob.player.Player;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.net.packets.Packet;
import me.jamboxman5.abnpgame.net.packets.PacketDisconnect;
import me.jamboxman5.abnpgame.net.packets.PacketLogin;
import me.jamboxman5.abnpgame.util.NetUtil;

import java.io.IOException;
import java.util.HashMap;

public class ClientManager {

    Client client;
    ABNPGame game;

    Array<String> connectedPlayers;
    HashMap<String, String> connectedPlayerNames;

    public ClientManager(ABNPGame game) {
        this.game = game;
    }

    public void sendPacketTCP(Packet packet) {
        client.sendTCP(packet);
    }
    public void sendPacketUDP(Packet p) { client.sendUDP(p); }

    public void connect(Player player, String address) {
        if (address.length() == 0) address = "localhost";
        client = new Client();
        Kryo kryo = client.getKryo();

        connectedPlayers = new Array<>();
        connectedPlayerNames = new HashMap<>();

        NetUtil.registerPackets(kryo);
        NetUtil.registerListeners(client);
        client.start();
        try {
            client.connect(5000, address, 13331, 13331);
            sendLogin(player);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendLogin(Player player) {
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
        game.getMapManager().removeOnlinePlayer(disconnect);
    }

    public void connectPlayer(OnlinePlayer joining) {
        game.getMapManager().addOnlinePlayer(joining);
        connectedPlayers.add(joining.getID());
        connectedPlayerNames.put(joining.getID(), joining.getName());
    }

    public boolean isConnected() {
        return (client != null && client.isConnected());
    }

    public Array<String> getConnectedPlayers() {
        if (connectedPlayers == null) return new Array<>();
        else return connectedPlayers;
    }

    public String getConnectedPlayerName(String uuid) {
        if (connectedPlayerNames.containsKey(uuid)) return connectedPlayerNames.get(uuid);
        else return "PLAYER NOT FOUND";
    }

    public void disconnect() {
        if (client == null) return;
        client.stop();
        client.close();
        try {
            client.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client = null;
        connectedPlayerNames = null;
        connectedPlayers = null;

    }
}
