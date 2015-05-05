package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.hud.BossBar;
import game.pop.PopMessage;
import game.pop.PopMessage.MessageType;
import game.projectiles.Projectile;
import globals.Projectiles;
import globals.Worlds;

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

public class Enemy_Boss_11_SuperFly extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int	MAX_LIFE		= 3900;
	private final static int	XP_GAIN_ON_KILL	= 700;
	private final static int	ATTACK_POWER	= 30;	// PHYSICAL ATAK
	private final static float	MOVE_SPEED_MIN	= 3;
	private final static float	MOVE_SPEED_MAX	= 5;
	private final static int	WIDTH			= 200;

	public Enemy_Boss_11_SuperFly(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_wasp_boss_walk);

		Vector2 position = EnemyPopConstants.getInstance().getFlyPosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);

		attackRight = new Animation(0.07f, R.c().enemy_wasp_boss_shoot);
		attackLeft = R.invertAnimation(R.c().enemy_wasp_boss_shoot, 0.07f);

		editCollisionBox(160, getHeight() - 80, 30);
		editBouncingBox(160, getHeight() - 80, 30);

		// Increase stat sert a rien ici car classe unique
		increaseStats(enemyCoef);

		// BOSS CAPACITY
		// colorization = new Color(214 / 255f, 119 / 255f, 105 / 255f, 1);
		BossBar.active = true;
		BossBar.enemy = this;
		BossBar.setBossName("BOSS #2 : " + Worlds.WOLRD_2.finalBoss); // GUEPE
		bumpSensibility = false;

		disablePhysics();
	}

	@Override
	protected void enemies_initialisation()
	{
		shootCooldwon = 0.2f;
		m_shootPauseTime = 0;

		if (Worlds.WOLRD_2.isCompleted())
		{
			m_goldValue = 1;
			m_goldQuantity = 10;
		} else
		{
			m_goldQuantity = 50;
			m_goldValue = 25;
		}

	}

	private enum BossPhases
	{
		FLY_PATROL(2f),
		SHOOT(1),
		CHANGE_OFFSET(1.5f);

		public float	phaseDuration;

		private BossPhases(float phaseDuration)
		{
			this.phaseDuration = phaseDuration;
		}
	}

	private BossPhases	m_bossPhase	= BossPhases.FLY_PATROL;				// Commence à patrol
	private Timer		timer		= new Timer(m_bossPhase.phaseDuration); // Timer qui devient vrai lorqu'il faut changer de phase;

	private void swichBetweenBossStates(float delta)
	{
		if (timer.doAction(delta))
		{
			// Changement prévu de phase
			BossPhases currentPhase = m_bossPhase;

			if (currentPhase == BossPhases.FLY_PATROL)
			{
				m_bossPhase = BossPhases.SHOOT;
			}
			if (currentPhase == BossPhases.SHOOT)
			{
				m_bossPhase = BossPhases.CHANGE_OFFSET;
			}
			if (currentPhase == BossPhases.CHANGE_OFFSET)
			{
				m_bossPhase = BossPhases.FLY_PATROL;
			}

			// Repercution sur le timer
			timer = new Timer(m_bossPhase.phaseDuration);
			System.out.println("BOSS #2 : Je change de phase ! " + m_bossPhase.name());
			GlobalController.fxController.addActor(new PopMessage(this, MessageType.BOSS_CRY_2));
			S.c().play(TyrianSound.soundEffect_boss2_pulse);
		}

	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		swichBetweenBossStates(delta);
		faireFaceTo(player);

		if (attackAnimation && m_animation.isAnimationFinished(animationStateTime))
		{
			setAttackFalse();
		}

		if (m_bossPhase == BossPhases.FLY_PATROL)
		{
			walk = true;
			shoot = false;
			clearActions();
		}
		if (m_bossPhase == BossPhases.SHOOT)
		{
			walk = false;
			shoot = true;

		}

		if (m_bossPhase == BossPhases.CHANGE_OFFSET)
		{
			walk = false;
			shoot = false;

			if (getActions().size == 0)
			{
				Vector2 position = EnemyPopConstants.getInstance().getAllBlockPosition();
				addAction(Actions.moveTo(getX(), position.y, BossPhases.CHANGE_OFFSET.phaseDuration));
			}
		}

	}

	@Override
	public void shootEngine()
	{
		Projectile p = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
		p.construct(Projectiles.ENEMY_BOSS_2);
		p.init(this);
		GlobalController.bulletControllerEnemy.addActor(p);
		S.c().play(TyrianSound.soundEffect_boss2_shoot);
	}

	@Override
	public void setShootAnimation()
	{
		shootRight = new Animation(0.1f, R.c().enemy_wasp_boss_shoot);
		shootLeft = R.invertAnimation(R.c().enemy_wasp_boss_shoot, 0.1f);
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
			BossBar.active = false;
			BossBar.enemy = null;
			return super.remove();
		}
		return super.remove();
	}

}
