package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.projectiles.Projectile;
import globals.Projectiles;

import java.util.Random;

import ressources.R;
import ressources.S;
import ressources.S.TyrianSound;
import utilities.enumerations.Direction;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_16_Eyes extends Enemies
{

	// Flying basic enemy
	// Move left then right, don't care about player

	private final static int	MAX_LIFE		= 175;
	private final static int	XP_GAIN_ON_KILL	= 50;
	private final static int	ATTACK_POWER	= 15;
	private final static float	MOVE_SPEED_MIN	= 3;
	private final static float	MOVE_SPEED_MAX	= 6;
	private final static float	MOVE_SPEED		= new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN;
	private final static int	WIDTH			= 80;

	private float				enemyCoef;

	public Enemy_16_Eyes(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, MOVE_SPEED, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_eyes_walk);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getFlyPosition();
		setPosition(position.x, position.y);
		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);
		disablePhysics();
		increaseStats(enemyCoef);

	}

	@Override
	protected void enemies_initialisation()
	{
		m_goldQuantity = 8;
		m_goldValue = 12;
		increaseStats(enemyCoef);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		pick(delta);
		EnemyComportements.navigateOverWorld(this, player);
	}

	private Timer	timerPick	= new Timer(2f);
	private boolean	canPick		= false;
	private boolean	pickReady	= true;

	@Override
	public void physicalAttackEngine()
	{
		super.physicalAttackEngine();

		// si player est en dessous
		// alors attaque

		if (getRight() > player.getBouncingBox().getX() - 20 && getX() < player.getBouncingBox().getX() + player.getBouncingBox().getWidth() + 20 && player.getTop() < getY())
		{
			canPick = true;
		} else
		{
			canPick = false;
		}
	}

	private void pick(float delta)
	{
		if (timerPick.doAction(delta))
		{
			pickReady = true; // Repasse ready toute les Xs
		}

		if (canPick && pickReady)
		{
			pickAction(delta);
			pickReady = false;
		}
	}

	private void pickAction(float delta)
	{
		Projectile p = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
		p.construct(Projectiles.FALLING_BOMB);
		p.init(this);
		GlobalController.bulletControllerEnemy.addActor(p);
		S.c().play(TyrianSound.soundEffect_meteorFall, player, this);
	}

}
