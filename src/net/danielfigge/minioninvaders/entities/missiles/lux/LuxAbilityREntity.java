package net.danielfigge.minioninvaders.entities.missiles.lux;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.PlayerCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.LuxEntity;
import net.danielfigge.minioninvaders.entities.missiles.MissileEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;

final public class LuxAbilityREntity extends MissileEntity {
	private boolean init = true;
	private boolean used = false;

	public LuxAbilityREntity(LuxEntity parent, GameState game, int x, int y) {
		super(parent, game, "missiles/lux/abilityR_init", x, y);
		removeWhenOutOfArea = false;
		dx = dy = 0;
	}

	public void BlastIsOver() {
		game.removeEntity(this);
	}

	public void initIsOver() {
		init = false;
		sprite = ResourceManager.get().getSprite("missiles/lux/abilityR");
	}

	public boolean isInitOver() {
		return !init;
	}

	@Override
	protected void calculateHitbox() {
		hitbox.setBounds((int) x + 50, (int) y, getW() - 50, getH());
	}

	@Override
	public void collidedWith(Entity other) {
		if ((isInitOver()) && (!used) && (other instanceof PlayerCreatureEntity)) {
			if (((CreatureEntity) other).hasEffectType("spellshield")) {
				((CreatureEntity) other).removeEffectByType("spellshield");
			} else {
				((CreatureEntity) other).damage(this, Config.get(Config.luxAbilityRDamage));
				((CreatureEntity) other).addEffect("passiveLux", game.getTime() + Config.get(Config.luxPassiveDuration));
			}
			used = true;
		}
	}
}