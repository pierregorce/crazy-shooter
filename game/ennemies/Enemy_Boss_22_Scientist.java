package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.fx.Beam;
import game.fx.ParticleColors;
import game.hud.BossBar;
import game.path.WayPoint;
import game.pop.PopMessage;
import game.pop.PopMessage.MessageType;
import game.projectiles.Projectile;
import globals.Projectiles;
import globals.Worlds;

import java.util.Random;

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.oasix.crazyshooter.Block;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_Boss_22_Scientist extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int				MAX_LIFE		= 8000;
	private static int						XP_GAIN_ON_KILL	= 2500;
	private final static int				ATTACK_POWER	= 25;
	private final static float				MOVE_SPEED_MIN	= 2.5f;
	private final static float				MOVE_SPEED_MAX	= 7.5f;

	private final static TextureRegion[]	walk_frames		= R.c().enemy_scientist_boss_walk;
	private final static int				PIXEL_SIZE		= 10;
	private final static int				WIDTH			= walk_frames[0].getRegionWidth() * PIXEL_SIZE;

	private float							enemyCoef;

	public Enemy_Boss_22_Scientist(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, walk_frames);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getFallOrPopablePosition();
		setPosition(position.x, position.y);
		editCollisionBox(60, 30, 0);
		direction = Direction.RIGHT_DIRECTION;
		walk = false;
		shoot = false;
		setOriginX(getWidth() / 2);

		BossBar.active = true;
		BossBar.enemy = this;
		BossBar.setBossName("BOSS #4 : " + Worlds.WOLRD_5.finalBoss);
		bumpSensibility = false;
	}

	@Override
	protected void enemies_initialisation()
	{
		shootCooldwon = 0.25f;
		m_shootPauseTime = 0f;
		m_shootRunTime = 1f;

		increaseStats(enemyCoef);

		if (Worlds.WOLRD_5.isCompleted())
		{
			m_goldValue = 1;
			m_goldQuantity = 10;
		} else
		{
			m_goldValue = 60;
			m_goldQuantity = 20;
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		swichBetweenBossStates(delta);

		EnemyComportements.physicalAttack(this, player);
		// shoot = false;
		// maxVelocityX = MOVE_SPEED_MAX;

		switch (m_bossPhase) {
		case PATROL:
			shoot = false;
			switchPlateformControlleur(delta);
			break;

		case LASER_ATTACK:
			walk = false;

			if (getActions().size > 0)
			{
				jump = true;
			}

			if (!laserAttackDone)
			{
				faireFaceTo(player);
				jump = true;

				if (getActions().size == 0)
				{
					addAction(Actions.delay(0.9f, Actions.run(new Runnable()
					{
						@Override
						public void run()
						{
							shoot = true;
							laserAttackDone = true;
						}
					})));
				}
			}
			break;

		default:
			break;
		}

		updateBeams();

		if (walk)
		{
			doPopDuringWalk(delta);
		}
	}

	private enum BossPhases
	{
		PATROL(8),
		LASER_ATTACK(5);

		public float	phaseDuration;

		private BossPhases(float phaseDuration)
		{
			this.phaseDuration = phaseDuration;
		}
	}

	private BossPhases	m_bossPhase		= BossPhases.PATROL;					// Commence à patrol
	private Timer		timer			= new Timer(m_bossPhase.phaseDuration); // Timer qui devient vrai lorqu'il faut changer de phase;
	private boolean		laserAttackDone	= false;

	private void swichBetweenBossStates(float delta)
	{
		if (timer.doAction(delta))
		{
			// Changement phase
			switch (m_bossPhase) {
			case PATROL:
				m_bossPhase = BossPhases.LASER_ATTACK;
				GlobalController.fxController.addActor(new PopMessage(this, MessageType.BOSS_CRY_5));
				maxJumpSpeedY = 5;
				m_switchPlateformTimer.resetForLunchImmediatly();
				controlled = false;
				break;
			case LASER_ATTACK:
				m_bossPhase = BossPhases.PATROL;
				laserAttackDone = false;
				maxJumpSpeedY = 30f;
				break;
			default:
				break;
			}
			// Repercution sur le timer
			timer = new Timer(m_bossPhase.phaseDuration);
		}

	}

	// Commence a climb car on est au sol
	private Timer		m_switchPlateformTimer	= new Timer(15f);
	private Block		finalBlockObjective		= GlobalController.blockController.getTopsBlocks();
	private Block		currentBlockObjective	= null;
	private WayPoint	target;
	private boolean		controlled				= false;

	private void switchPlateformControlleur(float delta)
	{
		if (m_switchPlateformTimer.doAction(delta))
		{
			finalBlockObjective = GlobalController.blockController.getTopsBlocks();
		}

		// Récupère des données relative au pathfinding
		if (getCollisionBlock() != null)
		{
			System.out.println("-----------------------------------------------");
			currentBlockObjective = GlobalController.blockController.getBlockObjective(finalBlockObjective, this);
			if (currentBlockObjective != null)
			{
				System.out.println("Player say : Mon block current obj est donc le : " + currentBlockObjective.debugNumber);
				target = GlobalController.blockController.getWayPointToCome(this, currentBlockObjective);
			} else
			{
				System.out.println("Je n'ai rien trouvé WTF !?#!");
				target = null;
			}
		}

		// Si non controlled alors
		// Go to target si non null
		// Patrol si target est null

		// Si controlled alors on ne touche pas a ses variables de mouvement et on giveAction 1 fois.

		if (!controlled)
		{
			if (target == null)
			{
				EnemyComportements.followPlayerAndPatrol(this, player);
			} else
			{
				if (getCollisionBox().contains(target.getCenterX(), target.getCenterY()))
				{
					setWalk(false);
					controlled = true;
				} else
				{
					EnemyComportements.goToTarget(this, target);
				}
			}
		}

		if (controlled && getActions().size == 0 && target != null)
		{
			target.giveAction(this);

			addAction(new SequenceAction(Actions.delay(0.7f), new RunnableAction()
			{
				@Override
				public void run()
				{
					controlled = false;
				}
			}));
		}
	}

	public Timer	timerPopSol	= new Timer(0.03f);

	private void doPopDuringWalk(float delta)
	{
		if (timerPopSol.doAction(delta))
		{
			Projectile p = new Projectile(Projectiles.ENEMY_BOSS_5_TAPIS);
			p.init(this);
			float min = 1.3f;
			float max = 1.35f;
			float time = new Random().nextFloat() * (max - min) + min;
			// add do endng effect
			p.addAction(Actions.delay(time, Actions.removeActor(p)));
			GlobalController.bulletControllerEnemy.addActor(p);
			p.setColor(ParticleColors.getInstance().getVioletWaste()[new Random().nextInt(ParticleColors.getInstance().getVioletWaste().length)]);
			p.setSize(p.getWidth() * 0.6f, p.getHeight() * 0.6f);
		}
	}

	private Beam	beam;	// Laser Beam

	private void updateBeams()
	{

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

		if (beam != null && beam.isVisible())
		{
			Vector2 vector = new Vector2(getRight() + 0, getCenterY() - 20);
			float dist = Math.abs(getCenterX() - player.getCenterX());
			beam.init(vector, dist, this);
			beam.setScientistSpecification();
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
	public void setShootAnimation()
	{
		shootRight = new Animation(0.07f, R.c().enemy_scientist_boss_shoot);
		shootLeft = R.invertAnimation(R.c().enemy_scientist_boss_shoot, 0.07f);

	}

}
