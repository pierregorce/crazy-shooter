package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.path.WayPoint;

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

public class Enemy_17_KingWizard extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int				MAX_LIFE		= 1000;
	private final static int				XP_GAIN_ON_KILL	= 550;
	private final static int				ATTACK_POWER	= 25;
	private final static float				MOVE_SPEED_MIN	= 7f;
	private final static float				MOVE_SPEED_MAX	= 7f;

	private final static TextureRegion[]	walk_frames		= R.c().enemy_kingWizard_walk;
	private final static int				PIXEL_SIZE		= 4;
	private final static int				WIDTH			= walk_frames[0].getRegionWidth() * PIXEL_SIZE;

	private float							enemyCoef;

	public Enemy_17_KingWizard(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, walk_frames);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getGroundBlockPosition();
		setPosition(position.x, position.y);
		editCollisionBox(60, 30, 0);
		direction = Direction.RIGHT_DIRECTION;
		walk = false;
		shoot = false;
		setOriginX(getWidth() / 2);
	}

	@Override
	protected void enemies_initialisation()
	{
		shootCooldwon = 0.08f;
		m_shootPauseTime = 0f;
		m_shootRunTime = 1f;
		m_goldQuantity = 10;
		m_goldValue = 10;
		increaseStats(enemyCoef);
	}

	private enum States
	{
		CLIMB,
		PATROL,
		SWITCH;
	}

	private States		wizardStates			= States.CLIMB;

	// Commence a climb car on est au sol
	private Timer		m_switchPlateformTimer	= new Timer(15f);
	private Block		finalBlockObjective		= GlobalController.blockController.getTopsBlocks();
	private Block		currentBlockObjective	= null;
	private WayPoint	target;
	private boolean		controlled				= false;

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (m_switchPlateformTimer.doAction(delta))
		{
			finalBlockObjective = GlobalController.blockController.getTopsBlocks();	// Block_5
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
			System.out.println("-----------------------------------------------");
		}

		// Si non controlled alors
		// Go to target si non null
		// Patrol si target est null

		// Si controlled alors on ne touche pas a ses variables de mouvement et on giveAction 1 fois.

		if (!controlled)
		{
			if (target == null)
			{
				EnemyComportements.patrolOnBlock(this, player);
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

		// if (getY() == GameStage.GROUND_HEIGHT)
		// {
		// System.out.println("Je suis tombé au sol, je vais devoir remonter");
		// controlled = false;
		// if (wizardStates != States.CLIMB)
		// {
		// wizardStates = States.CLIMB;
		// }
		// }

		// if (wizardStates == States.CLIMB && m_wayPointObjective.getWayPointObjective() == null)
		// {
		// System.out.println("J'ai atteint mon objectif final.");
		// wizardStates = States.PATROL;
		// }
		//
		// if (wizardStates == States.CLIMB)
		// {
		// grimpe();
		// }

		if (wizardStates == States.PATROL)
		{
			if (m_switchPlateformTimer.doAction(delta))
			{
				wizardStates = States.SWITCH;
				System.out.println("Je switch");
			} else
			{
				patrouille();
				System.out.println("Je patrouille");
				maxVelocityX = 3;
			}
		} else
		{
			maxVelocityX = MOVE_SPEED_MAX;
		}

		if (wizardStates == States.SWITCH)
		{
			System.out.println("Je veux switch");
			walk = true;
			direction = Direction.RIGHT_DIRECTION;
			// TODO
		}
	}

	public void patrouille()
	{
		// Patrouille si le player est loin
		float distance = Math.abs(getX() - player.getX());
		if (distance <= 200)
		{
			// Tir
			walk = false;
			shoot = true;
			faireFaceTo(player);
		} else
		{
			// Patrouille
			Block block = getCollisionBlock();

			if (block != null)
			{
				walk = true;
				shoot = false;
				if (getX() <= block.getX())
				{
					direction = Direction.RIGHT_DIRECTION;
				}
				if (getRight() >= block.getRight())
				{
					direction = Direction.LEFT_DIRECTION;
				}
			}
		}
	}

	@Override
	public void shootEngine()
	{
		// WizardProjectile bullet = ProjectilePoolFactory.getInstance().wizardBulletPool.obtain();
		// bullet.init(this, new Vector2(getRight(), getCenterY()));
		// GlobalController.bulletControllerEnemy.addActor(bullet);
		for (int i = 0; i < 4; i++)
		{

			// Vector2 vector = new Vector2(getRight(), getY() + 30);
			// Projectile bullet = ProjectilePoolFactory.getInstance().player_w6_projectilePool.obtain();
			// bullet.init(this, vector);
			// GlobalController.bulletControllerEnemy.addActor(bullet);
		}

		// BOSS_1_Projectile bullet = new BOSS_1_Projectile();
		// bullet.init(this, new Vector2(getCenterX(), getTop()));
		// GlobalController.bulletControllerEnemy.addActor(bullet);
	}

	@Override
	public void setShootAnimation()
	{
		shootRight = new Animation(0.1f, R.c().enemy_kingWizard_attack);
		shootLeft = R.invertAnimation(R.c().enemy_kingWizard_attack, 0.1f);
	}

}
