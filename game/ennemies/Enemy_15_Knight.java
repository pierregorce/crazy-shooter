package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.pop.PopMessage;
import game.pop.PopMessage.MessageType;
import game.projectiles.Projectile;
import globals.Projectiles;

import java.util.Random;

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

/**
 * Le knight follow simplement le player. Lorsque le player est sur le même block, se protège 2s - est vulnérable 3s Lorsque le player n'est plus sur le même block alors
 * patrol a nouveau
 *
 */
public class Enemy_15_Knight extends Enemies
{
	private final static int				MAX_LIFE		= 500;
	private final static int				XP_GAIN_ON_KILL	= 180;
	private final static int				ATTACK_POWER	= 20;
	private final static float				MOVE_SPEED_MIN	= 1.8f;
	private final static float				MOVE_SPEED_MAX	= 2.1f;

	private final static TextureRegion[]	walk_frames		= R.c().enemy_knight_walk;
	private final static int				PIXEL_SIZE		= 5;
	private final static int				WIDTH			= walk_frames[0].getRegionWidth() * PIXEL_SIZE;

	private float							enemyCoef;

	public Enemy_15_Knight(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, walk_frames);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getPopablePosition();
		setPosition(position.x, position.y);

		editCollisionBox(40, 20, 0);
		direction = Direction.RIGHT_DIRECTION;
		walk = false;
		shoot = false;
		bumpSensibility = false;
	}

	@Override
	protected void enemies_initialisation()
	{
		shootCooldwon = 0.2f;
		m_shootPauseTime = 0f;
		m_shootRunTime = 1f;
		m_goldQuantity = 10;
		m_goldValue = 10;
		increaseStats(enemyCoef);
	}

	private enum KnightStates
	{
		BLOCK(2f),
		SHOOT(1f),
		GO_STRAIGHT_WALK(1f),
		WALK(9999);
		public float	phaseDuration;

		private KnightStates(float phaseDuration)
		{
			this.phaseDuration = phaseDuration;
		}
	}

	private KnightStates	m_knightStates		= KnightStates.WALK;
	private Timer			timer				= new Timer(m_knightStates.phaseDuration);	// Timer qui devient vrai lorqu'il faut changer de phase;
	private Timer			delayedBlockTimer	= new Timer(0.75f);

	private void swichBetweenBossStates(float delta)
	{
		if (timer.doAction(delta))
		{
			// Changement phase
			switch (m_knightStates) {
			case BLOCK:
				m_knightStates = KnightStates.SHOOT;
				break;
			case SHOOT:
				m_knightStates = KnightStates.GO_STRAIGHT_WALK;
				break;
			case GO_STRAIGHT_WALK:
				m_knightStates = KnightStates.BLOCK;
				GlobalController.fxController.addActor(new PopMessage(this, MessageType.KNIGHT));
				break;

			default:
				break;
			}
			// Repercution sur le timer
			timer = new Timer(m_knightStates.phaseDuration);
		}

	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (EnemyComportements.isOnSameBlock(this, player))
		{
			if (m_knightStates == KnightStates.WALK)
			{
				if (delayedBlockTimer.doAction(delta))
				{
					m_knightStates = KnightStates.BLOCK;
					GlobalController.fxController.addActor(new PopMessage(this, MessageType.KNIGHT));
					timer = new Timer(m_knightStates.phaseDuration);
				}
			}
		} else
		{
			m_knightStates = KnightStates.WALK;
		}

		swichBetweenBossStates(delta);

		EnemyComportements.physicalAttack(this, player);
		walk = false;
		shoot = false;
		protection = false;

		switch (m_knightStates) {
		case WALK:
			knightWalkAction(delta);
			EnemyComportements.followPlayerAndPatrol(this, player);
			break;
		case SHOOT:
			knightShootAction(delta);
			faireFaceTo(player);
			break;
		case GO_STRAIGHT_WALK:
			knightStraightWalkAction(delta);
			EnemyComportements.followPlayerAndPatrol(this, player);
			break;
		case BLOCK:
			knightBlockAction(delta);
			faireFaceTo(player);
			break;
		default:
			break;
		}

	}

	private void knightBlockAction(float delta)
	{
		shoot = false;
		walk = false;
		protection = true;
		blockAnimation = true;
	}

	private void knightShootAction(float delta)
	{
		blockAnimation = false;
		shoot = true;
		walk = false;
		protection = false;

	}

	private void knightWalkAction(float delta)
	{
		blockAnimation = false;
		shoot = false;
		protection = false;
		walk = true;
	}

	private void knightStraightWalkAction(float delta)
	{
		blockAnimation = false;
		shoot = false;
		protection = false;
		walk = true;
	}

	@Override
	public void shootEngine()
	{
		Projectile p = new Projectile(Projectiles.ENEMY_KNIGHT);
		p.init(this);
		GlobalController.bulletControllerEnemy.addActor(p);
	}

	@Override
	public void setShootAnimation()
	{
		shootRight = new Animation(0.1f, R.c().enemy_knight_shoot);
		shootLeft = R.invertAnimation(R.c().enemy_knight_shoot, 0.1f);
	}

	// --------------------------------------------------------Block Gestion

	private Animation	blockLeft		= R.invertAnimation(R.c().enemy_knight_block, 0.1f);
	private Animation	blockRight		= new Animation(0.07f, R.c().enemy_knight_block);

	private boolean		blockAnimation	= false;

	private Animation getBlockAnimation()
	{
		if (direction == Direction.LEFT_DIRECTION)
		{
			return blockLeft;
		} else
		{
			return blockRight;
		}
	}

	@Override
	public Animation getCurrentAnimation()
	{
		if (blockAnimation)
		{
			return getBlockAnimation();
		} else
		{
			return super.getCurrentAnimation();
		}
	}

}
