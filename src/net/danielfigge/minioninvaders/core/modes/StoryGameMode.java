package net.danielfigge.minioninvaders.core.modes;

import net.danielfigge.minioninvaders.core.GameWindow;
import net.danielfigge.minioninvaders.core.Sprite;
import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.core.states.GameState.message;
import net.danielfigge.minioninvaders.entities.creatures.enemies.LuxEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.MinionEntity;
import net.danielfigge.minioninvaders.entities.creatures.enemies.SivirEntity;
import net.danielfigge.minioninvaders.entities.missiles.misc.EzrealUltiEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;

public class StoryGameMode extends SurvivalGameMode {

	private LuxEntity lux = null;
	private SivirEntity sivir = null;
	private boolean ezrealUltiLevel, sorakaUltiLevel, luxLevel, sivirLevel;
	private boolean ezrealUltiDone = false, sorakaUltiDone = false;
	private long sorakaUltiStartTime = 0;
	private Sprite luxSprite, arrowDown;

	public StoryGameMode(GameState gamestate) {
		super(gamestate);

		arrowDown = ResourceManager.get().getSprite("interface/arrow_down");
		luxSprite = ResourceManager.get().getSprite("creatures/lux");
	}

	public void startScreen() {
		window.drawText(
				GameWindow.FONT_MENU,
				width / 2,
				120,
				"Lux keeps laughing about Sona because\nshe is a weak supporter...\nHelp Sona to level up to 30\nto be strong enough to defeat Lux\nin a 1v1 duel of mages!",
				GameWindow.ALIGN_CENTER);
		luxSprite.draw((width / 2) - (luxSprite.getWidth() / 2), 320);
	}

	private void prepareLuxEntity() {
		// if (lux == null) {
		lux = new LuxEntity(gamestate, 375, 50);
		// } else {
		// lux.reset();
		// }
		if (!gamestate.getEntities().contains(lux)) {
			gamestate.addEntity(lux);
		}
	}

	private void prepareSivirEntity() {
		// if (sivir == null) {
		sivir = new SivirEntity(gamestate, 375, 50);
		// } else {
		// sivir.reset();
		// }
		if (!gamestate.getEntities().contains(sivir)) {
			gamestate.addEntity(sivir);
		}
	}

	@Override
	public void startLevel() {
		level = gamestate.getLevel();
		if (level == 20) {
			ezrealUltiLevel = true;
		} else {
			ezrealUltiLevel = false;
		}
		if (level == 30) {
			sorakaUltiLevel = true;
		} else {
			sorakaUltiLevel = false;
		}
		if (level == 31) {
			sivirLevel = true;
		} else {
			sivirLevel = false;
		}
		if ((level == 30) || (level == 31)) {
			luxLevel = true;
		} else {
			luxLevel = false;
		}
		if (luxLevel) {
			prepareLuxEntity();
		} else {
			lux = null;
		}
		if (sivirLevel) {
			prepareSivirEntity();
		} else {
			sivir = null;
		}
		if (ezrealUltiLevel) {
			ezrealUltiDone = false;
		}
		if (sorakaUltiLevel) {
			sorakaUltiDone = false;
		}
		sorakaUltiStartTime = 0;
		if ((!luxLevel) && (!sivirLevel)) {
			buildMinions();
			if (ezrealUltiLevel) {
				for (int row = Math.min(Config.get(Config.minionRows) + (int) (Config.get(Config.minionRowsPerLevel) * level),
						Config.get(Config.minionRowsCap)); row < Math.min(
						Config.get(Config.minionRows) + (int) (Config.get(Config.minionRowsPerLevel) * level),
						Config.get(Config.minionRowsCap))
						+ Config.get(Config.minionEzrealExtraRows); row++) {
					for (int x = 0; x < Math.min(Config.get(Config.minionCols) + (int) (Config.get(Config.minionColsPerLevel) * level),
							Config.get(Config.minionColsCap)); x++) {
						MinionEntity minion = new MinionEntity(gamestate, 100 + (x * 49), 10 + row * 30);
						minion.setCanShoot(false);
						minion.setSpeedUp(false);
						gamestate.addEntity(minion);
						if (random.nextInt(Config.get(Config.superminionAppearChance)
								+ (Config.get(Config.superminionAppearChancePerLevel) * level)) > 100000) {
							minion.makeSuperminion();
						}
					}
				}
			}
		}
	}

