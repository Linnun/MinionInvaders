package net.danielfigge.minioninvaders.entities.missiles.sona;

import java.util.ArrayList;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.EnemyCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.MinionEntity;
import net.danielfigge.minioninvaders.entities.creatures.player.SonaEntity;
import net.danielfigge.minioninvaders.entities.missiles.MissileEntity;
import net.danielfigge.minioninvaders.util.Config;


final public class SonaAbilityREntity extends MissileEntity {
	private ArrayList<CreatureEntity> entities;
	private boolean globalStunDone = false;

	public SonaAbilityREntity(SonaEntity parent, GameState game, int x, int y) {
		super(parent, game, "missiles/sona/abilityR", x, y);
		entities = new ArrayList<CreatureEntity>();
		moveSpeed = -Math.min(
				Config.get(Config.sonaAbilityRProjectileSpeed) + (Config.get(Config.sonaAbilityRCooldownPerLevel) * game.getLevel()),
				Config.get(Config.sonaAbilityRProjectileSpeedCap));
		dy = moveSpeed;
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof EnemyCreatureEntity) {
			if (!entities.contains(other)) {
				entities.add((CreatureEntity) other);
				if (((CreatureEntity) other).hasEffectType("spellshield")) {
					((CreatureEntity) other).removeEffectByType("spellshield");
					globalStunDone = true;
				} else {
					((CreatureEntity) other).damage(this,
							Config.get(Config.sonaAbilityRDamage) + (Config.get(Config.sonaAbilityRDamagePerLevel) * game.getLevel()));
					((CreatureEntity) other).stun(Math.max(
							Config.get(Config.sonaAbilityRStunDuration)
									+ (Config.get(Config.sonaAbilityRStunDurationPerLevel) * game.getLevel()),
							Config.get(Config.sonaAbilityRStunDurationCap)));
					if (!globalStunDone) {
						globalStunDone = true;
						ArrayList<Entity> entities = game.getEntities();
						for (int i = 0; i < entities.size(); i++) {
							Entity entity = entities.get(i);
							if (entity instanceof MinionEntity) {
								((MinionEntity) entity).stun(Math.max(
										Config.get(Config.sonaAbilityRStunDuration)
												+ (Config.get(Config.sonaAbilityRStunDurationPerLevel) * game.getLevel()),
										Config.get(Config.sonaAbilityRStunDurationCap)));
							}
						}
					}
				}
			}
		}
	}
}