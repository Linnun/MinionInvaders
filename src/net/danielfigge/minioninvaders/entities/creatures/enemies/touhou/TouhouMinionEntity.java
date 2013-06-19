package net.danielfigge.minioninvaders.entities.creatures.enemies.touhou;

import java.util.ArrayList;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.MinionEntity;
import net.danielfigge.minioninvaders.entities.missiles.minion.MinionShotEntity;
import net.danielfigge.minioninvaders.strategies.logic.Logic;
import net.danielfigge.minioninvaders.util.Config;


final public class TouhouMinionEntity extends MinionEntity {

	public TouhouMinionEntity(GameState game, int x, int y, Logic logic) {
		super(game, x, y);
		setMoveSpeed(Math.min(((Config.get(Config.minionMoveSpeed) + ((Config.get(Config.minionMoveSpeedPerLevel) * game.getLevel()))))
				* getMoveSpeedMultiplier(), Config.get(Config.minionMoveSpeedCap)));
		this.logic = logic;
		logic.setEntity(this);
	}

	@Override
	public void notifyMinionkill() {
	}

	@Override
	public void refreshMoveSpeed() {
		moveSpeed = normalMoveSpeed * getMoveSpeedMultiplier();
	}

	@Override
	public void notifyDeath() {
		ArrayList<Entity> entities = game.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = (Entity) entities.get(i);
			if (entity instanceof MinionEntity) {
				((MinionEntity) entity).notifyMinionkill();
			}
		}
		super.notifyDeath();
	}

	@Override
	public void turnAround() {
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