package net.danielfigge.minioninvaders.core.modes;

import java.util.ArrayList;

import net.danielfigge.minioninvaders.core.GameWindow;
import net.danielfigge.minioninvaders.core.enums.State;
import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.core.states.GameState.message;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.touhou.TouhouEmptyEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.touhou.TouhouMinionEntity;
import net.danielfigge.minioninvaders.strategies.logic.LeftToRightLogic;
import net.danielfigge.minioninvaders.strategies.logic.RightToLeftLogic;
import net.danielfigge.minioninvaders.strategies.logic.SimpleCircleLogic;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


public class TouhouGameMode extends GameMode {

	private int lives;
	private int levelPhase;
	private int endTime;

	public TouhouGameMode(GameState gamestate) {
		super(gamestate);

		lives = Config.get(Config.playerLives);
	}

	@Override
	public void startScreen() {
		window.drawText(
				GameWindow.FONT_MENU,
				width / 2,
				220,
				"In Touhou mode you got finite lives.\nIf you run out of lives\nyou'll have to start from level 1 again.\n\nCan you beat this challenging mode?",
				GameWindow.ALIGN_CENTER);
	}

	@Override
	public void startLevel() {
		if (gamestate.getTime() - gamestate.getStartTime() < 10000) {
			gamestate.setLevel(1);
		}
		levelPhase = 0;
		endTime = 0;
		TouhouEmptyEntity entity = new TouhouEmptyEntity(gamestate);
		gamestate.addEntity(entity);
		super.startLevel();
	}

	@Override
	public void render(long delta) {
		long levelDelta = gamestate.getTime() - gamestate.getLevelStartTime();
		window.drawText(GameWindow.FONT_MAIN_DIFFICULTY[gamestate.getDifficultyInt()], 55, 573, "Lives: " + lives);

		if ((levelPhase == 0) && (levelDelta > 1000)) {
			levelPhase++;
		}
		TouhouMinionEntity minion;
		switch (level) {
		case 1:
			switch (levelPhase) {
			case 2:
			case 4:
			case 6:
			case 8:
				waitForLevelPhaseEnd();
				break;
			case 1:
				minion = new TouhouMinionEntity(gamestate, 200, 200, new SimpleCircleLogic());
				gamestate.addEntity(minion);
				levelPhase++;
				break;
			case 3:
				for (int i = 0; i < 5; i++) {
					minion = new TouhouMinionEntity(gamestate, width + (i * 49), 50, new RightToLeftLogic());
					gamestate.addEntity(minion);
				}
				levelPhase++;
				break;
			case 5:
				for (int i = 0; i < 5; i++) {
					minion = new TouhouMinionEntity(gamestate, -49 - (i * 49), 120, new LeftToRightLogic());
					gamestate.addEntity(minion);
				}
				levelPhase++;
				break;
			case 7:
				for (int i = 0; i < 5; i++) {
					minion = new TouhouMinionEntity(gamestate, width + (i * 49), 50, new RightToLeftLogic());
					gamestate.addEntity(minion);
				}
				for (int i = 0; i < 5; i++) {
					minion = new TouhouMinionEntity(gamestate, -49 - (i * 49), 120, new LeftToRightLogic());
					gamestate.addEntity(minion);
				}
				levelPhase++;
				break;
			case 9:
				touhouLevelEnd(delta);
				break;
			}
			break;
		}
	}

	private void touhouLevelEnd(long delta) {
		endTime += delta;
		if (endTime < 1000) {
			return;
		}
		ArrayList<Entity> entities = gamestate.getEntities();
		for (Entity entity : entities) {
			if (entity instanceof TouhouEmptyEntity) {
				gamestate.removeEntity(entity);
				break;
			}
		}
	}

	private void waitForLevelPhaseEnd() {
		if (gamestate.getEnemyCount() == 1) {
			levelPhase++;
		}
	}

	@Override
	public void playerDead() {
		lives--;
		if (lives == 0) {
			if (gamestate.isWaitingForKeyPress()) {
				return;
			}
			gamestate.increaseDeathCount();
			message.text = "You lost!\nPress Space to get back to main menu";
			message.x = width / 2;
			message.y = 245;
			gamestate.setWaitingForKeyPress(true);
			ResourceManager.get().playSound("level_lose");
		} else {
			gamestate.preparePlayerEntity();
		}
	}

	@Override
	public void afterGameLost() {
		ResourceManager.get().getGame().setState(State.MAIN_MENU);
	}
}
