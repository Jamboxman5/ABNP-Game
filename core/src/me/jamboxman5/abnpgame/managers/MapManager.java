package me.jamboxman5.abnpgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.entity.player.OnlinePlayer;
import me.jamboxman5.abnpgame.entity.projectile.Projectile;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.screen.GameScreen;
import me.jamboxman5.abnpgame.weapon.Weapon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapManager {
	
	private final ABNPGame game;
	
	public Array<Entity> entities = new Array<>();
	public Array<Entity> disposingEntities = new Array<>();
	public Array<Projectile> projectiles = new Array<>();
	public Array<Projectile> disposingProjectiles = new Array<>();
	public Array<Map> maps = new Array<>();
	
	Map m;
	
	public MapManager(ABNPGame game) {
		this.game = game;
		
		getMaps();
	}

	void getMaps() {
		setup("Verdammtenstadt");
		setup("Black_Isle");
		setup("Farmhouse");
		setup("Karnivale");
		setup("Airbase");
	}

	public void draw() {
		
//		if (game.getScreen().getClass() != GameScreen.class) return;
//
//        int screenX = (int) (0 - game.getPlayer().getWorldX() + game.getPlayer().getScreenX());
//        int screenY = (int) (0 - game.getPlayer().getWorldY() + game.getPlayer().getScreenY());
//
////		int screenX = 0;
////		int screenY = 0;
//
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
//                RenderingHints.VALUE_RENDER_QUALITY);
//		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
//                RenderingHints.VALUE_STROKE_PURE);
//
//		g2.setColor(Color.black);
//		g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());
//		g2.drawImage(m.getImage(), screenX, screenY,(int) (m.getImage().getWidth()*gp.getZoom()), (int)(m.getImage().getHeight()*gp.getZoom()), null);
//
	}
	
	public void updateEntities() {
		for (Entity e : entities) { e.update(); }
		for (Entity e : disposingEntities) {
			if (entities.contains(e, false)) entities.removeValue(e, false);
		}
		disposingEntities = new Array<>();
	}
	
	public void drawEntities(Graphics2D g2) {
		for (Entity e : entities) { e.draw(); }
	}
	
	public void updateProjectiles() {
		for (Projectile p : projectiles) { p.update(); }
		for (Projectile p : disposingProjectiles) {
			if (projectiles.contains(p, false)) projectiles.removeValue(p, false);
		}
		disposingEntities = new Array<>();
	}
	
	public void drawProjectiles(Graphics2D g2) {
		for (Projectile p : projectiles) { p.draw(g2); }
	}
	
	public void addProjectile(Projectile p) {
		projectiles.add(p);
	}
	
	public void disposeProjectile(Projectile p) {
		disposingProjectiles.add(p);
	}
	
	public void setup(String imageName) {
		int x = 0;
		int y = 0;
		if (imageName.equals("Black_Isle")) {
			x = 1753;
			y = 1232;
		} else if (imageName.equals("Verdammtenstadt")) {
			x = 1426;
			y = 1374;
		} else if (imageName.equals("Farmhouse")) {
			x = 583;
			y = 483;
		} else if (imageName.equals("Airbase")) {
			x = 583;
			y = 483;
		} else if (imageName.equals("Karnivale")) {
			x = 583;
			y = 483;
		}
		m = new Map(imageName, x, y);
		Texture texture = new Texture(Gdx.files.internal("map/" + imageName + ".png/"));
		Sprite sprite = new Sprite(texture);
		sprite.scale(1.4f);
		m.setImage(sprite);
		maps.add(m);
	}

	public Map getActiveMap() {	return m; }
	
	public void addEntity(Entity entity) { entities.add(entity); }

	public Array<Entity> getEntities() { return entities; }

	public void removeConnectedPlayer(String username) {
		for (int i = 0; i < entities.size; i++) {
			if (entities.get(i) instanceof OnlinePlayer &&
				((OnlinePlayer)entities.get(i)).getUsername().equals(username)) {
				entities.removeIndex(i);
				break;
			}
		}
	}
	
	private int getConnectedPlayerIndex(String username) {
		for (int i = 0; i < entities.size; i++) {
			if (entities.get(i) instanceof OnlinePlayer && 
				((OnlinePlayer)entities.get(i)).getUsername().equals(username)) {
				return i;
			}
		}
		return -1;
	}
	
	public void movePlayer(String username, double x, double y, double rotation) {
		int index = getConnectedPlayerIndex(username);
		if (index < 0) return;
		entities.get(index).setWorldX(x*game.getZoom());
		entities.get(index).setWorldY(y*game.getZoom());
		entities.get(index).setRotation(rotation);
	}

	public void setMap(int mapIndex) {
		m = maps.get(mapIndex);
	}

	public void setMap(String map) {
		for(Map m2 : maps) {
			if (m2.toString().equals(map)) {
				m = m2;
				game.getPlayer().setWorldX(m.getDefaultX());
				game.getPlayer().setWorldY(m.getDefaultY());
				return;
			}
		}
	}
	
	public Array<Map> getMapList() { return maps; }

	public void setWeapon(String username, Weapon weapon) {
		int idx = getConnectedPlayerIndex(username);
		if (idx < 0) return;
		Weapon oldWeapon = ((OnlinePlayer)entities.get(idx)).getWeaponLoadout().getActiveWeapon();
		((OnlinePlayer)entities.get(idx)).getWeaponLoadout().addWeapon(weapon, true);
		((OnlinePlayer)entities.get(idx)).getWeaponLoadout().removeWeapon(oldWeapon);
	}
}
