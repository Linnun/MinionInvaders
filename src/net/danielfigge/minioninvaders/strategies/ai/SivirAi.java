package net.danielfigge.minioninvaders.strategies.ai;

import java.util.ArrayList;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.SivirEntity;
import net.danielfigge.minioninvaders.entities.missiles.sona.SonaAbilityREntity;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class SivirAi extends Ai {

	private SonaAbilityREntity sonaR = null;
	private int lastSonaRY = 0;

	public void calculate() {
		SivirEntity entity = (SivirEntity) this.entity;
		if (entity.isStunned()) {
			return;
		}
		if (sonaR == null) {
			ArrayList<Entity> entities = GameState.get().getEntities();
			for (Entity ent : entities) {
				if (ent instanceof SonaAbilityREntity) {
					sonaR = (SonaAbilityREntity) ent;
					break;
				}
			}
		} else {
			if ((sonaR.getY() + sonaR.getH() < y) || (sonaR.getY() == lastSonaRY)) {
				sonaR = null;
			} else {
				lastSonaRY = sonaR.getY();
			}
			entity.useAbilityE();
		}

		if (((dx < 0) && (x <= 0)) || ((dx > 0) && (x >= ResourceManager.get().getDimension().getWidth() - entity.getW()))) {
			dx = -dx;
		}
		if (((dy < 0) && (y <= 10)) || ((dy > 0) && (y >= 300))) {
			dy = -dy;
		}

		if (GameState.get().isAttackDelayOver()) {
			int sivirShotChance;
			switch (GameState.get().getDifficulty()) {
			case HARD:
				sivirShotChance = 114000;
				break;
			case LEGENDARY:
				sivirShotChance = 109000;
				break;
			default:
				sivirShotChance = 105000;
				break;
			}

			if (random.nextInt(sivirShotChance) > 100000) {
				entity.useShot();
			}

			if (random.nextInt(105000) > 100000) {
				entity.useAbilityQ();
			}

			entity.useAbilityR();
		}
	}
}