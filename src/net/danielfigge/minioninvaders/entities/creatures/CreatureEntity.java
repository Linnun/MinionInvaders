package net.danielfigge.minioninvaders.entities.creatures;

import java.util.ArrayList;
import java.util.HashMap;

import net.danielfigge.minioninvaders.core.GameWindow;
import net.danielfigge.minioninvaders.core.Sprite;
import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.strategies.logic.Logic;
import net.danielfigge.minioninvaders.util.ResourceManager;


abstract public class CreatureEntity extends Entity {
	protected GameState game;
	protected double prevDx, prevDy;
	protected HashMap<Sprite, Long> effects;
	protected ArrayList<Sprite> effectsRemoveList;
	protected HashMap<Double, Long> moveSpeedMultipliers;
	protected ArrayList<Double> moveSpeedMultipliersRemoveList;
	protected String originalSprite;
	protected double moveSpeed = 0, normalMoveSpeed = 0;
	protected int maxHitpoints = 0, hitpoints, shield, lastMaxShield;
	protected long lastShieldTime, lastStunTime;
	protected int lastShieldDuration, lastStunDuration;
	protected int shotCooldown = 0, abilityQCooldown = 0, abilityWCooldown = 0, abilityECooldown = 0, abilityRCooldown = 0;
	protected long lastShotTime, lastAbilityQTime, lastAbilityWTime, lastAbilityETime, lastAbilityRTime;
	protected Sprite entityIcon, healthbar, shieldbar, barborder;
	protected Logic logic = null;

	public CreatureEntity(GameState game, String sprite, int x, int y) {
		super(sprite, x, y);
		this.game = game;
		effects = new HashMap<Sprite, Long>();
		effectsRemoveList = new ArrayList<Sprite>();
		moveSpeedMultipliers = new HashMap<Double, Long>();
		moveSpeedMultipliersRemoveList = new ArrayList<Double>();
		originalSprite = sprite;
		healthbar = ResourceManager.get().getSprite("interface/healthbar");
		shieldbar = ResourceManager.get().getSprite("interface/shieldbar");
		barborder = ResourceManager.get().getSprite("interface/barborder");
		set();
	}

	protected void set() {
		sprite = ResourceManager.get().getSprite(originalSprite);
		prevDx = prevDy = 0;
		lastShieldTime = lastStunTime = 0;
		lastShieldDuration = lastStunDuration = 0;
		lastShotTime = lastAbilityQTime = lastAbilityWTime = lastAbilityETime = lastAbilityRTime = 0;
		hitpoints = maxHitpoints;
		shield = lastMaxShield = 0;
		refreshMoveSpeed();
	}

	public void reset() {
		set();
		effects.clear();
		effectsRemoveList.clear();
		moveSpeedMultipliers.clear();
		moveSpeedMultipliersRemoveList.clear();
	}