	@Override
	public void render(long delta) {
		if ((!luxLevel) && (!sivirLevel)) {
			drawEnemyCount();
		}

		if (!gamestate.isWaitingForKeyPress()) {
			if ((!ezrealUltiDone) && (ezrealUltiLevel) && (gamestate.getLevelStartTime() + 2000 <= gamestate.getTime())) {
				ezrealUltiDone = true;
				EzrealUltiEntity ezulti = new EzrealUltiEntity(gamestate, -500, 240);
				gamestate.addEntity(ezulti);
				ResourceManager.get().playSound("ezreal_ulti");
			}

			if ((sorakaUltiLevel) && ((sorakaUltiStartTime == 0) || (sorakaUltiStartTime + 1000 > gamestate.getTime()))) {
				if ((!sorakaUltiDone) && (sorakaUltiStartTime == 0)
						&& (lux.getHitpoints() <= Config.get(Config.sorakaUltiLuxHealthActivation))) {
					sorakaUltiStartTime = gamestate.getTime();
					lux.heal(Config.get(Config.sorakaUltiHealAmount));
					lux.addEffect("healSorakaUlti", sorakaUltiStartTime + 1000);
					sorakaUltiDone = true;
					ResourceManager.get().playSound("soraka_ulti");
				}
			}

			switch (level) {
			case 1:
				window.drawText(GameWindow.FONT_MAIN, width / 2, 170,
						"You can move by using the arrow keys\nand fire autoattacks by pressing Space.", GameWindow.ALIGN_CENTER);
				window.drawText(GameWindow.FONT_MAIN, width - 10, 410, "Defeat all enemies in a level to reach the next level.",
						GameWindow.ALIGN_RIGHT);
				arrowDown.draw(590, 450);
				break;
			case 2:
				window.drawText(
						GameWindow.FONT_MAIN,
						width / 2,
						170,
						"Press Escape to pause the game and get an overview over your abilities.\n\nUse the Q, W, E and R keys to use them.",
						GameWindow.ALIGN_CENTER);
				window.drawText(GameWindow.FONT_MAIN, 320, 375,
						"You can look here to see if your abilities are ready.\nIf they are on cooldown, they're shown grey.",
						GameWindow.ALIGN_CENTER);
				arrowDown.draw(320, 450);
				break;
			case 3:
				window.drawText(
						GameWindow.FONT_MAIN,
						width / 2,
						170,
						"Your abilities will become stronger with every level\nbut you will also get more and stronger enemies with every level!",
						GameWindow.ALIGN_CENTER);
				window.drawText(GameWindow.FONT_MAIN, 10, 410, "These are your hitpoints. Make sure to keep an eye on them!");
				arrowDown.draw(145, 450);
				break;
			case 4:
				window.drawText(
						GameWindow.FONT_MAIN,
						width / 2,
						170,
						"That's pretty much all you need to know.\nUse your abilities wisely and you should succeed.\nDon't give up!\n\nNow level yourself up to defeat Lux!",
						GameWindow.ALIGN_CENTER);
				break;
			}
		}
	}

	@Override
	public void drawMessage() {
		if ((level == 30) && (gamestate.levelUpPending())) {
			window.drawText(
					GameWindow.FONT_MENU,
					width / 2,
					120,
					"Congratulations, you made it! \nYou leveled all the way up and\nin the end you were able to defeat Lux!\nNobody will call Sona weak anymore!\n\nBut wait.. Lux got Revive...\nand this time Sivir is rushing to help her!",
					GameWindow.ALIGN_CENTER);
		} else if ((level == 31) && (gamestate.levelUpPending())) {
			window.drawText(GameWindow.FONT_TITLE, width / 2, 20, "M i n i o n  I n v a d e r s", GameWindow.ALIGN_CENTER);
			window.drawText(GameWindow.FONT_SMALL, width - 10, 535, "A game by Daniel Figge", GameWindow.ALIGN_RIGHT);
			window.drawText(
					GameWindow.FONT_MENU,
					width / 2,
					120,
					"Wow, you were even able to survive that gank!\nYou are worth being considered a full mage now!\n\nThanks for playing!\n\nYou can press Space to continue\nthe game in survival mode.\nHow far can you get?",
					GameWindow.ALIGN_CENTER);
		} else {
			super.drawMessage();
		}
	}

	@Override
	public void playerDead() {
		if (gamestate.isWaitingForKeyPress()) {
			return;
		}
		gamestate.increaseDeathCount();
		message.text = "You lost!\nPress Space to retry this level";
		message.x = width / 2;
		message.y = 245;
		gamestate.setWaitingForKeyPress(true);
		if ((!ResourceManager.get().isSoundPlaying("lux_abilityR")) && (!ResourceManager.get().isSoundPlaying("lux_abilityR2"))
				&& (!ResourceManager.get().isSoundPlaying("lux_abilityR3")) && (!ResourceManager.get().isSoundPlaying("level_lose"))
				&& (!ResourceManager.get().isSoundPlaying("lux_win")) && (!ResourceManager.get().isSoundPlaying("lux_win2"))
				&& (!ResourceManager.get().isSoundPlaying("sivir_win"))) {
			if (sivirLevel) {
				ResourceManager.get().playSound("sivir_win");
			} else if (luxLevel) {
				ResourceManager.get().playSound((random.nextBoolean()) ? "lux_win" : "lux_win2");
			} else {
				ResourceManager.get().playSound("level_lose");
			}
		} else {
			ResourceManager.get().playSound("level_lose");
		}
	}
}
