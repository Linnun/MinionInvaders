package net.danielfigge.minioninvaders.core.modes;

import net.danielfigge.minioninvaders.core.GameWindow;
import net.danielfigge.minioninvaders.core.Sprite;
import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.creatures.enemies.MinionEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;

public class SurvivalGameMode extends GameMode {

	protected Sprite guiEnemies;

	public SurvivalGameMode(GameState gamestate) {
		super(gamestate);

		guiEnemies = ResourceManager.get().getSprite("interface/enemies");
	}

	@Override
	public void startScreen() {
		window.drawText(GameWindow.FONT_MENU, width / 2, 220,
				"In survival mode there is no end,\nthe game will always get harder.\n\nHow far can you go?", GameWindow.ALIGN_CENTER);
	}

	@Override
	public void startLevel() {
		super.startLevel();
		buildMinions();
	}

	protected void buildMinions() {
		for (int row = 0; row < Math.min(Config.get(Config.minionRows) + (int) (Config.get(Config.minionRowsPerLevel) * level),
				Config.get(Config.minionRowsCap)); row++) {
			for (int x = 0; x < Math.min(Config.get(Config.minionCols) + (int) (Config.get(Config.minionColsPerLevel) * level),
					Config.get(Config.minionColsCap)); x++) {
				MinionEntity minion = new MinionEntity(gamestate, 100 + (x * 49), 10 + row * 30);
				gamestate.addEntity(minion);
				if (random.nextInt(Config.get(Config.superminionAppearChance)
						+ (Config.get(Config.superminionAppearChancePerLevel) * level)) > 100000) {
					minion.makeSuperminion();
				}
			}
		}
	}

	@Override
	public void render(long delta) {
		drawEnemyCount();
	}

	public void drawEnemyCount() {
		guiEnemies.draw(635, height - guiEnemies.getHeight() - 2);
		window.drawText(GameWindow.FONT_MAIN_DIFFICULTY[gamestate.getDifficultyInt()], 630, 573, gamestate.getEnemyCount() + "/"
				+ gamestate.getMaxEnemyCount(), GameWindow.ALIGN_RIGHT);
	}
}
