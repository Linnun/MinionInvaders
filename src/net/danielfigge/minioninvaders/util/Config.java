package net.danielfigge.minioninvaders.util;

import net.danielfigge.minioninvaders.core.states.GameState;

public class Config {

	// General
	public static int[][] enemyAttackDelay = { { 500, 500, 500, 500 }, { 500, 500, 500, 500 }, { 500, 500, 500, 500 } };
	public static int[][] playerLives = { { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 3, 3, 3, 3 } };

	// Sona Base
	public static int[][] sonaHitpoints = { { 1144, 953, 862, 1 }, { 1144, 953, 862, 1 }, { 1, 1, 1, 1 } };
	public static int[][] sonaHitpointsPerLevel = { { 113, 94, 85, 0 }, { 113, 94, 85, 0 }, { 0, 0, 0, 0 } };
	public static int[][] sonaShotDamage = { { 61, 51, 41, 41 }, { 61, 51, 41, 41 }, { 61, 51, 41, 41 } };
	public static double[][] sonaMoveSpeed = { { 225, 200, 175, 175 }, { 225, 200, 175, 175 }, { 225, 200, 175, 175 } };
	public static int[][] sonaShotCooldown = { { 600, 650, 700, 700 }, { 600, 650, 700, 700 }, { 600, 650, 700, 700 } };
	public static int[][] sonaShotCooldownPerLevel = { { 4, 3, 3, 3 }, { 4, 3, 3, 3 }, { 4, 3, 3, 3 } };
	public static int[][] sonaShotCooldownCap = { { 333, 333, 333, 333 }, { 333, 333, 333, 333 }, { 333, 333, 333, 333 } };
	public static int[][] sonaShotProjectileSpeed = { { 450, 425, 400, 375 }, { 450, 425, 400, 375 }, { 450, 425, 400, 375 } };
	public static int[][] sonaShotProjectileSpeedPerLevel = { { 5, 5, 5, 5 }, { 5, 5, 5, 5 }, { 5, 5, 5, 5 } };
	public static int[][] sonaShotProjectileSpeedCap = { { 500, 500, 500, 500 }, { 500, 500, 500, 500 }, { 500, 500, 500, 500 } };

