package me.jamboxman5.abnpgame.net.p2p.script;

import com.esotericsoftware.kryonet.Server;
import me.jamboxman5.abnpgame.net.p2p.DiscreteServer;
import me.jamboxman5.abnpgame.script.MissionScript;

public class MultiplayerScript extends MissionScript {

    Server server;

    public MultiplayerScript(Server server) {
        this.server = server;
    }

    @Override
    public void run() {

    }

}
