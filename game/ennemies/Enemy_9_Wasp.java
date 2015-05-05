package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.projectiles.Projectile;
import globals.Projectiles;

import java.util.Random;

import ressources.R;
import ressources.S;
import ressources.S.TyrianSound;
import screen.MyGdxGame;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pools;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_9_Wasp extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int	MAX_LIFE		= 120;
	private final static int	XP_GAIN_ON_KILL	= 120;
	private final static int	ATTACK_POWER	= 20;	// PHYSICAL ATAK
	private final static float	MOVE_SPEED_MIN	= 2;
	private final static float	MOVE_SPEED_MAX	= 3.5f;
	private final static int	WIDTH			= 80;

	private float				enemyCoef;

	public Enemy_9_Wasp(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_wasp_walk);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getFlyPosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);

		attackRight = new Animation(0.07f, R.c().enemy_wasp_shoot);
		attackLeft = R.invertAnimation(R.c().enemy_wasp_shoot, 0.07f);

		editCollisionBox(160, getHeight() - 130, 30);
		editBouncingBox(160, getHeight(), 30);

		// Increase stat sert a rien ici car classe unique
		increaseStats(enemyCoef);

		bumpSensibility = true;
		disablePhysics();
	}

	@Override
	protected void enemies_initialisation()
	{
		m_goldQuantity = 5;
		m_goldValue = 7;
		shootCooldwon = 0.1f;
		m_shootPauseTime = 0;

		increaseStats(enemyCoef);
	}

	private enum EnemyPhases
	{
		FLY_PATROL(3f),
		SHOOT(0.3f),
		CHANGE_OFFSET(1.5f);

		public float	phaseDuration;

		private EnemyPhases(float phaseDuration)
		{
			this.phaseDuration = phaseDuration;
		}
	}

	private EnemyPhases	m_enemyPhase	= EnemyPhases.FLY_PATROL;					// Commence à patrol
	private Timer		timer			= new Timer(m_enemyPhase.phaseDuration);	// Timer qui devient vrai lorqu'il faut changer de phase;

	private void swichBetweenBossStates(float delta)
	{
		if (timer.doAction(delta))
		{
			// Changement prévu de phase
			EnemyPhases currentPhase = m_enemyPhase;

			if (currentPhase == EnemyPhases.FLY_PATROL)
			{
				m_enemyPhase = EnemyPhases.SHOOT;
			}
			if (currentPhase == EnemyPhases.SHOOT)
			{
				m_enemyPhase = EnemyPhases.CHANGE_OFFSET;
			}
			if (currentPhase == EnemyPhases.CHANGE_OFFSET)
			{
				m_enemyPhase = EnemyPhases.FLY_PATROL;
			}

			// Repercution sur le timer
			timer = new Timer(m_enemyPhase.phaseDuration);
			System.out.println("XX : Je change de phase ! " + m_enemyPhase.name());
		}

	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		swichBetweenBossStates(delta);
		// faireFaceAuPlayer(player);

		if (attackAnimation && m_animation.isAnimationFinished(animationStateTime))
		{
			setAttackFalse();
		}

		if (m_enemyPhase == EnemyPhases.FLY_PATROL)
		{
			walk = true;
			shoot = false;
			clearActions();
		}
		if (m_enemyPhase == EnemyPhases.SHOOT)
		{
			walk = false;
			shoot = true;

		}

		if (m_enemyPhase == EnemyPhases.CHANGE_OFFSET)
		{
			walk = false;
			shoot = false;

			if (getActions().size == 0)
			{
				Vector2 position = EnemyPopConstants.getInstance().getFlyPosition();
				addAction(Actions.moveTo(getX(), position.y, EnemyPhases.CHANGE_OFFSET.phaseDuration));
			}
		}

	}

	@Override
	public void shootEngine()
	{
		Projectile p = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
		p.construct(Projectiles.ENEMY_WASP);
		p.init(this);
		GlobalController.bulletControllerEnemy.addActor(p);
		S.c().play(TyrianSound.soundEffect_enemies_beeShoot);
	}

	@Override
	public void setShootAnimation()
	{
		shootRight = new Animation(0.1f, R.c().enemy_wasp_shoot);
		shootLeft = R.invertAnimation(R.c().enemy_wasp_shoot, 0.1f);
	}

	@Override
	public void enemyDirectionEngine()
	{
		super.enemyDirectionEngine();

		if (getRight() > MyGdxGame.VIRTUAL_WORLD_WIDTH)
		{
			direction = Direction.LEFT_DIRECTION;
		}
		if (getX() < 0)
		{
			direction = Direction.RIGHT_DIRECTION;
		}
	}

	@Override
	public void physicalAttackEngine()
	{
		super.physicalAttackEngine();

		// si player en collision avec l'enemy -> physical attack
		if (player.getBouncingBox().overlaps(getBouncingBox()))
		{
			setAttackTrue(); // PERMET DE LANCER LACTION
			// Fait perdre de la vie au player
			player.setLosingLifeEvent(true);
			player.setLoosingLiveValueEvent(getAttackPower());

			// Met l'enemy en collision -> arret

			if (player.getRight() > getX())
			{
				// player a droite de enemy
				// enemy va vers la droite
				player.setBumpingRightEvent(true);
			}
			if (player.getX() < getX())
			{
				player.setBumpingLeftEvent(true);
				// player a gauche de enemy
				// enemy va a gauche
			}
			// Met l'enemy en collision -> arret
			setWalk(false);

		} else
		{
			// si plus de collision alors l'enemy repart
			if (!attackAnimation)
			{
				setWalk(true);
			}

		}

	}

	// --------------------------------------------------------Attack Gestion

	private Animation	attackLeft;
	private Animation	attackRight;
	private boolean		attackAnimation	= false;

	private Animation getAttackAnimation()
	{
		if (direction == Direction.LEFT_DIRECTION)
		{
			return attackLeft;
		} else
		{
			return attackRight;
		}
	}

	public void setAttackTrue()
	{
		if (!attackAnimation)
		{
			attackAnimation = true;
			animationStateTime = 0;
		}
	}

	public void setAttackFalse()
	{
		attackAnimation = false;
		animationStateTime = 0;
	}

	@Override
	public Animation getCurrentAnimation()
	{
		if (attackAnimation)
		{
			return getAttackAnimation();
		} else
		{
			return super.getCurrentAnimation();
		}
	}

	@Override
	public boolean remove()
	{
		if (super.remove())
		{
			return super.remove();
		}
		return super.remove();
	}

}
