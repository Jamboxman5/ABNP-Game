package me.jamboxman5.abnpgame.entity.player;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.Mob;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.InputKeys;
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.RedDotSight;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import java.awt.*;

public class Player extends Mob {
	
	private final static int defaultSpeed = 10;
	private String gamerTag;
	protected int money;
	protected int exp;
	
	protected WeaponLoadout weapons;
		
	public Player(ABNPGame gamePanel, String name) {
		super(gamePanel, 
			  name, 
			  gamePanel.getMapManager().getActiveMap().getDefaultX(), 
			  gamePanel.getMapManager().getActiveMap().getDefaultY(), 
			  defaultSpeed);

		WeaponModLoadout mods = new WeaponModLoadout();
		mods.addMod(new RedDotSight());
		weapons = new WeaponLoadout();
		weapons.getActiveWeapon().setMods(mods);
		
		gamerTag = name;
		
		screenX = Gdx.graphics.getWidth()/2;
		screenY = Gdx.graphics.getHeight()/2;

		footstep1 = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/entity/player/footsteps/Player_Footstep_1.wav"));
		footstep2 = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/entity/player/footsteps/Player_Footstep_2.wav"));
		setDefaults();
	}

	private void setDefaults() {		
		setSpeed(6.5);
		setRotation(0);
		collisionWidth = 50;
		collision = new Rectangle((int)(getWorldX())-(collisionWidth/4),
				  (int)(getWorldY())-(collisionWidth/2),
				  (int)(collisionWidth*1.5) , 
				  (int)(collisionWidth));
	}
	
