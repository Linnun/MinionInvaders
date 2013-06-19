package net.danielfigge.minioninvaders.entities.creatures;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.strategies.ai.Ai;

abstract public class EnemyCreatureEntity extends CreatureEntity {

	protected Ai ai = null;

	public EnemyCreatureEntity(GameState game, String sprite, int x, int y) {
		super(game, sprite, x, y);
		if (ai != null) {
			ai.setEntity(this);
		}
	}

	@Override
	public void doLogic() {
		super.doLogic();
		if (isStunned()) {
			return;
		}
		if (ai != null) {
			try {
				ai.sync();
			} catch (NullPointerException e) {
			}
		}
	}
}