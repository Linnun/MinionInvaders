package net.danielfigge.minioninvaders.entities.missiles.sona;

import java.util.ArrayList;

import net.danielfigge.minioninvaders.core.Sprite;
import net.danielfigge.minioninvaders.core.states.GameState;
import net.danielfigge.minioninvaders.entities.Entity;
import net.danielfigge.minioninvaders.entities.creatures.CreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.EnemyCreatureEntity;
import net.danielfigge.minioninvaders.entities.creatures.player.SonaEntity;
import net.danielfigge.minioninvaders.entities.missiles.MissileEntity;
import net.danielfigge.minioninvaders.util.Config;
import net.danielfigge.minioninvaders.util.ResourceManager;


final public class SonaShotEntity extends MissileEntity {
	private Sprite[] frames = new Sprite[2];
	private long lastFrameChange;
	private long frameDuration = 200;
	private int frameNumber;
	private boolean used = false;
	private boolean powerChord = false;
	private char powerChordType;

	public SonaShotEntity(SonaEntity parent, GameState game, int x, int y) {
		super(parent, game, "missiles/sona/shot", x, y);
		frames[0] = this.sprite;
		frames[1] = ResourceManager.get().getSprite("missiles/sona/shot2");
		moveSpeed = -Math.min(
				(Config.get(Config.sonaShotProjectileSpeed) + (Config.get(Config.sonaShotProjectileSpeedPerLevel) * game.getLevel())),
				Config.get(Config.sonaShotProjectileSpeedCap));
		dy = moveSpeed;
	}

	public boolean isPowerChord() {
		return powerChord;
	}

	public char getPowerChordType() {
		return this.powerChordType;
	}

	public void setPowerChord(char powerChordType) {
		powerChord = true;
		this.powerChordType = powerChordType;
		this.sprite = ResourceManager.get().getSprite("missiles/sona/powerchord" + powerChordType);
		frames[0] = this.sprite;
		frames[1] = ResourceManager.get().getSprite("missiles/sona/powerchord" + powerChordType + "2");
	}

	@Override
	public void move(long delta) {
		super.move(delta);
		lastFrameChange += delta;
		if (lastFrameChange > frameDuration) {
			lastFrameChange = 0;
			frameNumber++;
			if (frameNumber >= frames.length) {
				frameNumber = 0;
			}
			sprite = frames[frameNumber];
		}
	}

	@Override
	public void collidedWith(Entity other) {
		if (used) {
			return;
		}
		if (other instanceof EnemyCreatureEntity) {
			game.removeEntity(this);
			((CreatureEntity) other).damage(this, Config.get(Config.sonaShotDamage));
			if (powerChord) {
				switch (powerChordType) {
				case 'Q':
					((CreatureEntity) other).damage(this, Config.get(Config.sonaPowerChordQDamage));
					break;
				case 'W':
					parent.addEffect("bufferDiminuendo", game.getTime() + Config.get(Config.sonaPowerChordWDuration));
					break;
				case 'E':
					long time = game.getTime();
					ArrayList<Entity> entities = game.getEntities();
					for (int i = 0; i < entities.size(); i++) {
						Entity entity = (Entity) entities.get(i);
						if (entity instanceof EnemyCreatureEntity) {
							((CreatureEntity) entity).addMoveSpeedMultiplier(Config.get(Config.sonaPowerChordEMultiplier),
									time + Config.get(Config.sonaPowerChordEDuration));
						}
					}
					((SonaEntity) parent).setLastPowerChordETime(time);
					break;
				}
				ResourceManager.get().playSound("sona_powerchord_hit");
			} else {
				ResourceManager.get().playSound("sona_shot_hit");
			}
			used = true;
		}
	}
}