	@Override
	public void update() {

		screenX = Gdx.graphics.getWidth()/2;
		screenY = Gdx.graphics.getHeight()/2;

		animFrame -= 1;
		
		if (animFrame < 0) {
			weapons.getActiveWeapon().idle();
			animFrame = weapons.getActiveWeapon().idleSprites.size-1;
		}
		
		setRotation(getAngleToCursor());
		
		if (Gdx.input.isKeyPressed(InputKeys.FORWARD)) {
            setDirection("forward");
        } else if (Gdx.input.isKeyPressed(InputKeys.BACK)) {
            setDirection("back");
        } else if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {
            setDirection("left");
        } else if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
            setDirection("right");
        }

		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			if (weapons.getActiveWeapon() instanceof Firearm) {
				Firearm arm = (Firearm) weapons.getActiveWeapon();
				if (arm.canReload()) arm.reload();
			}
		}
		
		if (Gdx.input.isKeyPressed(InputKeys.FORWARD)
                || Gdx.input.isKeyPressed(InputKeys.BACK)
                || Gdx.input.isKeyPressed(InputKeys.LEFT)
                || Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
			
			if (screenX == gp.getMousePointer().x &&
				screenY == gp.getMousePointer().y) {
				basicMove();
				isMoving = true;
			}
			
			else if (Gdx.input.isKeyPressed(InputKeys.FORWARD)) {

				if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {
					move(gp.getWorldMousePointer().cpy().rotateAroundDeg(position, 45));

				} else if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
					move(gp.getWorldMousePointer().cpy().rotateAroundDeg(position, -45));

				} else {
					move(gp.getWorldMousePointer().cpy());
				}

				isMoving = true;

			} else if (Gdx.input.isKeyPressed(InputKeys.BACK)) {

				if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {
					move(gp.getWorldMousePointer().cpy().rotateAroundDeg(position, 180-45));

				} else if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
					move(gp.getWorldMousePointer().cpy().rotateAroundDeg(position, 180+45));

				} else {
					move(gp.getWorldMousePointer().cpy().rotateAroundDeg(position, 180));
				}

				isMoving = true;

			} else if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {

				move(gp.getWorldMousePointer().cpy().rotateAroundDeg(position, 90));
				isMoving = true;
			} else if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {

				move(gp.getWorldMousePointer().cpy().rotateAroundDeg(position, 270));
				isMoving = true;
			}

			if (stepCounter == 1) {
				footstep1.play();
			} else if (stepCounter == 20) {
				footstep2.play();
			} else if (stepCounter == 40) {
				stepCounter = 0;
			}

		} else {
			isMoving = false;
			velocity = new Vector2();
			acceleration = new Vector2();
			stepCounter = 0;
		}

		velocity.add(acceleration);

		switch(direction) {
			case "forward":
				velocity.limit(speed);
				break;
			default:
				velocity.limit(speed/1.75f);
				break;
		}

		position.add(velocity);
		
		if (Gdx.input.isTouched()) {
			weapons.getActiveWeapon().attack();
		}
		
	}
	
	
	
	public void basicMove() {
		
		
		switch (getDirection()) {
	    	case "forward":
	    		setWorldY(getWorldY() - getStrafeSpeed());
	    		break;
	    	case "back": 
	    		setWorldY(getWorldY() + getStrafeSpeed());
	    		break;
	    	case "left": 
	    		setWorldX(getWorldX() - getStrafeSpeed());
	    		break;
	    	case "right": 
	    		setWorldX(getWorldX() + getStrafeSpeed());
	    		break;
		}
	

		
	}

	public void updateScreenX() {
		screenX = Gdx.graphics.getWidth()/2;
		screenY = Gdx.graphics.getHeight()/2;
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeRenderer shape) {
//		if (!(gp.getScreen().getClass().toString().contains("InGame"))) return;
//
//
//		if (gp.getMousePointer() == null) return;
//
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
//	                RenderingHints.VALUE_RENDER_QUALITY);
//		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
//				RenderingHints.VALUE_STROKE_PURE);
//
//		//DRAW PLAYER
//
		int x = (int) (position.x - gp.getPlayer().getWorldX() + screenX);
		int y = (int) (position.y - gp.getPlayer().getWorldY() + screenY);

		if (weapons.getActiveWeapon().hasRedDotSight()) {
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			shape.begin(ShapeRenderer.ShapeType.Filled);
			drawRedDotSight(shape, gp.getMousePointer().x, gp.getMousePointer().y);
			shape.end();
		}
//
//		g2.setColor(Color.red);
//
////			g2.drawLine(0, gp.getHeight()/2, gp.getWidth(), gp.getHeight()/2);
////		    g2.drawLine(gp.getWidth()/2, 0, gp.getWidth()/2, gp.getHeight());
//
//		AffineTransform tx = new AffineTransform();
//		AffineTransform oldTrans = g2.getTransform();
//
//		tx.setToTranslation(x, y);
		batch.begin();
		Sprite toDraw = weapons.getActiveWeapon().getPlayerSprite(animFrame);
		if (gp.getPlayer().equals(this)) {
			batch.setTransformMatrix(new Matrix4().translate(x, y, 0).rotate(0f, 0f, 1f, (float) (Math.toDegrees(getDrawingAngle()) + 360)));
			toDraw.setPosition((-toDraw.getWidth() / 2) + weapons.getActiveWeapon().getXOffset(), (-toDraw.getHeight() / 2) + weapons.getActiveWeapon().getYOffset());

			toDraw.draw(batch);
			batch.setTransformMatrix(new Matrix4());

		}
		batch.end();

	}

	private double getAngleDistanceModifier(int x, int y) {
		double ac = Math.abs(y - gp.getMousePointer().y);
		double cb = Math.abs(x - gp.getMousePointer().x);

		double distance = Math.hypot(ac, cb);
//		if (distance < 40) return 30;
		return 30/(distance/40);

//		return Math.hypot(ac, cb);
	}

	public void drawRedDotSight(ShapeRenderer shape, double x, double y) {

		shape.setColor(.8f, 0f, 0f, .5f);
		shape.rectLine(new Vector2(screenX, screenY), new Vector2(gp.getMousePointer().x, gp.getMousePointer().y), 2);
		shape.circle(gp.getMousePointer().x, gp.getMousePointer().y, 3, 4);
		shape.setColor((float) (255.0/255.0), (float) (200.0/255.0), (float) (200.0/255.0), 1f);
		shape.circle(gp.getMousePointer().x, gp.getMousePointer().y, 1, 4);

//        g2.setComposite(comp);
//		g2.setStroke(new BasicStroke(2));
//	    g2.drawLine(x, y, (int)gp.getMousePointer().getX(), (int)gp.getMousePointer().getY());
//	    g2.fillOval((int)gp.getMousePointer().getX()-2, (int)gp.getMousePointer().getY()-2, 4, 4);
//	    g2.setComposite(old);
//	    g2.setColor(new Color(255,200,200));
//	    g2.fillOval((int)gp.getMousePointer().getX()-1, (int)gp.getMousePointer().getY()-1, 1, 1);
	}

	public float getAngleToCursor() {
		try {
			double num = screenY - gp.getMousePointer().y;
			double denom = screenX - gp.getMousePointer().x;
			return (float) Math.atan(num/denom);
		} catch (NullPointerException e) {
			return 0;
		}
		
	}

	public float getAngleX() {
		return (float) (screenX - gp.getMousePointer().x);

	}

	public float getAngleY() {
		return (float) (screenY - gp.getMousePointer().y);

	}
	
	public float getDrawingAngle() {
		try {
			float num = (float) (getScreenY() - gp.getMousePointer().y);
			float denom = (float) (getScreenX() - gp.getMousePointer().x);
			if (denom == 0 && num == 0) return (float) (-Math.PI/2);
			float angle = (float) Math.atan(num/denom);
			if ((int)gp.getMousePointer().x <= screenX) {
				   return (float) (angle - Math.toRadians(180));
			   } else {
				   return angle;
			   }
		} catch (NullPointerException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	public WeaponLoadout getWeaponLoadout() { return weapons; }
	public int getScreenX() { return screenX; }
	public int getScreenY() { return screenY; }
	public void setRotation(double i) { rotation = i; }
	public double getRotation() { return rotation; }
	public void setName(String newName) { gamerTag = newName; }
	public String getName() { return gamerTag; }
	
	public double getStrafeSpeed() {
		return getSpeed() *.65;
	}

	@Override
	public boolean hasCollided(double xComp, double yComp) {
		// TODO Auto-generated method stub
		return false;
	}
	public String getUsername() { return name; }

	public double getAdjustedRotation() {
		return getDrawingAngle();
	}

	public void setAnimFrame(int i) {
		animFrame = i;
	}
	public void setWeaponLoadout(WeaponLoadout newLoadout) {
		weapons = newLoadout;
	}

	public void setMoney(int money) { this.money = money; }
	public void setExp(int exp) { this.exp = exp; }
	public int getExp() { return exp; }
	public int getMoney() { return money; }

}
