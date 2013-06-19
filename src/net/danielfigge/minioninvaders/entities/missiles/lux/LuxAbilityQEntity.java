package net.danielfigge.minioninvaders.entities.missiles.lux;

import java.util.ArrayList;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.PlayerCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.LuxEntity;
import net.danielfigge.minioninvaders.entities.missiles.MissileEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class LuxAbilityQEntity extends MissileEntity {
	private ArrayList<PlayerCreatureEntity> entities;

	public LuxAbilityQEntity(LuxEntity parent, GameState game, int x, int y) {
		super(parent, game, "missiles/lux/abilityQ", x, y);
		entities = new ArrayList<PlayerCreatureEntity>();
		moveSpeed = Config.get(Config.luxAbilityQProjectileSpeed);
		dx = 0;
		dy = moveSpeed;
	}

	@Override
	protected void calculateHitbox() {
		hitbox.setBounds((int) x + 10, (int) y + 80, getW() - 19, getH() - 80);
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof PlayerCreatureEntity) {
			if (!entities.contains(other)) {
				entities.add((PlayerCreatureEntity) other);
				if (((CreatureEntity) other).hasEffectType("spellshield")) {
					((CreatureEntity) other).removeEffectByType("spellshield");
				} else {
					ResourceManager.get().playSound("lux_abilityQ_hit");
					((CreatureEntity) other).damage(this, Config.get(Config.luxAbilityQDamage));
					((CreatureEntity) other).stun(Config.get(Config.luxAbilityQStunDuration));
					((CreatureEntity) other).addEffect("passiveLux", game.getTime() + Config.get(Config.luxPassiveDuration));
				}
				if (entities.size() >= 2) {
					game.removeEntity(this);
				}
			}
		}
	}
}