package me.jamboxman5.abnpgame.net.packets;

import me.jamboxman5.abnpgame.net.GameClient;
import me.jamboxman5.abnpgame.net.GameServer;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;

public class Packet04Weapon extends Packet {

	private String username;
	private String weapon;
	
	public Packet04Weapon(byte[] data) {
		super(04);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.weapon = dataArray[1];
	}
	
	public Packet04Weapon(String username, String weapon) {
		super(04);
		this.username = username;
		this.weapon = weapon;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("04" + this.username + "," + this.weapon).getBytes();
	}
	
	public String getUsername() { return username; }
	public Weapon getWeapon() {
		switch(weapon) {
		case "M4A1":
			return new RifleM4A1();
		case "Winchester 12GA":
			return new ShotgunWinchester12();
		case "M1911":
			return new Pistol1911();
		default:
			return new RifleM4A1();
		}
	}
}
