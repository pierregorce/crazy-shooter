package globals;

public enum Upgrades
{
	// Objts directement construit
	LIFE_MAX(0, "Max Life", "Increases your maximum health by 20 points.", 20, "", 0, 1),
	DAMAGE_DONE(0, "Damage Done", "Increases damage your weapon produced by 1 points.", 1, "", 0, 1),
	ATTACK_SPEED(0, "Attack Speed", "Increases attack speed of your weapon by 0.01 seconds.", 0.005f, "", 0, 2),
	HEALTH_PACK(0, "Health pack", "Increases life gain in the life packs by 4 HP.", 2, "", 0, 2),
	CRITICAL_CHANCE(0, "Critical Chance", "Increases the critical chance of your bullets by 2 percent.", 0.01f, "", 0, 2),
	MOVE_SPEED(0, "NOT USED FOR NOW", "", 0.02f, "+2%", 0, 0),
	JUMP_HEIGHT(0, "NOT USED FOR NOW", "", 0.03f, "+5%", 0, 1);

	public int		point;
	public String	stringDescription;
	public float	amount;
	public String	stringAmount;
	public int		levelUnlock;
	public int		cost;
	public String	title;

	// Constructeur
	private Upgrades(int point, String title, String stringDescription, float amount, String stringAmount, int levelUnlock, int cost)
	{
		this.point = point;
		this.stringDescription = stringDescription;
		this.stringAmount = stringAmount;
		this.amount = amount;
		this.levelUnlock = levelUnlock;
		this.cost = cost;
		this.title = title;
	}

	// ---- Gestion de la sauvegarde et chargement
	// Passe par un tableau d'int pour sauvegarder en JSON

	public static int[] send()
	{
		int[] data = new int[values().length];

		for (int i = 0; i < values().length; i++)
		{
			data[i] = values()[i].point;
		}
		return data;

	}

	public static void retrieve(Integer[] integers)
	{
		try
		{
			for (int i = 0; i < values().length; i++)
			{
				values()[i].point = integers[i];
			}
		} catch (Exception e)
		{
			// Lors de la mise à jour, reset les points
			reset();
		}
	}

	public static void reset()
	{
		for (int i = 0; i < values().length; i++)
		{
			values()[i].point = 0;
		}
	}
}
