package me.jamboxman5.abnpgame.net.packets;

import me.jamboxman5.abnpgame.net.GameClient;
import me.jamboxman5.abnpgame.net.GameServer;

public abstract class Packet {

	public static enum PacketTypes {
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02), MAP(03), WEAPON(04);
		private int packetID;
		private PacketTypes(int packetId) {
			this.packetID = packetId;
			
		}
		public int getID() {
			return packetID;
		}
	}
	
	public byte packetID;
	
	public Packet(int packetId) {
		this.packetID = (byte) packetId;
	}
	
	public abstract void writeData(GameClient client);
	
	public abstract void writeData(GameServer server);
	
	public abstract byte[] getData();
	
	public String readData(byte[] data) {
		String message = new String(data).trim();
		return message.substring(2);
	}
	
	public static PacketTypes lookupPacket(int id) {
		for (PacketTypes p : PacketTypes.values()) {
			if (p.getID() == id) {
				return p;
			}
		}
		return PacketTypes.INVALID;
	}
	
	public static PacketTypes lookupPacket(String packetID) {
		try {
			return lookupPacket(Integer.parseInt(packetID));
		} catch (NumberFormatException e) {
			return PacketTypes.INVALID;
		}
	}
}
