package net.danielfigge.minioninvaders.entities.creatures;

import net.danielfigge.minioninvaders.core.Sprite;
import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.util.ResourceManager;

abstract public class PlayerCreatureEntity extends CreatureEntity {

	protected Sprite passiveIcon, abilityQIcon, abilityWIcon, abilityEIcon, abilityRIcon, abilityDIcon, abilityFIcon, passiveIconActive,
			abilityQIconActive, abilityWIconActive, abilityEIconActive, abilityRIconActive, abilityDIconActive, abilityFIconActive,
			passiveIconPassive, abilityQIconPassive, abilityWIconPassive, abilityEIconPassive, abilityRIconPassive, abilityDIconPassive,
			abilityFIconPassive;

	protected int abilityDCooldown = 0, abilityFCooldown = 0;
	protected long lastAbilityDTime, lastAbilityFTime;

	public PlayerCreatureEntity(GameState game, String sprite, int x, int y) {
		super(game, sprite, x, y);
	}

	@Override
	protected void set() {
		super.set();
		lastAbilityDTime = lastAbilityFTime = 0;
	}

	public void resetPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void useAbilityD() {
		if (isAbilityDOnCooldown()) {
			return;
		}
		lastAbilityDTime = game.getTime();
		abilityD();
	}

	public void useAbilityF() {
		if (isAbilityFOnCooldown()) {
			return;
		}
		lastAbilityFTime = game.getTime();
		abilityF();
	}

	abstract protected void abilityD();

	abstract protected void abilityF();

	public long getAbilityDCooldown() {
		return Math.max(lastAbilityDTime + abilityDCooldown - game.getTime(), 0);
	}

	public long getAbilityFCooldown() {
		return Math.max(lastAbilityFTime + abilityFCooldown - game.getTime(), 0);
	}

	public boolean isAbilityDOnCooldown() {
		return (getAbilityDCooldown() > 0);
	}

	public boolean isAbilityFOnCooldown() {
		return (getAbilityFCooldown() > 0);
	}

	@Override
	public void draw() {
		super.draw();
		drawHealthbar(0, (int) (ResourceManager.get().getDimension().getHeight() - entityIcon.getHeight()));
	}

	abstract public void drawPauseMenuInfo();

	abstract public void drawIcons();
}