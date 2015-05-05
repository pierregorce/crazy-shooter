package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.fx.LimaceParticle;
import game.fx.ParticleColors;

import java.util.Random;

import ressources.R;
import ressources.S;
import ressources.S.TyrianSound;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_4_Limace extends Enemies
{

	/*
	 * Walking basic enemy Try to catch player
	 */

	private final static int	MAX_LIFE		= 40;
	private final static int	XP_GAIN_ON_KILL	= 30;
	private final static int	ATTACK_POWER	= 20;
	private final static float	MOVE_SPEED_MIN	= 0.35f;
	private final static float	MOVE_SPEED_MAX	= 0.75f;
	private final static float	MOVE_SPEED		= new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN;
	private final static int	WIDTH			= 75;

	private float				enemyCoef;

	public Enemy_4_Limace(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, MOVE_SPEED, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_limace_walk);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getGroundBlockPosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);
	}

	@Override
	protected void enemies_initialisation()
	{
		m_goldQuantity = 2;
		m_goldValue = 3;
		increaseStats(enemyCoef);
	}

	@Override
	public void enemyDirectionEngine()
	{
		super.enemyDirectionEngine();
		EnemyComportements.followPlayerAndPatrolOnGround(this, player);
	}

	@Override
	public void shootEngine()
	{
		super.shootEngine();
	}

	@Override
	public void physicalAttackEngine()
	{
		super.physicalAttackEngine();

		// si player en collision avec l'enemy -> physical attack
		if (player.getBouncingBox().overlaps(getBouncingBox()))
		{

			// Ralenti le player
			player.setReduceSpeedEvent(true);
			// Fait perdre de la vie au player
			player.setLosingLifeEvent(true);
			player.setLoosingLiveValueEvent(getAttackPower());
			// System.out.println("EVENT-enemy");
			// Fait bumper le player - pour ne pas avoir left et right à true il faut filtrer voir si ceux-ci ne sont pas deja a true
			// if (!player.isBumpingLeftEvent() && !player.isBumpingRightEvent()) {
			if (player.getRight() > getX())
			{
				// player a droite de enemy
				// enemy va vers la droite
				player.setBumpingRightEvent(true);
				// System.out.println("lol");
			}
			if (player.getX() < getX())
			{
				player.setBumpingLeftEvent(true);
				// player a gauche de enemy
				// enemy va a gauche
				// System.out.println("lol2");
			}
			// Met l'enemy en collision -> arret
			setWalk(false);
			// }
		} else
		{
			// si plus de collision alors l'enemy repart
			setWalk(true);
		}

	}

	private boolean	spit				= false;
	private boolean	spitEvent			= false;
	private Timer	spitTimer			= new Timer(3);
	private float	spitPopTime			= 0;
	private float	spitPopDelay		= 0.10f;
	private float	spitGlobalTime		= 0;
	private float	spitDuration		= 0.75f;
	private Vector2	spitStartPosition	= new Vector2();
	boolean			spitSound			= false;

	@Override
	public void act(float delta)
	{
		super.act(delta);

		// this enemy type don't shoot but spite !!!
		float distance = Math.abs(getX() - player.getX());
		if (distance <= 550)
		{
			// si le playe rest a moins de 700px alors spit (permet d'evite le camera shake quand le player est loin)
			spitEvent = true;
		} else
		{
			spitEvent = false;
		}

		if (spit)
		{

			if (spitSound)
			{
				S.c().play(TyrianSound.soundEffect_enemies_greenEnemyBurp);
				spitSound = false;
			}
			// GameStage.cameraShake(7);
			walk = false;

			if (spitPopTime > spitPopDelay)
			{
				// envoie des particle
				for (int i = 0; i < 8; i++)
				{
					spitStartPosition = new Vector2(getRight(), getTop() - 10);
					LimaceParticle limaceParticle = new LimaceParticle();
					limaceParticle.init(this, spitStartPosition, ParticleColors.getInstance().getGreenColor());
					GlobalController.limaceParticleController.addActor(limaceParticle);
				}

				spitPopTime = 0;
			} else
			{
				spitPopTime += delta;
			}

			if (spitGlobalTime > spitDuration)
			{
				spit = false;

			} else
			{
				spitGlobalTime += delta;
			}
		}

		if (!spit)
		{
			spitPopTime = 0;
			spitGlobalTime = 0;
			walk = true;
		}

		if (spitTimer.doAction(delta))
		{
			System.out.println("limace spit !");
			spit = true;
			spitSound = true;
		}

	}

	@Override
	public Animation getCurrentAnimation()
	{
		if (spit)
		{
			animationStateTime = 0.15f;
			return getWalkAnimation();
		} else
		{
			return super.getCurrentAnimation();
		}
	}

	@Override
	public void throwProjectile()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setShootAnimation()
	{
		// TODO Auto-generated method stub

	}

}
