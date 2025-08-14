package me.jamboxman5.abnpgame.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import me.jamboxman5.abnpgame.map.MapType;
import me.jamboxman5.abnpgame.net.listeners.*;
import me.jamboxman5.abnpgame.net.packets.*;
import me.jamboxman5.abnpgame.weapon.WeaponType;

public class NetUtil {

    public static void registerPackets(Kryo kryo) {
        kryo.register(Packet.class);
        kryo.register(PacketLogin.class);
        kryo.register(PacketMove.class);
        kryo.register(PacketMap.class);
        kryo.register(PacketWeaponChange.class);
        kryo.register(PacketShoot.class);
        kryo.register(MapType.class);
        kryo.register(WeaponType.class);
        kryo.register(PacketDisconnect.class);
    }

    public static void registerListeners(Client client) {
        client.addListener(new PacketDisconnectListener());
        client.addListener(new PacketMapListener());
        client.addListener(new PacketWeaponChangeListener());
        client.addListener(new PacketMoveListener());
        client.addListener(new PacketLoginListener());
        client.addListener(new PacketShootListener());
    }

}
