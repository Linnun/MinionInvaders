package net.danielfigge.minioninvaders.entities.creatures.enemies;

import java.util.ArrayList;
import java.util.Random;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.EnemyCreatureEntity;
import net.danielfigge.minioninvaders.entities.missiles.minion.MinionShotEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


public class MinionEntity extends EnemyCreatureEntity {
	private int minionKillMultiplier = 0;
	protected Random random;
	protected boolean superminion = false;
	protected boolean canShoot = true;
	protected boolean speedUp = true;

	public MinionEntity(GameState game, int x, int y) {
		super(game, "creatures/minion", x, y);

		setHitpoints(Config.get(Config.minionHitpoints));
		shotCooldown = Config.get(Config.minionShotCooldown);
	}

	@Override
	protected void set() {
		super.set();
		random = new Random();
		dx = -moveSpeed;
	}

	public void setCanShoot(boolean canShoot) {
		this.canShoot = canShoot;
	}

	public void setSpeedUp(boolean speedUp) {
		this.speedUp = speedUp;
	}

	public void notifyMinionkill() {
		minionKillMultiplier++;
		refreshMoveSpeed();
	}

	@Override
	protected void refreshMoveSpeed() {
		moveSpeed = Math.min(
				((Config.get(Config.minionMoveSpeed) + ((Config.get(Config.minionMoveSpeedPerLevel) * game.getLevel()))) * Math.pow(
						Config.get(Config.minionMovespeedUpPerMinionKill), minionKillMultiplier))
						* getMoveSpeedMultiplier(), Config.get(Config.minionMoveSpeedCap));
		if (dx < 0) {
			dx = -moveSpeed;
		} else {
			dx = moveSpeed;
		}
	}

	public void makeSuperminion() {
		superminion = true;
		x -= 2;
		y += 2;
		setHitpoints(Config.get(Config.superminionHitpoints));
		shotCooldown = Config.get(Config.superminionShotCooldown);
		sprite = ResourceManager.get().getSprite("creatures/superminion");
	}

	@Override
	public void notifyDeath() {
		ArrayList<Entity> entities = game.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = (Entity) entities.get(i);
			if (entity instanceof MinionEntity) {
				if (speedUp) {
					((MinionEntity) entity).notifyMinionkill();
				}
			}
		}
		super.notifyDeath();
	}

	public boolean isSuperminion() {
		return superminion;
	}

	public void turnAround() {
		dx = -dx;
		y += Config.get(Config.minionPxDownPerRowPassed);
	}

	@Override
	public void doLogic() {
		super.doLogic();
		if (isStunned()) {
			return;
		}

		if (((dx < 0) && (x <= 0)) || ((dx > 0) && (x >= ResourceManager.get().getDimension().getWidth() - getW()))) {
			ArrayList<Entity> entities = game.getEntities();
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);
				if (entity instanceof MinionEntity) {
					((MinionEntity) entity).turnAround();
				}
			}
		}

		if (y > ResourceManager.get().getDimension().height - getH()) {
			game.playerDead();
		}

		if (game.isAttackDelayOver()) {
			int chance;
			if (isSuperminion()) {
				chance = random.nextInt(Config.get(Config.superminionShotChance)
						+ ((int) (Config.get(Config.superminionShotChancePerLevel) * game.getLevel())));
			} else {
				chance = random.nextInt(Config.get(Config.minionShotChance)
						+ ((int) (Config.get(Config.minionShotChancePerLevel) * game.getLevel())));
			}
			if (chance > 100000) {
				useShot();
			}
		}
	}

	@Override
	protected void shot() {
		if (!canShoot) {
			return;
		}
		MinionShotEntity shot;
		shot = new MinionShotEntity(this, game, (int) x + (getW() / 2) - 12, (int) y + (getH() / 2));
		game.addEntity(shot);
	}

	@Override
	protected void abilityQ() {
	}

	@Override
	protected void abilityW() {
	}

	@Override
	protected void abilityE() {
	}

	@Override
	protected void abilityR() {
	}
}