	// Sona Abilities
	public static int[][] sonaAbilitiesPerPowerChord = { { 3, 3, 3, 3 }, { 3, 3, 3, 3 }, { 3, 3, 3, 3 } };
	public static int[][] sonaAbilityQWECooldown = { { 300, 500, 800, 1000 }, { 300, 500, 800, 1000 }, { 300, 500, 800, 1000 } };
	public static int[][] sonaAbilityQDamage = { { 88, 73, 58, 58 }, { 88, 73, 58, 58 }, { 88, 73, 58, 58 } };
	public static int[][] sonaAbilityQDamagePerLevel = { { 6, 5, 4, 4 }, { 6, 5, 4, 4 }, { 6, 5, 4, 4 } };
	public static int[][] sonaAbilityQCooldown = { { 3000, 3000, 3000, 3000 }, { 3000, 3000, 3000, 3000 }, { 3000, 3000, 3000, 3000 } };
	public static int[][] sonaAbilityQCooldownPerLevel = { { 75, 75, 75, 75 }, { 75, 75, 75, 75 }, { 75, 75, 75, 75 } };
	public static int[][] sonaAbilityQProjectileSpeed = { { 600, 575, 550, 525 }, { 600, 575, 550, 525 }, { 600, 575, 550, 525 } };
	public static int[][] sonaAbilityQProjectileSpeedPerLevel = { { 7, 7, 7, 7 }, { 7, 7, 7, 7 }, { 7, 7, 7, 7 } };
	public static int[][] sonaAbilityQProjectileSpeedCap = { { 730, 730, 730, 730 }, { 730, 730, 730, 730 }, { 730, 730, 730, 730 } };
	public static int[][] sonaAbilityWHealAmount = { { 85, 71, 64, 64 }, { 85, 71, 64, 64 }, { 85, 71, 64, 64 } };
	public static int[][] sonaAbilityWHealAmountPerLevel = { { 44, 37, 35, 35 }, { 44, 37, 35, 35 }, { 44, 37, 35, 35 } };
	public static int[][] sonaAbilityWCooldown = { { 3000, 3000, 3000, 3000 }, { 3000, 3000, 3000, 3000 }, { 3000, 3000, 3000, 3000 } };
	public static int[][] sonaAbilityEDuration = { { 1800, 1500, 1200, 1200 }, { 1800, 1500, 1200, 1200 }, { 1800, 1500, 1200, 1200 } };
	public static double[][] sonaAbilityEMoveSpeedMultiplier = { { 1.35, 1.3, 1.25, 1.2 }, { 1.35, 1.3, 1.25, 1.2 },
			{ 1.35, 1.3, 1.25, 1.2 } };
	public static double[][] sonaAbilityEMoveSpeedMultiplierPerLevel = { { 0.03, 0.025, 0.02, 0.015 }, { 0.03, 0.025, 0.02, 0.015 },
			{ 0.03, 0.025, 0.02, 0.015 } };
	public static int[][] sonaAbilityECooldown = { { 3000, 3000, 3000, 3000 }, { 3000, 3000, 3000, 3000 }, { 3000, 3000, 3000, 3000 } };
	public static int[][] sonaAbilityRDamage = { { 424, 353, 298, 298 }, { 424, 353, 298, 298 }, { 424, 353, 298, 298 } };
	public static int[][] sonaAbilityRDamagePerLevel = { { 6, 4, 3, 3 }, { 6, 4, 3, 3 }, { 6, 4, 3, 3 } };
	public static int[][] sonaAbilityRStunDuration = { { 1200, 1000, 800, 800 }, { 1200, 1000, 800, 800 }, { 1200, 1000, 800, 800 } };
	public static int[][] sonaAbilityRStunDurationPerLevel = { { 85, 75, 70, 70 }, { 85, 75, 70, 70 }, { 85, 75, 70, 70 } };
	public static int[][] sonaAbilityRStunDurationCap = { { 3000, 3000, 3000, 3000 }, { 3000, 3000, 3000, 3000 },
			{ 3000, 3000, 3000, 3000 } };
	public static int[][] sonaAbilityRCooldown = { { 30000, 30000, 30000, 30000 }, { 30000, 30000, 30000, 30000 },
			{ 30000, 30000, 30000, 30000 } };
	public static int[][] sonaAbilityRCooldownPerLevel = { { 500, 500, 500, 500 }, { 500, 500, 500, 500 }, { 500, 500, 500, 500 } };
	public static int[][] sonaAbilityRCooldownCap = { { 15000, 15000, 15000, 15000 }, { 15000, 15000, 15000, 15000 },
			{ 15000, 15000, 15000, 15000 } };
	public static int[][] sonaAbilityRProjectileSpeed = { { 650, 625, 600, 575 }, { 650, 625, 600, 575 }, { 650, 625, 600, 575 } };
	public static int[][] sonaAbilityRProjectileSpeedPerLevel = { { 10, 10, 10, 10 }, { 10, 10, 10, 10 }, { 10, 10, 10, 10 } };
	public static int[][] sonaAbilityRProjectileSpeedCap = { { 1000, 1000, 1000, 1000 }, { 1000, 1000, 1000, 1000 },
			{ 1000, 1000, 1000, 1000 } };
	public static int[][] sonaAbilityDCooldown = { { 60000, 60000, 60000, 60000 }, { 60000, 60000, 60000, 60000 },
			{ 60000, 60000, 60000, 60000 } };
	public static int[][] sonaAbilityDHealAmount = { { 171, 171, 171, 171 }, { 171, 171, 171, 171 }, { 171, 171, 171, 171 } };
	public static int[][] sonaAbilityDHealAmountPerLevel = { { 62, 62, 62, 62 }, { 62, 62, 62, 62 }, { 62, 62, 62, 62 } };
	public static int[][] sonaAbilityFCooldown = { { 60000, 60000, 60000, 60000 }, { 60000, 60000, 60000, 60000 },
			{ 60000, 60000, 60000, 60000 } };
	public static int[][] sonaAbilityFDistance = { { 125, 125, 125, 125 }, { 125, 125, 125, 125 }, { 125, 125, 125, 125 } };

