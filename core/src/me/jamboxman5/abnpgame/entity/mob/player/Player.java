package me.jamboxman5.abnpgame.entity.mob.player;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.mob.npc.Ally;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.net.packets.PacketMove;
import me.jamboxman5.abnpgame.net.packets.PacketShoot;
import me.jamboxman5.abnpgame.util.CameraUtil;
import me.jamboxman5.abnpgame.util.InputKeys;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;

public class Player extends Survivor {
	
	private final static int defaultSpeed = 5;
	private String gamerTag;
	protected int money;
	protected int stamina = 200;

	protected boolean sprinting = false;
	protected int maxStamina = 200;
	protected int staminaRegenMS = 100;
	protected int staminaRegenRate = 1;
	protected long lastStaminaRegen = 0;

	protected float stateTime = 0;
	protected float legStateTime = 0;

	protected Array<Ally> companions;

	public Player(ABNPGame gamePanel, String name, String uuid) {
		super(gamePanel, 
			  name, 
			  gamePanel.getMapManager().getActiveMap().getPlayerSpawn(),
			  100, 100,
			  defaultSpeed);

		legsDecal = Decal.newDecal(currentAnimation.getKeyFrame(0), true);
		bodyDecal = Decal.newDecal(new TextureRegion(weapons.getActiveWeapon().getPlayerSprite(0).getTexture()), true);
		legsDecal.setRotationX(90);
		bodyDecal.setRotationX(90);


		gamerTag = name;
		this.uuid = uuid;
		System.out.println("Generated player: " + uuid);

		screenX = Gdx.graphics.getWidth()/2;
		screenY = Gdx.graphics.getHeight()/2;
		companions = new Array<>();

		footstep1 = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/entity/player/footsteps/Player_Footstep_1.wav"));
		footstep2 = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/entity/player/footsteps/Player_Footstep_2.wav"));
		collision = new Circle(position.x, position.y, 25);
	}


	
	@Override
	public void update(float delta) {

		super.update(delta);

		if (!gp.getPlayer().getID().equals(uuid)) return;

		stateTime += delta;
		legStateTime += delta;

		Vector2 center = new Vector2(Settings.screenWidth/2f, Settings.screenHeight/2f);
		Vector2 mouse = gp.getMousePointer();

		Vector2 displacement = mouse.cpy().sub(center);

		Vector3 displacement3D = position.cpy().add(displacement.x, 0, displacement.y);

		aimTarget = displacement3D;

		((Circle)collision).setPosition(new Vector2(position.x, position.z + 10).rotateAroundDeg(new Vector2(position.x, position.z), (float) (Math.toDegrees(getDrawingAngle()) + 360)));
		boolean rotating = false;
		float oldRotation = getRotation();
		setRotation(getAngleToCursor());
		if (getRotation() != oldRotation) rotating = true;
		
		if (Gdx.input.isKeyPressed(InputKeys.FORWARD)) {
            setDirection("forward");
        } else if (Gdx.input.isKeyPressed(InputKeys.BACK)) {
            setDirection("back");
        } else if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {
            setDirection("left");
        } else if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
            setDirection("right");
        } else {
			setDirection("still");
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
				stateTime = 0;
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.F) && !weapons.getActiveWeapon().isMeleeAnimation()) {
			if (weapons.getActiveWeapon() instanceof Firearm) {
				Firearm arm = (Firearm) weapons.getActiveWeapon();
				arm.melee();
				stateTime = 0;
			} else {
				weapons.getActiveWeapon().attack(this, Math.toRadians(jitter));
				stateTime = 0;
			}
		}

		if (Gdx.input.isKeyPressed(InputKeys.FORWARD)
				|| Gdx.input.isKeyPressed(InputKeys.BACK)
				|| Gdx.input.isKeyPressed(InputKeys.LEFT)
				|| Gdx.input.isKeyPressed(InputKeys.RIGHT)) {


			if (Gdx.input.isKeyPressed(InputKeys.FORWARD)) {
				if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {
					move(rotateAroundY(aimTarget, position, 45f));
				} else if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
					move(rotateAroundY(aimTarget, position, -45f));
				} else {
					move(aimTarget.cpy());
				}
				isMoving = true;

			} else if (Gdx.input.isKeyPressed(InputKeys.BACK)) {
				if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {
					move(rotateAroundY(aimTarget, position, 135f)); // 180-45
				} else if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
					move(rotateAroundY(aimTarget, position, 225f)); // 180+45
				} else {
					move(rotateAroundY(aimTarget, position, 180f));
				}
				isMoving = true;

			} else if (Gdx.input.isKeyPressed(InputKeys.LEFT)) {
				move(rotateAroundY(aimTarget, position, 90f));
				isMoving = true;
			} else if (Gdx.input.isKeyPressed(InputKeys.RIGHT)) {
				move(rotateAroundY(aimTarget, position, 270f));
				isMoving = true;
			}
		} else {
			isMoving = false;
			velocity = new Vector3();
			acceleration = new Vector3();
			stepCounter = 0;
		}

		if (weapons.getActiveWeapon().getCurrentAnimation().isAnimationFinished(stateTime)) {
			stateTime = 0;
			if (isMoving) weapons.getActiveWeapon().move();
			else weapons.getActiveWeapon().idle();
		}

		if (Gdx.input.isTouched()) {
			if (gp.isMultiplayer()) {
				PacketShoot shoot = new PacketShoot();
				shoot.uuid = uuid;
				gp.getClientManager().sendPacketTCP(shoot);
				weapons.getActiveWeapon().fakeAttack(this);
			} else if (weapons.getActiveWeapon().attack(this, Math.toRadians(jitter))) {
				stateTime = 0;
				if (weapons.getActiveWeapon() instanceof Firearm) jitter = (float) (Math.random() * weapons.getActiveFirearm().getRecoil());
				if (Math.random() > .5) jitter = -jitter;

			}
		}

		if (isDead()) {
			gp.gameOver();
		}

		if ((isMoving || rotating) && gp.isMultiplayer()) {
			PacketMove move = new PacketMove();
			move.uuid = uuid;
			move.x = getWorldX();
			move.y = getWorldZ();
			move.rotation = getDrawingAngle();
			move.jitter = jitter;
			gp.getClientManager().sendPacketUDP(move);
		}

		if (isMoving) {
			switch (getDirection()) {
				case "forward": {
					if (sprinting) {
						if (changedAnimation(runAnimation)) legStateTime = 0;
						currentAnimation = runAnimation;
					}
					else {
						if (changedAnimation(walkAnimation)) legStateTime = 0;
						currentAnimation = walkAnimation;
					}
					break;
				}
				case "left": {
					if (changedAnimation(strafeLeftAnimation)) legStateTime = 0;
					currentAnimation = strafeLeftAnimation;
					break;
				}
				case "right": {
					if (changedAnimation(strafeRightAnimation)) legStateTime = 0;
					currentAnimation = strafeRightAnimation;
					break;
				}
				case "back": {
					if (changedAnimation(strafeBackAnimation)) legStateTime = 0;
					currentAnimation = strafeBackAnimation;
					break;
				}
				default: {
					if (changedAnimation(idleAnimation)) legStateTime = 0;
					currentAnimation = idleAnimation;
					break;
				}
			}
		} else {
			if (changedAnimation(idleAnimation)) legStateTime = 0;
			currentAnimation = idleAnimation;
		}
		
	}

	Vector3 rotateAroundY(Vector3 point, Vector3 pivot, float degrees) {
		float rad = (float) Math.toRadians(degrees);
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		float x = point.x - pivot.x;
		float z = point.z - pivot.z;

		float newX = x * cos - z * sin + pivot.x;
		float newZ = x * sin + z * cos + pivot.z;

		return new Vector3(newX, point.y, newZ); // keep Y unchanged
	}
	
	protected boolean changedAnimation(Animation<TextureRegion> newAnimation) {
		return currentAnimation != newAnimation;
	}

	public void basicMove() {
		
		
		switch (getDirection()) {
	    	case "forward":
	    		setWorldZ(getWorldZ() - getStrafeSpeed());
	    		break;
	    	case "back": 
	    		setWorldZ(getWorldZ() + getStrafeSpeed());
	    		break;
	    	case "left": 
	    		setWorldX(getWorldX() - getStrafeSpeed());
	    		break;
	    	case "right": 
	    		setWorldX(getWorldX() + getStrafeSpeed());
	    		break;
		}
	

		
	}


	@Override
	public void draw(DecalBatch batch, ShapeRenderer shape, PerspectiveCamera camera) {

		if (weapons.getActiveWeapon().hasMod(WeaponMod.ModType.RedDotSight)) {
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			shape.begin(ShapeRenderer.ShapeType.Filled);

			// Convert start/end to world coords if needed
			Vector2 start = new Vector2(position.x, position.z);

			Vector2 mouse = ABNPGame.getInstance().getMousePointer();
			Vector2 pos2D = new Vector2(position.x, position.z);
			Vector2 mouseWorld = pos2D.cpy().add(CameraUtil.getWorldPosScreenCenterDisplacement(camera, shape.getTransformMatrix().getTranslation(new Vector3()).y, mouse.x, mouse.y));


			drawRedDotSight(shape, pos2D, mouseWorld);
			shape.end();
		}

		float angleDeg = (float) Math.toDegrees(getDrawingAngle()) + jitter;

		TextureRegion weaponFrame = weapons.getActiveWeapon().getPlayerSprite(stateTime);
		bodyDecal.setTextureRegion(weaponFrame);
		bodyDecal.setWidth(weaponFrame.getRegionWidth());
		bodyDecal.setHeight(weaponFrame.getRegionHeight());

		TextureRegion legFrame = currentAnimation.getKeyFrame(legStateTime);
		legsDecal.setTextureRegion(legFrame);
		legsDecal.setWidth(legFrame.getRegionWidth());
		legsDecal.setHeight(legFrame.getRegionHeight());

		float scale = 0.25f;

		{

			//BODY TRANSFORMATION

			bodyDecal.setScale(scale);

			float w = bodyDecal.getWidth() * scale;
			float h = bodyDecal.getHeight() * scale;

			Vector3 drawPos = new Vector3(position.x, position.y, -position.z);
			drawPos.y += 80f;

			Vector3 up = new Vector3(camera.up).nor();
			Vector3 right = new Vector3(camera.direction).crs(up).nor();

			Vector2 offset = weapons.getActiveWeapon().getOffset();

			float cos = MathUtils.cosDeg(angleDeg);
			float sin = MathUtils.sinDeg(angleDeg);

			Vector3 rotatedOffset = new Vector3()
					.mulAdd(right, (offset.x * cos - offset.y * sin))
					.mulAdd(up, (offset.x * sin + offset.y * cos));

			Vector3 pivot = new Vector3(drawPos.add(rotatedOffset));

			bodyDecal.setPosition(pivot);
			bodyDecal.lookAt(camera.position, camera.up);
			bodyDecal.rotateZ(angleDeg);

		}

		{

			//LEG TRANSFORMATION

			legsDecal.setScale(scale);

			float w = legsDecal.getWidth() * scale;
			float h = legsDecal.getHeight() * scale;

			Vector3 drawPos = new Vector3(position.x, position.y, -position.z);
			drawPos.y += 40f;

			Vector3 up = new Vector3(camera.up).nor();
			Vector3 right = new Vector3(camera.direction).crs(up).nor();

			float offRight = 0f;
			float offUp = 6f;

			float cos = MathUtils.cosDeg(angleDeg);
			float sin = MathUtils.sinDeg(angleDeg);

			Vector3 rotatedOffset = new Vector3()
					.mulAdd(right, (offRight * cos - offUp * sin))
					.mulAdd(up, (offRight * sin + offUp * cos));

			Vector3 pivot = new Vector3(drawPos.add(rotatedOffset));

			legsDecal.setPosition(pivot);
			legsDecal.lookAt(camera.position, camera.up);
			legsDecal.rotateZ(angleDeg);

		}



		batch.add(legsDecal);
		batch.add(bodyDecal);


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
	
	public float getDrawingAngle() {
//		try {
			float num = (position.y - gp.getWorldMousePointer().y);
			float denom = (position.x - gp.getWorldMousePointer().x);
			if (denom == 0 && num == 0) return (float) (-Math.PI/2);
			float angle = (float) Math.atan(num/denom);
			if ((int)gp.getMousePointer().x <= screenX) {
				   return (float) (angle - Math.toRadians(180));
			   } else {
				   return angle;
			   }
//		} catch (NullPointerException e) {
//			e.printStackTrace();
//			return 0;
//		}
		
	}

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


	public void setMoney(int money) { this.money = money; }

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

	public void setUsername(String name) {
		this.name = name;
	}

	public void addCompanion(Ally companion) {
		companions.add(companion);
	}

	public Array<Ally> getCompanions() { return companions; }
}
