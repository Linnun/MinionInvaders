package net.danielfigge.minioninvaders.util;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import net.danielfigge.minioninvaders.core.GameWindow;
import net.danielfigge.minioninvaders.core.MinionInvaders;
import net.danielfigge.minioninvaders.core.Sprite;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class ResourceManager {

	private static final ResourceManager single = new ResourceManager();

	public static ResourceManager get() {
		return single;
	}

	private GameWindow window;
	private MinionInvaders game;
	private HashMap<String, Audio> soundMap, musicMap;

	private ResourceManager() {
	}

	public void setGame(MinionInvaders game) {
		this.game = game;
	}

	public MinionInvaders getGame() {
		return game;
	}

	public GameWindow getGameWindow() {
		if (window == null) {
			window = new GameWindow();
		}
		return window;
	}

	public Sprite getSprite(String ref) {
		if (window == null) {
			throw new RuntimeException("Attempt to retrieve sprite before game window was created");
		}
		return new Sprite(window, ref);
	}

	public Texture getTexture(String key) {
		try {
			return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("resources/sprites/" + key + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public URL getResource(String path) {
		return getClass().getResource(path);
	}

	public Dimension getDimension() {
		return game.getDimension();
	}

	public void initAudio() {
		soundMap = new HashMap<String, Audio>();
		musicMap = new HashMap<String, Audio>();
		// Music
		musicMap.put("lol_retro", AudioManager.get().initMusic("lol_retro"));
		// Game
		soundMap.put("level_win", AudioManager.get().initSound("level_win"));
		soundMap.put("level_lose", AudioManager.get().initSound("level_lose"));
		// Minion
		soundMap.put("minion_shot_hit", AudioManager.get().initSound("minion_shot_hit"));
		// Sona
		soundMap.put("sona_die", AudioManager.get().initSound("sona_die"));
		soundMap.put("sona_shot", AudioManager.get().initSound("sona_shot"));
		soundMap.put("sona_shot_hit", AudioManager.get().initSound("sona_shot_hit"));
		soundMap.put("sona_powerchord", AudioManager.get().initSound("sona_powerchord"));
		soundMap.put("sona_powerchord_hit", AudioManager.get().initSound("sona_powerchord_hit"));
		soundMap.put("sona_abilityQ", AudioManager.get().initSound("sona_abilityQ"));
		soundMap.put("sona_abilityW", AudioManager.get().initSound("sona_abilityW"));
		soundMap.put("sona_abilityE", AudioManager.get().initSound("sona_abilityE"));
		soundMap.put("sona_abilityR", AudioManager.get().initSound("sona_abilityR"));
		soundMap.put("sona_abilityD", AudioManager.get().initSound("sona_abilityD"));
		soundMap.put("sona_abilityF", AudioManager.get().initSound("sona_abilityF"));
		// Lux
		soundMap.put("lux_win", AudioManager.get().initSound("lux_win"));
		soundMap.put("lux_win2", AudioManager.get().initSound("lux_win2"));
		soundMap.put("lux_die", AudioManager.get().initSound("lux_die"));
		soundMap.put("lux_shot", AudioManager.get().initSound("lux_shot"));
		soundMap.put("lux_shot_hit", AudioManager.get().initSound("lux_shot_hit"));
		soundMap.put("lux_passive_hit", AudioManager.get().initSound("lux_passive_hit"));
		soundMap.put("lux_abilityQ", AudioManager.get().initSound("lux_abilityQ"));
		soundMap.put("lux_abilityQ2", AudioManager.get().initSound("lux_abilityQ2"));
		soundMap.put("lux_abilityQ_hit", AudioManager.get().initSound("lux_abilityQ_hit"));
		soundMap.put("lux_abilityW", AudioManager.get().initSound("lux_abilityW"));
		soundMap.put("lux_abilityW2", AudioManager.get().initSound("lux_abilityW2"));
		soundMap.put("lux_abilityW_hit", AudioManager.get().initSound("lux_abilityW_hit"));
		soundMap.put("lux_abilityR", AudioManager.get().initSound("lux_abilityR"));
		soundMap.put("lux_abilityR2", AudioManager.get().initSound("lux_abilityR2"));
		soundMap.put("lux_abilityR3", AudioManager.get().initSound("lux_abilityR3"));
		// Sivir
		soundMap.put("sivir_win", AudioManager.get().initSound("sivir_win"));
		soundMap.put("sivir_die", AudioManager.get().initSound("sivir_die"));
		soundMap.put("sivir_shot", AudioManager.get().initSound("sivir_shot"));
		soundMap.put("sivir_shot_hit", AudioManager.get().initSound("sivir_shot_hit"));
		soundMap.put("sivir_abilityQ", AudioManager.get().initSound("sivir_abilityQ"));
		soundMap.put("sivir_abilityQ_hit", AudioManager.get().initSound("sivir_abilityQ_hit"));
		soundMap.put("sivir_abilityE", AudioManager.get().initSound("sivir_abilityE"));
		soundMap.put("sivir_abilityR", AudioManager.get().initSound("sivir_abilityR"));
		// Misc
		soundMap.put("ezreal_ulti", AudioManager.get().initSound("ezreal_ulti"));
		soundMap.put("ezreal_ulti_hit", AudioManager.get().initSound("ezreal_ulti_hit"));
		soundMap.put("ezreal_laugh", AudioManager.get().initSound("ezreal_laugh"));
		soundMap.put("soraka_ulti", AudioManager.get().initSound("soraka_ulti"));
	}

	public boolean isSoundPlaying(String key) {
		if (!soundMap.containsKey(key)) {
			System.err.println("Trying to access illegal audio key: " + key);
			return false;
		}

		return soundMap.get(key).isPlaying();
	}

	public void stopSound(String key) {
		if (!soundMap.containsKey(key)) {
			System.err.println("Trying to access illegal audio key: " + key);
			return;
		}

		Audio audio = soundMap.get(key);
		if (audio.isPlaying()) {
			audio.stop();
		}
	}

	public void playSound(String key) {
		playSound(key, false);
	}

	public void playSound(String key, boolean doNothingIfRunning) {
		if (!soundMap.containsKey(key)) {
			System.err.println("Trying to access illegal audio key: " + key);
			return;
		}

		Audio audio = soundMap.get(key);
		if (doNothingIfRunning && audio.isPlaying()) {
			return;
		}
		AudioManager.get().playSound(audio);
	}

	public void playMusic(String key) {
		playMusic(key, true);
	}

	public void playMusic(String key, boolean doNothingIfRunning) {
		if (!musicMap.containsKey(key)) {
			System.err.println("Trying to access illegal audio key: " + key);
			return;
		}

		Audio audio = musicMap.get(key);
		if (doNothingIfRunning && audio.isPlaying()) {
			return;
		}
		AudioManager.get().playMusic(audio);
	}

	public void cleanUp() {
		AudioManager.get().cleanUp();
		if (window != null) {
			window.cleanUp();
			window = null;
		}
	}
}