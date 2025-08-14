package me.jamboxman5.abnpgame.net.listeners;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.maps.*;
import me.jamboxman5.abnpgame.net.packets.PacketMap;
import me.jamboxman5.abnpgame.screen.GameScreen;
import me.jamboxman5.abnpgame.script.BasicScript;

public class PacketMapListener extends Listener {

    @Override
    public void received(Connection conn, Object obj) {
        if (obj instanceof PacketMap) {
            PacketMap map = (PacketMap) obj;
            System.out.println("Received map " + map.type + "...");

            Map selected = new Farmhouse();

            switch(map.type) {
                case AIRBASE: {
                    selected = new Airbase();
                    break;
                }
                case BLACKISLE: {
                    selected = new BlackIsle();
                    break;

                }
                case FARMHOUSE: {
                    selected = new Farmhouse();
                    break;

                }
                case KARNIVALE: {
                    selected = new Karnivale();
                    break;

                }
                case VERDAMMTENSTADT: {
                    selected = new Verdammtenstadt();
                    break;
                }
            }
            ABNPGame game = ABNPGame.getInstance();
            Screen old = game.getScreen();
            game.setScreen(new GameScreen(game, selected, new BasicScript()));
            game.disposeScreen(old);
        }
    }
}
