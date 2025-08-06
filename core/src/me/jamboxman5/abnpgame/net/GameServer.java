package me.jamboxman5.abnpgame.net;

import me.jamboxman5.abnpgame.entity.mob.player.OnlinePlayer;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.net.packets.*;
import me.jamboxman5.abnpgame.net.packets.Packet.PacketTypes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Thread {

	private DatagramSocket socket;
	private ABNPGame gp;
	private boolean bound = true;
	private Map activeMap;
	
	private List<OnlinePlayer> connectedPlayers = new ArrayList<>();
	
	public GameServer(ABNPGame gamePanel) {
		gp = gamePanel;
		activeMap = gp.getMapManager().getActiveMap();
		try {
			socket = new DatagramSocket(13331);
		} catch (SocketException e) {
			e.printStackTrace();
		} 
	}
	
	public void run() {
		while (bound) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//			String message = new String(packet.getData()).trim();
//			System.out.println("CLIENT [" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "] > " + message);
//			if (message.equalsIgnoreCase("ping")) {
//				System.out.println("Returning pong...");
//				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
//			}
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0,2));
		Packet packet = null;
		switch (type) {
		default:
			break;
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet00Login)packet).getUsername() + " has connected.");
			OnlinePlayer player = new OnlinePlayer(gp, ((Packet00Login)packet).getUsername(), ((Packet00Login)packet).getX(), ((Packet00Login)packet).getY(), address, port);
			this.addConnection(player, (Packet00Login)packet);
			new Packet03Map(activeMap.toString()).writeData(this);;
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect)packet).getUsername() + " has disconnected.");
			this.removeConnection((Packet01Disconnect)packet);
			break;
		case MOVE:
			packet = new Packet02Move(data);
			//System.out.println(((Packet02Move) packet).getUsername() + " new position: X:" + ((Packet02Move) packet).getX() + " Y:" + ((Packet02Move) packet).getY() + " Rotation:" + ((Packet02Move) packet).getRotation());
			this.handleMove(((Packet02Move) packet));
		}
	}

	private void handleMove(Packet02Move packet) {
		if (getConnectedPlayer(packet.getUsername()) != null) {
			packet.writeData(this);
		}
	}

	public void removeConnection(Packet01Disconnect packet) {
		connectedPlayers.remove(getConnectedPlayerIndex(packet.getUsername()));
		packet.writeData(this);
	}
	
	public OnlinePlayer getConnectedPlayer(String username) {
		for (OnlinePlayer p : connectedPlayers) {
			if (p.getUsername().equals(username)) {
				return p;
			}
		}
		return null;
	}
	
	public int getConnectedPlayerIndex(String username) {
		for (int i = 0; i < connectedPlayers.size(); i++) {
			if (connectedPlayers.get(i).getUsername().equals(username)) {
				return i;
			}
		}
		return 0;
	}

	public void addConnection(OnlinePlayer player, Packet00Login packet) {
		boolean alreadyConnected = false;
		for (OnlinePlayer p : connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if (p.ipAddress == null) {
					p.ipAddress = player.ipAddress;
				}
				
				if (p.port == -1) {
					p.port = player.port;
				}
				alreadyConnected = true;
			} else {
				sendData(packet.getData(), p.ipAddress, p.port);
				packet = new Packet00Login(p.getUsername(), p.getWorldX(), p.getWorldY());
				sendData(packet.getData(), player.ipAddress, player.port);
			}
		}
		if (!alreadyConnected) {
			connectedPlayers.add(player);
		}
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (OnlinePlayer p: connectedPlayers) {
			sendData(data, p.ipAddress, p.port);
		}
	}
}
