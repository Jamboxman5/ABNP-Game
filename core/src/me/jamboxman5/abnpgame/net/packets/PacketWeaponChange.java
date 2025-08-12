package me.jamboxman5.abnpgame.net.packets;

import me.jamboxman5.abnpgame.weapon.WeaponType;

public class PacketWeaponChange extends Packet {
    public WeaponType type;
    public String uuid;
}
