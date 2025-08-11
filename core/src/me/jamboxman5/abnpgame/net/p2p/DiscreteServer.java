package me.jamboxman5.abnpgame.net.p2p;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import me.jamboxman5.abnpgame.net.packets.Login;
import me.jamboxman5.abnpgame.net.packets.Move;

import java.io.IOException;

public class DiscreteServer {

    private Server server;

    public DiscreteServer() {
        server = new Server();
    }

    public void start() throws IOException {
        Kryo kryo = server.getKryo();

        kryo.register(Login.class);
        kryo.register(Move.class);

        server.addListener(new Listener() {
            public void received(Connection conn, Object obj) {
                if (obj instanceof Login) {
                    Login login = (Login) obj;
                    System.out.println("User connected: " + login.username);
                }
                if (obj instanceof Move) {
                    Move move = (Move) obj;
                    System.out.println("Move: " + move.x + "," + move.y + " | " + move.rotation);
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
