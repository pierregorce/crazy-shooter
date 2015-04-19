package globals;

import ressources.R;
import screen.level.LevelData;
import screen.level.Levels;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import files.Files;

/**
 * Attention les fins de world doivent correspondre à un boss dans le level-config.json
 */
public enum Worlds
{
	WOLRD_1(R.c().world_images[0], "Green valleys", "The butcher", "Rookie", 3, false, 0, 10),
	WOLRD_2(R.c().world_images[1], "Forge enfer", "Dumblebee", "Basic", 5, false, 11, 18),
	WOLRD_3(R.c().world_images[2], "The ascenssion", "Sherkan", "Dangerous", 6, false, 19, 25),
	WOLRD_4(R.c().world_images[3], "Syn City", "The kraken", "Hell", 7, false, 26, 34),
	WOLRD_5(R.c().world_images[4], "The depths of putricides", "Ratchet", "Hell", 8, false, 35, 43),
	WOLRD_6(R.c().world_images[5], "Deep Russia", "Da colonel", "Inferno", 9, false, 43, 50);

	public TextureRegion	level;
	public String			name;
	public String			finalBoss;
	public String			plateformType;
	public int				onComplete;
	private boolean			isCompleted;
	public int				startWorld;
	public int				endWorld;

	// isCompleted permettra de ne plus toucher les golds des boss plusieurs fois ainsi que les étoiles de fin de world.

	private Worlds(TextureRegion level, String name, String finalBoss, String plateformType, int onComplete, boolean isCompleted, int startWorld, int endWorld)
	{
		this.level = level;
		this.name = name;
		this.finalBoss = finalBoss;
		this.plateformType = plateformType;
		this.onComplete = onComplete;
		this.isCompleted = isCompleted;
		this.startWorld = startWorld;
		this.endWorld = endWorld;
	}

	public static int	LAST_LEVEL	= 35;

	/**
	 * Retourne le numéro du monde dans lequel se situe le level demandé
	 */
	public static int getWorldNumber(int levelNumber)
	{
		int k = 0;
		for (Worlds world : Worlds.values())
		{
			if (levelNumber >= world.startWorld && levelNumber <= world.endWorld)
			{
				return k;
			}
			k++;
		}
		return k;
	}

	/**
	 * Retourne le monde dans lequel se situe le level demandé Retourne null si rien trouvé
	 */
	public static Worlds getWorldByLevel(int levelNumber)
	{

		for (Worlds world : Worlds.values())
		{
			if (world.startWorld >= levelNumber && levelNumber <= world.endWorld)
			{
				return world;
			}
		}
		return null;
	}

	// ------------------------------------------- Récompenses world

	public boolean isCompleted()
	{
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted)
	{
		// Si le monde est déjà completé on ne crédite pas les étoiles
		if (isCompleted)
		{

		} else
		{

		}
		this.isCompleted = isCompleted;
	}

	public static int getStars()
	{
		int total = 0;

		for (Worlds world : Worlds.values())
		{
			if (world.isCompleted)
			{
				total += world.onComplete;
			}
		}
		return total;
	}

	public static int getProgression()
	{

		Levels levels = Files.levelDataRead();
		int k = 0;

		for (LevelData level : levels.level)
		{
			if (level.levelComplete.equals("true"))
			{
				k++;
			}
		}
		float f = (float) k / (float) LAST_LEVEL;
		System.out.println(f);
		return (int) (f * 100);
	}

	/**
	 * Renvoie le dernier niveau accessible
	 */
	public static LevelData getLastLevelUnlock()
	{
		Levels levels = Files.levelDataRead();

		for (LevelData level : levels.level)
		{
			if (level.levelComplete.equals("false"))
			{
				return level;
			}
		}

		return null;
	}

}