	public void setHitpoints(int hitpoints) {
		maxHitpoints = this.hitpoints = hitpoints;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public int getMaxHitpoints() {
		return maxHitpoints;
	}

	public int getShield() {
		return shield;
	}

	public int getLastMaxShield() {
		return lastMaxShield;
	}

	public void setMoveSpeed(double moveSpeed) {
		normalMoveSpeed = this.moveSpeed = moveSpeed;
		refreshMoveSpeed();
	}

	public double getMoveSpeed() {
		return moveSpeed;
	}

	public double getMoveSpeedMultiplier() {
		double moveSpeedMultiplier = 1;
		for (double multiplier : moveSpeedMultipliers.keySet()) {
			moveSpeedMultiplier *= multiplier;
		}
		return moveSpeedMultiplier;
	}

	protected void refreshMoveSpeed() {
		moveSpeed = normalMoveSpeed * getMoveSpeedMultiplier();
	}

	public void damage(Entity entity, int damage) {
		if (shield >= damage) {
			shield -= damage;
		} else {
			damage -= shield;
			shield = 0;
			hitpoints -= damage;
		}
		checkAlive();
	}

	public void stun(int duration) {
		lastStunTime = game.getTime();
		lastStunDuration = duration;
		prevDx = dx;
		prevDy = dy;
		dx = dy = 0;
	}

	public boolean isStunned() {
		return ((lastStunTime != 0) && (lastStunTime + lastStunDuration - game.getTime() > 0));
	}

	public void shield(int amount, int duration) {
		if (shield <= amount) {
			shield = lastMaxShield = amount;
			lastShieldTime = game.getTime();
			lastShieldDuration = duration;
		}
	}

	protected boolean canMove() {
		if (isStunned()) {
			return false;
		}
		return true;
	}

	@Override
	public void move(long delta) {
		if (!canMove()) {
			return;
		}
		super.move(delta);
	}

	@Override
	public void doLogic() {
		super.doLogic();
		if (isStunned()) {
			if (lastStunTime + lastStunDuration < game.getTime()) {
				dx = prevDx;
				dy = prevDy;
			}
		}
		if (shield > 0) {
			if (lastShieldTime + lastShieldDuration < game.getTime()) {
				shield = 0;
			}
		}
		for (Sprite sprite : effects.keySet()) {
			if (effects.get(sprite) < game.getTime()) {
				effectsRemoveList.add(sprite);
			}
		}
		for (Sprite sprite : effectsRemoveList) {
			if (effects.containsKey(sprite)) {
				effects.remove(sprite);
			}
		}
		effectsRemoveList.clear();
		for (double multiplier : moveSpeedMultipliers.keySet()) {
			if (moveSpeedMultipliers.get(multiplier) < game.getTime()) {
				moveSpeedMultipliersRemoveList.add(multiplier);
			}
		}
		for (double multiplier : moveSpeedMultipliersRemoveList) {
			if (moveSpeedMultipliers.containsKey(multiplier)) {
				moveSpeedMultipliers.remove(multiplier);
			}
		}
		moveSpeedMultipliersRemoveList.clear();
		refreshMoveSpeed();
		if (logic != null) {
			logic.sync();
		}
	}

	public void useShot() {
		if (isShotOnCooldown()) {
			return;
		}
		lastShotTime = game.getTime();
		shot();
	}

	public void useAbilityQ() {
		if (isAbilityQOnCooldown()) {
			return;
		}
		lastAbilityQTime = game.getTime();
		abilityQ();
	}

	public void useAbilityW() {
		if (isAbilityWOnCooldown()) {
			return;
		}
		lastAbilityWTime = game.getTime();
		abilityW();
	}

	public void useAbilityE() {
		if (isAbilityEOnCooldown()) {
			return;
		}
		lastAbilityETime = game.getTime();
		abilityE();
	}

	public void useAbilityR() {
		if (isAbilityROnCooldown()) {
			return;
		}
		lastAbilityRTime = game.getTime();
		abilityR();
	}

	abstract protected void shot();

	abstract protected void abilityQ();

	abstract protected void abilityW();

	abstract protected void abilityE();

	abstract protected void abilityR();

	public long getShotCooldown() {
		return Math.max(lastShotTime + shotCooldown - game.getTime(), 0);
	}

	public long getAbilityQCooldown() {
		return Math.max(lastAbilityQTime + abilityQCooldown - game.getTime(), 0);
	}

	public long getAbilityWCooldown() {
		return Math.max(lastAbilityWTime + abilityWCooldown - game.getTime(), 0);
	}

	public long getAbilityECooldown() {
		return Math.max(lastAbilityETime + abilityECooldown - game.getTime(), 0);
	}

	public long getAbilityRCooldown() {
		return Math.max(lastAbilityRTime + abilityRCooldown - game.getTime(), 0);
	}

	public boolean isShotOnCooldown() {
		return (getShotCooldown() > 0);
	}

	public boolean isAbilityQOnCooldown() {
		return (getAbilityQCooldown() > 0);
	}

	public boolean isAbilityWOnCooldown() {
		return (getAbilityWCooldown() > 0);
	}

	public boolean isAbilityEOnCooldown() {
		return (getAbilityECooldown() > 0);
	}

	public boolean isAbilityROnCooldown() {
		return (getAbilityRCooldown() > 0);
	}

	public void heal(int heal) {
		if (hitpoints + heal > maxHitpoints) {
			hitpoints = maxHitpoints;
		} else {
			hitpoints += heal;
		}
	}

	protected void checkAlive() {
		if (shield == 0) {
			removeEffectByType("shield");
		}
		if (hitpoints <= 0) {
			hitpoints = 0;
			notifyDeath();
		}
	}

	public void notifyDeath() {
		game.removeEntity(this);
	}

	public void addEffect(String ref, long end) {
		effects.put(ResourceManager.get().getSprite("effects/" + ref), end);
	}

	public void removeEffect(String ref) {
		for (Sprite sprite : effects.keySet()) {
			if (sprite.getFilename().equals(ref)) {
				effectsRemoveList.add(sprite);
			}
		}
	}

	public long getEffectDuration(String ref) {
		for (Sprite sprite : effects.keySet()) {
			if (sprite.getFilename().equals(ref)) {
				return effects.get(sprite);
			}
		}
		return 0;
	}

	public boolean hasEffect(String ref) {
		for (Sprite sprite : effects.keySet()) {
			if (sprite.getFilename().equals(ref)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasEffectType(String ref) {
		for (Sprite sprite : effects.keySet()) {
			if (sprite.getFilename().startsWith(ref)) {
				return true;
			}
		}
		return false;
	}

	public void removeEffectByType(String ref) {
		for (Sprite sprite : effects.keySet()) {
			if (sprite.getFilename().startsWith(ref)) {
				effectsRemoveList.add(sprite);
			}
		}
	}

	public void addMoveSpeedMultiplier(double multiplier, long end) {
		moveSpeedMultipliers.put(multiplier, end);
	}

	public void draw() {
		super.draw();
		for (Sprite sprite : effects.keySet()) {
			sprite.draw((int) (x + (getW() / 2)) - (sprite.getWidth() / 2), (int) (y + (getH() / 2)) - (sprite.getHeight() / 2));
		}
	}

	public void drawHealthbar(int x, int y) {
		if (entityIcon != null) {
			entityIcon.draw(x, y);
			healthbar.draw(x + 50, y + 3, (int) (((getHitpoints() * 100) / getMaxHitpoints()) * 1.2), 20);
			barborder.draw(x + 49, y + 2, 122, 1);
			barborder.draw(x + 49, y + 23, 122, 1);
			barborder.draw(x + 49, y + 3, 1, 20);
			barborder.draw(x + 170, y + 3, 1, 20);
			ResourceManager.get().getGameWindow()
					.drawText(GameWindow.FONT_SMALL, x + 110, y + 5, getHitpoints() + "/" + getMaxHitpoints(), GameWindow.ALIGN_CENTER);
			if ((getShield() > 0) && (getLastMaxShield() > 0)) {
				shieldbar.draw(x + 50, y + 25, (int) (((getShield() * 100) / getLastMaxShield()) * 1.2), 20);
				barborder.draw(x + 49, y + 24, 122, 1);
				barborder.draw(x + 49, y + 45, 122, 1);
				barborder.draw(x + 49, y + 25, 1, 20);
				barborder.draw(x + 170, y + 25, 1, 20);
				ResourceManager.get().getGameWindow()
						.drawText(GameWindow.FONT_SMALL, x + 110, y + 28, getShield() + "/" + getLastMaxShield(), GameWindow.ALIGN_CENTER);
			}
		}
	}

	@Override
	public void collidedWith(Entity other) {
		// collisions with creatures are usually handled within the missiles
	}
}