package me.jamboxman5.abnpgame.net.p2p;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import me.jamboxman5.abnpgame.net.packets.PacketLogin;
import me.jamboxman5.abnpgame.net.packets.PacketMap;
import me.jamboxman5.abnpgame.net.packets.PacketMove;
import me.jamboxman5.abnpgame.util.NetUtil;

import java.io.IOException;

public class DiscreteServer {

    private Server server;

    public DiscreteServer() {
        server = new Server();
    }

    public void start() throws IOException {
        Kryo kryo = server.getKryo();

        NetUtil.registerPackets(kryo);

        server.addListener(new Listener() {
            public void received(Connection conn, Object obj) {
                if (obj instanceof PacketLogin) {
                    PacketLogin login = (PacketLogin) obj;
                    System.out.println("User connected: " + login.username);
                }
                if (obj instanceof PacketMove) {
                    PacketMove move = (PacketMove) obj;
                    System.out.println("Move: " + move.x + "," + move.y + " | " + move.rotation);
                }
                if (obj instanceof PacketMap) {
                    PacketMap map = (PacketMap) obj;
                    server.sendToAllTCP(map);
                }
            }
        });

        server.bind(54555, 54777);
        server.start();
        System.out.println("SERVER STARTED!");
    }

    public void stop() throws IOException {
        server.stop();
        server.dispose();
    }

    public Server getServer() { return server; }

}
