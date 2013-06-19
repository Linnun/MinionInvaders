package net.danielfigge.minioninvaders.strategies.ai;

import java.util.Random;

import net.danielfigge.minioninvaders.strategies.EntityStrategy;


abstract public class Ai extends EntityStrategy {

	protected Random random;

	public Ai() {
		super();
		random = new Random();
	}
}