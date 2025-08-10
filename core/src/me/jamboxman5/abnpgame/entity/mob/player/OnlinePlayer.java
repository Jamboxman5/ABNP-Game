package me.jamboxman5.abnpgame.entity.mob.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.net.packets.Packet02Move;

import java.net.InetAddress;

public class OnlinePlayer extends Player {

	public InetAddress ipAddress;
	public int port;
	private double lastRotation;
	
	public OnlinePlayer(ABNPGame gamePanel, String name, InetAddress ipAddress, int port) {
		super(gamePanel, name);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public OnlinePlayer(ABNPGame gamePanel, String name, int x, int y, InetAddress ipAddress, int port) {
		super(gamePanel, name);
		this.ipAddress = ipAddress;
		this.port = port;
		setWorldX(x);
		setWorldY(y);
	}

	public OnlinePlayer(ABNPGame game, Player player, InetAddress ipAddress, int port) {
		super(game, player.getUsername());
		this.ipAddress = ipAddress;
		this.port = port;


	}

	@Override
	public void update(float delta) {
		if (gp.getPlayer() == this) {
			super.update(delta);
			if (isMoving || (rotation != lastRotation)) {
				Packet02Move packet = new Packet02Move(getUsername(), position.x, position.y, getDrawingAngle());
				packet.writeData(gp.getClient());
			}
			lastRotation = rotation;

			Packet02Move packetMove = new Packet02Move(getUsername(), getWorldX(), getWorldY(), getAdjustedRotation());
			packetMove.writeData(gp.getClient());
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeRenderer shape) {
		if (gp.getPlayer() == this) {
			super.draw(batch, shape);
			return;
		} 

	}

}
