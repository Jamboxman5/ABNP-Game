package me.jamboxman5.abnpgame.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.jamboxman5.abnpgame.main.ABNPGame;

import java.awt.*;

public abstract class Entity {
	
	protected final ABNPGame gp;
	
	private Sprite sprite;
	
	protected double worldX, worldY;
	protected String name;
	protected double speed;
	protected double rotation;
	protected Rectangle collision;
	protected int collisionWidth;
	protected int animFrame;
	
	protected String direction;
//	private int spriteCounter = 0;
//	private int spriteNumber = 1;
	
	public Entity(ABNPGame gamePanel) {
		gp = gamePanel;
		setDirection("forward");
	}

	public void setDirection(String dir) { direction = dir; }
	public void setWorldX(double x) { worldX = x; }
	public void setWorldY(double y) { worldY = y; }
	public void setSpeed(double speed) { this.speed = speed; }

	public abstract void update();
	public abstract void draw(SpriteBatch batch, ShapeRenderer shape);
	
	public Sprite setup(String imagePath, float scale) {
		Texture t = new Texture(Gdx.files.internal(imagePath));
		Sprite s = new Sprite(t);
		s.scale(scale);
		return s;
//        BufferedImage image = null;
//
//        try {
//        	InputStream src = getClass().getResourceAsStream("/me/jamboxman5/abnpgame" + imagePath + ".png");
//            image = ImageIO.read(src);
//
//        } catch (IOException | IllegalArgumentException e) {
//            e.printStackTrace();
//            System.out.println(imagePath);
//        }
//
//        return Utilities.scaleImage(image, (int)(image.getWidth() * scale), (int)(image.getHeight() * scale));
    }
	
	public void moveIfCollisionNotDetected() {
//        if (!isCollisionOn() && !gamePanel.getKeyHandler().isEnterPressed() && !gamePanel.getKeyHandler().isSpacePressed()) {
            switch (getDirection()) {
                case "forward":
                	setWorldY(getWorldY() - getSpeed());
                	break;
                case "back": 
                	setWorldY(getWorldY() + getSpeed());
                	break;
                case "left": 
                	setWorldX(getWorldX() - getSpeed());
                	break;
                case "right": 
                	setWorldX(getWorldX() + getSpeed());
                	break;
            }
        }
	
	
	public void setSprite(Sprite img) { sprite = img; }
	public Sprite getSprite() { return sprite; }
	public double getSpeed() { return speed; }
	public double getWorldX() { return worldX; }
	public double getWorldY() { return worldY; }
	public double getAdjustedWorldX() { return worldX; }
	public double getAdjustedWorldY() { return worldY; }
	public String getDirection() { return direction; }

	public String getName() { return name; }
	public int getScreenY() {
		return (int) (worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX());
	}
	public int getScreenX() {
		return (int) (worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY());
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	public Shape getCollision() { return collision; }

}
