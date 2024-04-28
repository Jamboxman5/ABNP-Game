package me.jamboxman5.abnpgame.entity;

import com.badlogic.gdx.Gdx;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.ScreenInfo;

public abstract class Mob extends Entity {
	
	protected String name;
	protected int speed;
	protected int numSteps = 0;
	public boolean isMoving;
	protected int scale = 1;
	
	protected int screenX;
	protected int screenY;
	
	public Mob(ABNPGame gamePanel, String type, int x, int y, int speed) {
		super(gamePanel);
		this.name = type;
		this.worldX = x;
		this.worldY = y;
		this.speed = speed;
	}
	
	public void move(double xComp, double yComp) {
		
		if (gp.getMousePointer() == null) return;

		
		
		numSteps++;
		
		if (!hasCollided(xComp, yComp)) {
			
			switch(direction) {
			case "forward":
				mfw(xComp,yComp);
				break;
			case "back":
				mbk(xComp,yComp);
				break;
			case "left":
				mlt(xComp,yComp);
				break;
			case "right":
				mrt(xComp,yComp);
				break;
			}
		}
	}
	
	public void mfw(double xComp, double yComp) {

		if (gp.getMousePointer() == null) return;

		if (screenX < gp.getMousePointer().getX()) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else if (screenX > gp.getMousePointer().getX()) {
			setWorldX(getWorldX() - xComp);
			setWorldY(getWorldY() - yComp);
		} else if (screenY < gp.getMousePointer().getY()) {
			setWorldX(getWorldX() - xComp);
			setWorldY(getWorldY() - yComp);
		} else {
			setWorldX(getWorldX() - xComp);
			setWorldY(getWorldY() - yComp);
		}
	}
	public void mbk(double xComp, double yComp) {

		if (screenX < gp.getMousePointer().getX()) {
			setWorldX(getWorldX() - xComp);
			setWorldY(getWorldY() - yComp);
		} else if (screenX > gp.getMousePointer().getX()) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else if (screenY < gp.getMousePointer().getY()) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		}
	}
	public void mrt(double xComp, double yComp) {
		if (gp.getMousePointer() == null) return;

		if (screenX < gp.getMousePointer().getX()) {
			setWorldX(getWorldX() - xComp);
			setWorldY(getWorldY() - yComp);
		} else if (screenX > gp.getMousePointer().getX()) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else if (screenY < gp.getMousePointer().getY()) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		}
	}
	public void mlt(double xComp, double yComp) {
		if (gp.getMousePointer() == null) return;

		if (screenX < gp.getMousePointer().getX()) {
			setWorldX(getWorldX() - xComp);
			setWorldY(getWorldY() - yComp);
		} else if (screenX > gp.getMousePointer().getX()) {
			setWorldX(getWorldX() + xComp);
			setWorldY(getWorldY() + yComp);
		} else if (screenY < gp.getMousePointer().getY()) {
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
