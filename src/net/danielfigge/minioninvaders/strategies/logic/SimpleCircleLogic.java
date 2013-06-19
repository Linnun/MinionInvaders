package net.danielfigge.minioninvaders.strategies.logic;

import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;

public class SimpleCircleLogic extends Logic {

	@Override
	protected void calculate() {
		CreatureEntity entity = (CreatureEntity) this.entity;
		if (entity.isStunned()) {
			return;
		}
		dx = Math.sin(time / 500.0) * entity.getMoveSpeed();
		dy = Math.cos(time / 500.0) * entity.getMoveSpeed();
		entity.useShot();
	}
}