package me.jamboxman5.abnpgame.net.packets;

import me.jamboxman5.abnpgame.net.GameClient;
import me.jamboxman5.abnpgame.net.GameServer;

public class Packet02Move extends Packet {

	private double x, y, rotation;
	private String entityID;
	
	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.entityID = dataArray[0];
		this.x = Double.parseDouble(dataArray[1]);
		this.y = Double.parseDouble(dataArray[2]);
		this.rotation = Double.parseDouble(dataArray[3]);
	}
	
	public Packet02Move(String entityID, double x, double y, double rotation) {
		super(02);
		this.entityID = entityID;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
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
		return ("02" + this.entityID + "," + this.x + "," + this.y + "," + this.rotation).getBytes();
	}
	
	public String getUsername() { return entityID; }
	public double getX() { return x; }
	public double getY() { return y; }
	public float getRotation() { return (float) rotation; }
}
