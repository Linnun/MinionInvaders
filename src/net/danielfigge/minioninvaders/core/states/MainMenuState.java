package net.danielfigge.minioninvaders.core.states;

import java.io.IOException;

import net.danielfigge.minioninvaders.core.GameWindow;
import net.danielfigge.minioninvaders.core.MinionInvaders;
import net.danielfigge.minioninvaders.core.enums.Difficulty;
import net.danielfigge.minioninvaders.core.enums.Mode;
import net.danielfigge.minioninvaders.core.enums.State;
import net.danielfigge.minioninvaders.util.ResourceManager;

import org.lwjgl.LWJGLException;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.WheelWidget;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleButtonModel;
import de.matthiasmann.twl.model.SimpleChangableListModel;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

final public class MainMenuState extends Widget implements StateIf {

	private static final MainMenuState single = new MainMenuState();

	private GUI gui;
	private MinionInvaders game;

	private SimpleChangableListModel<String> modeList, difficultyList, levelList;
	private WheelWidget<String> modeWheel, difficultyWheel, levelWheel;
	private SimpleButtonModel buttonModelOK;
	private Button buttonOK;
	private boolean buttonOKIsPressed = false;

	private int modeX, difficultyX, levelX;

	private int modeWheelSelection = 0, difficultyWheelSelection = 1, levelWheelSelection = 0;

	public static MainMenuState get() {
		return single;
	}

	private MainMenuState() {
		setTheme("minioninvaders");

		// modeList = new SimpleChangableListModel<String>("Story", "Survival", "Touhou");
		modeList = new SimpleChangableListModel<String>("Story", "Survival");
		modeWheel = new WheelWidget<String>(modeList);
		modeWheel.setCyclic(false);
		add(modeWheel);

		difficultyList = new SimpleChangableListModel<String>("Easy", "Medium", "Hard", "Legendary");
		difficultyWheel = new WheelWidget<String>(difficultyList);
		difficultyWheel.setCyclic(false);
		add(difficultyWheel);

		buildLevelWheel();
		add(levelWheel);

		buttonModelOK = new SimpleButtonModel();
		buttonOK = new Button();
		buttonOK.setTheme("normalButton");
		buttonOK.setText("Start");
		buttonOK.setModel(buttonModelOK);
		add(buttonOK);

		game = ResourceManager.get().getGame();
		try {
			LWJGLRenderer renderer = new LWJGLRenderer();
			gui = new GUI(this, renderer);

			ThemeManager theme = ThemeManager.createThemeManager(ResourceManager.get().getResource("/resources/ui.xml"), renderer);
			gui.applyTheme(theme);
		} catch (LWJGLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		reinit();
	}

	private void buildLevelWheel() {
		int unlockedLevels = (game == null) ? 1 : GameState.get().getLevelFromProps();
		levelList = new SimpleChangableListModel<String>();
		for (int i = 1; i <= unlockedLevels; i++) {
			levelList.addElement("Level " + i);
		}
		levelWheel = new WheelWidget<String>(levelList);
		levelWheel.setCyclic(false);
	}

	public void reinit() {
		removeChild(levelWheel);
		buildLevelWheel();
		add(levelWheel);
		modeWheel.setSelected(modeWheelSelection);
		difficultyWheel.setSelected(difficultyWheelSelection);
		levelWheel.setSelected(levelWheelSelection);
	}

	@Override
	protected void layout() {
		modeWheel.adjustSize();
		difficultyWheel.adjustSize();
		levelWheel.adjustSize();

		modeX = ((int) ResourceManager.get().getDimension().getWidth() / 2)
				- ((modeWheel.getWidth() + difficultyWheel.getWidth() + levelWheel.getWidth()) / 2);
		modeWheel.setPosition(modeX, 220);

		difficultyX = modeX + modeWheel.getWidth() + 5;
		difficultyWheel.setPosition(difficultyX, 220);

		levelX = difficultyX + difficultyWheel.getWidth() + 5;
		levelWheel.setPosition(levelX, 220);

		buttonOK.adjustSize();
		buttonOK.setSize(buttonOK.getWidth() + 10, buttonOK.getHeight());
		buttonOK.setPosition(((int) ResourceManager.get().getDimension().getWidth() / 2) - (buttonOK.getWidth() / 2), 450);
	}

	public void update() {
		render();
	}

	public void render(long delta) {
		render();
	}

	public void render() {
		GameWindow window = ResourceManager.get().getGameWindow();
		int width = (int) ResourceManager.get().getDimension().getWidth();
		window.drawText(GameWindow.FONT_TITLE, width / 2, 20, "M i n i o n  I n v a d e r s", GameWindow.ALIGN_CENTER);
		window.drawText(GameWindow.FONT_MENU, width / 2, 120, "Select game settings!", GameWindow.ALIGN_CENTER);
		// window.drawText(GameWindow.FONT_SMALL, modeX, 400, "Drag the setting wheels with the mouse (Touhou Mode will always start at level 1!)");
		window.drawText(GameWindow.FONT_SMALL, modeX, 400, "Drag the setting wheels with the mouse");
		window.drawText(GameWindow.FONT_SMALL, width - 10, 535, "A game by Daniel Figge", GameWindow.ALIGN_RIGHT);
		window.drawText(GameWindow.FONT_MENU, modeX, 190, "Mode");
		window.drawText(GameWindow.FONT_MENU, difficultyX, 190, "Difficulty");
		window.drawText(GameWindow.FONT_MENU, levelX, 190, "Level");

		gui.update();

		if (buttonModelOK.isPressed()) {
			if (!buttonOKIsPressed) {
				buttonOKIsPressed = true;
				modeWheelSelection = modeWheel.getSelected();
				difficultyWheelSelection = difficultyWheel.getSelected();
				levelWheelSelection = levelWheel.getSelected();
				Mode mode = null;
				if (modeList.getEntry(modeWheelSelection).equalsIgnoreCase("story")) {
					mode = Mode.STORY;
				} else if (modeList.getEntry(modeWheelSelection).equalsIgnoreCase("survival")) {
					mode = Mode.SURVIVAL;
				} else if (modeList.getEntry(modeWheelSelection).equalsIgnoreCase("touhou")) {
					mode = Mode.TOUHOU;
				}
				GameState.get().setMode(mode);
				Difficulty difficulty = null;
				if (difficultyList.getEntry(difficultyWheelSelection).equalsIgnoreCase("easy")) {
					difficulty = Difficulty.EASY;
				} else if (difficultyList.getEntry(difficultyWheelSelection).equalsIgnoreCase("medium")) {
					difficulty = Difficulty.MEDIUM;
				} else if (difficultyList.getEntry(difficultyWheelSelection).equalsIgnoreCase("hard")) {
					difficulty = Difficulty.HARD;
				} else if (difficultyList.getEntry(difficultyWheelSelection).equalsIgnoreCase("legendary")) {
					difficulty = Difficulty.LEGENDARY;
				}
				GameState.get().setDifficulty(difficulty);
				game.setState(State.GAME);
				GameState.get().setLevel(Integer.parseInt(levelList.getEntry(levelWheelSelection).replace("Level ", "")));
			}
		} else {
			buttonOKIsPressed = false;
		}
	}
}