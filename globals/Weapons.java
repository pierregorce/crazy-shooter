package globals;

import ressources.R;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum Weapons
{
	BASIC("SIMPLE GUN", 0.15f, true, 0, true, 0, R.c().player_weapons[0], Projectiles.PLAYER_BASIC_GUN),
	DUAL_GUN("DUAL GUN", 0.22f, false, 200, true, 0, R.c().player_weapons[1], Projectiles.PLAYER_DUAL_GUN),
	PUMP("PUMP GUN", 0.6f, false, 500, true, 25, R.c().player_weapons[2], Projectiles.PLAYER_PUMP),
	BAZOOKA("BAZOOKA", 0.45f, false, 1500, true, 10, R.c().player_weapons[3], Projectiles.PLAYER_BAZOOKA),
	MACHINE("ASSAULT RIFLE", 0.1f, false, 3500, true, 0, R.c().player_weapons[4], Projectiles.PLAYER_MACHINE),
	FLAME_THROWER("FLAME THROWER", 0.04f, false, 6500, false, 0, R.c().player_weapons[5], Projectiles.PLAYER_FLAME_THROWER),
	LASER("ULTRALASER", 0.08f, false, 9500, false, 0, R.c().player_weapons[6], Projectiles.PLAYER_LASER),
	PLAYER_MINIGUN("MINIGUN", 0.06f, false, 9999, true, 8, R.c().player_weapons[7], Projectiles.PLAYER_MINIGUN),
	PLAYER_GRENADE_LUNCHER("GRENADE THROWER", 0.6f, false, 12800, false, 15, R.c().player_weapons[8], Projectiles.PLAYER_GRENADE_LUNCHER),
	PLAYER_SNIPER("RAIL GUN", 0.3f, false, 15000, true, 0, R.c().player_weapons[9], Projectiles.PLAYER_RAILGUN),
	PLAYER_ROCKET_LUNCHER("ROCKET LUNCHER", 0.4f, false, 18500, false, 20, R.c().player_weapons[10], Projectiles.PLAYER_ROCKET_LUNCHER),
	LIGHTING_GUN("LIGHTING GUN", 0.2f, false, 24500, false, 0, R.c().player_weapons[11], Projectiles.PLAYER_LIGHTING_GUN);

	// Weapons spécifique
	public float			fireRate;
	public boolean			isBuy;
	public int				cost;
	public TextureRegion	weaponsTextureRegion;
	public Projectiles		projectileType;
	public boolean			ammo;
	public int				recoilStrenght;
	public String			name;

	private Weapons(String name, float fireRate, boolean isBuy, int cost, boolean ammo, int recoilStrenght, TextureRegion weaponsTextureRegion, Projectiles projectileType)
	{
		this.fireRate = fireRate;
		this.isBuy = isBuy;
		this.cost = cost;
		this.weaponsTextureRegion = weaponsTextureRegion;
		this.projectileType = projectileType;
		this.ammo = ammo;
		this.recoilStrenght = recoilStrenght;
		this.name = name;
	}

	// ---- Gestion de la sauvegarde et chargement
	// Passe par un tableau d'int pour sauvegarder en JSON

	public static boolean[] send()
	{
		boolean[] data = new boolean[values().length];

		for (int i = 0; i < values().length; i++)
		{
			data[i] = values()[i].isBuy;
		}
		return data;
	}

	public static void retrieve(Boolean[] booleans)
	{
		try
		{
			for (int i = 0; i < values().length; i++)
			{
				values()[i].isBuy = booleans[i];
			}
		} catch (Exception e)
		{
		}
	}
}
