package net.danielfigge.minioninvaders.strategies.logic;

import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;

public class RightToLeftLogic extends Logic {

	public void calculate() {
		CreatureEntity entity = (CreatureEntity) this.entity;
		if (entity.isStunned()) {
			return;
		}
		dx = -entity.getMoveSpeed();
		dy = 0;
		if (x < -entity.getW()) {
			entity.notifyDeath();
		}
		entity.useShot();
	}
}