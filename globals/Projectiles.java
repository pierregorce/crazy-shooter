package globals;

import game.projectiles.ProjectileComportementFrame;
import game.projectiles.ProjectileComportements;
import ressources.R;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.Player;

public enum Projectiles
{
	PLAYER_BASIC_GUN(8, 2, 20, 1, new Vector2(50, 50), 1000, 50, true, new ProjectileComportements().new Straight(), false, R.c().weapons_w0_projectile),
	PLAYER_DUAL_GUN(12, 2, 20, 1, new Vector2(2, 2), 1000, 50, true, new ProjectileComportements().new Straight_Inverted(), false, R.c().weapons_w1_projectile),
	PLAYER_PUMP(7, 2, 20, 4, new Vector2(2, 2), 1000, 50, true, new ProjectileComportements().new Straight(), false, R.c().weapons_w2_projectile),
	PLAYER_BAZOOKA(5, 2, 22, 1, new Vector2(2, 2), 1000, 50, true, new ProjectileComportements().new Straight_Bazooka(), false, R.c().weapons_w3_projectile),
	PLAYER_BAZOOKA_EXPLOSION(5, 1, 22, 4, new Vector2(0, 0), 99999, 50, true, new ProjectileComportements().new Explosion(60, 60), true, R.c().fx_explode_circle),
	PLAYER_MACHINE(7, 2, 20, 1, new Vector2(50, 50), 1000, 50, true, new ProjectileComportements().new Straight(), false, R.c().weapons_w4_projectile),
	PLAYER_FLAME_THROWER(6, 1, 12, 4, new Vector2(2, 2), 1000, 5, true, new ProjectileComportements().new Flame(), false, R.c().weapons_w5_projectile),
	PLAYER_LASER(1, 2, 25, 2, new Vector2(30, 10), 600, 5, true, new ProjectileComportements().new Straight(), false, R.c().weapons_w0_projectile),
	PLAYER_MINIGUN(6, 2, 20, 2, new Vector2(70, 20), 1000, 5, true, new ProjectileComportements().new Straight(), false, R.c().weapons_w7_projectile),
	PLAYER_GRENADE_LUNCHER(12, 2, 20, 1, new Vector2(50, 50), 99999, 5, true, new ProjectileComportements().new Physic_Grenade(25, 2, 15, 2), false, R.c().weapons_w8_projectile),
	PLAYER_GRENADE_EXPLOSION(7, 2, 20, 4, new Vector2(0, 0), 99999, 20, true, new ProjectileComportements().new Explosion(60, 60), true, R.c().fx_pop),
	PLAYER_RAILGUN(12, 2, 25, 1, new Vector2(41 * Player.heightCoef, 17 * 4 * Player.heightCoef), 1000, 5, false, new ProjectileComportements().new Straight(), false, R.c().weapons_w9_projectile),
	PLAYER_ROCKET_LUNCHER(12, 2, 20, 1, new Vector2(37 * Player.heightCoef, 15 * 4 * Player.heightCoef), 1000, 5, true, new ProjectileComportements().new Straight(), false, R.c().weapons_w10_projectile),
	PLAYER_LIGHTING_GUN(12, 2, 20, 1, new Vector2(41 * Player.heightCoef, 15 * 4 * Player.heightCoef), 700, 5, true, new ProjectileComportements().new Straight(), false, R.c().weapons_w0_projectile),
	ENEMY_WIZARD(10, 2, 20, 1, new Vector2(2, 2), 1000, 50, true, new ProjectileComportements().new Straight(), false, R.c().enemy_wizard_bullet),
	ENEMY_LIMON(12, 2, 20, 3, new Vector2(2, 2), 1000, 50, true, new ProjectileComportements().new Straight(), false, R.c().enemy_limon_bullet),
	ENEMY_FANTOMAS(15, 2, 20, 1, new Vector2(2, 2), 1000, 50, true, new ProjectileComportements().new Target_Player_Action(), false, R.c().enemy_fantomas_bullet),
	ENEMY_WASP(10, 2, 20, 1, new Vector2(2, 2), 1000, 50, true, new ProjectileComportements().new Straight(), false, R.c().enemy_wasp_bullet),
	ENEMY_TURREL(4, 2, 20, 1, new Vector2(20, 100), 1000, 50, true, new ProjectileComportements().new Physic(13, 5, 22, 5, 1, R.c().fx_explode_square_2), false, R.c().enemy_turrel_bullet),
	ENEMY_BOSS_1(10, 2, 20, 3, new Vector2(50, 50), 1000, 50, true, new ProjectileComportements().new Physic(19, 7, 34, 2), false, R.c().enemy_wasp_bullet),
	ENEMY_BOSS_2(25, 2, 20, 1, new Vector2(2, 20), 2000, 100, true, new ProjectileComportements().new Straight(), false, R.c().enemy_wasp_boss_bullet),
	ENEMY_BOSS_3(20, 2, 20, 1, new Vector2(2, 2), 2000, 50, true, new ProjectileComportements().new Physic(15, 5, 25, 5), false, R.c().enemy_invocator_boss_bullet),
	ENEMY_POT(10, 2, 20, 3, new Vector2(50, 50), 1000, 50, true, new ProjectileComportements().new Physic(19, 7, 34, 2), false, R.c().enemy_wasp_bullet),
	ENEMY_ROBOT(30, 2, 25, 6, new Vector2(30, 10), 600, 20, true, new ProjectileComportements().new Straight(), false, R.c().weapons_w0_projectile),
	ENEMY_METEORE(15, 2, 20, 1, new Vector2(2, 2), 2000, 50, true, new ProjectileComportements().new Fall_Meteore(5, 2, 8, 5), false, R.c().enemy_meteor),
	BARREL_EXPLOSION(75, 2, 20, 15, new Vector2(2, 2), 99999, 50, true, new ProjectileComportements().new Explosion(80, 80), true, R.c().fx_pop),
	FALLING_BOMB(9, 5, 20, 1, new Vector2(2, 2), 99999, 50, true, new ProjectileComportements().new Bomb(2, 1, 3, 1), false, R.c().enemy_bomb),
	BARREL_NAPALM(10, 1, 20, 15, new Vector2(2, 2), 1000, 50, true, new ProjectileComportements().new Explosion(50, 50, 35), true, R.c().fx_explode_square_1),
	TAPIS(10, 1, 20, 15, new Vector2(2, 2), 99999, 50, false, new ProjectileComportements().new PhysicMud(13, 5, 22, 5, 1), false, R.c().irregularParticles);

