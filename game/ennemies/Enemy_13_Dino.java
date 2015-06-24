package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;

import java.util.Random;

import ressources.R;
import ressources.S;
import ressources.S.TyrianSound;
import utilities.enumerations.Direction;

import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_13_Dino extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int	MAX_LIFE		= 250;
	private final static int	XP_GAIN_ON_KILL	= 140;
	private final static int	ATTACK_POWER	= 25;
	private final static float	MOVE_SPEED_MIN	= 2.5f;
	private final static float	MOVE_SPEED_MAX	= 5.5f;
	private final static int	WIDTH			= 95;

	private float				enemyCoef;

	public Enemy_13_Dino(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_dino_walk);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getFallOrPopablePosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		walk = true;
		shoot = false;
	}

	@Override
	protected void enemies_initialisation()
	{
		m_goldQuantity = 8;
		m_goldValue = 10;
		increaseStats(enemyCoef);
	}

	private Timer	timer	= new Timer(0.4f);

	@Override
	public void act(float delta)
	{
		super.act(delta);
		EnemyComportements.followPlayerAndPatrolOnGround(this, player);
		EnemyComportements.physicalAttack(this, player);

		if (timer.doAction(delta))
		{
			S.c().playRandomPitch(TyrianSound.soundEffect_enemies_bumpyEnemyJump, player, this);
		}

	}
}
