package net.danielfigge.minioninvaders.entities.missiles.lux;

import java.util.Random;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.PlayerCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.LuxEntity;
import net.danielfigge.minioninvaders.entities.missiles.MissileEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class LuxShotEntity extends MissileEntity {
	private long lastFrameChange;
	private Random random;
	private boolean used = false;

	public LuxShotEntity(LuxEntity parent, GameState game, int x, int y) {
		super(parent, game, "missiles/lux/shot", x, y);
		random = new Random();
		moveSpeed = Config.get(Config.luxShotProjectileSpeed);
		switch (game.getDifficulty()) {
		case HARD:
		case LEGENDARY:
			dy = Math.max(random.nextDouble() * moveSpeed, moveSpeed);
			dx = random.nextDouble() * dy;
			if (random.nextBoolean()) {
				dx = -dx;
			}
			if (random.nextBoolean()) {
				dy = -dy;
			}
			break;
		default:
			dy = Math.max(random.nextDouble() * moveSpeed, moveSpeed);
			dx = random.nextDouble() * (dy / 2);
			break;
		}
	}

	@Override
	public void move(long delta) {
		super.move(delta);
		lastFrameChange += delta;
		if (lastFrameChange > 10) {
			lastFrameChange = 0;
			sprite.rotate(45);
		}
	}

	@Override
	public void collidedWith(Entity other) {
		if (used) {
			return;
		}
		if (other instanceof PlayerCreatureEntity) {
			game.removeEntity(this);

			int damage = Config.get(Config.luxShotDamage);
			if (((CreatureEntity) other).hasEffect("passiveLux")) {
				damage *= Config.get(Config.luxPassiveMultiplier);
				((CreatureEntity) other).removeEffect("passiveLux");
				ResourceManager.get().playSound("lux_passive_hit");
			} else {
				ResourceManager.get().playSound("lux_shot_hit");
			}
			((CreatureEntity) other).damage(this, damage);
			used = true;
		}
	}
}