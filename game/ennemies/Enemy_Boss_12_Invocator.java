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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Pools;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_Boss_12_Invocator extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int	MAX_LIFE				= 5500;
	private final static int	XP_GAIN_ON_KILL			= 880;
	private final static int	ATTACK_POWER			= 55;				// PHYSICAL ATAK
	private final static float	MOVE_SPEED_MIN			= 1;
	private final static float	MOVE_SPEED_MAX			= 2;
	private final static int	WIDTH					= 170;

	// Invocator : le faire invoquer les mob jusqu'a ce qu'il soit touché par une balle
	// Si touché alors stop durant 5s.
	// Invoque des enemy en fonction de son pourcentage de vie
	// Si entre 100 et 60 : spider
	// Si entre 60 et 20 : wasp
	// Si en dessous de 20 : teleporter

	Timer						timerMessage			= new Timer(0.4f);
	Timer						timerCooldownInvoque	= new Timer(0.7f);
	Timer						timerMeteorPoping		= new Timer(0.4f);
	Timer						timerProjectileSend		= new Timer(0.5f);
	boolean						moveOnBlockDone			= false;

	private enum BossPhases
	{
		PATROL(1f),
		INVOCK(3.5f),
		METEOR_RAIN(4),
		FLY_TO_TRHOW_PROJECTILES(6f);

		public float	phaseDuration;

		private BossPhases(float phaseDuration)
		{
			this.phaseDuration = phaseDuration;
		}
	}

	private BossPhases	m_bossPhase	= BossPhases.PATROL;					// Commence à patrol
	private Timer		timer		= new Timer(m_bossPhase.phaseDuration); // Timer qui devient vrai lorqu'il faut changer de phase;

	// Le boss patrouille.
	// Le boss invoque des enemies
	// Le boss devient immune (mettre une bulle par dessus lui et invoque une pluie de boule
	// Le boss s'envole jusqu'au centre de la map et envoie des boules dans toutes les direction à la manière d'un cercle.
	// Chacune des phases à lieu aléatoirement

	private void swichBetweenBossStates(float delta)
	{

		if (timer.doAction(delta))
		{
			// Changement aléatoire de phase
			BossPhases currentPhase = m_bossPhase;
			while (currentPhase == m_bossPhase)
			{
				m_bossPhase = BossPhases.values()[new Random().nextInt(BossPhases.values().length)];
				moveOnBlockDone = false;
			}

			// Repercution sur le timer
			timer = new Timer(m_bossPhase.phaseDuration);
			System.out.println("BOSS #3 : Je change de phase !");
			GlobalController.fxController.addActor(new PopMessage(this, MessageType.BOSS_PHASE_3));
		}

	}

	public Enemy_Boss_12_Invocator(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_invocator_boss_walk);

		Vector2 position = EnemyPopConstants.getInstance().getFallPosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;

		flyRight = new Animation(0.07f, R.c().enemy_invocator_boss_fly);
		flyLeft = R.invertAnimation(R.c().enemy_invocator_boss_fly, 0.07f);

		// editCollisionBox(160, getHeight() - 130, 30);
		// editBouncingBox(160, getHeight(), 30);

		// Increase stat sert a rien ici car classe unique
		increaseStats(enemyCoef);

		// BOSS CAPACITY
		// colorization = new Color(214 / 255f, 119 / 255f, 105 / 255f, 1);
		// colorization = new Color(Color.GREEN);
		BossBar.active = true;
		BossBar.enemy = this;
		BossBar.setBossName("BOSS #3 : " + Worlds.WOLRD_3.finalBoss); // WIZARD
		bumpSensibility = false;

		float off = 0;
		editCollisionBox(getWidth() - off, getHeight() - 80, off);

	}

	@Override
	protected void enemies_initialisation()
	{
		// shootCooldwon = 0.2f;
		// m_shootPauseTime = 0f;
		// m_shootRunTime = 1f;

		if (Worlds.WOLRD_3.isCompleted())
		{
			m_goldValue = 1;
			m_goldQuantity = 10;
		} else
		{
			m_goldQuantity = 50;
			m_goldValue = 25;
		}

		increaseStats(0);

	}

	@Override
	public void setShootAnimation()
	{
		shootRight = new Animation(0.1f, R.c().enemy_invocator_boss_shoot);
		shootLeft = R.invertAnimation(R.c().enemy_invocator_boss_shoot, 0.1f);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		EnemyComportements.physicalAttackOnly(this, player);

		swichBetweenBossStates(delta);

		if (flyAnimation && m_animation.isAnimationFinished(animationStateTime))
		{
			setFlyFalse();
		}

		// -------------------------------------Action en fonction de l'état

		if (m_bossPhase == BossPhases.INVOCK)
		{
			faireFaceTo(player);
			invock(delta);
		}

		if (m_bossPhase == BossPhases.PATROL)
		{
			EnemyComportements.followPlayerAndPatrol(this, player);
			colorization = Color.WHITE;
		}

		if (m_bossPhase == BossPhases.METEOR_RAIN)
		{
			faireFaceTo(player);
			meteore_rain(delta);
		}

		if (m_bossPhase == BossPhases.FLY_TO_TRHOW_PROJECTILES)
		{
			fly_to_throw(delta);
		}

	}

	// ----------------------------------------------------------------------------- Phases Gestion

	public void invock(float delta)
	{

		walk = false;
		shoot = true;

		colorization = new Color(0.9f, 0.9f, 0.7f, 1);

		if (timerCooldownInvoque.doAction(delta))
		{
			GlobalController.enemyController.addActor(new Enemy_1_Spider_basic(player, 0.9f, new Vector2(getCenterX(), getY())));
			GlobalController.fxController.addActor(new PopMessage(this, MessageType.BOSS_CRY_3));
			S.c().play(TyrianSound.soundEffect_boss3_tearAttack, player, this);
		}
	}

	public void fly_to_throw(float delta)
	{
		// START

		// WAIT 1s
		// FLY 4s
		// WAIT 1s

		// /END

		setFlyTrue();
		walk = false;
		shoot = false;
		colorization = new Color(0.9f, 1, 1, 1);

		// SHOOT TOUT LE TEMPS DURANT CETTE PHASE

		if (timerProjectileSend.doAction(delta))
		{
			for (int i = 0; i < 15; i++)
			{
				Projectile p = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
				p.construct(Projectiles.ENEMY_BOSS_3);
				p.init(this);
				GlobalController.bulletControllerEnemy.addActor(p);
			}
			S.c().play(TyrianSound.soundEffect_boss3_magic, player, this);
		}

		// MOVE ON A BLOCK

		if (!moveOnBlockDone)
		{
			RunnableAction a = new RunnableAction()
			{
				@Override
				public void run()
				{
					disablePhysics();
				}
			};

			Vector2 newPosition = EnemyPopConstants.getInstance().getSmallBlocksPosition();
			MoveToAction b = Actions.moveTo(newPosition.x, newPosition.y + 90, 3.5f);

			RunnableAction c = new RunnableAction()
			{
				@Override
				public void run()
				{
					enablePhysics();
				}
			};

			clearActions();
			SequenceAction sequenceAction = new SequenceAction(a, Actions.delay(1), b, Actions.delay(1), c);
			addAction(sequenceAction);
			moveOnBlockDone = true;
		}

	}

	public void meteore_rain(float delta)
	{
		walk = false;
		shoot = false;
		colorization = new Color(1, 0.9f, 1, 1);

		if (timerMeteorPoping.doAction(delta))
		{
			for (int i = 0; i < 2; i++)
			{
				Projectile p = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
				p.construct(Projectiles.ENEMY_METEORE);
				p.init(this);
				p.setPosition(new Random().nextInt(MyGdxGame.VIRTUAL_WORLD_WIDTH), MyGdxGame.VIRTUAL_WORLD_HEIGHT - 300);
				GlobalController.bulletControllerEnemy.addActor(p);
			}
			S.c().play(TyrianSound.soundEffect_meteorFall, player, this);
		}
	}

	// --------------------------------------------------------Fly Animation Gestion

	private Animation	flyLeft;
	private Animation	flyRight;
	private boolean		flyAnimation	= false;

	private Animation getFlyAnimation()
	{
		if (direction == Direction.LEFT_DIRECTION)
		{
			return flyLeft;
		} else
		{
			return flyRight;
		}
	}

	public void setFlyTrue()
	{
		if (!flyAnimation)
		{
			flyAnimation = true;
			animationStateTime = 0;
		}
	}

	public void setFlyFalse()
	{
		flyAnimation = false;
		animationStateTime = 0;
	}

	@Override
	public Animation getCurrentAnimation()
	{
		if (flyAnimation)
		{
			return getFlyAnimation();
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
