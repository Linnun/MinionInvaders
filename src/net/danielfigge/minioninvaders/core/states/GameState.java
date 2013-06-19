package net.danielfigge.minioninvaders.core.states;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import net.danielfigge.minioninvaders.core.GameWindow;
import net.danielfigge.minioninvaders.core.MinionInvaders;
import net.danielfigge.minioninvaders.core.Sprite;
import net.danielfigge.minioninvaders.core.enums.Difficulty;
import net.danielfigge.minioninvaders.core.enums.Mode;
import net.danielfigge.minioninvaders.core.enums.State;
import net.danielfigge.minioninvaders.core.modes.GameMode;
import net.danielfigge.minioninvaders.core.modes.StoryGameMode;
import net.danielfigge.minioninvaders.core.modes.SurvivalGameMode;
import net.danielfigge.minioninvaders.core.modes.TouhouGameMode;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.EnemyCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.PlayerCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.player.SonaEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.PathTools;
import net.danielfigge.minioninvaders.util.ResourceManager;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;


final public class GameState implements StateIf {

	private static final GameState single = new GameState();

	private MinionInvaders game;

	final public static class message {
		public static String text;
		public static int x, y;
	}

	private int width;

	private Mode mode = Mode.STORY;
	private Difficulty difficulty = Difficulty.MEDIUM;

	private int unlockableLevels = 31;
	private ArrayList<Entity> entities = new ArrayList<Entity>(), removeList = new ArrayList<Entity>();
	private PlayerCreatureEntity player = null;

	private boolean pauseHasBeenReleased = false, fireHasBeenReleased = false, abilityQHasBeenReleased = false,
			abilityWHasBeenReleased = false, abilityEHasBeenReleased = false, abilityRHasBeenReleased = false,
			abilityDHasBeenReleased = false, abilityFHasBeenReleased = false;

	private long lastNoteAnimationTime;
	private int noteX1, noteX2, noteX3, noteY1, noteY2, noteY3;

	private int maxEnemyCount = 0, enemyCount = 0, deathCount = 0;

	private boolean waitingForKeyPress = true, paused = false;
	private boolean levelUp = false;
	private int level;

	private long startTime = 0, levelStartTime = 0, pauseStartTime = 0, pausedTime = 0;

	private Sprite win1, win2, guiDeaths;

	private Random random = new Random();

	private GameMode gamemode;

	public static GameState get() {
		return single;
	}

	private GameState() {
		game = ResourceManager.get().getGame();
		width = (int) ResourceManager.get().getDimension().getWidth();
		message.text = "Press Space to start";
		message.x = width / 2;
		message.y = 470;

		initSprites();
		startGame();
	}

	public int getLevel() {
		return level;
	}

	public void startGame() {
		entities.clear();
		removeList.clear();
		paused = false;
		waitingForKeyPress = true;
		levelUp = false;
		message.text = "Press Space to start";
		message.x = width / 2;
		message.y = 470;
		preparePlayerEntity();
		level = 1;
		deathCount = 0;
		startTime = getTime();
		switch (mode) {
		case STORY:
			gamemode = new StoryGameMode(this);
			break;
		case SURVIVAL:
			gamemode = new SurvivalGameMode(this);
			break;
		case TOUHOU:
			gamemode = new TouhouGameMode(this);
			break;
		}
	}

	public void startLevel() {
		if (levelUp) {
			levelUp = false;
			level++;
		}
		if ((level > getLevelFromProps()) && (level <= unlockableLevels)) {
			writeLevelIntoProps();
		}

		paused = false;
		pauseHasBeenReleased = fireHasBeenReleased = abilityQHasBeenReleased = abilityWHasBeenReleased = abilityEHasBeenReleased = abilityRHasBeenReleased = abilityDHasBeenReleased = abilityFHasBeenReleased = false;

		maxEnemyCount = 0;
		enemyCount = 1;
		entities.clear();
		removeList.clear();
		levelStartTime = getTime();
		gamemode.startLevel();
		preparePlayerEntity();
	}

	private void initSprites() {
		win1 = ResourceManager.get().getSprite("particles/win1");
		win2 = ResourceManager.get().getSprite("particles/win2");
		guiDeaths = ResourceManager.get().getSprite("interface/deaths");
	}

