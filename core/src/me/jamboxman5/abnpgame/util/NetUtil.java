package me.jamboxman5.abnpgame.util;

import com.esotericsoftware.kryo.Kryo;
import me.jamboxman5.abnpgame.map.MapType;
import me.jamboxman5.abnpgame.net.packets.Packet;
import me.jamboxman5.abnpgame.net.packets.PacketLogin;
import me.jamboxman5.abnpgame.net.packets.PacketMap;
import me.jamboxman5.abnpgame.net.packets.PacketMove;

public class NetUtil {

    public static void registerPackets(Kryo kryo) {
        kryo.register(Packet.class);
        kryo.register(PacketLogin.class);
        kryo.register(PacketMove.class);
        kryo.register(PacketMap.class);
        kryo.register(MapType.class);
    }

}
