package net.danielfigge.minioninvaders.entities.missiles.sivir;

import java.util.Random;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.PlayerCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.SivirEntity;
import net.danielfigge.minioninvaders.entities.missiles.MissileEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class SivirShotEntity extends MissileEntity {
	private long lastFrameChange;
	private Random random;
	private boolean used = false;

	public SivirShotEntity(SivirEntity parent, GameState game, int x, int y) {
		super(parent, game, "missiles/sivir/shot", x, y);
		random = new Random();
		moveSpeed = Config.get(Config.sivirShotProjectileSpeed);
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
		if (lastFrameChange > 5) {
			lastFrameChange = 0;
			sprite.rotate(10);
		}
	}

	@Override
	public void collidedWith(Entity other) {
		if (used) {
			return;
		}
		if (other instanceof PlayerCreatureEntity) {
			game.removeEntity(this);
			ResourceManager.get().playSound("sivir_shot_hit");
			((CreatureEntity) other).damage(this, Config.get(Config.sivirShotDamage));
			parent.heal((int) Math.round(Config.get(Config.sivirLifeSteal) * Config.get(Config.sivirShotDamage)));
			parent.addMoveSpeedMultiplier(Config.get(Config.sivirPassiveMoveSpeedMultiplier),
					game.getTime() + Config.get(Config.sivirPassiveDuration));
			used = true;
		}
	}
}