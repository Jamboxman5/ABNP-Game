package me.jamboxman5.abnpgame.entity;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import me.jamboxman5.abnpgame.main.ABNPGame;

public abstract class Entity {
	
	protected final ABNPGame gp;
	
	private Sprite sprite;

	protected Vector3 position;
	protected String name;
	protected double speed;
	protected float rotation;
	protected Shape2D collision;
	protected int animFrame;
	protected boolean isSolid;
	
	protected String direction;
	protected final static float defaultSpriteScale = .25f;
	protected String uuid;

//	private int spriteCounter = 0;
//	private int spriteNumber = 1;

	public Entity(ABNPGame gamePanel) {
		gp = gamePanel;
		setDirection("forward");
//		uuid = UUID.randomUUID().toString();
	}

	public void setDirection(String dir) { direction = dir; }
	public void setWorldX(double x) { position.x = (float) x; }
	public void setWorldZ(double z) { position.z = (float) z; }
	public void setWorldY(double y) { position.y = (float) y; }
	public void setPosition(float x, float y, float z) { position = new Vector3(x, y, z); }
	public void setPosition(Vector3 newPosition) { position = newPosition; }
	public void setSpeed(double speed) { this.speed = speed; }

	public boolean isSolid() { return isSolid; }

	public abstract void update(float delta);
	public abstract void draw(DecalBatch batch, ShapeRenderer shape, PerspectiveCamera camera);
	
	public void moveIfCollisionNotDetected() {
//        if (!isCollisionOn() && !gamePanel.getKeyHandler().isEnterPressed() && !gamePanel.getKeyHandler().isSpacePressed()) {
            switch (getDirection()) {
                case "forward":
                	setWorldZ(getWorldZ() - getSpeed());
                	break;
                case "back": 
                	setWorldZ(getWorldZ() + getSpeed());
                	break;
                case "left": 
                	setWorldX(getWorldX() - getSpeed());
                	break;
                case "right": 
                	setWorldX(getWorldX() + getSpeed());
                	break;
            }
        }

	public float getAngleToPoint(Vector3 target) {
		try {
			float num = (float) (position.z - target.z);
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
	public float getWorldX() { return position.x; }
	public float getWorldZ() { return position.z; }
	public float getWorldY() { return position.y; }
	public Vector3 getPosition() { return position; }
	public String getDirection() { return direction; }

	public String getName() { return name; }
	public void setRotation(float i) { rotation = i; }
	public Shape2D getCollision() { return collision; }

	protected static Sprite setup(String imagePath, AssetManager assets, Float scale) {
		Sprite s = new Sprite(assets.get(imagePath, Texture.class));
		if (scale == null) {
			s.setScale(defaultSpriteScale);
		} else {
			s.setScale(scale);
		}
		return s;

	}

	public float distanceTo(Vector3 target) {
		float dx = target.x - position.x;
		float dy = target.y - position.y;
		float dz = target.z - position.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public void drawCollision(ShapeRenderer shape) {
		ABNPGame gp = ABNPGame.getInstance();

		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.setAutoShapeType(true);
		shape.setColor(Color.RED);
		shape.circle(getWorldX(), getWorldZ(), 2);

		if (collision instanceof Circle) {
			Circle circle = (Circle) getCollision();

			shape.set(ShapeRenderer.ShapeType.Line);
			shape.circle(circle.x, circle.y, circle.radius); // world coords directly
			shape.end();

		} else if (collision instanceof Polygon) {
			Polygon poly = (Polygon) getCollision();

			shape.set(ShapeRenderer.ShapeType.Line);
			shape.polygon(poly.getTransformedVertices()); // already in world coords
			shape.end();
		} else {
			shape.end();
		}
	}


	public float getRotation() { return rotation; }


	public String getID() { return uuid; }
}
