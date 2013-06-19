package net.danielfigge.minioninvaders.entities.creatures.player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Random;

import net.danielfigge.minioninvaders.core.GameWindow;
import net.danielfigge.minioninvaders.core.Sprite;
import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.EnemyCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.PlayerCreatureEntity;
import net.danielfigge.minioninvaders.entities.missiles.sona.SonaAbilityQEntity;
import net.danielfigge.minioninvaders.entities.missiles.sona.SonaAbilityREntity;
import net.danielfigge.minioninvaders.entities.missiles.sona.SonaShotEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class SonaEntity extends PlayerCreatureEntity {

	private char lastQWEAbility;
	private int powerChordCount;
	private long lastPowerChordETime, lastAbilityQWETime, lastNoteAnimationTime;
	private Sprite abilityWParticle, abilityWParticle2, abilityEParticle, abilityEParticle2;
	private int noteX1, noteX2, noteX3, noteY1, noteY2, noteY3;
	private Random random;

	public SonaEntity(GameState game, int x, int y) {
		super(game, "creatures/sona", x, y);
		entityIcon = ResourceManager.get().getSprite("icons/sona/creature");
		passiveIconActive = ResourceManager.get().getSprite("icons/sona/passiveActive");
		abilityQIconActive = ResourceManager.get().getSprite("icons/sona/abilityQActive");
		abilityWIconActive = ResourceManager.get().getSprite("icons/sona/abilityWActive");
		abilityEIconActive = ResourceManager.get().getSprite("icons/sona/abilityEActive");
		abilityRIconActive = ResourceManager.get().getSprite("icons/sona/abilityRActive");
		abilityDIconActive = ResourceManager.get().getSprite("icons/sona/abilityDActive");
		abilityFIconActive = ResourceManager.get().getSprite("icons/sona/abilityFActive");
		passiveIconPassive = ResourceManager.get().getSprite("icons/sona/passivePassive");
		abilityQIconPassive = ResourceManager.get().getSprite("icons/sona/abilityQPassive");
		abilityWIconPassive = ResourceManager.get().getSprite("icons/sona/abilityWPassive");
		abilityEIconPassive = ResourceManager.get().getSprite("icons/sona/abilityEPassive");
		abilityRIconPassive = ResourceManager.get().getSprite("icons/sona/abilityRPassive");
		abilityDIconPassive = ResourceManager.get().getSprite("icons/sona/abilityDPassive");
		abilityFIconPassive = ResourceManager.get().getSprite("icons/sona/abilityFPassive");
		passiveIcon = passiveIconPassive;
		abilityQIcon = abilityQIconActive;
		abilityWIcon = abilityWIconActive;
		abilityEIcon = abilityEIconActive;
		abilityRIcon = abilityRIconActive;
		abilityDIcon = abilityDIconActive;
		abilityFIcon = abilityFIconActive;
		abilityWParticle = ResourceManager.get().getSprite("particles/sona_abilityW");
		abilityWParticle2 = ResourceManager.get().getSprite("particles/sona_abilityW2");
		abilityEParticle = ResourceManager.get().getSprite("particles/sona_abilityE");
		abilityEParticle2 = ResourceManager.get().getSprite("particles/sona_abilityE2");
	}

	@Override
	protected void set() {
		super.set();
		random = new Random();
		lastPowerChordETime = lastAbilityQWETime = lastNoteAnimationTime = 0;
		powerChordCount = 0;
		setHitpoints(Config.get(Config.sonaHitpoints) + (Config.get(Config.sonaHitpointsPerLevel) * game.getLevel()));
		setMoveSpeed(Config.get(Config.sonaMoveSpeed));
		shotCooldown = Math.max(Config.get(Config.sonaShotCooldown) - (Config.get(Config.sonaShotCooldownPerLevel) * game.getLevel()),
				Config.get(Config.sonaShotCooldownCap));
		abilityQCooldown = Math.max(
				Config.get(Config.sonaAbilityQCooldown) - (Config.get(Config.sonaAbilityQCooldownPerLevel) * game.getLevel()),
				Config.get(Config.sonaAbilityQWECooldown));
		abilityWCooldown = Math.max(Config.get(Config.sonaAbilityWCooldown), Config.get(Config.sonaAbilityQWECooldown));
		abilityECooldown = Math.max(Config.get(Config.sonaAbilityECooldown), Config.get(Config.sonaAbilityQWECooldown));
		abilityRCooldown = Math.max(
				Config.get(Config.sonaAbilityRCooldown) - (Config.get(Config.sonaAbilityRCooldownPerLevel)) * game.getLevel(),
				Config.get(Config.sonaAbilityRCooldownCap));
		abilityDCooldown = Config.get(Config.sonaAbilityDCooldown);
		abilityFCooldown = Config.get(Config.sonaAbilityFCooldown);
	}

	public boolean isPowerChordWActive() {
		return (getPowerChordWTime() > 0);
	}

	public boolean isPowerChordEActive() {
		return (getPowerChordETime() > 0);
	}

	public long getPowerChordWTime() {
		if (!hasEffect("bufferDiminuendo")) {
			return 0;
		}
		return getEffectDuration("bufferDiminuendo") - game.getTime();
	}

	public long getPowerChordETime() {
		return Math.max(lastPowerChordETime + Config.get(Config.sonaAbilityEDuration) - game.getTime(), 0);
	}

	public void setLastPowerChordETime(long time) {
		lastPowerChordETime = time;
	}

	@Override
	public void damage(Entity entity, int damage) {
		if (hasEffect("bufferDiminuendo")) {
			damage *= Config.get(Config.sonaPowerChordWMultiplier);
		}
		super.damage(entity, damage);
	}

	@Override
	public void move(long delta) {
		super.move(delta);
		if (!canMove()) {
			return;
		}
		if ((dx < 0) && (x <= 0)) {
			x = 0;
		}
		if ((dx > 0) && (x >= ResourceManager.get().getDimension().getWidth() - getW())) {
			x = ResourceManager.get().getDimension().getWidth() - getW();
		}
	}

	@Override
	public void notifyDeath() {
		ResourceManager.get().playSound("sona_die", true);
		game.playerDead();
	}

	@Override
	protected void shot() {
		SonaShotEntity shot = new SonaShotEntity(this, game, (int) x + (getW() / 2) - 5, (int) y + (getH() / 2) - 30);
		if (powerChordCount >= 3) {
			shot.setPowerChord(lastQWEAbility);
			powerChordCount = 0;
			ResourceManager.get().playSound("sona_powerchord");
		} else {
			ResourceManager.get().playSound("sona_shot");
		}
		game.addEntity(shot);
	}

	@Override
	protected void abilityQ() {
		lastAbilityQWETime = game.getTime();
		SonaAbilityQEntity shot;
		shot = new SonaAbilityQEntity(this, game, (int) x + (getW() / 2) - 6 - 20, (int) y + (getH() / 2) - 23);
		game.addEntity(shot);
		shot = new SonaAbilityQEntity(this, game, (int) x + (getW() / 2) - 6 + 20, (int) y + (getH() / 2) - 23);
		game.addEntity(shot);
		powerChordCount++;
		if (powerChordCount == 3) {
			lastShotTime = 0;
		}
		lastQWEAbility = 'Q';
		ResourceManager.get().playSound("sona_abilityQ");
	}

	@Override
	protected void abilityW() {
		lastAbilityQWETime = game.getTime();
		heal(Config.get(Config.sonaAbilityWHealAmount) + (Config.get(Config.sonaAbilityWHealAmountPerLevel) * game.getLevel()));
		powerChordCount++;
		if (powerChordCount == 3) {
			lastShotTime = 0;
		}
		lastQWEAbility = 'W';
		ResourceManager.get().playSound("sona_abilityW");
	}

	@Override
	protected void abilityE() {
		lastAbilityQWETime = game.getTime();
		addMoveSpeedMultiplier(
				Config.get(Config.sonaAbilityEMoveSpeedMultiplier)
						+ (Config.get(Config.sonaAbilityEMoveSpeedMultiplierPerLevel) * game.getLevel()),
				lastAbilityQWETime + Config.get(Config.sonaAbilityEDuration));
		refreshMoveSpeed();
		powerChordCount++;
		if (powerChordCount == 3) {
			lastShotTime = 0;
		}
		lastQWEAbility = 'E';
		ResourceManager.get().playSound("sona_abilityE");
	}

	@Override
	protected void abilityR() {
		SonaAbilityREntity shot = new SonaAbilityREntity(this, game, (int) x + (getW() / 2) - 32, (int) y + (getH() / 2) - 69);
		game.addEntity(shot);
		ResourceManager.get().playSound("sona_abilityR");
	}

	@Override
	protected void abilityD() {
		heal(Config.get(Config.sonaAbilityDHealAmount) + (Config.get(Config.sonaAbilityDHealAmountPerLevel) * game.getLevel()));
		addEffect("healHeal", game.getTime() + 1500);
		ResourceManager.get().playSound("sona_abilityD");
	}

	@Override
	protected void abilityF() {
		if ((isStunned()) || (dx == 0) || (x == 0) || (x == ResourceManager.get().getDimension().getWidth() - getW())) {
			lastAbilityFTime = 0;
			return;
		}

		if (dx < 0) {
			x -= Config.get(Config.sonaAbilityFDistance);
		} else if (dx > 0) {
			x += Config.get(Config.sonaAbilityFDistance);
		}

		if (x < 0) {
			x = 0;
		} else if (x > ResourceManager.get().getDimension().getWidth() - getW()) {
			x = ResourceManager.get().getDimension().getWidth() - getW();
		}
		ResourceManager.get().playSound("sona_abilityF");
	}

	@Override
	public long getAbilityQCooldown() {
		return Math.max(super.getAbilityQCooldown(),
				Math.max(lastAbilityQWETime + Config.get(Config.sonaAbilityQWECooldown) - game.getTime(), 0));
	}

	@Override
	public long getAbilityWCooldown() {
		return Math.max(super.getAbilityWCooldown(),
				Math.max(lastAbilityQWETime + Config.get(Config.sonaAbilityQWECooldown) - game.getTime(), 0));
	}

	@Override
	public long getAbilityECooldown() {
		return Math.max(super.getAbilityECooldown(),
				Math.max(lastAbilityQWETime + Config.get(Config.sonaAbilityQWECooldown) - game.getTime(), 0));
	}

	@Override
	public void draw() {
		super.draw();
		if (isPowerChordWActive()) {
			ResourceManager
					.get()
					.getGameWindow()
					.drawText(GameWindow.FONT_SMALL, (int) x + (getW() / 2), (int) y - 20,
							"Diminuendo: " + String.valueOf(Math.ceil(getPowerChordWTime() / 1000.0 * 10.0) / 10.0) + "s",
							GameWindow.ALIGN_CENTER);
		}
		if (isPowerChordEActive()) {
			ResourceManager
					.get()
					.getGameWindow()
					.drawText(GameWindow.FONT_SMALL, (int) x + (getW() / 2), (int) y - ((isPowerChordWActive()) ? 35 : 20),
							"Tempo: " + String.valueOf(Math.ceil(getPowerChordETime() / 1000.0 * 10.0) / 10.0) + "s",
							GameWindow.ALIGN_CENTER);
		}
		if (lastAbilityWTime + 1000 - game.getTime() > 0) {
			if ((lastNoteAnimationTime == 0) || (lastNoteAnimationTime + 100 < game.getTime())) {
				lastNoteAnimationTime = game.getTime();
				noteX1 = random.nextInt(10);
				noteX2 = random.nextInt(10);
				noteX3 = random.nextInt(10);
				noteY1 = random.nextInt(10);
				noteY2 = random.nextInt(10);
				noteY3 = random.nextInt(10);
			}
			abilityWParticle.draw((int) x + (getW() / 2) - 0 - 30 - noteX1, (int) y - 48 - noteY1);
			abilityWParticle.draw((int) x + (getW() / 2) - 8 - 50 - noteX2, (int) y - 0 - noteY2);
			abilityWParticle2.draw((int) x + (getW() / 2) - 8 + 50 + noteX3, (int) y - 16 - noteY3);
		}
		if (isAbilityEActive()) {
			if ((lastNoteAnimationTime == 0) || (lastNoteAnimationTime + 100 < game.getTime())) {
				lastNoteAnimationTime = game.getTime();
				noteX1 = random.nextInt(10);
				noteX2 = random.nextInt(10);
				noteX3 = random.nextInt(10);
				noteY1 = random.nextInt(10);
				noteY2 = random.nextInt(10);
				noteY3 = random.nextInt(10);
			}
			abilityEParticle.draw((int) x + (getW() / 2) - 0 - 30 - noteX1, (int) y - 48 - noteY1);
			abilityEParticle.draw((int) x + (getW() / 2) - 8 - 50 - noteX2, (int) y - 0 - noteY2);
			abilityEParticle2.draw((int) x + (getW() / 2) - 8 + 50 + noteX3, (int) y - 16 - noteY3);
		}
	}

	@Override
	protected void calculateHitbox() {
		hitbox.setBounds((int) x + 12, (int) y + 3, getW() - 24, getH() - 25);
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof EnemyCreatureEntity) {
			notifyDeath();
		}
	}

	public boolean isAbilityEActive() {
		return (lastAbilityETime + Config.get(Config.sonaAbilityEDuration) - game.getTime() > 0);
	}

	@Override
	public void drawPauseMenuInfo() {
		GameWindow window = ResourceManager.get().getGameWindow();
		DecimalFormat f = new DecimalFormat("##0.0##");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		f.setDecimalFormatSymbols(dfs);

		passiveIconActive.draw(10, 70);
		abilityQIconActive.draw(10, 150);
		abilityWIconActive.draw(10, 210);
		abilityEIconActive.draw(10, 290);
		abilityRIconActive.draw(10, 360);
		abilityDIconActive.draw(10, 440);
		abilityFIconActive.draw(10, 520);
		window.drawText(
				GameWindow.FONT_SMALL,
				50,
				55,
				"Power Chord (Passive)\nAfter casting "
						+ Config.get(Config.sonaAbilitiesPerPowerChord)
						+ " spells, Sona's next attack has an additional effect depending on which spell was cast last.\nHer spells \"Hymn of Valor\", \"Aria of Perseverance\" and \"Song of Cerlerity\" will also set each other on cooldown for "
						+ f.format(Config.get(Config.sonaAbilityQWECooldown) / 1000.0) + "s.");
		window.drawText(
				GameWindow.FONT_SMALL,
				50,
				125,
				"Hymn of Valor (Q) - Cooldown: "
						+ f.format(Config.get(Config.sonaAbilityQCooldown) / 1000.0)
						+ "s - "
						+ f.format(Config.get(Config.sonaAbilityQCooldownPerLevel) / 1000.0)
						+ "s per Level (currently: "
						+ f.format(Math.max(
								(Config.get(Config.sonaAbilityQCooldown) - (Config.get(Config.sonaAbilityQCooldownPerLevel) * game
										.getLevel())), Config.get(Config.sonaAbilityQWECooldown)) / 1000.0)
						+ "s) (This cooldown can not go below "
						+ f.format(Config.get(Config.sonaAbilityQWECooldown) / 1000.0)
						+ "s!)\nSona sends out two bolts of sound, dealing "
						+ Config.get(Config.sonaAbilityQDamage)
						+ " + "
						+ Config.get(Config.sonaAbilityQDamagePerLevel)
						+ " per level (currently: "
						+ (Config.get(Config.sonaAbilityQDamage) + (Config.get(Config.sonaAbilityQDamagePerLevel) * game.getLevel()))
						+ ") damage to the first enemies they hit.\n(Power Chord) - Staccato: If this spell was last cast when Sona's Power Chord is read, her next attack will deal "
						+ Config.get(Config.sonaPowerChordQDamage) + " additional damage.");
		window.drawText(
				GameWindow.FONT_SMALL,
				50,
				195,
				"Aria of Perseverance (W) - Cooldown: "
						+ f.format(Config.get(Config.sonaAbilityWCooldown) / 1000.0)
						+ "s\nSona sends out healing melodies, healing herself by "
						+ Config.get(Config.sonaAbilityWHealAmount)
						+ " + "
						+ Config.get(Config.sonaAbilityWHealAmountPerLevel)
						+ " per level (currently: "
						+ (Config.get(Config.sonaAbilityWHealAmount) + (Config.get(Config.sonaAbilityWHealAmountPerLevel) * game.getLevel()))
						+ ").\n(Power Chord) - Diminuendo: If this spell was last cast when Sona's Power Chord is ready, Sona will receive "
						+ f.format(100 - (100 * Config.get(Config.sonaPowerChordWMultiplier))) + "% less damage\nfor "
						+ f.format(Config.get(Config.sonaPowerChordWDuration) / 1000.0) + " seconds.");
		window.drawText(
				GameWindow.FONT_SMALL,
				50,
				265,
				"Song of Celerity (E) - Cooldown: "
						+ f.format(Config.get(Config.sonaAbilityECooldown) / 1000.0)
						+ "s\nSona energizes herself with an burst of speed of "
						+ f.format((Config.get(Config.sonaAbilityEMoveSpeedMultiplier) * 100) - 100)
						+ "% + "
						+ f.format(Config.get(Config.sonaAbilityEMoveSpeedMultiplierPerLevel) * 100)
						+ "% per level (currently: "
						+ f.format((Config.get(Config.sonaAbilityEMoveSpeedMultiplier) * 100)
								+ (Config.get(Config.sonaAbilityEMoveSpeedMultiplierPerLevel) * 100 * game.getLevel()) - 100)
						+ "%) for "
						+ f.format(Config.get(Config.sonaAbilityEDuration) / 1000.0)
						+ " seconds."
						+ "\n(Power Chord) - Tempo: If this spell was last cast when Sona's Power Chord is ready, her next attack will slow all enemies by "
						+ f.format(100 - (100 * Config.get(Config.sonaPowerChordEMultiplier))) + "%\nfor "
						+ f.format(Config.get(Config.sonaPowerChordEDuration) / 1000.0) + " seconds.");
		window.drawText(
				GameWindow.FONT_SMALL,
				50,
				345,
				"Crescendo (R) - Cooldown: "
						+ f.format(Config.get(Config.sonaAbilityRCooldown) / 1000.0)
						+ "s - "
						+ f.format(Config.get(Config.sonaAbilityRCooldownPerLevel) / 1000.0)
						+ "s per Level (currently: "
						+ f.format(Math.max(
								(Config.get(Config.sonaAbilityRCooldown) - (Config.get(Config.sonaAbilityRCooldownPerLevel) * game
										.getLevel())), Config.get(Config.sonaAbilityRCooldownCap)) / 1000.0)
						+ "s) (This cooldown can not go below "
						+ f.format(Config.get(Config.sonaAbilityRCooldownCap) / 1000.0)
						+ "s!)\nSona plays her ultimate chord, dealing "
						+ Config.get(Config.sonaAbilityRDamage)
						+ " + "
						+ Config.get(Config.sonaAbilityRDamagePerLevel)
						+ " per level (currently: "
						+ (Config.get(Config.sonaAbilityRDamage) + (Config.get(Config.sonaAbilityRDamagePerLevel) * game.getLevel()))
						+ ") damage to all enemies it hits and stunning them and all minions\nfor "
						+ f.format(Config.get(Config.sonaAbilityRStunDuration) / 1000.0)
						+ "s + "
						+ f.format(Config.get(Config.sonaAbilityRStunDurationPerLevel) / 1000.0)
						+ "s per level (currently: "
						+ f.format(Math.min((Config.get(Config.sonaAbilityRStunDuration) + (Config
								.get(Config.sonaAbilityRStunDurationPerLevel) * game.getLevel())), Config
								.get(Config.sonaAbilityRStunDurationCap)) / 1000.0) + " seconds). (This stun duration can not go above "
						+ f.format(Config.get(Config.sonaAbilityRStunDurationCap) / 1000.0) + "s!)");
		window.drawText(
				GameWindow.FONT_SMALL,
				50,
				425,
				"Heal (D) - Cooldown: "
						+ f.format(Config.get(Config.sonaAbilityDCooldown) / 1000.0)
						+ "s\nSona instantly restores "
						+ Config.get(Config.sonaAbilityDHealAmount)
						+ " + "
						+ Config.get(Config.sonaAbilityDHealAmountPerLevel)
						+ " per level (currently: "
						+ (Config.get(Config.sonaAbilityDHealAmount) + (Config.get(Config.sonaAbilityDHealAmountPerLevel) * game.getLevel()))
						+ ") health.");
		window.drawText(
				GameWindow.FONT_SMALL,
				50,
				505,
				"Flash (F) - Cooldown: "
						+ f.format(Config.get(Config.sonaAbilityFCooldown) / 1000.0)
						+ "s\nSona teleports to a nearby location into the direction she is currently moving. (This ability can only be used while Sona is moving!)");
	}

	@Override
	public void drawIcons() {
		GameWindow window = ResourceManager.get().getGameWindow();
		passiveIcon = (powerChordCount >= 3) ? passiveIconActive : passiveIconPassive;
		passiveIcon.draw(187, 560);
		if ((powerChordCount > 0) && (powerChordCount < Config.get(Config.sonaAbilitiesPerPowerChord))) {
			window.drawText(GameWindow.FONT_SMALL, 203, 586, "(" + powerChordCount + ")", GameWindow.ALIGN_CENTER);
		}

		abilityQIcon = (isAbilityQOnCooldown()) ? abilityQIconPassive : abilityQIconActive;
		abilityQIcon.draw(222, 560);
		window.drawText(GameWindow.FONT_SMALL, 238, 586,
				((isAbilityQOnCooldown()) ? String.valueOf(Math.ceil(getAbilityQCooldown() / 1000.0 * 10.0) / 10.0) + "s" : "Q"),
				GameWindow.ALIGN_CENTER);

		abilityWIcon = (isAbilityWOnCooldown()) ? abilityWIconPassive : abilityWIconActive;
		abilityWIcon.draw(257, 560);
		window.drawText(GameWindow.FONT_SMALL, 273, 586,
				((isAbilityWOnCooldown()) ? String.valueOf(Math.ceil(getAbilityWCooldown() / 1000.0 * 10.0) / 10.0) + "s" : "W"),
				GameWindow.ALIGN_CENTER);

		abilityEIcon = (isAbilityEOnCooldown()) ? abilityEIconPassive : abilityEIconActive;
		abilityEIcon.draw(292, 560);
		window.drawText(GameWindow.FONT_SMALL, 309, 586,
				((isAbilityEOnCooldown()) ? String.valueOf(Math.ceil(getAbilityECooldown() / 1000.0 * 10.0) / 10.0) + "s" : "E"),
				GameWindow.ALIGN_CENTER);

		abilityRIcon = (isAbilityROnCooldown()) ? abilityRIconPassive : abilityRIconActive;
		abilityRIcon.draw(327, 560);
		window.drawText(GameWindow.FONT_SMALL, 344, 586,
				((isAbilityROnCooldown()) ? String.valueOf(Math.ceil(getAbilityRCooldown() / 1000.0 * 10.0) / 10.0) + "s" : "R"),
				GameWindow.ALIGN_CENTER);

		abilityDIcon = (isAbilityDOnCooldown()) ? abilityDIconPassive : abilityDIconActive;
		abilityDIcon.draw(377, 560);
		window.drawText(GameWindow.FONT_SMALL, 394, 586,
				((isAbilityDOnCooldown()) ? String.valueOf(Math.ceil(getAbilityDCooldown() / 1000.0 * 10.0) / 10.0) + "s" : "D"),
				GameWindow.ALIGN_CENTER);

		abilityFIcon = (isAbilityFOnCooldown()) ? abilityFIconPassive : abilityFIconActive;
		abilityFIcon.draw(412, 560);
		window.drawText(GameWindow.FONT_SMALL, 429, 586,
				((isAbilityFOnCooldown()) ? String.valueOf(Math.ceil(getAbilityFCooldown() / 1000.0 * 10.0) / 10.0) + "s" : "F"),
				GameWindow.ALIGN_CENTER);
	}
}