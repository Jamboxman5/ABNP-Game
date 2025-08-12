package me.jamboxman5.abnpgame.net.p2p;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import me.jamboxman5.abnpgame.net.packets.*;
import me.jamboxman5.abnpgame.util.NetUtil;

import java.io.IOException;
import java.util.HashMap;

public class DiscreteServer {

    private Server server;

    HashMap<Connection, PacketLogin> connections;

    public DiscreteServer() {
        server = new Server();
    }

    public void start() throws IOException {
        Kryo kryo = server.getKryo();

        connections = new HashMap<Connection, PacketLogin>();

        NetUtil.registerPackets(kryo);

        server.addListener(new Listener() {
            public void received(Connection conn, Object obj) {
                if (obj instanceof PacketLogin) {
                    PacketLogin login = (PacketLogin) obj;
                    connections.put(conn, login);
                    System.out.println("User connected: " + login.username + " (" + login.uuid + ")");
                    server.sendToAllExceptTCP(conn.getID(), login);
                    for (Connection c : getServer().getConnections()) {
                        if (c.getID() != conn.getID()) {
                            server.sendToTCP(conn.getID(), connections.get(c));
                        }
                    }

                }
                if (obj instanceof PacketMove) {
                    PacketMove move = (PacketMove) obj;
                    server.sendToAllExceptUDP(conn.getID(), move);
                }
                if (obj instanceof PacketMap) {
                    PacketMap map = (PacketMap) obj;
                    server.sendToAllTCP(map);
                }
                if (obj instanceof PacketWeaponChange) {
                    PacketWeaponChange weaponChange = (PacketWeaponChange) obj;
                    server.sendToAllExceptTCP(conn.getID(), weaponChange);
                }
                if (obj instanceof PacketShoot) {
                    PacketShoot shoot = (PacketShoot) obj;
                    server.sendToAllExceptTCP(conn.getID(), shoot);
                }
            }
        });

        server.bind(13331, 13331);
        server.start();
        System.out.println("SERVER STARTED!");
    }

    public void stop() throws IOException {
        server.stop();
        server.dispose();
    }

    public Server getServer() { return server; }

}
