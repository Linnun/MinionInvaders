package net.danielfigge.minioninvaders.entities.missiles.lux;

import java.util.ArrayList;
import java.util.Random;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.EnemyCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.LuxEntity;
import net.danielfigge.minioninvaders.entities.missiles.BoomerangMissileEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class LuxAbilityWEntity extends BoomerangMissileEntity {
	private long lastFrameChange;
	private Random random;
	private ArrayList<CreatureEntity> entities;

	public LuxAbilityWEntity(LuxEntity parent, GameState game, int x, int y) {
		super(parent, game, "missiles/lux/abilityW", x, y);
		entities = new ArrayList<CreatureEntity>();
		random = new Random();
		removeWhenOutOfArea = false;
		moveSpeed = Config.get(Config.luxAbilityWProjectileSpeed);
		phase1Time = Config.get(Config.luxAbilityWPhase1Time);
		dy = random.nextDouble() * moveSpeed;
		dx = moveSpeed - dy;
		if (random.nextBoolean()) {
			dx = -dx;
		}
		if (random.nextBoolean()) {
			dy = -dy;
		}
	}

	@Override
	public void move(long delta) {
		super.move(delta);
		lastFrameChange += delta;
		if (lastFrameChange > 10) {
			lastFrameChange = 0;
			sprite.rotate(13);
		}
	}

	@Override
	public void phase1IsOver() {
		super.phase1IsOver();
		entities.clear();
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof EnemyCreatureEntity) {
			if (!entities.contains(other)) {
				entities.add(((CreatureEntity) other));
				((CreatureEntity) other).shield(Config.get(Config.luxAbilityWShieldAmount), Config.get(Config.luxAbilityWShieldTime));
				((CreatureEntity) other).addEffect("shieldLux", game.getTime() + Config.get(Config.luxAbilityWShieldTime));
			}
		}
		if ((!phase1) && (other == parent)) {
			ResourceManager.get().stopSound("lux_abilityW");
			ResourceManager.get().stopSound("lux_abilityW2");
			ResourceManager.get().playSound("lux_abilityW_hit");
			game.removeEntity(this);
		}
	}
}