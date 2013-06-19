package net.danielfigge.minioninvaders.entities.missiles.minion;

import java.util.Random;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.PlayerCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.MinionEntity;
import net.danielfigge.minioninvaders.entities.missiles.MissileEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class MinionShotEntity extends MissileEntity {
	private long lastFrameChange;
	private Random random;
	private boolean used = false;

	public MinionShotEntity(MinionEntity parent, GameState game, int x, int y) {
		super(parent, game, "missiles/minion/" + ((parent.isSuperminion()) ? "supershot" : "shot"), x, y);
		random = new Random();
		if (parent.isSuperminion()) {
			moveSpeed = Math.min(
					Config.get(Config.superminionShotProjectileSpeed)
							+ (Config.get(Config.superminionShotProjectileSpeedPerLevel) * game.getLevel()),
					Config.get(Config.superminionShotProjectileSpeedCap));
		} else {
			moveSpeed = Math.min(Config.get(Config.minionShotProjectileSpeed)
					+ (Config.get(Config.minionShotProjectileSpeedPerLevel) * game.getLevel()),
					Config.get(Config.minionShotProjectileSpeedCap));
		}

		dy = moveSpeed;
		dx = random.nextDouble()
				* (dy / ((parent.isSuperminion()) ? Config.get(Config.superminionShotAngleDivisor) : Config
						.get(Config.minionShotAngleDivisor)));
		if (random.nextBoolean()) {
			dx = -dx;
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
			ResourceManager.get().playSound("minion_shot_hit");
			int damage = Config.get(Config.minionShotDamage) + (Config.get(Config.minionShotDamagePerLevel) * game.getLevel());
			if (((MinionEntity) parent).isSuperminion()) {
				damage *= Config.get(Config.superminionDamageMultiplier);
			}
			((CreatureEntity) other).damage(this, damage);
			used = true;
		}
	}
}