package net.danielfigge.minioninvaders.entities.missiles.sona;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.EnemyCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.player.SonaEntity;
import net.danielfigge.minioninvaders.entities.missiles.MissileEntity;
import net.danielfigge.minioninvaders.util.Config;

final public class SonaAbilityQEntity extends MissileEntity {
	private boolean used = false;

	public SonaAbilityQEntity(SonaEntity parent, GameState game, int x, int y) {
		super(parent, game, "missiles/sona/abilityQ", x, y);
		moveSpeed = -Math.min(Config.get(Config.sonaAbilityQProjectileSpeed)
				+ (Config.get(Config.sonaAbilityQProjectileSpeedPerLevel) * game.getLevel()),
				Config.get(Config.sonaAbilityQProjectileSpeedCap));
		dy = moveSpeed;
	}

	@Override
	public void collidedWith(Entity other) {
		if (used) {
			return;
		}
		if (other instanceof EnemyCreatureEntity) {
			game.removeEntity(this);
			if (((CreatureEntity) other).hasEffectType("spellshield")) {
				((CreatureEntity) other).removeEffectByType("spellshield");
			} else {
				((CreatureEntity) other).damage(this,
						Config.get(Config.sonaAbilityQDamage) + (Config.get(Config.sonaAbilityQDamagePerLevel) * game.getLevel()));
			}
			used = true;
		}
	}
}