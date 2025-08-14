package me.jamboxman5.abnpgame.net.listeners;

import com.badlogic.gdx.Screen;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.jamboxman5.abnpgame.entity.mob.player.OnlinePlayer;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.maps.*;
import me.jamboxman5.abnpgame.net.packets.PacketLogin;
import me.jamboxman5.abnpgame.net.packets.PacketMap;
import me.jamboxman5.abnpgame.screen.GameScreen;
import me.jamboxman5.abnpgame.script.BasicScript;

public class PacketLoginListener extends Listener {

    @Override
    public void received(Connection conn, Object obj) {
        if (obj instanceof PacketLogin) {
            PacketLogin login = (PacketLogin) obj;
            OnlinePlayer joining = new OnlinePlayer(ABNPGame.getInstance(), login.username, login.uuid);
            ABNPGame.getInstance().getClientManager().connectPlayer(joining);
        }
    }
}
