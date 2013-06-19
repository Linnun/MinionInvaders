package net.danielfigge.minioninvaders.strategies.logic;

import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.util.ResourceManager;

public class LeftToRightLogic extends Logic {

	public void calculate() {
		CreatureEntity entity = (CreatureEntity) this.entity;
		if (entity.isStunned()) {
			return;
		}
		dx = entity.getMoveSpeed();
		dy = 7.5;
		if (x > ResourceManager.get().getDimension().getWidth()) {
			entity.notifyDeath();
		}
		entity.useShot();
	}
}