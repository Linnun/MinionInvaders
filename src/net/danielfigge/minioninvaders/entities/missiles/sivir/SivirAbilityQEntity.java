package net.danielfigge.minioninvaders.entities.missiles.sivir;

import java.util.ArrayList;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.PlayerCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.SivirEntity;
import net.danielfigge.minioninvaders.entities.missiles.BoomerangMissileEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class SivirAbilityQEntity extends BoomerangMissileEntity {
	private long lastFrameChange;
	private ArrayList<CreatureEntity> entities;

	public SivirAbilityQEntity(SivirEntity parent, GameState game, int x, int y) {
		super(parent, game, "missiles/sivir/abilityQ", x, y);
		entities = new ArrayList<CreatureEntity>();
		removeWhenOutOfArea = false;
		moveSpeed = Config.get(Config.sivirAbilityQProjectileSpeed);
		phase1Time = Config.get(Config.sivirAbilityQPhase1Time);
		double deltaX = game.getPlayer().getX() - x;
		double deltaY = game.getPlayer().getY() - y;
		double deltaMax = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		dx = ((deltaX) / deltaMax) * moveSpeed;
		dy = ((deltaY) / deltaMax) * moveSpeed;
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
		if (other instanceof PlayerCreatureEntity) {
			if (!entities.contains(other)) {
				entities.add(((CreatureEntity) other));
				if (((CreatureEntity) other).hasEffectType("spellshield")) {
					((CreatureEntity) other).removeEffectByType("spellshield");
				} else {
					ResourceManager.get().playSound("sivir_abilityQ_hit");
					((CreatureEntity) other).damage(this, Config.get(Config.sivirAbilityQDamage));
					parent.heal((int) Math.round(Config.get(Config.sivirLifeSteal) * Config.get(Config.sivirAbilityQDamage)));
				}
			}
		}
		if ((!phase1) && (other == parent)) {
			game.removeEntity(this);
		}
	}
}