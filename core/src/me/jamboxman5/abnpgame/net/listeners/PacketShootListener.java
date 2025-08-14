package me.jamboxman5.abnpgame.net.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.jamboxman5.abnpgame.entity.mob.player.OnlinePlayer;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.net.packets.PacketLogin;
import me.jamboxman5.abnpgame.net.packets.PacketShoot;

public class PacketShootListener extends Listener {

    @Override
    public void received(Connection conn, Object obj) {
        if (obj instanceof PacketShoot) {
            PacketShoot shoot = (PacketShoot) obj;
            ABNPGame.getInstance().getMapManager().onlinePlayerShoot(shoot);
        }
    }
}
