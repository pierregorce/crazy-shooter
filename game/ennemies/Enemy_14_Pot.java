package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.projectiles.Projectile;
import globals.Projectiles;

import java.util.Random;

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

/**
 * Wizard : Can shoot, Can walk, Can't jump, Se deplace jusqu'a une distance de X px du player puis attend. Si le player est au même Y alors shoot. Si le player est trop
 * prés alors recule //TODO
 * 
 * @author Pierre
 *
 */
public class Enemy_14_Pot extends Enemies
{
	private final static int	MAX_LIFE		= 500;
	private final static int	XP_GAIN_ON_KILL	= 70;
	private final static int	ATTACK_POWER	= 20;
	private final static float	MOVE_SPEED_MIN	= 1;
	private final static float	MOVE_SPEED_MAX	= 2;
	private final static int	WIDTH			= 105;

	private float				enemyCoef;

	public Enemy_14_Pot(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_pot_idle);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getSmallBlocksPosition();
		setPosition(position.x, position.y);

		editCollisionBox(40, 20, 0);
		direction = Direction.RIGHT_DIRECTION;
		walk = false;
		shoot = true;
		bumpSensibility = false;
	}

	Timer	timer_shooting	= new Timer(1f, 2f);	// for shoot animation

	@Override
	protected void enemies_initialisation()
	{
		shootCooldwon = 0.2f;
		m_shootPauseTime = 0f;
		m_shootRunTime = 1f;
		m_goldQuantity = 15;
		m_goldValue = 10;
		increaseStats(enemyCoef);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		if (timer_shooting.doAction(delta))
		{
			shoot = true;
		} else
		{
			shoot = false;
		}
		this.faireFaceTo(player);
	}

	@Override
	public void shootEngine()
	{
		Projectile p = new Projectile(Projectiles.TAPIS);
		p.init(this);
		float min = 4f;
		float max = 6;
		float time = new Random().nextFloat() * (max - min) + min;
		// add do endng effect
		p.addAction(Actions.delay(time, Actions.removeActor(p)));
		GlobalController.bulletControllerEnemy.addActor(p);
	}

	@Override
	public void setShootAnimation()
	{
		shootRight = new Animation(0.1f, R.c().enemy_pot_shoot);
		shootLeft = R.invertAnimation(R.c().enemy_pot_shoot, 0.1f);
	}

}
