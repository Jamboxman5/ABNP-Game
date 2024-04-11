package me.jamboxman5.abnpgame.entity.player;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.jamboxman5.abnpgame.entity.Mob;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.ScreenInfo;
import me.jamboxman5.abnpgame.util.InputKeys;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;
import me.jamboxman5.abnpgame.weapon.mods.RedDotSight;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import java.awt.*;

public class Player extends Mob {
	
	private final static int defaultSpeed = 5;
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
		
		setDefaults();
	}

	private void setDefaults() {		
		setSpeed(6.5);
		setRotation(0);
		collisionWidth = 50;
		collision = new Rectangle((int)(getAdjustedWorldX())-(collisionWidth/4), 
				  (int)(getAdjustedWorldY())-(collisionWidth/2), 
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
		
		if (Gdx.input.isKeyPressed(InputKeys.FORWARD)
                || Gdx.input.isKeyPressed(InputKeys.BACK)
                || Gdx.input.isKeyPressed(InputKeys.LEFT)
                || Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
			
			if (screenX == gp.getMousePointer().getX() &&
				screenY == gp.getMousePointer().getY()) {
				basicMove();
				isMoving = true;
				return;
			}
            
			double xComp = 0;
			double yComp = 0;
			
			if (Gdx.input.isKeyPressed(InputKeys.FORWARD)) {
				if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
	            	xComp = getStrafeSpeed() * Math.cos(rotation);
					yComp = getStrafeSpeed() * Math.sin(rotation);
					xComp += (getStrafeSpeed() * Math.cos(rotation - Math.toRadians(90)));
					yComp += (getStrafeSpeed() * Math.sin(rotation - Math.toRadians(90)));
					move(xComp, yComp);
		            isMoving = true;
				} else if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {
					xComp = getStrafeSpeed() * Math.cos(rotation);
					yComp = getStrafeSpeed() * Math.sin(rotation);
					xComp += (getStrafeSpeed() * Math.cos(rotation + Math.toRadians(90)));
					yComp += (getStrafeSpeed() * Math.sin(rotation + Math.toRadians(90)));
					move(xComp, yComp);
		            isMoving = true;
				} else {
					xComp = getSpeed() * Math.cos(rotation);
					yComp = getSpeed() * Math.sin(rotation);
					move(xComp, yComp);
		            isMoving = true;
				}
			} else if (Gdx.input.isKeyPressed(InputKeys.BACK)) {
				if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {
					xComp = (getStrafeSpeed() * Math.cos(rotation));
					yComp = (getStrafeSpeed() * Math.sin(rotation));
					xComp += (getStrafeSpeed() * Math.cos(rotation - Math.toRadians(90)))/2;
					yComp += (getStrafeSpeed() * Math.sin(rotation - Math.toRadians(90)))/2;
					move(xComp, yComp);
		            isMoving = true;
				} else if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
					xComp = (getStrafeSpeed() * Math.cos(rotation));
					yComp = (getStrafeSpeed() * Math.sin(rotation));
					xComp += (getStrafeSpeed() * Math.cos(rotation + Math.toRadians(90)))/2;
					yComp += (getStrafeSpeed() * Math.sin(rotation + Math.toRadians(90)))/2;
					move(xComp, yComp);
		            isMoving = true;
				} else {
					xComp = getStrafeSpeed() * Math.cos(rotation);
					yComp = getStrafeSpeed() * Math.sin(rotation);
					move(xComp, yComp);
		            isMoving = true;
				}
			} else if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {
				xComp = getStrafeSpeed() * Math.cos(rotation - Math.toRadians(90));
				yComp = getStrafeSpeed() * Math.sin(rotation - Math.toRadians(90));
				move(xComp, yComp);
	            isMoving = true;
			} else if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
				xComp = getStrafeSpeed() * Math.cos(rotation + Math.toRadians(90));
				yComp = getStrafeSpeed() * Math.sin(rotation + Math.toRadians(90));
				move(xComp, yComp);
	            isMoving = true;
			} 
			
			if (xComp != 0.0 || yComp != 0.0) {
				
			} else {
				isMoving = false;
			}

		} else {
			isMoving = false;
		}
		
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
	public void draw(SpriteBatch batch) {
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
		int x = (int) (worldX - gp.getPlayer().getWorldX() + screenX);
		int y = (int) (worldY - gp.getPlayer().getWorldY() + screenY);
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
		Sprite toDraw = weapons.getActiveWeapon().getPlayerSprite(animFrame);
		if (gp.getPlayer().equals(this)) {
			toDraw.setCenter(x,y);
			toDraw.setRotation((float) ((float) Math.toDegrees(getDrawingAngle()) + 360 + getAngleDistanceModifier(x, y)));
			toDraw.draw(batch);
//
//			if (weapons.getActiveWeapon().hasRedDotSight()) {
//				drawRedDotSight(g2, x, y);
//			}
//
//			if (gp.getMousePointer().getX() == screenX &&
//				   	gp.getMousePointer().getY() == screenY) {
//				tx.rotate(0);
//			} else {
//				tx.rotate(0);
//				tx.rotate(getDrawingAngle());
//			}
		}
//
//
//		g2.transform(tx);
//
//		BufferedImage sprite = weapons.getActiveWeapon().getPlayerSprite(animFrame);
//		g2.drawImage(sprite, (int)(-sprite.getWidth()+(85*gp.getZoom())), (int)(-sprite.getHeight()+(weapons.getActiveWeapon().getYOffset()*gp.getZoom())), null);
//		g2.setTransform(new AffineTransform());
//		g2.setTransform(oldTrans);

//		batch.draw(toDraw, x, y);

//
//		if (gp.isDebugMode()) {
//			x = (int) (collision.x - gp.getPlayer().getWorldX() + gp.getPlayer().screenX);
//			y = (int) (collision.y - gp.getPlayer().getWorldY() + gp.getPlayer().screenY);
//
//			g2.setColor(Color.red);
//			g2.setStroke(new BasicStroke(3));
//			g2.drawRect(x, y, collision.width, collision.height);
//
////		   	g2.drawRect((int)(x), (int)(y), (int)(collision.width*gp.getZoom()),(int) (collision.height*gp.getZoom()));
//		}

	}

	private double getAngleDistanceModifier(int x, int y) {
		double ac = Math.abs(y - gp.getMousePointer().getY());
		double cb = Math.abs(x - gp.getMousePointer().getX());

		double distance = Math.hypot(ac, cb);
//		if (distance < 40) return 30;
		return 30/(distance/40);

//		return Math.hypot(ac, cb);
	}

	public void drawRedDotSight(Graphics2D g2, int x, int y) {
		Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f);
        Composite old = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g2.setComposite(comp);
		g2.setStroke(new BasicStroke(2));
	    g2.drawLine(x, y, (int)gp.getMousePointer().getX(), (int)gp.getMousePointer().getY());
	    g2.fillOval((int)gp.getMousePointer().getX()-2, (int)gp.getMousePointer().getY()-2, 4, 4);
	    g2.setComposite(old);
	    g2.setColor(new Color(255,200,200));
	    g2.fillOval((int)gp.getMousePointer().getX()-1, (int)gp.getMousePointer().getY()-1, 1, 1);
	}

	public float getAngleToCursor() {
		try {
			double num = screenY - gp.getMousePointer().getY();
			double denom = screenX - gp.getMousePointer().getX();
			return (float) Math.atan(num/denom);
		} catch (NullPointerException e) {
			return 0;
		}
		
	}
	
	public float getDrawingAngle() {
		try {
			float num = (float) (getScreenY() - gp.getMousePointer().getY());
			float denom = (float) (getScreenX() - gp.getMousePointer().getX());
			if (denom == 0 && num == 0) return (float) (-Math.PI/2);
			float angle = (float) Math.atan(num/denom);
			if ((int)gp.getMousePointer().getX() <= screenX) {
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
