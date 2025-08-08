package me.jamboxman5.abnpgame.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.main.ABNPGame;

import java.util.UUID;

public abstract class Entity {
	
	protected final ABNPGame gp;
	
	private Sprite sprite;

	protected Vector2 position;
	protected String name;
	protected double speed;
	protected float rotation;
	protected Circle collision;
	protected int animFrame;
	
	protected String direction;
	protected final static float defaultSpriteScale = .25f;
	protected String id;

//	private int spriteCounter = 0;
//	private int spriteNumber = 1;
	
	public Entity(ABNPGame gamePanel) {
		gp = gamePanel;
		setDirection("forward");
		id = UUID.randomUUID().toString();
	}

	public void setDirection(String dir) { direction = dir; }
	public void setWorldX(double x) { position.x = (float) x; }
	public void setWorldY(double y) { position.y = (float) y; }
	public void setPosition(float x, float y) { position = new Vector2(x, y); }
	public void setPosition(Vector2 newPosition) { position = newPosition; }
	public void setSpeed(double speed) { this.speed = speed; }

	public abstract void update(float delta);
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

	public float getAngleToPoint(Vector2 target) {
		try {
			float num = (float) (position.y - target.y);
			float denom = (float) (position.x - target.x);
			if (denom == 0 && num == 0) return (float) (-Math.PI/2);
			float angle = (float) Math.atan(num/denom);
			if ((int)target.x <= position.x) {
				return (float) (angle - Math.toRadians(180));
			} else {
				return angle;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			return 0;
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

	public void setRotation(float i) { rotation = i; }
	public Circle getCollision() { return collision; }

	protected static Sprite setup(String imagePath, Float scale) {
		Texture t = new Texture(Gdx.files.internal(imagePath + ".png/"));
		Sprite s = new Sprite(t);
		if (scale == null) {
			s.setScale(defaultSpriteScale);
		} else {
			s.setScale(scale);
		}
		return s;

	}

	public float distanceTo(Vector2 target) {
		return (float) Math.sqrt((target.y - position.y) * (target.y - position.y) + (target.x - position.x) * (target.x - position.x));

	}

	public void drawCollision(ShapeRenderer shape) {
		int x = (int) (((collision.x - gp.getPlayer().getWorldX())*.5) + gp.getPlayer().getScreenX());
		int y = (int) (((collision.y - gp.getPlayer().getWorldY())*.5) + gp.getPlayer().getScreenY());


		shape.begin(ShapeRenderer.ShapeType.Line);
		shape.setColor(Color.RED);
		shape.circle(x, y, collision.radius);
		shape.end();
	}

	public float getRotation() { return rotation; }


	public String getID() { return id; }
}
