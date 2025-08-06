package me.jamboxman5.abnpgame.entity.mob;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.LivingEntity;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Settings;

public abstract class Mob extends LivingEntity {
	
	protected String name;
	protected int speed;
	public boolean isMoving;
	protected int scale = 1;
	protected float maxForce;
	
	protected int screenX;
	protected int screenY;

	protected Vector2 velocity;
	protected Vector2 acceleration;

	protected Sound footstep1;
	protected Sound footstep2;
	protected int stepCounter = 0;
	protected long lastStep = System.currentTimeMillis();
	protected float jitter = 0;
	protected float jitterValue = .2f;
	protected Vector2 target;


	public Mob(ABNPGame gamePanel, String type, Vector2 startPos, int health, int maxHealth, int speed) {
		super(gamePanel, health, maxHealth);
		this.name = type;
//		this.worldX = x;
//		this.worldY = y;
		this.speed = speed;

		velocity = new Vector2(0,0);
		acceleration = new Vector2(0,0);
		position = startPos;
		maxForce = .5f;
		collision = new Circle(position, 30);

	}

	@Override
	public void update(float delta) {

		if (jitter < 0) jitter += .1;
		if (jitter > 0) jitter -= .1;

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

	}
	
	public void move(Vector2 target) {
		
		if (gp.getMousePointer() == null) return;

		jitter += jitterValue;
		int stepTime = (int) (2500 * (1.0/velocity.len()));

		if (System.currentTimeMillis() - lastStep > stepTime) {
			stepCounter++;
			lastStep = System.currentTimeMillis();

			if (stepCounter % 2 == 1) {
				footstep1.play(Settings.sfxVolume);
				jitterValue = -jitterValue;
			} else {
				footstep2.play(Settings.sfxVolume);
				stepCounter = 0;
				jitterValue = -jitterValue;
			}

//			else if (stepCounter == stepTime) {
//				footstep2.play();
//			} else if (stepCounter == stepTime*2) {
//				stepCounter = 0;
//			}

		}


		
		if (!hasCollided(target.x, target.y)) {

			seek(target);

		}


	}
	
	public void mfw(Vector2 target) {

		seek(target);



	}

	public void pursue(Mob pursuing) {
		Vector2 target = new Vector2(pursuing.getCollision().x, pursuing.getCollision().y);
		Vector2 prediction = pursuing.velocity.cpy();
		prediction.scl(10);
		target.add(prediction);

		seek(target);

//        if (!pathFind((int) target.x, (int) target.y)) pathFind((int) screen.player.bounds.x, (int) screen.player.bounds.y);
	}

	public void seek(Vector2 target) {
		Vector2 force = target.sub(position);
		force.setLength(speed);
		Vector2 steer = force.sub(velocity);
		steer.limit(maxForce);

		acceleration.add(steer);
		acceleration.limit(speed);

//		System.out.println(acceleration);

//        if (!deadSpace.contains(new Vector2(bounds.x + steer.x, bounds.y))) bounds.x += steer.x;
//        else bounds.x += steer.y;
//        if (!deadSpace.contains(new Vector2(bounds.x, bounds.y + steer.y))) bounds.y += steer.y;
//        else bounds.y += steer.x;
	}

	public void arrive(Vector2 target, float slowingRadius, float stopRadius) {
//		Vector2 force = target.cpy().sub(new Vector2(position.x, position.y));
//
//		int radius = 300;
//		float distance = force.len();
//		if (distance < radius) {
//			float m = map(distance, 0, radius, 0, speed);
//			force.setLength(m);
//		}
//		else {
//			force.setLength(speed);
//		}
//
//		force.sub(velocity);
//		force.limit(maxForce);
//
//
//
//		acceleration.add(force);
//		acceleration.limit(speed);

		Vector2 desiredVelocity = target.cpy().sub(position);
		float distance = desiredVelocity.len() - stopRadius;

		if (distance < slowingRadius)  {
			desiredVelocity.nor().scl(speed).scl(distance/slowingRadius);
		} else {
			desiredVelocity.nor().scl(speed);
		}

		Vector2 steer = desiredVelocity.sub(velocity);
		velocity = velocity.cpy().add(steer).limit(speed);
		velocity.limit(speed/1.5f);
		position.add(velocity);

	}

	public static float map(float val, float oldmax, float max, float newMin, float newMax)
	{
		val = (val - oldmax)/(max - oldmax);
		return newMin + val * (newMax - newMin);
	}

	public void mbk(double xComp, double yComp) {

		if (screenX < gp.getMousePointer().x) {
			setWorldX(getWorldX() - xComp);
			setWorldY(getWorldY() - yComp);
		} else if (screenX > gp.getMousePointer().x) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else if (screenY < gp.getMousePointer().y) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		}
	}
	public void mrt(double xComp, double yComp) {
		if (gp.getMousePointer() == null) return;

		if (screenX < gp.getMousePointer().x) {
			setWorldX(getWorldX() - xComp);
			setWorldY(getWorldY() - yComp);
		} else if (screenX > gp.getMousePointer().x) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else if (screenY < gp.getMousePointer().y) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		}
	}
	public void mlt(double xComp, double yComp) {
		if (gp.getMousePointer() == null) return;

		if (screenX < gp.getMousePointer().x) {
			setWorldX(getWorldX() - xComp);
			setWorldY(getWorldY() - yComp);
		} else if (screenX > gp.getMousePointer().x) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else if (screenY < gp.getMousePointer().y) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		}
	}
	
	public abstract boolean hasCollided(double xComp, double yComp);
	public String getName() { return name; } 

}
