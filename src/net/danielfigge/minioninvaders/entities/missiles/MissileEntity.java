package net.danielfigge.minioninvaders.entities.missiles;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.util.ResourceManager;

abstract public class MissileEntity extends Entity {
	protected CreatureEntity parent;
	protected GameState game;
	protected boolean removeWhenOutOfArea = true;
	protected double moveSpeed = 0;

	public MissileEntity(CreatureEntity parent, GameState game, String sprite, int x, int y) {
		super(sprite, x, y);

		this.parent = parent;
		this.game = game;
	}

	@Override
	public void move(long delta) {
		super.move(delta);

		if (removeWhenOutOfArea) {
			if ((x < -sprite.getWidth()) || (y < -sprite.getHeight()) || (x > ResourceManager.get().getDimension().getWidth())
					|| (y > ResourceManager.get().getDimension().getHeight())) {
				game.removeEntity(this);
			}
		}
	}
}