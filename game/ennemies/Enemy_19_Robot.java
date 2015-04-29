package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.fx.Beam;
import game.fx.BloodParticle;
import game.fx.ParticleColors;
import game.projectiles.Projectile;
import globals.Projectiles;

import java.util.Random;

import ressources.R;
import screen.MyGdxGame;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_19_Robot extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int				MAX_LIFE		= 500;
	private final static int				XP_GAIN_ON_KILL	= 180;
	private final static int				ATTACK_POWER	= 45;
	private final static float				MOVE_SPEED_MIN	= 1;
	private final static float				MOVE_SPEED_MAX	= 5;

	private final static TextureRegion[]	textures		= R.c().enemy_robot_walk;
	private final static int				HEIGHT			= textures[0].getRegionHeight() * MyGdxGame.PIXEL_SIZE;

	private float							enemyCoef;

	public Enemy_19_Robot(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, HEIGHT, textures);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getFallOrPopablePosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);
		increaseStats(enemyCoef);
	}

	@Override
	protected void enemies_initialisation()
	{
		m_goldQuantity = 5;
		m_goldValue = 15;
		m_shootPauseTime = 0;
		shootCooldwon = 0.05f;
		m_shootRunTime = 1;
		increaseStats(enemyCoef);
	}

	private Beam	beam;									// Laser Beam
	private Timer	delayedShootTime	= new Timer(0.6f);

	private void updateBeams()
	{
		if (beam != null && beam.isVisible())
		{
			Vector2 vector = new Vector2(getRight() - 23 + 15, getY() - 8 + 40 + 1);
			float dist = Math.abs(getCenterX() - player.getCenterX());
			beam.init(vector, dist, this);
			beam.setRobotSpecification();
		}
	}

	@Override
	public boolean remove()
	{
		if (beam != null)
		{
			beam.remove();
		}
		return super.remove();
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		EnemyComportements.physicalAttack(this, player);

		if (Math.abs(getX() - player.getX()) < Projectiles.ENEMY_ROBOT.lenghtAlive && player.getY() == getY())
		{
			if (!shoot && delayedShootTime.doAction(delta))
			{
				shoot = true;
			}
		} else
		{
			shoot = false;
		}

		if (shoot)
		{
			if (beam == null)
			{
				beam = new Beam();
				beam.setVisible(true);
				GlobalController.fxController.addActor(beam);
			} else
			{
				beam.setVisible(true);
			}
		} else
		{
			if (beam != null)
			{
				beam.setVisible(false);
			}
		}

		if (shoot)
		{
			faireFaceTo(player);
			setWalk(false);
		} else
		{
			EnemyComportements.followPlayerAndPatrol(this, player);
		}

		updateBeams();
	}

	@Override
	public void shootEngine()
	{
		Projectile p = new Projectile(Projectiles.ENEMY_ROBOT);
		p.init(this);
		float dist = Math.abs(getRight() - player.getX()) - 20;
		p.setTarget(this, new Vector2(dist, player.getY()));
		GlobalController.bulletControllerEnemy.addActor(p);
	}

	@Override
	protected void explosionOnLosingLife()
	{
		// super.explosionOnLosingLife();

		for (int i = 0; i < 3; i++)
		{
			BloodParticle bloodParticle = GlobalController.bloodParticlePool.obtain();
			bloodParticle.init(getCenterX(), getCenterY(), ParticleColors.getInstance().getIceColor());
			GlobalController.particleController.addActor(bloodParticle);
		}
	}
}
