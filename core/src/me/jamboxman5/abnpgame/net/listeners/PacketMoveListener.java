package me.jamboxman5.abnpgame.net.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.net.packets.PacketMove;

public class PacketMoveListener extends Listener {
    @Override
    public void received(Connection conn, Object obj) {
        if (obj instanceof PacketMove) {
//            System.out.println("Move: " + move.uuid + " - (" + move.x + "," + move.y + ") | angle: " + move.rotation);
            ABNPGame.getInstance().getMapManager().updateOnlinePlayerPosition((PacketMove) obj);
        }
    }
}
