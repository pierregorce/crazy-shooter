package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.hud.BossBar;
import game.pop.PopMessage;
import game.pop.PopMessage.MessageType;
import game.projectiles.Projectile;
import globals.Projectiles;

import java.util.Random;

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
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
	Timer						timerCooldownInvoque	= new Timer(0.5f);
	Timer						timerMeteorPoping		= new Timer(0.4f);
	Timer						timerProjectileSend		= new Timer(0.5f);
	boolean						projectileSend			= false;
	float						popPhase2				= MAX_LIFE * 0.3f;

	private enum BossPhases
	{
		PATROL(1),
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
		BossBar.setBossName("BOSS #3 : INVOCATOR"); // WIZARD
		bumpSensibility = false;
		m_goldQuantity = 50;
		m_goldValue = 20;
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
		swichBetweenBossStates(delta);

		if (flyAnimation && m_animation.isAnimationFinished(animationStateTime))
		{
			setFlyFalse();
		}

		// Action en fonction de l'état
		if (m_bossPhase == BossPhases.INVOCK)
		{
			walk = false;
			shoot = true;
			colorization = new Color(0.9f, 0.9f, 0.7f, 1);
			if (timerCooldownInvoque.doAction(delta))
			{
				GlobalController.enemyController.addActor(new Enemy_1_Spider_basic(player, 0.9f, new Vector2(getCenterX(), getY())));
				GlobalController.fxController.addActor(new PopMessage(this, MessageType.BOSS_CRY_3));

				// R.c().soundEffect_boss3_magic[new Random().nextInt(R.c().soundEffect_boss3_magic.length)].play(MusicManager.sfxVolume_BossLow);

			}

		}

		if (m_bossPhase == BossPhases.PATROL)
		{
			walk = true;
			shoot = false;
			colorization = new Color(1, 1, 1, 1);
		}

		if (m_bossPhase == BossPhases.METEOR_RAIN)
		{
			walk = false;
			shoot = false;
			colorization = new Color(1, 0.9f, 1, 1);

			if (timerMeteorPoping.doAction(delta))
			{
				for (int i = 0; i < 3; i++)
				{
					Projectile p = new Projectile(Projectiles.ENEMY_METEORE);
					p.init(this);
					GlobalController.bulletControllerEnemy.addActor(p);
				}
			}

		}
		if (m_bossPhase == BossPhases.FLY_TO_TRHOW_PROJECTILES)
		{
			setFlyTrue();
			walk = false;
			shoot = false;
			colorization = new Color(0.9f, 1, 1, 1);

			if (timerProjectileSend.doAction(delta))
			{
				for (int i = 0; i < 15; i++)
				{
					Projectile p = new Projectile(Projectiles.ENEMY_BOSS_3);
					p.init(this);
					GlobalController.bulletControllerEnemy.addActor(p);
				}
				// R.c().soundEffect_boss3_tearAttack[new Random().nextInt(R.c().soundEffect_boss3_tearAttack.length)].play(MusicManager.sfxVolume_BossLow);
			}

			if (getActions().size == 0)
			{

				RunnableAction action0 = new RunnableAction()
				{
					@Override
					public void run()
					{
						disablePhysics();
					}
				};
				// MoveToAction action1 = Actions.moveTo(getX(), BlockController.OFFSET_3_HEIGHT - 150, 1.25f);

				RunnableAction action2 = new RunnableAction()
				{
					@Override
					public void run()
					{
						projectileSend = true;
					}
				};

				// DelayAction action3 = Actions.delay(3.5f);

				int moveBy;

				if (player.getX() - getX() > 0)
				{
					moveBy = 400;
				} else
				{
					moveBy = -400;
				}

				// MoveToAction action3 = Actions.moveTo(getX() + moveBy, BlockController.OFFSET_3_HEIGHT - 150, 3.5f);

				RunnableAction action4 = new RunnableAction()
				{
					@Override
					public void run()
					{
						projectileSend = false;
					}
				};

				MoveToAction action5 = Actions.moveTo(getX(), getY(), 1.25f);

				RunnableAction action6 = new RunnableAction()
				{
					@Override
					public void run()
					{
						enablePhysics();
					}
				};

				SequenceAction sequenceAction = new SequenceAction();
				sequenceAction.addAction(action0);
				// sequenceAction.addAction(action1);
				sequenceAction.addAction(action2);
				// sequenceAction.addAction(action3);
				sequenceAction.addAction(action4);
				sequenceAction.addAction(action5);
				sequenceAction.addAction(action6);

				addAction(sequenceAction);
			}
		}

	}

	@Override
	public void setLife(int life)
	{
		super.setLife(life);
	}

	@Override
	public void enemyDirectionEngine()
	{
		super.enemyDirectionEngine();
		faireFaceTo(player);
	}

	@Override
	public void physicalAttackEngine()
	{
		super.physicalAttackEngine();

		// si player en collision avec l'enemy -> physical attack
		if (player.getBouncingBox().overlaps(getBouncingBox()))
		{
			// setAttackTrue(); // PERMET DE LANCER LACTION
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
			setWalk(true);

		}

	}

	// --------------------------------------------------------Fly Gestion

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

	@Override
	protected void enemies_initialisation()
	{
		// TODO Auto-generated method stub

	}

}
