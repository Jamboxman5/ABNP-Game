package me.jamboxman5.abnpgame.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.main.ABNPGame;

import java.awt.*;

public abstract class Entity {
	
	protected final ABNPGame gp;
	
	private Sprite sprite;

	protected Vector2 position;
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
	public void setWorldX(double x) { position.x = (float) x; }
	public void setWorldY(double y) { position.y = (float) y; }
	public void setPosition(float x, float y) { position = new Vector2(x, y); }
	public void setPosition(Vector2 newPosition) { position = newPosition; }
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
	public double getWorldX() { return position.x; }
	public double getWorldY() { return position.y; }
	public Vector2 getPosition() { return position; }
	public String getDirection() { return direction; }

	public String getName() { return name; }
	public int getScreenY() {
		return (int) (position.x - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX());
	}
	public int getScreenX() {
		return (int) (position.y - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY());
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	public Shape getCollision() { return collision; }

}
