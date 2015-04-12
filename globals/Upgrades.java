package globals;

public enum Upgrades
{
	// Objts directement construit
	LIFE_MAX(0, "Max Life", "Increase you max life by 20", 20, "+20HP", 0, 1),
	CRITICAL_CHANCE(0, "critical chance", "CRITICAL HIT", 0.05f, "+5%", 0, 2),
	HEALTH_PACK(0, "Health pack", "Increase health gain by /n health pack by 2 hp", 2, "+2 HP", 0, 2),
	DAMAGE_DONE(0, "Damage Done", "DAMMAMMAMAMGE DONE", 0.07f, "+10%", 0, 1),
	ATTACK_SPEED(0, "Attack Speed", "DAMMAMMAMAMGE DONE", 0.07f, "+10%", 0, 1),
	JUMP_HEIGHT(0, "Jump Height", "eddse ", 0.03f, "+5%", 0, 1);

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

		// Base64Coder b;

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
