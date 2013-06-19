package net.danielfigge.minioninvaders.entities.missiles.misc;

import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.missiles.MissileEntity;
import net.danielfigge.minioninvaders.util.ResourceManager;

public class EzrealUltiEntity extends MissileEntity {
	private double moveSpeed = 800;
	private int damage = 200;

	public EzrealUltiEntity(GameState game, int x, int y) {
		super(null, game, "missiles/ezreal_ulti", x, y);
		removeWhenOutOfArea = false;
		dx = moveSpeed;
	}

	@Override
	public void move(long delta) {
		super.move(delta);
		if (x > ResourceManager.get().getDimension().getWidth()) {
			ResourceManager.get().playSound("ezreal_laugh");
			game.removeEntity(this);
		}
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof CreatureEntity) {
			game.removeEntity(other);
			ResourceManager.get().playSound("ezreal_ulti_hit");
			((CreatureEntity) other).damage(this, damage);
		}
	}
}