package net.danielfigge.minioninvaders.strategies.ai;

import java.util.ArrayList;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.LuxEntity;
import net.danielfigge.minioninvaders.entities.missiles.sona.SonaAbilityREntity;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class LuxAi extends Ai {

	private SonaAbilityREntity sonaR = null;
	private int lastSonaRY = 0;

	private void doUltiLogic() {
		LuxEntity entity = (LuxEntity) this.entity;
		if (entity.getLastR() != null) {
			if (entity.isUltiInitTimeOver() && (!entity.getLastR().isInitOver())) {
				entity.getLastR().initIsOver();
			}
			if (entity.isUltiBlastTimeOver()) {
				entity.getLastR().BlastIsOver();
				entity.resetLastR();
			}
		}
	}

	public void calculate() {
		LuxEntity entity = (LuxEntity) this.entity;
		if (entity.isStunned()) {
			doUltiLogic();
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
			if ((x > 55) && (x + entity.getW() < ResourceManager.get().getDimension().getWidth() - 55)) {
				if (sonaR.getX() + (sonaR.getW() / 2) <= x + (entity.getW() / 2)) {
					dx = entity.getMoveSpeed() * 0.5;
				} else {
					dx = -entity.getMoveSpeed() * 0.5;
				}
				dy = -entity.getMoveSpeed() * 0.25;
			}

			if ((sonaR.getY() + sonaR.getH() < y) || (sonaR.getY() == lastSonaRY)) {
				sonaR = null;
			} else {
				lastSonaRY = sonaR.getY();
			}
		}

		if (entity.getLastR() == null) {
			if (((dx < 0) && (x <= 0)) || ((dx > 0) && (x >= ResourceManager.get().getDimension().getWidth() - entity.getW()))) {
				dx = -dx;
			}
			if (((dy < 0) && (y <= 10)) || ((dy > 0) && (y >= 300))) {
				dy = -dy;
			}

			if (GameState.get().isAttackDelayOver()) {
				int luxShotChance;
				switch (GameState.get().getDifficulty()) {
				case HARD:
					luxShotChance = 113000;
					break;
				case LEGENDARY:
					luxShotChance = 108000;
					break;
				default:
					luxShotChance = 104000;
					break;
				}

				if (random.nextInt(luxShotChance) > 100000) {
					entity.useShot();
				}

				if (((int) x > GameState.get().getPlayer().getX() - 22) && ((int) x < GameState.get().getPlayer().getX() + 22)) {
					entity.useAbilityQ();
				}

				entity.useAbilityW();

				if ((entity.getLastAbilityQTime() == 0) || (entity.getLastAbilityQTime() + 400 < GameState.get().getTime())) {
					if (entity.isAbilityQOnCooldown()) {
						if (y >= ResourceManager.get().getDimension().height - (entity.getH() / 2) - 530) {
							if (((int) x > GameState.get().getPlayer().getX() - 50) && ((int) x < GameState.get().getPlayer().getX() + 50)) {
								entity.useAbilityR();
							}
						}
					}
				}
			}
		} else {
			doUltiLogic();
		}
	}
}