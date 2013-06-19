package net.danielfigge.minioninvaders.entities.creatures.enemies;

import java.util.ArrayList;
import java.util.Random;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.EnemyCreatureEntity;
import net.danielfigge.minioninvaders.entities.missiles.lux.LuxAbilityQEntity;
import net.danielfigge.minioninvaders.entities.missiles.lux.LuxAbilityREntity;
import net.danielfigge.minioninvaders.entities.missiles.lux.LuxAbilityWEntity;
import net.danielfigge.minioninvaders.entities.missiles.lux.LuxShotEntity;
import net.danielfigge.minioninvaders.strategies.ai.LuxAi;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class LuxEntity extends EnemyCreatureEntity {
	private long lastMoveSpeedChange = 0;
	private Random random;
	private double passiveMultiplier;
	private LuxAbilityREntity lastR = null;

	public LuxEntity(GameState game, int x, int y) {
		super(game, "creatures/lux", x, y);
		entityIcon = ResourceManager.get().getSprite("icons/lux/creature");
		setHitpoints(Config.get(Config.luxHitpoints));
		setMoveSpeed(Config.get(Config.luxMoveSpeed));
		shotCooldown = Config.get(Config.luxShotCooldown);
		abilityQCooldown = Config.get(Config.luxAbilityQCooldown);
		abilityWCooldown = Config.get(Config.luxAbilityWCooldown);
		abilityRCooldown = Config.get(Config.luxAbilityRCooldown);
		ai = new LuxAi();
		ai.setEntity(this);
	}

	@Override
	protected void set() {
		super.set();
		ai = new LuxAi();
		random = new Random();
		lastR = null;
	}

	public LuxAbilityREntity getLastR() {
		return lastR;
	}

	public void resetLastR() {
		lastR = null;
	}

	public long getLastAbilityQTime() {
		return lastAbilityQTime;
	}

	@Override
	public void reset() {
		super.reset();
		dx = (random.nextBoolean()) ? moveSpeed : -moveSpeed;
	}

	@Override
	public void move(long delta) {
		if (!canMove()) {
			return;
		}
		lastMoveSpeedChange += delta;

		if (lastR != null) {
			dx = dy = 0;
		} else if (lastMoveSpeedChange > 150) {
			lastMoveSpeedChange = 0;
			if (dx > 0) {
				dx = Math.abs(random.nextDouble() * moveSpeed);
			} else if (dx < 0) {
				dx = -Math.abs(random.nextDouble() * moveSpeed);
			} else {
				if (random.nextBoolean()) {
					dx = Math.abs(random.nextDouble() * moveSpeed);
				} else {
					dx = -Math.abs(random.nextDouble() * moveSpeed);
				}
			}
			if (dy > 0) {
				dy = Math.abs(random.nextDouble() * moveSpeed);
			} else if (dy < 0) {
				dy = -Math.abs(random.nextDouble() * moveSpeed);
			} else {
				if (random.nextBoolean()) {
					dy = Math.abs(random.nextDouble() * moveSpeed);
				} else {
					dy = -Math.abs(random.nextDouble() * moveSpeed);
				}
			}
		}

		super.move(delta);
	}

	@Override
	public void notifyDeath() {
		game.removeEntity(lastR);
		ArrayList<Entity> entities = game.getEntities();
		for (Entity entity : entities) {
			if (entity instanceof LuxAbilityWEntity) {
				game.removeEntity(entity);
			}
		}
		ResourceManager.get().stopSound("lux_abilityQ");
		ResourceManager.get().stopSound("lux_abilityQ2");
		ResourceManager.get().stopSound("lux_abilityW");
		ResourceManager.get().stopSound("lux_abilityW2");
		ResourceManager.get().stopSound("lux_abilityR");
		ResourceManager.get().stopSound("lux_abilityR2");
		ResourceManager.get().stopSound("lux_abilityR3");
		ResourceManager.get().playSound("lux_die");
		super.notifyDeath();
	}

	public boolean isUltiInitTimeOver() {
		return ((lastAbilityRTime != 0) && (lastAbilityRTime + Config.get(Config.luxAbilityRInitTime) < game.getTime()));
	}

	public boolean isUltiBlastTimeOver() {
		return ((lastAbilityRTime != 0) && (lastAbilityRTime + Config.get(Config.luxAbilityRInitTime)
				+ Config.get(Config.luxAbilityRBlastTime) < game.getTime()));
	}

	@Override
	protected void shot() {
		LuxShotEntity shot;
		shot = new LuxShotEntity(this, game, (int) x + (getW() / 2) - 24, (int) y + (getH() / 2));
		game.addEntity(shot);
		ResourceManager.get().playSound("lux_shot");
	}

	@Override
	protected void abilityQ() {
		LuxAbilityQEntity shot;
		shot = new LuxAbilityQEntity(this, game, (int) x + (getW() / 2) - 24, (int) y + (getH() / 2));
		game.addEntity(shot);
		ResourceManager.get().playSound((random.nextBoolean()) ? "lux_abilityQ" : "lux_abilityQ2");
	}

	@Override
	protected void abilityW() {
		LuxAbilityWEntity shot;
		shot = new LuxAbilityWEntity(this, game, (int) x + (getW() / 2) - 24, (int) y + (getH() / 2));
		game.addEntity(shot);
		ResourceManager.get().playSound((random.nextBoolean()) ? "lux_abilityW" : "lux_abilityW2");
	}

	@Override
	protected void abilityE() {
	}

	@Override
	protected void abilityR() {
		LuxAbilityREntity shot;
		shot = new LuxAbilityREntity(this, game, (int) x + (getW() / 2) - 222, (int) y + (getH() / 2));
		game.addEntity(shot);
		lastR = shot;
		switch (random.nextInt(3)) {
		case 0:
			ResourceManager.get().playSound("lux_abilityR");
			break;
		case 1:
			ResourceManager.get().playSound("lux_abilityR2");
			break;
		case 2:
			ResourceManager.get().playSound("lux_abilityR3");
			break;
		}
	}

	public void setPassiveMultiplier(double multiplier) {
		passiveMultiplier = multiplier;
	}

	public double getPassiveMultiplier() {
		return passiveMultiplier;
	}

	@Override
	public void draw() {
		super.draw();
		drawHealthbar(0, 0);
	}
}