package globals;

public class PlayerStats
{
	public static int			level;							// Variable unique du level;
	public static int			currentXP;						// Variable unique d'xp;
	public static int			ressource;						// Variable contenant la ressource recoltée
	public static int			weaponsType;					// Variable contenant le numéro de l'arme selectionnée

	private final static int	BASE_LIFE				= 100;
	private final static float	BASE_MOVE_SPEED			= 8;	// 8 de base, 0.017 * x = 8 x = 470
	private final static float	BASE_IMMUNE_TIME		= 1f;
	private final static int	BASE_JUMP_HEIGHT		= 27;
	private final static int	BASE_LIFEBOX			= 25;
	private final static float	BASE_CRITICAL_CHANCE	= 0.2f;
	private final static float	BASE_DAMAGE				= 0;	// L'arme apporte les dmg
	private final static float	BASE_ATTACK_SPEED		= 0;	// L'arme apporte le reste

	// ------------------------------------ TALENTS

	private static int getTalentPointsMax()
	{
		return level;
	}

	public static int getTalentPointsRemaining()
	{
		int points = 0;

		for (int i = 0; i < Upgrades.values().length; i++)
		{
			points += Upgrades.values()[i].point;
		}
		return getTalentPointsMax() - points;
	}

	// -------------------------------------- STATS

	public static int getRequiertXP()
	{
		return getRequiertXP(level);
	}

	private static int getRequiertXP(int level)
	{
		return (level + 1) * 200;
	}

	public static int getSumXpForLevel(int level)
	{
		int total = 0;

		for (int i = 0; i < level; i++)
		{
			total += getRequiertXP(i);
		}
		return total;
	}

	public static int getLevelForXP(int xp)
	{

		for (int i = 1; i < 80; i++)
		{
			if (xp < getSumXpForLevel(i))
			{
				return i;
			}
		}

		return 0;
	}

	public static int getMaxLife()
	{
		return (int) (BASE_LIFE + Upgrades.LIFE_MAX.point * Upgrades.LIFE_MAX.amount);
	}

	public static float getMoveSpeed()
	{
		return BASE_MOVE_SPEED * (1 + Upgrades.DAMAGE_DONE.point * Upgrades.DAMAGE_DONE.amount);
	}

	public static float getMoveSpeedReduce()
	{
		return (float) (getMoveSpeed() * 0.3);
	}

	public static float getImuneTime()
	{
		return BASE_IMMUNE_TIME;
	}

	public static float getJumpHeight()
	{
		return BASE_JUMP_HEIGHT * (1 + Upgrades.JUMP_HEIGHT.point * Upgrades.JUMP_HEIGHT.amount);
	}

	public static int getLifeBoxHp()
	{
		return (int) (BASE_LIFEBOX + Upgrades.HEALTH_PACK.point * Upgrades.HEALTH_PACK.amount);
	}

	public static float getCriticalChance()
	{
		return BASE_CRITICAL_CHANCE + Upgrades.CRITICAL_CHANCE.point * Upgrades.CRITICAL_CHANCE.amount;
	}

	public static float getDamage()
	{
		return BASE_DAMAGE + Upgrades.DAMAGE_DONE.point * Upgrades.DAMAGE_DONE.amount;
	}

	public static float getAttackSpeed()
	{
		return BASE_ATTACK_SPEED + Upgrades.ATTACK_SPEED.point * Upgrades.ATTACK_SPEED.amount;
	}

	public static Weapons getCurrentWeapons()
	{
		return Weapons.values()[PlayerStats.weaponsType];
	}

	public static class PlayerSavedDatas
	{
		public int	level;
		public int	currentXp;
		public int	ressource;
		public int	weaponsType;
	}

}
