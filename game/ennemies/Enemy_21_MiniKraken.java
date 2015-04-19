package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.path.WayPoint;

import java.util.Random;

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.oasix.crazyshooter.Block;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_21_MiniKraken extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int				MAX_LIFE		= 250;
	private static int						XP_GAIN_ON_KILL	= 250;
	private final static int				ATTACK_POWER	= 25;
	private final static float				MOVE_SPEED_MIN	= 2.5f;
	private final static float				MOVE_SPEED_MAX	= 7.5f;

	private final static TextureRegion[]	walk_frames		= R.c().enemy_minikraken_walk;
	private final static int				PIXEL_SIZE		= 4;
	private final static int				WIDTH			= walk_frames[0].getRegionWidth() * PIXEL_SIZE;

	private float							enemyCoef;

	public Enemy_21_MiniKraken(Player player, float enemyCoef)
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
		shootCooldwon = 0.25f;
		m_shootPauseTime = 0f;
		m_shootRunTime = 1f;
		m_goldQuantity = 10;
		m_goldValue = 10;
		increaseStats(enemyCoef);
	}

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
		EnemyComportements.physicalAttackOnly(this, player);
		shoot = false;
		maxVelocityX = MOVE_SPEED_MAX;

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

	public Enemy_21_MiniKraken(Player player, float enemyCoef, Vector2 position)
	{
		this(player, enemyCoef);
		setPosition(position.x, position.y);
		XP_GAIN_ON_KILL = 0;
		m_goldQuantity = 0;
		m_goldValue = 0;
	}

}
