package me.jamboxman5.abnpgame.entity.mob;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.entity.LivingEntity;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Settings;

public abstract class Mob extends LivingEntity {

	protected String name;
	protected float speed;
	public boolean isMoving;
	protected int scale = 1;
	protected float maxForce;

	protected int screenX;
	protected int screenY;

	protected Vector3 velocity;
	protected Vector3 acceleration;

	protected Sound footstep1;
	protected Sound footstep2;
	protected int stepCounter = 0;
	protected long lastStep = System.currentTimeMillis();
	protected float jitter = 0;
	protected float jitterValue = .2f;
	protected Vector3 target;

	public Mob(ABNPGame gamePanel, String type, Vector3 startPos, int health, int maxHealth, float speed) {
		super(gamePanel, health, maxHealth);
		this.name = type;
		this.speed = speed;

		velocity = new Vector3();
		acceleration = new Vector3();
		position = startPos.cpy();
		maxForce = .5f;
		collision = new Circle(position.x, position.z, 20); // XZ plane
	}

	@Override
	public void update(float delta) {
		if (jitter < 0) jitter += .1;
		if (jitter > 0) jitter -= .1;

		// Apply acceleration
		velocity.add(acceleration);
		acceleration.scl(0); // reset acceleration each frame

		// Limit speed based on direction
		switch(direction) {
			case "forward":
				if (velocity.len() > speed) velocity.nor().scl(speed);
				break;
			default:
				if (velocity.len() > speed / 1.75f) velocity.nor().scl(speed / 1.75f);
				break;
		}

		// Move position
		position.add(velocity);

		// Update collision circle in XZ plane
		((Circle) collision).setPosition(position.x, position.z);
	}

	public void move(Vector3 target) {
		if (gp.getMousePointer() == null) return;

		jitter += jitterValue;
		int stepTime = (int) (2500 * (1.0 / velocity.len()));

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
		}

		if (!hasCollided(target.x, target.z)) {
			seek(target);
		}
	}

	public void seek(Vector3 target) {
		Vector3 desired = target.cpy().sub(position);
		desired.y = 0; // stay in XZ plane
		desired.nor().scl(speed);

		Vector3 steer = desired.sub(velocity);
		if (steer.len() > maxForce) steer.nor().scl(maxForce);

		acceleration.add(steer);
		if (acceleration.len() > speed) acceleration.nor().scl(speed);
	}

	public void arrive(Vector3 target, float slowingRadius, float stopRadius) {
		Vector3 desired = target.cpy().sub(position);
		desired.y = 0;
		float distance = desired.len() - stopRadius;

		if (distance < slowingRadius) {
			desired.nor().scl(speed * (distance / slowingRadius));
		} else {
			desired.nor().scl(speed);
		}

		Vector3 steer = desired.sub(velocity);
		if (steer.len() > maxForce) steer.nor().scl(maxForce);

		acceleration.add(steer);
		if (acceleration.len() > speed) acceleration.nor().scl(speed);
	}

	@Override
	public Circle getCollision() {
		return (Circle) collision;
	}

	public void pursue(Mob pursuing) {
		Vector3 target = pursuing.position.cpy();
		Vector3 prediction = pursuing.velocity.cpy().scl(10);
		target.add(prediction);
		seek(target);
	}

	// Utility for mapping values (unchanged)
	public static float map(float val, float oldmax, float max, float newMin, float newMax) {
		val = (val - oldmax) / (max - oldmax);
		return newMin + val * (newMax - newMin);
	}

	public abstract boolean hasCollided(double xComp, double yComp);
	public String getName() { return name; }

	public enum PursuitType {
		ARRIVE, PURSUE, SEEK;
	}
}
