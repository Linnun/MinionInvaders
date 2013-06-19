package net.danielfigge.minioninvaders.core;

import java.awt.Dimension;

import net.danielfigge.minioninvaders.core.enums.State;
import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.core.states.MainMenuState;
import net.danielfigge.minioninvaders.util.AudioManager;
import net.danielfigge.minioninvaders.util.ResourceManager;

import org.lwjgl.Sys;

public class MinionInvaders implements GameWindowCallbackIf {

	private boolean vsync = true;
	private int maxFps = 60;
	private int width = 800;
	private int height = 600;
	private String windowTitle = "MinionInvaders 1.3.12";

	private State state = State.MAIN_MENU;

	private GameWindow window = null;

	private int fps;

	private long lastFpsTime = 0, lastGarbageCollectionTime = 0, lastLoopTime = 0;

	public MinionInvaders() {
		ResourceManager.get().setGame(this);
		window = ResourceManager.get().getGameWindow();
		window.setResolution(width, height);
		window.setGameWindowCallback(this);
		window.setIcons(new String[] { "/resources/icons/icon16.png", "/resources/icons/icon32.png", "/resources/icons/icon64.png",
				"/resources/icons/icon128.png" });
		window.setTitle(windowTitle);
		window.setFramerate(maxFps);
		window.setVsync(vsync);
		window.startRendering();
	}

	public Dimension getDimension() {
		return new Dimension(width, height);
	}

	public void setState(State state) {
		this.state = state;
		if (state == State.GAME) {
			GameState.get().startGame();
		} else if (state == State.MAIN_MENU) {
			MainMenuState.get().reinit();
		}
	}

	public void initialise() {
		ResourceManager.get().initAudio();
	}

	public long getSystemTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public void frameRendering() {
		long delta = getSystemTime() - lastLoopTime;
		lastLoopTime = getSystemTime();
		lastFpsTime += delta;
		fps++;
		if (lastFpsTime >= 1000) {
			window.setTitle(windowTitle + " (FPS: " + fps + ")");
			lastFpsTime = 0;
			fps = 0;
		}

		if ((lastGarbageCollectionTime == 0) || (lastGarbageCollectionTime + 5000 < getSystemTime())) {
			lastGarbageCollectionTime = getSystemTime();
			Runtime.getRuntime().gc();
		}

		ResourceManager.get().playMusic("lol_retro");
		AudioManager.get().poll();

		switch (state) {
		case MAIN_MENU:
			MainMenuState.get().render(delta);
			break;
		case GAME:
			GameState.get().render(delta);
		}
	}

	public void sleep(long duration) {
		if (!window.displayReady()) {
			return;
		}
		try {
			Thread.sleep((duration * Sys.getTimerResolution()) / 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void windowClosed() {
		ResourceManager.get().cleanUp();
		System.exit(0);
	}
}