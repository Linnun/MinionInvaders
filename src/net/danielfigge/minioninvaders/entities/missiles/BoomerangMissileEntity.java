package net.danielfigge.minioninvaders.entities.missiles;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;

abstract public class BoomerangMissileEntity extends MissileEntity {
	protected boolean phase1 = true;
	protected long startTime = 0;
	protected int phase1Time = 1000;

	public BoomerangMissileEntity(CreatureEntity parent, GameState game, String sprite, int x, int y) {
		super(parent, game, sprite, x, y);
		startTime = game.getTime();
	}

	@Override
	public void move(long delta) {
		super.move(delta);

		if (!phase1) {
			double deltaX = parent.getX() - x;
			double deltaY = parent.getY() - y;
			double deltaMax = Math.max(Math.abs(deltaX), Math.abs(deltaY));
			dx = ((deltaX) / deltaMax) * moveSpeed;
			dy = ((deltaY) / deltaMax) * moveSpeed;
		}
	}

	@Override
	public void doLogic() {
		super.doLogic();
		if ((startTime != 0) && (phase1) && (startTime + phase1Time < game.getTime())) {
			phase1IsOver();
		}
	}

	protected void phase1IsOver() {
		phase1 = false;
	}
}