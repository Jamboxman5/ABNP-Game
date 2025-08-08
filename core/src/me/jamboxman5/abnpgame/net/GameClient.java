package me.jamboxman5.abnpgame.net;

import me.jamboxman5.abnpgame.entity.mob.player.OnlinePlayer;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.net.packets.*;
import me.jamboxman5.abnpgame.net.packets.Packet.PacketType;

import java.io.IOException;
import java.net.*;

public class GameClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;
	private ABNPGame gp;
	
	public GameClient(ABNPGame gamePanel, String address) {
		gp = gamePanel;
		try {
			socket = new DatagramSocket();
			ipAddress = InetAddress.getByName(address);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

//			String message = new String(packet.getData()).trim();
//			System.out.println("SERVER > " + message);		
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0,2));
		Packet packet = null;
		switch (type) {
		default:
			break;
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			handleLogin((Packet00Login) packet, address, port);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + 
			((Packet01Disconnect)packet).getUsername() + " has left the game.");
			gp.getMapManager().removeConnectedPlayer(((Packet01Disconnect)packet).getUsername());
			break;
		case MOVE:
			packet = new Packet02Move(data);
			handleMove((Packet02Move) packet);
			break;
		case MAP:
			packet = new Packet03Map(data);
			gp.getMapManager().setMap(((Packet03Map)packet).getMap());
			break;
		case WEAPON:
			packet = new Packet04Weapon(data);
			handleWeaponSwitch((Packet04Weapon) packet);
			break;
		}
	}
	
	private void handleWeaponSwitch(Packet04Weapon packet) {
		gp.getMapManager().setWeapon(packet.getUsername(), packet.getWeapon());
	}
	
	private void handleMove(Packet02Move packet) {
		gp.getMapManager().movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getRotation()); 
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 13331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + 
		packet.getUsername() + " has joined the game.");
		OnlinePlayer player = new OnlinePlayer(gp, packet.getUsername(), packet.getX(), packet.getY(), address, port);
		gp.getMapManager().addEntity(player);
	}
}
