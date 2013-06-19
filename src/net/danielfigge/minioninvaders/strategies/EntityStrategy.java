package net.danielfigge.minioninvaders.strategies;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;

abstract public class EntityStrategy extends Strategy {

	protected Entity entity;
	protected double x, y;
	protected double dx, dy;
	protected long startTime, time;

	public EntityStrategy() {
		x = y = dx = dy = 0;
		startTime = GameState.get().getTime();
		time = 0;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public void sync() throws NullPointerException {
		x = entity.getX();
		y = entity.getY();
		dx = entity.getDx();
		dy = entity.getDy();
		time = GameState.get().getTime() - startTime;
		calculate();
		entity.setDx(dx);
		entity.setDy(dy);
	}

	abstract protected void calculate();
}