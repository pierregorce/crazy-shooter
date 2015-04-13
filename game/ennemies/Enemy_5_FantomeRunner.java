package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.fx.MetalParticle;
import game.fx.ParticleColors;
import game.sound.MusicManager;

import java.util.Random;

import ressources.R;
import ressources.S;
import utilities.enumerations.Direction;

import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;

public class Enemy_5_FantomeRunner extends Enemies
{
	/*
	 * Walking && charge/takl enemy Charge quand le Y est identique sinon attends a une distance random un peu comme celui qui tire
	 */

	private final static int	MAX_LIFE		= 60;
	private final static int	XP_GAIN_ON_KILL	= 45;
	private final static int	ATTACK_POWER	= 25;
	private final static float	MOVE_SPEED_MIN	= 6;
	private final static float	MOVE_SPEED_MAX	= 8;
	private final static int	WIDTH			= 60;

	private float				m_initialVelocity;
	private float				enemyCoef;

	public Enemy_5_FantomeRunner(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_fantome_walk);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getGroundBlockPosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);

		m_initialVelocity = maxVelocityX;
		maxJumpSpeedY = 11;
	}

	@Override
	protected void enemies_initialisation()
	{
		m_goldQuantity = 4;
		m_goldValue = 4;
		increaseStats(enemyCoef);
	}

	@Override
	public void physicalAttackEngine()
	{
		super.physicalAttackEngine();

		// Lance sa charge a 400px de player
		if (Math.abs(getX() - player.getX()) < 600) // FIXME && getY() <= GameStage.GROUND_HEIGHT)
		{
			maxVelocityX = 20;
		} else
		{
			maxVelocityX = m_initialVelocity;
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		EnemyComportements.navigateOverWorld(this, player);
		EnemyComportements.physicalAttack(this, player);
		walk = true; // Pour empecher l'enemy de s'arreter quand phyical attack

		jump = true;

		if (getCollisionBlock() != null)
		{
			for (int i = 0; i < 4; i++)
			{
				MetalParticle metalParticle = GlobalController.metalParticlePool.obtain();
				metalParticle.init(getCenterX(), getCenterY(), ParticleColors.getInstance().getIceColor());
				GlobalController.particleController.addActor(metalParticle);
			}
			if (maxVelocityX == m_initialVelocity)
			{
				S.c().soundEffect_enemies_bumpyEnemyJump.play(MusicManager.sfxVolume);
			} else
			{
				S.c().soundEffect_enemies_bumpyEnemyCharge.play(MusicManager.sfxVolume);
			}
		}
	}

}
