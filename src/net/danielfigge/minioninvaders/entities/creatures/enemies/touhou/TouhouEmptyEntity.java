package net.danielfigge.minioninvaders.entities.creatures.enemies.touhou;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.EnemyCreatureEntity;

final public class TouhouEmptyEntity extends EnemyCreatureEntity {

	public TouhouEmptyEntity(GameState game) {
		super(game, null, 0, 0);
	}

	@Override
	public void set() {
	}

	@Override
	public void reset() {
	}

	@Override
	public void damage(Entity entity, int damage) {
	}

	@Override
	public void draw() {
	}

	@Override
	public void checkAlive() {
	}

	@Override
	public void doLogic() {
	}

	@Override
	protected void shot() {
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