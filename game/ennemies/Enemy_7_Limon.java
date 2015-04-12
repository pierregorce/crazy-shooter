package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.projectiles.Projectile;
import game.sound.MusicManager;
import globals.Projectiles;

import java.util.Random;

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;

/**
 * Limon : Can shoot, Can walk, Can't jump, Se deplace jusqu'a une distance de X px du player puis attend. Si le player est au même Y alors shoot. Si le player est trop
 * prés alors recule //TODO
 * 
 * @author Pierre
 *
 */
public class Enemy_7_Limon extends Enemies
{
	private final static int	MAX_LIFE		= 120;
	private final static int	XP_GAIN_ON_KILL	= 80;
	private final static int	ATTACK_POWER	= 20;
	private final static float	MOVE_SPEED_MIN	= 1;
	private final static float	MOVE_SPEED_MAX	= 2;
	private final static int	WIDTH			= 70;

	private Player				player;
	private float				enemyCoef;

	public Enemy_7_Limon(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH,
				R.c().enemy_limon_walk);

		this.player = player;
		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getSmallBlocksPosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		walk = true;
		editCollisionBox(40, 20, 0);
	}

	@Override
	protected void enemies_initialisation()
	{
		shootCooldwon = 0.30f;
		m_goldQuantity = 4;
		m_goldQuantity = 8;
		m_shootPauseTime = 0.7f;
		m_shootRunTime = 0.15f;
		increaseStats(enemyCoef);
	}

	@Override
	public void enemyDirectionEngine()
	{
		super.enemyDirectionEngine();

		// Avance jusqu'a 200px de player.
		// Si le player plus prés alors recule
		// Sinon avance

		walk = false;
		shoot = false;

		float distance = Math.abs(getX() - player.getX());

		if (distance >= 700)
		{
			// Avance
			walk = true;
			faireFaceTo(player);
		}
		if (distance <= 300)
		{
			// Ne bouge plus et tire
			// walk = true;
			// tournerLeDosAuPlayer(player);
		}

		if (!walk)
		{
			faireFaceTo(player);
			shoot = true;
		}
	}

	@Override
	public void shootEngine()
	{
		for (int i = 0; i < Projectiles.ENEMY_LIMON.quantityPerShoot; i++)
		{
			Projectile p = new Projectile(Projectiles.ENEMY_LIMON);
			p.init(this);
			GlobalController.bulletControllerEnemy.addActor(p);
		}

		R.c().soundEffect_enemies_laserEnemy[new Random().nextInt(R.c().soundEffect_enemies_laserEnemy.length)].play(MusicManager.sfxVolume);

	}

	@Override
	public void setShootAnimation()
	{
		shootRight = new Animation(0.07f, R.c().enemy_limon_shoot);
		shootLeft = R.invertAnimation(R.c().enemy_limon_shoot, 0.07f);

	}
}