	public int							damage;
	public int							damageModulation;
	public float						speed;
	public int							quantityPerShoot;
	public Vector2						characterAnchor;
	public int							lenghtAlive;
	public int							bumpAmount;
	public boolean						removeOnCollision	= true;		// TODO ERREUR INUTILE ? hé non ça serivra !
	public ProjectileComportementFrame	projectileComportement;
	public boolean						animated;
	public TextureRegion[]				projectileTextureRegion;

	// According to constructor
	public Sound[]						randomSound;
	public Sound						uniqueSound;

	private Projectiles(int damage, int damageModulation, float speed, int quantityPerShoot, Vector2 characterAnchor, int lenghtAlive, int bumpAmount, boolean removeOnCollision,
			ProjectileComportementFrame projectileComportement, boolean animated, TextureRegion... projectileTextureRegion)
	{
		this.damage = damage;
		this.damageModulation = damageModulation;
		this.speed = speed;
		this.quantityPerShoot = quantityPerShoot;
		this.characterAnchor = characterAnchor;
		this.lenghtAlive = lenghtAlive;
		this.bumpAmount = bumpAmount;
		this.projectileTextureRegion = projectileTextureRegion;
		this.removeOnCollision = removeOnCollision;
		this.projectileComportement = projectileComportement;
		this.animated = animated;
	}
}
