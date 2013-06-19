package net.danielfigge.minioninvaders.core.modes;

import java.util.Random;

import net.danielfigge.minioninvaders.core.GameWindow;
import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.core.states.GameState.message;
import net.danielfigge.minioninvaders.util.ResourceManager;


abstract public class GameMode {

	protected GameState gamestate;
	protected GameWindow window;
	protected int width, height;
	protected int level;
	protected Random random = new Random();

	public GameMode(GameState gamestate) {
		this.gamestate = gamestate;
		window = ResourceManager.get().getGameWindow();
		width = (int) ResourceManager.get().getDimension().getWidth();
		height = (int) ResourceManager.get().getDimension().getHeight();
	}

	abstract public void startScreen();

	public void startLevel() {
		level = gamestate.getLevel();
	}

	abstract public void render(long delta);

	public void drawMessage() {
		window.drawText(GameWindow.FONT_MENU, message.x, message.y, message.text, GameWindow.ALIGN_CENTER);
	}

	public void playerDead() {
		if (gamestate.isWaitingForKeyPress()) {
			return;
		}
		gamestate.increaseDeathCount();
		message.text = "You lost!\nPress Space to retry this level";
		message.x = width / 2;
		message.y = 245;
		gamestate.setWaitingForKeyPress(true);
		ResourceManager.get().playSound("level_lose");
	}

	public void afterGameLost() {
		gamestate.startLevel();
	}
}
