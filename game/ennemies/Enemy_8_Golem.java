package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.sound.MusicManager;

import java.util.Random;

import ressources.R;
import ressources.S;
import utilities.enumerations.Direction;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.Player;

public class Enemy_8_Golem extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int	MAX_LIFE		= 200;
	private final static int	XP_GAIN_ON_KILL	= 85;
	private final static int	ATTACK_POWER	= 20;
	private final static float	MOVE_SPEED_MIN	= 1;
	private final static float	MOVE_SPEED_MAX	= 3;
	private final static int	WIDTH			= 150;

	private Player				player;
	private Sound				soundSlash		= S.c().soundEffect_enemies_umbrellaSlash;
	private boolean				soundPlaying	= false;
	private float				enemyCoef;

	public Enemy_8_Golem(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_golem_walk);

		this.player = player;
		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getGroundBlockPosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);

		attackRight = new Animation(0.07f, R.c().enemy_golem_attack);
		attackLeft = R.invertAnimation(R.c().enemy_golem_attack, 0.07f);

		editCollisionBox(85, getHeight() - 20, 30);
		editBouncingBox(85, getHeight(), 30);
	}

	@Override
	protected void enemies_initialisation()
	{
		m_goldQuantity = 6;
		m_goldValue = 12;
		increaseStats(enemyCoef);
	}

	public Enemy_8_Golem(Player player, float enemyCoef, Vector2 position)
	{
		this(player, enemyCoef);
		setPosition(position.x, position.y);
	}

	@Override
	public void act(float delta)
	{

		super.act(delta);

		if (attackAnimation && m_animation.isAnimationFinished(animationStateTime))
		{
			setAnimationTeleportFalse();
		}
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
			setTeleportApparationTrue(); // PERMET DE LANCER LACTION
			// Fait perdre de la vie au player
			player.setLosingLifeEvent(true);
			player.setLoosingLiveValueEvent(getAttackPower());

			if (!soundPlaying)
			{
				soundSlash.loop(MusicManager.sfxVolume);
				soundPlaying = true;
			}

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
			if (!attackAnimation)
			{
				setWalk(true);
				soundSlash.stop();
				soundPlaying = false;
			}

		}

	}

	// --------------------------------------------------------Attack Gestion

	private Animation	attackLeft;
	private Animation	attackRight;
	private boolean		attackAnimation	= false;

	private Animation getAttackAnimation()
	{
		if (direction == Direction.LEFT_DIRECTION)
		{
			return attackLeft;
		} else
		{
			return attackRight;
		}
	}

	public void setTeleportApparationTrue()
	{
		if (!attackAnimation)
		{
			attackAnimation = true;

			animationStateTime = 0;
		}
	}

	public void setAnimationTeleportFalse()
	{
		attackAnimation = false;
		animationStateTime = 0;
	}

	@Override
	public Animation getCurrentAnimation()
	{
		if (attackAnimation)
		{
			return getAttackAnimation();
		} else
		{
			return super.getCurrentAnimation();
		}
	}

}