	public void preparePlayerEntity() {
		if (player == null) {
			player = new SonaEntity(this, (width / 2) - 15, 510);
		} else {
			player.reset();
			player.resetPosition((width / 2) - 15, 510);
		}
		if (!entities.contains(player)) {
			entities.add(player);
		}
	}

	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}

	public void playerDead() {
		gamemode.playerDead();
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public long getStartTime() {
		return startTime;
	}

	public long getLevelStartTime() {
		return levelStartTime;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public PlayerCreatureEntity getPlayer() {
		return player;
	}

	public void focusLost() {
		if (!waitingForKeyPress) {
			pauseGame();
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void pauseGame() {
		if (!waitingForKeyPress) {
			waitingForKeyPress = paused = true;
			pauseStartTime = game.getSystemTime();
		}
	}

	public void unpauseGame() {
		if (paused) {
			paused = false;
			pausedTime += game.getSystemTime() - pauseStartTime;
		}
	}

	public int getLevelFromProps() {
		PathTools.makeSureDataFilesExist();
		int result = 1;
		try {
			FileInputStream fstream = new FileInputStream(PathTools.getDataDirectory() + "level.mi");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				result = Integer.parseInt((String) strLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void writeLevelIntoProps() {
		PathTools.makeSureDataFilesExist();
		try {
			FileWriter fstream = new FileWriter(new File(PathTools.getDataDirectory() + "level.mi"));
			BufferedWriter bw = new BufferedWriter(fstream);
			bw.write(String.valueOf(level));
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void increaseDeathCount() {
		deathCount++;
	}

	public void setWaitingForKeyPress(boolean waitingForKeyPress) {
		this.waitingForKeyPress = waitingForKeyPress;
	}

	protected void levelWin() {
		message.text = "You won level " + level + "!\nPress Space to start level " + (level + 1);
		message.x = width / 2;
		message.y = 245;
		ResourceManager.get().playSound("level_win");
		waitingForKeyPress = true;
		levelUp = true;
	}

	public long getTime() {
		return game.getSystemTime() - pausedTime;
	}

	private String getTimeString() {
		if (startTime == 0) {
			return "0:00";
		}
		long time = (long) ((getTime() - startTime) / 1000.0);
		int sec = (int) (time % 60);
		int min = (int) (time / 60);
		return String.valueOf(min) + ((sec < 10) ? ":0" : ":") + String.valueOf(sec);
	}

	public Mode getMode() {
		return mode;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public int getModeInt() {
		switch (mode) {
		case STORY:
			return 0;
		case SURVIVAL:
			return 1;
		case TOUHOU:
			return 2;
		}
		return 0;
	}

	public int getDifficultyInt() {
		switch (difficulty) {
		case EASY:
			return 0;
		case MEDIUM:
			return 1;
		case HARD:
			return 2;
		case LEGENDARY:
			return 3;
		}
		return 1;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public int getEnemyCount() {
		return enemyCount;
	}

	public int getMaxEnemyCount() {
		return maxEnemyCount;
	}

	public boolean isWaitingForKeyPress() {
		return waitingForKeyPress;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean levelUpPending() {
		return levelUp;
	}

	public void render(long delta) {
		GameWindow window = ResourceManager.get().getGameWindow();
		int width = (int) ResourceManager.get().getDimension().getWidth();

		gamemode.render(delta);

		window.drawText(GameWindow.FONT_MAIN_DIFFICULTY[getDifficultyInt()], width - 5, 573, "Level " + level, GameWindow.ALIGN_RIGHT);

		guiDeaths.draw(width - guiDeaths.getWidth() - 40, 0);
		window.drawText(GameWindow.FONT_MAIN_DIFFICULTY[getDifficultyInt()], width - 5, 0, String.valueOf(deathCount),
				GameWindow.ALIGN_RIGHT);
		window.drawText(GameWindow.FONT_MAIN_DIFFICULTY[getDifficultyInt()], 710, 0, getTimeString(), GameWindow.ALIGN_RIGHT);

		if ((!paused) && ((mode != Mode.STORY) || (level > 1))) {
			player.drawIcons();
		}

		if (levelUp) {
			if ((lastNoteAnimationTime == 0) || (lastNoteAnimationTime + 100 < getTime())) {
				lastNoteAnimationTime = getTime();
				noteX1 = random.nextInt(10);
				noteX2 = random.nextInt(10);
				noteX3 = random.nextInt(10);
				noteY1 = random.nextInt(10);
				noteY2 = random.nextInt(10);
				noteY3 = random.nextInt(10);
			}

			win1.draw(player.getX() + (player.getW() / 2) - 0 - 30 - noteX1, player.getY() - 48 - noteY1);
			win1.draw(player.getX() + (player.getW() / 2) - 8 - 50 - noteX2, player.getY() - 0 - noteY2);
			win2.draw(player.getX() + (player.getW() / 2) - 8 + 50 + noteX3, player.getY() - 16 - noteY3);
		}

		if (!waitingForKeyPress) {
			if (enemyCount == 0) {
				levelWin();
			}

			enemyCount = 0;
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);
				if (entity instanceof EnemyCreatureEntity) {
					enemyCount++;
				}
				entity.move(delta);
				entity.doLogic();
			}
			if (maxEnemyCount == 0) {
				maxEnemyCount = enemyCount;
			}

			for (int p = 0; p < entities.size(); p++) {
				for (int s = p + 1; s < entities.size(); s++) {
					Entity entity1 = (Entity) entities.get(p);
					Entity entity2 = (Entity) entities.get(s);
					if (entity1.collidesWith(entity2)) {
						entity1.collidedWith(entity2);
						entity2.collidedWith(entity1);
					}
				}
			}
		}

		if (paused) {
			pausedTime += game.getSystemTime() - pauseStartTime;
			pauseStartTime = game.getSystemTime();
		}

		if ((Display.isActive()) || (Display.isVisible()) || (Display.isDirty())) {
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);
				if (((waitingForKeyPress) && (levelUp) && (!(entity instanceof SonaEntity))) || (paused)) {
					continue;
				}
				entity.draw();
			}
		}

		entities.removeAll(removeList);
		removeList.clear();

		if (waitingForKeyPress) {
			if (paused) {
				message.text = "Paused\nPress Space to continue or X to get to the main menu";
				message.x = width / 2;
				message.y = 0;
				player.drawPauseMenuInfo();
			}
			if (message.text.equals("Press Space to start")) {
				window.drawText(GameWindow.FONT_TITLE, width / 2, 20, "M i n i o n  I n v a d e r s", GameWindow.ALIGN_CENTER);
				window.drawText(GameWindow.FONT_SMALL, width - 10, 535, "A game by Daniel Figge", GameWindow.ALIGN_RIGHT);
				gamemode.startScreen();
			}
			gamemode.drawMessage();
			game.sleep(25);
		}

		boolean leftPressed = window.isKeyPressed(Keyboard.KEY_LEFT);
		boolean rightPressed = window.isKeyPressed(Keyboard.KEY_RIGHT);
		boolean firePressed = window.isKeyPressed(Keyboard.KEY_SPACE);
		boolean pausePressed = window.isKeyPressed(Keyboard.KEY_ESCAPE);
		boolean XPressed = window.isKeyPressed(Keyboard.KEY_X);
		boolean abilityQPressed = window.isKeyPressed(Keyboard.KEY_Q);
		boolean abilityWPressed = window.isKeyPressed(Keyboard.KEY_W);
		boolean abilityEPressed = window.isKeyPressed(Keyboard.KEY_E);
		boolean abilityRPressed = window.isKeyPressed(Keyboard.KEY_R);
		boolean abilityDPressed = window.isKeyPressed(Keyboard.KEY_D);
		boolean abilityFPressed = window.isKeyPressed(Keyboard.KEY_F);

		if ((XPressed) && (paused)) {
			game.setState(State.MAIN_MENU);
		}

		if (!waitingForKeyPress) {
			if (pausePressed) {
				if (pauseHasBeenReleased) {
					pauseHasBeenReleased = false;
					pauseGame();
				}
			} else {
				pauseHasBeenReleased = true;
			}
			if ((leftPressed) && (!rightPressed)) {
				player.setDx(-player.getMoveSpeed());
			} else if ((rightPressed) && (!leftPressed)) {
				player.setDx(player.getMoveSpeed());
			} else {
				player.setDx(0);
			}
			if (firePressed) {
				player.useShot();
			}
			if ((mode != Mode.STORY) || (level > 1)) {
				if (abilityQPressed) {
					if (abilityQHasBeenReleased) {
						abilityQHasBeenReleased = false;
						player.useAbilityQ();
					}
				} else {
					abilityQHasBeenReleased = true;
				}
				if (abilityWPressed) {
					if (abilityWHasBeenReleased) {
						abilityWHasBeenReleased = false;
						player.useAbilityW();
					}
				} else {
					abilityWHasBeenReleased = true;
				}
				if (abilityEPressed) {
					if (abilityEHasBeenReleased) {
						abilityEHasBeenReleased = false;
						player.useAbilityE();
					}
				} else {
					abilityEHasBeenReleased = true;
				}
				if (abilityRPressed) {
					if (abilityRHasBeenReleased) {
						abilityRHasBeenReleased = false;
						player.useAbilityR();
					}
				} else {
					abilityRHasBeenReleased = true;
				}
				if (abilityDPressed) {
					if (abilityDHasBeenReleased) {
						abilityDHasBeenReleased = false;
						player.useAbilityD();
					}
				} else {
					abilityDHasBeenReleased = true;
				}
				if (abilityFPressed) {
					if (abilityFHasBeenReleased) {
						abilityFHasBeenReleased = false;
						player.useAbilityF();
					}
				} else {
					abilityFHasBeenReleased = true;
				}
			}
		} else {
			if (firePressed) {
				if (fireHasBeenReleased) {
					waitingForKeyPress = false;
					fireHasBeenReleased = false;
					if (paused) {
						unpauseGame();
					} else {
						if (deathCount == 0) {
							startLevel();
						} else {
							gamemode.afterGameLost();
						}
					}
				}
			} else {
				fireHasBeenReleased = true;
			}
		}

		if (!Display.isActive()) {
			focusLost();
		}
	}

	public boolean isAttackDelayOver() {
		return (GameState.get().getLevelStartTime() + Config.get(Config.enemyAttackDelay) < GameState.get().getTime());
	}

	public void refreshEnemyCount() {
		enemyCount = 0;
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity instanceof EnemyCreatureEntity) {
				enemyCount++;
			}
		}
	}
}