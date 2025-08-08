package me.jamboxman5.abnpgame.entity.mob.player;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.InputKeys;
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

public class Player extends Survivor {
	
	private final static int defaultSpeed = 8;
	private String gamerTag;
	protected int money;
	protected int stamina = 200;
	protected int exp;

	protected boolean sprinting = false;
	protected int maxStamina = 200;
	protected int staminaRegenMS = 100;
	protected int staminaRegenRate = 1;
	protected long lastStaminaRegen = 0;

	public Player(ABNPGame gamePanel, String name) {
		super(gamePanel, 
			  name, 
			  gamePanel.getMapManager().getActiveMap().getPlayerSpawn(),
			  100, 100,
			  defaultSpeed);

		gamerTag = name;
		
		screenX = Gdx.graphics.getWidth()/2;
		screenY = Gdx.graphics.getHeight()/2;

		footstep1 = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/entity/player/footsteps/Player_Footstep_1.wav"));
		footstep2 = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/entity/player/footsteps/Player_Footstep_2.wav"));
		setDefaults();
	}


	
	@Override
	public void update(float delta) {

		super.update(delta);

		aimTarget = gp.getWorldMousePointer();

		screenX = Gdx.graphics.getWidth()/2;
		screenY = Gdx.graphics.getHeight()/2;

		collision.setPosition(new Vector2(position.x, position.y+10).rotateAroundDeg(position, (float) (Math.toDegrees(getDrawingAngle()) + 360)));

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

		if (Gdx.input.isKeyPressed(InputKeys.SHIFT) && stamina > 0) {
			sprinting = true;
			speed = (int) (defaultSpeed * 1.5);
		} else {
			sprinting = false;
			speed = defaultSpeed;
		}

		if (sprinting && velocity.len() > 0) stamina--;
		else if (!Gdx.input.isKeyPressed(InputKeys.SHIFT) && stamina < maxStamina) {
			if (System.currentTimeMillis() - lastStaminaRegen >= staminaRegenMS) {
				stamina+=staminaRegenRate;
				lastStaminaRegen = System.currentTimeMillis();
			}
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



		} else {
			isMoving = false;
			velocity = new Vector2();
			acceleration = new Vector2();
			stepCounter = 0;
		}



		if (Gdx.input.isTouched()) {
			if (weapons.getActiveWeapon().attack(this, Math.toRadians(jitter))) {

				jitter = (float) (Math.random() * weapons.getActiveWeapon().getRecoil());
				if (Math.random() > .5) jitter = -jitter;

			}
		}

		if (isDead()) {
			gp.gameOver();
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

		int x = (int) (position.x - gp.getPlayer().getWorldX() + screenX);
		int y = (int) (position.y - gp.getPlayer().getWorldY() + screenY);

		if (weapons.getActiveWeapon().hasRedDotSight()) {
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			shape.begin(ShapeRenderer.ShapeType.Filled);
			drawRedDotSight(shape, new Vector2(screenX, screenY), gp.getMousePointer());
			shape.end();
		}

		batch.begin();
		Sprite toDraw = weapons.getActiveWeapon().getPlayerSprite(animFrame);
		if (gp.getPlayer().equals(this)) {
			batch.setTransformMatrix(new Matrix4().translate(x, y, 0).rotate(0f, 0f, 1f, (float) (Math.toDegrees(getDrawingAngle()) + 360) + jitter));
			toDraw.setPosition((-toDraw.getWidth() / 2) + weapons.getActiveWeapon().getXOffset(), (-toDraw.getHeight() / 2) + weapons.getActiveWeapon().getYOffset());

			toDraw.draw(batch);
			batch.setTransformMatrix(new Matrix4());

		}
		batch.end();


	}

	public void drawCollision(ShapeRenderer shape) {
		int x = (int) (collision.x - gp.getPlayer().getWorldX() + screenX);
		int y = (int) (collision.y - gp.getPlayer().getWorldY() + screenY);

		shape.begin(ShapeRenderer.ShapeType.Line);
		shape.setColor(Color.RED);
		shape.circle(x, y, collision.radius);
		shape.end();
	}

	private void setDefaults() {
//		setSpeed(6.5);
		setRotation(0);
		collision = new Circle(position.x, position.y, 35);
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
			float num = (float) (position.y - gp.getWorldMousePointer().y);
			float denom = (float) (position.x - gp.getWorldMousePointer().x);
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
	public int getScreenX() { return Gdx.graphics.getWidth()/2; }
	public int getScreenY() { return Gdx.graphics.getHeight()/2; }
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

	public void setWeaponLoadout(WeaponLoadout newLoadout) {
		weapons = newLoadout;
	}

	public void setMoney(int money) { this.money = money; }
	public void setExp(int exp) { this.exp = exp; }
	public int getExp() { return exp; }
	public int getMoney() { return money; }
	public void giveMoney(int money) { this.money += money; }

	public void takeMoney(int spent) {
		money -= spent;
		if (money < 0) money = 0;
	}

	public void giveExp(int i) { exp += i;
	}

	public int getStamina() { return stamina;
	}

	public Object getMaxStamina() { return maxStamina;
	}

	public float getStaminaRatio() { return ((float)stamina)/((float)maxStamina);
	}
}
