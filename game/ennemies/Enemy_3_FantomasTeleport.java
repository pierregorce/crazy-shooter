package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.projectiles.Projectile;
import game.sound.MusicManager;
import globals.Projectiles;

import java.util.Random;

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.Block;
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
public class Enemy_3_FantomasTeleport extends Enemies
{
	private final static int	LIFE_MAX		= 95;
	private final static int	XP_GAIN_ON_KILL	= 100;
	private final static int	ATTACK_POWER	= 45;
	private final static float	MOVE_SPEED_MIN	= 2;
	private final static float	MOVE_SPEED_MAX	= 4;
	private final static int	WIDTH			= 130;

	private float				enemyCoef;
	private Vector2				m_oldBlockPosition;

	public Enemy_3_FantomasTeleport(Player player, float enemyCoef)
	{
		super(player, LIFE_MAX, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH,
				R.c().enemy_fantomas_walk);
		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getSmallBlocksPosition();
		m_oldBlockPosition = position;
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		editCollisionBox(40, 20, 0);

		bumpSensibility = false;
		walk = true;
		shoot = false;
		setTeleportAnimation();

	}

	@Override
	protected void enemies_initialisation()
	{
		shootCooldwon = 0.15f;
		m_shootPauseTime = 0.6f;
		m_shootRunTime = 0.2f;
		m_goldQuantity = 6;
		m_goldValue = 10;
		increaseStats(enemyCoef);
	}

	@Override
	public void enemyDirectionEngine()
	{
		// Patrouille si le player est loin
		float distance = Math.abs(getX() - player.getX());
		walk = false;
		shoot = false;

		if (distance <= 500)
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

		// Control terminal pour ne plus tirer et marcher durant une teleportation
		if (isTeleportAnimationRunning())
		{
			walk = false;
			shoot = false;
		}

	}

	private Timer	teleportationTimer	= new Timer(4.5f);

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (teleportationTimer.doAction(delta))
		{
			setTeleportDisparitionTrue();
		}

		if (teleportVanishAnimation && m_animation.isAnimationFinished(animationStateTime))
		{
			Vector2 newPosition;
			do
			{
				newPosition = EnemyPopConstants.getInstance().getSmallBlocksPosition();
			} while (newPosition.x == m_oldBlockPosition.x);

			m_oldBlockPosition = newPosition;
			setCollisionBlock(null);
			setPosition(newPosition.x, newPosition.y + 0);
			faireFaceTo(player);
			setAnimationTeleportFalse();
			setTeleportApparationTrue();
		}

		if (teleportApparitionAnimation && m_animation.isAnimationFinished(animationStateTime))
		{
			setAnimationTeleportFalse();
		}
	}

	@Override
	public void shootEngine()
	{
		Vector2 position = new Vector2(getX(), getY());
		Vector2 playerPosition = new Vector2(player.getX(), player.getY());
		if (position.dst(playerPosition) <= 500)
		{
			Projectile p = new Projectile(Projectiles.ENEMY_FANTOMAS);
			p.init(this);
			GlobalController.bulletControllerEnemy.addActor(p);
			R.c().soundEffect_enemies_wizardFireball[new Random().nextInt(R.c().soundEffect_enemies_wizardFireball.length)]
					.play(MusicManager.sfxVolume);
		}
	}

	@Override
	public void setShootAnimation()
	{
		shootRight = new Animation(0.1f, R.c().enemy_fantomas_shoot);
		shootLeft = R.invertAnimation(R.c().enemy_fantomas_shoot, 0.1f);
	}

	public void setTeleportAnimation()
	{
		teleport_apparitionRight = new Animation(0.1f, R.c().enemy_fantomas_teleport_out);
		teleport_apparitionLeft = R.invertAnimation(R.c().enemy_fantomas_teleport_out, 0.1f);

		teleport_vanishRight = new Animation(0.1f, R.c().enemy_fantomas_teleport);
		teleport_vanishLeft = R.invertAnimation(R.c().enemy_fantomas_teleport, 0.1f);
	}

	// ---------------------Teleport Gestion

	private Animation	teleport_vanishLeft;
	private Animation	teleport_vanishRight;
	private Animation	teleport_apparitionLeft;
	private Animation	teleport_apparitionRight;
	private boolean		teleportApparitionAnimation	= false;
	private boolean		teleportVanishAnimation		= false;

	private Animation getTeleportApparitionAnimation()
	{
		if (direction == Direction.LEFT_DIRECTION)
		{
			return teleport_apparitionLeft;
		} else
		{
			return teleport_apparitionRight;
		}

	}

	private Animation getTeleportVanishAnimation()
	{
		if (direction == Direction.LEFT_DIRECTION)
		{
			return teleport_vanishLeft;
		} else
		{
			return teleport_vanishRight;
		}
	}

	public void setTeleportApparationTrue()
	{
		teleportApparitionAnimation = true;
		animationStateTime = 0;
		// SFX
		R.c().soundEffect_enemies_wizardSpawn[new Random().nextInt(R.c().soundEffect_enemies_wizardSpawn.length)].play(MusicManager.sfxVolume);
	}

	public void setTeleportDisparitionTrue()
	{
		teleportVanishAnimation = true;
		animationStateTime = 0;
		// SFX
		R.c().soundEffect_enemies_wizardTeleport[new Random().nextInt(R.c().soundEffect_enemies_wizardTeleport.length)].play(MusicManager.sfxVolume);
	}

	public void setAnimationTeleportFalse()
	{
		teleportVanishAnimation = false;
		teleportApparitionAnimation = false;
		animationStateTime = 0;
	}

	public boolean isTeleportAnimationRunning()
	{
		return teleportApparitionAnimation || teleportVanishAnimation;
	}

	@Override
	public Animation getCurrentAnimation()
	{
		if (teleportApparitionAnimation)
		{
			return getTeleportApparitionAnimation();
		} else if (teleportVanishAnimation)
		{
			return getTeleportVanishAnimation();
		} else
		{
			return super.getCurrentAnimation();
		}
	}

}
