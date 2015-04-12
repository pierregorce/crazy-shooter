package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.pop.PopMessage;
import game.pop.PopMessage.MessageType;

import java.util.Random;

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_15_Knight extends Enemies
{
	private final static int				MAX_LIFE		= 500;
	private final static int				XP_GAIN_ON_KILL	= 70;
	private final static int				ATTACK_POWER	= 20;
	private final static float				MOVE_SPEED_MIN	= 1;
	private final static float				MOVE_SPEED_MAX	= 2;

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
		BLOCK(2.5f),
		SHOOT(2.5f),
		WALK(9999);

		public float	phaseDuration;

		private KnightStates(float phaseDuration)
		{
			this.phaseDuration = phaseDuration;
		}
	}

	private KnightStates	m_knightStates	= KnightStates.WALK;
	private Timer			timer;									// Timer qui devient vrai lorqu'il faut changer de phase;

	private void swichBetweenBossStates(float delta)
	{
		if (timer.doAction(delta))
		{
			// Changement phase
			switch (m_knightStates) {
			case BLOCK:
				m_knightStates = KnightStates.SHOOT;
				System.out.println("Je blockais, je tir");
				break;
			case SHOOT:
				m_knightStates = KnightStates.BLOCK;
				System.out.println("Je tirai, je block");
				break;

			default:
				break;
			}
			// Repercution sur le timer
			timer = new Timer(m_knightStates.phaseDuration);
			System.out.println("Knight : Je change de phase !");
			GlobalController.fxController.addActor(new PopMessage(this, MessageType.KNIGHT));
		}

	}

	// Si le player est en l'air, WALK jusqu'a 200px de lui puis BLOCK. Si il est plus loin que 200px lorsque le player est en l'air, s'approche à 600px puis BLOCK
	// Lorsque le player redescent : Si pas en l'air
	// Alternance de BLOCK ET DE SHOOT selon les timers

	@Override
	public void act(float delta)
	{
		super.act(delta);

		// Gestion des phases
		float distance = Math.abs(getX() - player.getX());

		if (player.getCollisionBlock() != null || player.isInAir() || distance > 1200) // Player n'est plus au sol
		{
			if (m_knightStates != KnightStates.WALK)
			{
				m_knightStates = KnightStates.WALK;
				GlobalController.fxController.addActor(new PopMessage(this, MessageType.KNIGHT));
			}
		} else
		{
			if (m_knightStates == KnightStates.WALK)
			{
				System.out.println("Je block");
				GlobalController.fxController.addActor(new PopMessage(this, MessageType.KNIGHT));
				m_knightStates = KnightStates.BLOCK; // Passage en mode blocage directement après que le player arrive au sol
				timer = new Timer(m_knightStates.phaseDuration);
			}
			swichBetweenBossStates(delta); // Puis switch de phase tranquillou
		}

		// Gestion des actions durant les phases
		walk = false;
		shoot = false;
		protection = false;
		faireFaceTo(player);

		switch (m_knightStates) {
		case WALK:
			knightWalkAction(delta);
			break;
		case SHOOT:
			knightShootAction(delta);
			break;
		case BLOCK:
			knightBlockAction(delta);
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

		// Si le player est en l'air, WALK jusqu'a 200px de lui puis BLOCK. Si il est plus loin que 200px lorsque le player est en l'air, s'approche à 600px puis BLOCK
		float distance = Math.abs(getX() - player.getX());

		if (distance >= 600)
		{
			// Avance
			walk = true;
			faireFaceTo(player);
		}

		if (distance <= 200)
		{
			// Recule
			walk = true;
			tournerLeDosAuPlayer(player);
		}
	}

	@Override
	public void shootEngine()
	{
		// WizardProjectile bullet = ProjectilePoolFactory.getInstance().wizardBulletPool.obtain();
		// bullet.init(this, new Vector2(getRight(), getCenterY()));
		// GlobalController.bulletControllerEnemy.addActor(bullet);

	}

	@Override
	public void physicalAttackEngine()
	{
		super.physicalAttackEngine();
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
