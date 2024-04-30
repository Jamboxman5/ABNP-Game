package me.jamboxman5.abnpgame.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.ScreenInfo;

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

	public Mob(ABNPGame gamePanel, String type, int x, int y, int health, int maxHealth, int speed) {
		super(gamePanel, health, maxHealth);
		this.name = type;
//		this.worldX = x;
//		this.worldY = y;
		this.speed = speed;

		velocity = new Vector2(0,0);
		acceleration = new Vector2(0,0);
		position = new Vector2(x, y);
		maxForce = .5f;

	}
	
	public void move(Vector2 target) {
		
		if (gp.getMousePointer() == null) return;
		
		stepCounter++;
		
		if (!hasCollided(target.x, target.y)) {
			
//			switch(direction) {
//			case "forward":
//				mfw(target);
//				break;
////			case "back":
////				mbk(xComp,yComp);
////				break;
////			case "left":
////				mlt(xComp,yComp);
////				break;
////			case "right":
////				mrt(xComp,yComp);
////				break;
//			}

			seek(target);

		}

//		System.out.println(position);

	}
	
	public void mfw(Vector2 target) {

//		if (gp.getMousePointer() == null) return;
//
//		if (screenX < gp.getMousePointer().getX()) {
//			setWorldX(getWorldX() + xComp);
//			setWorldY(getWorldY() + yComp);
//		} else if (screenX > gp.getMousePointer().getX()) {
//			setWorldX(getWorldX() - xComp);
//			setWorldY(getWorldY() - yComp);
//		} else if (screenY < gp.getMousePointer().getY()) {
//			setWorldX(getWorldX() - xComp);
//			setWorldY(getWorldY() - yComp);
//		} else {
//			setWorldX(getWorldX() - xComp);
//			setWorldY(getWorldY() - yComp);
//		}

//		System.out.println(target);
		seek(target);
//		System.out.println(target);


//		acceleration.add(target.x - position.x, target.y - position.y);
//		acceleration.limit(speed);


	}

	public void pursue(Mob pursuing) {
		Vector2 target = pursuing.position.cpy();
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