	// Sona PowerChord
	public static int[][] sonaPowerChordQDamage = sonaShotDamage;
	public static int[][] sonaPowerChordWDuration = { { 4500, 4000, 3500, 1500 }, { 4500, 4000, 3500, 1500 }, { 4500, 4000, 3500, 1500 } };
	public static double[][] sonaPowerChordWMultiplier = { { 0.75, 0.8, 0.85, 0 }, { 0.75, 0.8, 0.85, 0 }, { 0.75, 0.8, 0.85, 0 } };
	public static int[][] sonaPowerChordEDuration = { { 2500, 2000, 1500, 1500 }, { 2500, 2000, 1500, 1500 }, { 2500, 2000, 1500, 1500 } };
	public static double[][] sonaPowerChordEMultiplier = { { 0.5, 0.6, 0.7, 0.7 }, { 0.5, 0.6, 0.7, 0.7 }, { 0.5, 0.6, 0.7, 0.7 } };

	// Minions
	public static int[][] minionHitpoints = { { 35, 35, 35, 35 }, { 35, 35, 35, 35 }, { 35, 35, 35, 35 } };
	public static int[][] minionShotDamage = { { 79, 84, 89, 1 }, { 79, 84, 89, 1 }, { 1, 1, 1, 1 } };
	public static int[][] minionShotDamagePerLevel = { { 35, 43, 44, 0 }, { 35, 43, 44, 0 }, { 0, 0, 0, 0 } };
	public static int[][] minionShotCooldown = { { 500, 500, 500, 500 }, { 500, 500, 500, 500 }, { 500, 500, 500, 500 } };
	public static double[][] minionMoveSpeed = { { 48, 48, 48, 48 }, { 48, 48, 48, 48 }, { 300, 300, 300, 300 } };
	public static double[][] minionMoveSpeedPerLevel = { { 1, 2, 3, 3 }, { 1, 2, 3, 3 }, { 1, 2, 3, 3 } };
	public static double[][] minionMovespeedUpPerMinionKill = { { 1.0125, 1.015, 1.0175, 1.0175 }, { 1.0125, 1.015, 1.0175, 1.0175 },
			{ 1.0125, 1.015, 1.0175, 1.0175 } };
	public static int[][] minionMoveSpeedCap = { { 330, 350, 370, 400 }, { 330, 350, 370, 400 }, { 330, 350, 370, 400 } };
	public static int[][] minionShotChance = { { 100025, 100050, 100055, 100050 }, { 100025, 100050, 100055, 100050 }, { 1, 1, 1, 1 } };
	public static double[][] minionShotChancePerLevel = { { 3, 5, 5.5, 5 }, { 3, 5, 5.5, 5 }, { 1, 1, 1, 1 } };
	public static int[][] minionShotProjectileSpeed = { { 150, 175, 200, 200 }, { 150, 175, 200, 200 }, { 150, 150, 150, 150 } };
	public static int[][] minionShotProjectileSpeedPerLevel = { { 5, 5, 5, 5 }, { 5, 5, 5, 5 }, { 5, 5, 5, 5 } };
	public static int[][] minionShotProjectileSpeedCap = { { 400, 400, 400, 400 }, { 400, 400, 400, 400 }, { 400, 400, 400, 400 } };
	public static int[][] minionPxDownPerRowPassed = { { 3, 4, 4, 4 }, { 3, 4, 4, 4 }, { 0, 0, 0, 0 } };
	public static int[][] minionRows = { { 1, 1, 2, 2 }, { 1, 1, 2, 2 }, { 0, 0, 0, 0 } };
	public static float[][] minionRowsPerLevel = { { 1 / 3.5f, 1 / 3f, 1 / 3f, 1 / 3f }, { 1 / 3.5f, 1 / 3f, 1 / 3f, 1 / 3f },
			{ 0, 0, 0, 0 } };
	public static int[][] minionRowsCap = { { 8, 10, 11, 11 }, { 8, 10, 11, 11 }, { 0, 0, 0, 0 } };
	public static int[][] minionCols = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
	public static float[][] minionColsPerLevel = { { 1f, 1f, 1f, 1f }, { 1f, 1f, 1f, 1f }, { 0, 0, 0, 0 } };
	public static int[][] minionColsCap = { { 13, 14, 14, 14 }, { 13, 14, 14, 14 }, { 0, 0, 0, 0 } };
	public static int[][] minionEzrealExtraRows = { { 5, 5, 5, 5 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
	public static int[][] minionShotAngleDivisor = { { 9, 9, 9, 10 }, { 9, 9, 9, 10 }, { 1, 1, 1, 1 } };

	// Superminions
	public static int[][] superminionHitpoints = { { 1, 70, 70, 70 }, { 1, 70, 70, 70 }, { 1, 70, 70, 70 } };
	public static double[][] superminionDamageMultiplier = { { 1, 1.5, 1.5, 1 }, { 1, 1.5, 1.5, 1 }, { 1, 1, 1, 1 } };
	public static int[][] superminionShotCooldown = { { 500, 500, 500, 500 }, { 500, 500, 500, 500 }, { 500, 500, 500, 500 } };
	public static int[][] superminionShotChance = { { 1, 100100, 100110, 100100 }, { 1, 100100, 100110, 100100 }, { 1, 1, 1, 1 } };
	public static double[][] superminionShotChancePerLevel = { { 1, 7, 7.5, 7 }, { 1, 7, 7.5, 7 }, { 1, 1, 1, 1 } };
	public static int[][] superminionShotProjectileSpeed = { { 1, 175, 230, 255 }, { 1, 175, 230, 255 }, { 1, 175, 230, 255 } };
	public static int[][] superminionShotProjectileSpeedPerLevel = { { 1, 5, 6, 6 }, { 1, 5, 6, 6 }, { 1, 5, 6, 6 } };
	public static int[][] superminionShotProjectileSpeedCap = { { 1, 470, 470, 470 }, { 1, 470, 470, 470 }, { 1, 470, 470, 470 } };
	public static int[][] superminionAppearChance = { { 1, 102500, 103750, 103750 }, { 1, 102500, 103750, 103750 }, { 1, 1, 1, 1 } };
	public static int[][] superminionAppearChancePerLevel = { { 1, 500, 700, 700 }, { 1, 500, 700, 700 }, { 1, 1, 1, 1 } };
	public static int[][] superminionShotAngleDivisor = { { 1, 4, 4, 9 }, { 1, 4, 4, 9 }, { 1, 1, 1, 1 } };

	// Lux
	public static int[][] luxHitpoints = { { 4992, 5629, 5734, 5734 }, { 4992, 5629, 5734, 5734 }, { 4992, 5629, 5734, 5734 } };
	public static double[][] luxMoveSpeed = { { 500, 500, 500, 500 }, { 500, 500, 500, 500 }, { 500, 500, 500, 500 } };
	public static int[][] luxShotDamage = { { 299, 359, 396, 1 }, { 299, 359, 396, 1 }, { 1, 1, 1, 1 } };
	public static int[][] luxShotCooldown = { { 50, 50, 50, 50 }, { 50, 50, 50, 50 }, { 50, 50, 50, 50 } };
	public static int[][] luxShotProjectileSpeed = { { 300, 300, 300, 300 }, { 300, 300, 300, 300 }, { 300, 300, 300, 300 } };
	public static double[][] luxPassiveMultiplier = { { 1.6, 1.75, 1.9, 1 }, { 1.6, 1.75, 1.9, 1 }, { 1, 1, 1, 1 } };
	public static int[][] luxPassiveDuration = { { 1600, 2000, 2400, 2400 }, { 1600, 2000, 2400, 2400 }, { 1600, 2000, 2400, 2400 } };
	public static int[][] luxAbilityQDamage = { { 563, 706, 813, 1 }, { 563, 706, 813, 1 }, { 1, 1, 1, 1 } };
	public static int[][] luxAbilityQStunDuration = { { 1500, 2000, 2000, 2000 }, { 1500, 2000, 2000, 2000 }, { 1500, 2000, 2000, 2000 } };
	public static int[][] luxAbilityQCooldown = { { 2500, 2000, 1750, 1750 }, { 2500, 2000, 1750, 1750 }, { 2500, 2000, 1750, 1750 } };
	public static int[][] luxAbilityQProjectileSpeed = { { 600, 625, 650, 675 }, { 600, 625, 650, 675 }, { 600, 625, 650, 675 } };
	public static int[][] luxAbilityWShieldAmount = { { 341, 393, 463, 463 }, { 341, 393, 463, 463 }, { 341, 393, 463, 463 } };
	public static int[][] luxAbilityWShieldTime = { { 2000, 3000, 4000, 4000 }, { 2000, 3000, 4000, 4000 }, { 2000, 3000, 4000, 4000 } };
	public static int[][] luxAbilityWPhase1Time = { { 1250, 1250, 1250, 1250 }, { 1250, 1250, 1250, 1250 }, { 1250, 1250, 1250, 1250 } };
	public static int[][] luxAbilityWCooldown = { { 8500, 7500, 7000, 7000 }, { 8500, 7500, 7000, 7000 }, { 8500, 7500, 7000, 7000 } };
	public static int[][] luxAbilityWProjectileSpeed = { { 400, 400, 400, 400 }, { 400, 400, 400, 400 }, { 400, 400, 400, 400 } };
	public static int[][] luxAbilityRDamage = { { 1587, 1991, 2452, 1 }, { 1587, 1991, 2452, 1 }, { 1, 1, 1, 1 } };
	public static int[][] luxAbilityRInitTime = { { 1000, 1000, 1000, 1000 }, { 1000, 1000, 1000, 1000 }, { 1000, 1000, 1000, 1000 } };
	public static int[][] luxAbilityRBlastTime = { { 400, 400, 400, 400 }, { 400, 400, 400, 400 }, { 400, 400, 400, 400 } };
	public static int[][] luxAbilityRCooldown = { { 8000, 6000, 5000, 5000 }, { 8000, 6000, 5000, 5000 }, { 8000, 6000, 5000, 5000 } };
	public static int[][] sorakaUltiHealAmount = { { 1, 3136, 3629, 3629 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 } };
	public static int[][] sorakaUltiLuxHealthActivation = { { -1000, 468, 468, 468 }, { -1000, -1000, -1000, -1000 },
			{ -1000, -1000, -1000, -1000 } };

	// Sivir
	public static int[][] sivirHitpoints = { { 5012, 5763, 5912, 5912 }, { 5012, 5763, 5912, 5912 }, { 5012, 5763, 5912, 5912 } };
	public static double[][] sivirMoveSpeed = { { 500, 500, 500, 500 }, { 500, 500, 500, 500 }, { 500, 500, 500, 500 } };
	public static double[][] sivirLifeSteal = { { 0.15, 0.15, 0.15, 0.15 }, { 0.15, 0.15, 0.15, 0.15 }, { 0.15, 0.15, 0.15, 0.15 } };
	public static int[][] sivirShotDamage = { { 312, 354, 421, 1 }, { 312, 354, 421, 1 }, { 1, 1, 1, 1 } };
	public static int[][] sivirShotCooldown = { { 35, 35, 35, 35 }, { 35, 35, 35, 35 }, { 35, 35, 35, 35 } };
	public static int[][] sivirShotProjectileSpeed = { { 400, 400, 400, 400 }, { 400, 400, 400, 400 }, { 400, 400, 400, 400 } };
	public static double[][] sivirPassiveMoveSpeedMultiplier = { { 1.05, 1.1, 1.15, 1.2 }, { 1.05, 1.1, 1.15, 1.2 },
			{ 1.05, 1.1, 1.15, 1.2 } };
	public static int[][] sivirPassiveDuration = { { 1500, 2000, 2500, 3000 }, { 1500, 2000, 2500, 3000 }, { 1500, 2000, 2500, 3000 } };
	public static int[][] sivirAbilityQDamage = { { 401, 462, 513, 1 }, { 401, 462, 513, 1 }, { 1, 1, 1, 1 } };
	public static int[][] sivirAbilityQCooldown = { { 10000, 9000, 8000, 8000 }, { 10000, 9000, 8000, 8000 }, { 10000, 9000, 8000, 8000 } };
	public static int[][] sivirAbilityQProjectileSpeed = { { 400, 425, 450, 475 }, { 400, 425, 450, 475 }, { 400, 425, 450, 475 } };
	public static int[][] sivirAbilityQPhase1Time = { { 1000, 1000, 1000, 1000 }, { 1000, 1000, 1000, 1000 }, { 1000, 1000, 1000, 1000 } };
	public static int[][] sivirAbilityECooldown = { { 12000, 11000, 10000, 10000 }, { 12000, 11000, 10000, 10000 },
			{ 12000, 11000, 10000, 10000 } };
	public static int[][] sivirAbilityEDuration = { { 2000, 2500, 3000, 3000 }, { 2000, 2500, 3000, 3000 }, { 2000, 2500, 3000, 3000 } };
	public static int[][] sivirAbilityRCooldown = { { 20000, 25000, 30000, 30000 }, { 20000, 25000, 30000, 30000 },
			{ 20000, 25000, 30000, 30000 } };
	public static int[][] sivirAbilityRDuration = { { 5000, 7000, 9000, 10000 }, { 5000, 7000, 9000, 10000 }, { 5000, 7000, 9000, 10000 } };
	public static double[][] sivirAbilityRSelfMoveSpeedMultiplier = { { 1.1, 1.15, 1.2, 1.25 }, { 1.1, 1.15, 1.2, 1.25 },
			{ 1.1, 1.15, 1.2, 1.255 } };
	public static double[][] sivirAbilityRAllyMoveSpeedMultiplier = sivirAbilityRSelfMoveSpeedMultiplier;

	public static int get(int[][] var) {
		int result = 0;
		try {
			result = var[GameState.get().getModeInt()][GameState.get().getDifficultyInt()];
		} catch (Exception e) {
			// System.out.println("Warning: Failed to get config variable");
		}
		return result;
	}

	public static long get(long[][] var) {
		long result = 0L;
		try {
			result = var[GameState.get().getModeInt()][GameState.get().getDifficultyInt()];
		} catch (Exception e) {
			// System.out.println("Warning: Failed to get config variable");
		}
		return result;
	}

	public static double get(double[][] var) {
		double result = 0;
		try {
			result = var[GameState.get().getModeInt()][GameState.get().getDifficultyInt()];
		} catch (Exception e) {
			// System.out.println("Warning: Failed to get config variable");
		}
		return result;
	}

	public static float get(float[][] var) {
		float result = 0f;
		try {
			result = var[GameState.get().getModeInt()][GameState.get().getDifficultyInt()];
		} catch (Exception e) {
			// System.out.println("Warning: Failed to get config variable");
		}
		return result;
	}

	public static boolean get(boolean[][] var) {
		boolean result = false;
		try {
			result = var[GameState.get().getModeInt()][GameState.get().getDifficultyInt()];
		} catch (Exception e) {
			// System.out.println("Warning: Failed to get config variable");
		}
		return result;
	}
}