package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.sound.MusicManager;

import java.util.Random;

import ressources.R;
import ressources.S;
import utilities.enumerations.Direction;

import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_1_Spider_basic extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int	MAX_LIFE		= 25;
	private static int			XP_GAIN_ON_KILL	= 15;
	private final static int	ATTACK_POWER	= 15;
	private final static float	MOVE_SPEED_MIN	= 1;
	private final static float	MOVE_SPEED_MAX	= 5;
	private final static int	WIDTH			= 85;

	private float				enemyCoef;

	public Enemy_1_Spider_basic(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_spider_walk);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getFallOrPopablePosition();
		setPosition(position.x, position.y);
		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);
		increaseStats(enemyCoef);

	}

	@Override
	protected void enemies_initialisation()
	{
		m_goldQuantity = 1;
		m_goldValue = 3;
		increaseStats(enemyCoef);
	}

	private Timer	timer	= new Timer(0.6f);

	@Override
	public void act(float delta)
	{
		super.act(delta);
		if (timer.doAction(delta))
		{
			S.c().soundEffect_enemies_whiteSlowEnemy.play(MusicManager.sfxVolume);
		}

		EnemyComportements.followPlayerAndPatrolOnGround(this, player);
		EnemyComportements.physicalAttack(this, player);
	}

	public Enemy_1_Spider_basic(Player player, float enemyCoef, Vector2 position)
	{
		this(player, enemyCoef);
		setPosition(position.x, position.y);
		// Provient d'un pop du boss
		XP_GAIN_ON_KILL = 0;
		m_goldQuantity = 0;
		m_goldValue = 0;
	}
}
