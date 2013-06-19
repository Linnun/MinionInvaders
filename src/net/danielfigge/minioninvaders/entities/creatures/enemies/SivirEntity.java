package net.danielfigge.minioninvaders.entities.creatures.enemies;

import java.util.ArrayList;
import java.util.Random;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.EnemyCreatureEntity;
import net.danielfigge.minioninvaders.entities.missiles.sivir.SivirAbilityQEntity;
import net.danielfigge.minioninvaders.entities.missiles.sivir.SivirShotEntity;
import net.danielfigge.minioninvaders.strategies.ai.SivirAi;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class SivirEntity extends EnemyCreatureEntity {
	private long lastMoveSpeedChange = 0;
	private Random random;

	public SivirEntity(GameState game, int x, int y) {
		super(game, "creatures/sivir", x, y);
		entityIcon = ResourceManager.get().getSprite("icons/sivir/creature");
		setHitpoints(Config.get(Config.sivirHitpoints));
		setMoveSpeed(Config.get(Config.sivirMoveSpeed));
		shotCooldown = Config.get(Config.sivirShotCooldown);
		abilityQCooldown = Config.get(Config.sivirAbilityQCooldown);
		abilityECooldown = Config.get(Config.sivirAbilityECooldown);
		abilityRCooldown = Config.get(Config.sivirAbilityRCooldown);
		ai = new SivirAi();
		ai.setEntity(this);
	}

	@Override
	protected void set() {
		super.set();
		random = new Random();
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

		if (lastMoveSpeedChange > 150) {
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
		ArrayList<Entity> entities = game.getEntities();
		for (Entity entity : entities) {
			if (entity instanceof SivirAbilityQEntity) {
				game.removeEntity(entity);
			}
		}
		ResourceManager.get().stopSound("sivir_abilityR");
		ResourceManager.get().playSound("sivir_die");
		super.notifyDeath();
	}

	@Override
	protected void shot() {
		SivirShotEntity shot;
		shot = new SivirShotEntity(this, game, (int) x + (getW() / 2) - 24, (int) y + (getH() / 2));
		game.addEntity(shot);
		ResourceManager.get().playSound("sivir_shot");
	}

	@Override
	protected void abilityQ() {
		SivirAbilityQEntity shot;
		shot = new SivirAbilityQEntity(this, game, (int) x + (getW() / 2) - 24, (int) y + (getH() / 2));
		game.addEntity(shot);
		ResourceManager.get().playSound("sivir_abilityQ");
	}

	@Override
	protected void abilityW() {
	}

	@Override
	protected void abilityE() {
		addEffect("spellshieldSivir", game.getTime() + Config.get(Config.sivirAbilityEDuration));
		ResourceManager.get().playSound("sivir_abilityE");
	}

	@Override
	protected void abilityR() {
		addMoveSpeedMultiplier(Config.get(Config.sivirAbilityRSelfMoveSpeedMultiplier),
				game.getTime() + Config.get(Config.sivirAbilityRDuration));
		ArrayList<Entity> entities = game.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = (Entity) entities.get(i);
			if ((entity instanceof EnemyCreatureEntity) && (entity != this)) {
				((CreatureEntity) entity).addMoveSpeedMultiplier(Config.get(Config.sivirAbilityRAllyMoveSpeedMultiplier), game.getTime()
						+ Config.get(Config.sivirAbilityRDuration));
			}
		}
		ResourceManager.get().playSound("sivir_abilityR");
	}

	@Override
	public void draw() {
		super.draw();
		drawHealthbar(0, 50);
	}
}