package me.jamboxman5.abnpgame.net.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.net.packets.PacketDisconnect;

public class PacketDisconnectListener extends Listener {

    @Override
    public void received(Connection conn, Object obj) {
        if (obj instanceof PacketDisconnect) {
            ABNPGame.getInstance().disconnectPlayer((PacketDisconnect) obj);
        }
    }

}
