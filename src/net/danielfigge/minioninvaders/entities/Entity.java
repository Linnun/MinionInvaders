package net.danielfigge.minioninvaders.entities;

import java.awt.Rectangle;

import net.danielfigge.minioninvaders.core.Sprite;
import net.danielfigge.minioninvaders.util.ResourceManager;


public abstract class Entity {
	protected Sprite sprite;
	protected double x, y;
	protected double dx, dy;
	protected Rectangle hitbox;

	public Entity(String ref, int x, int y) {
		if (ref != null) {
			this.sprite = ResourceManager.get().getSprite(ref);
		}
		this.x = x;
		this.y = y;
		dx = dy = 0;
		hitbox = new Rectangle();
	}

	public void setSprite(String ref) {
		this.sprite = ResourceManager.get().getSprite(ref);
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void move(long delta) {
		x += (delta * dx) / 1000.0;
		y += (delta * dy) / 1000.0;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
	}

	public void draw() {
		sprite.draw((int) x, (int) y);
	}

	public void doLogic() {
		calculateHitbox();
	}

	protected void calculateHitbox() {
		hitbox.setBounds((int) x, (int) y, getW(), getH());
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public int getW() {
		return (int) getSprite().getWidth();
	}

	public int getH() {
		return (int) getSprite().getHeight();
	}

	public boolean collidesWith(Entity other) {
		return hitbox.intersects(other.getHitbox());
	}

	public abstract void collidedWith(Entity other);
}