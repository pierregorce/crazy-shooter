package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.hud.BossBar;
import game.pop.PopMessage;
import game.pop.PopMessage.MessageType;
import game.projectiles.Projectile;
import globals.Projectiles;
import globals.Worlds;

import java.util.Random;

import ressources.ActionFactory;
import ressources.DrawableAnimation;
import ressources.R;
import ressources.Ressource;
import ressources.S;
import ressources.S.TyrianSound;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Pools;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_Boss_10_Golem extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int	MAX_LIFE			= 2600;
	private final static int	XP_GAIN_ON_KILL		= 550;
	private final static int	ATTACK_POWER		= 35;												// PHYSICAL ATAK
	private final static float	MOVE_SPEED_MIN		= 1;
	private final static float	MOVE_SPEED_MAX		= 3;
	private final static int	WIDTH				= 270;

	private Color				normalColor			= new Color(214 / 255f, 119 / 255f, 105 / 255f, 1);
	private Color				tackleColor			= new Color(140 / 255f, 140 / 255f, 140 / 255f, 1);

	private boolean				soundSlashPlaying	= false;
	private Timer				timerSpeak			= new Timer(5f);
	private Timer				timerSpit			= new Timer(1f);

	// private Sound soundSlash = S.c().soundEffect_boss1_umbrellaSlash;

	public Enemy_Boss_10_Golem(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_golem_walk);

		Vector2 position = EnemyPopConstants.getInstance().getGroundBlockPosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);

		attackRight = new Animation(0.07f, R.c().enemy_golem_attack);
		attackLeft = R.invertAnimation(R.c().enemy_golem_attack, 0.07f);

		editCollisionBox(160, getHeight() - 130, 30);
		editBouncingBox(160, getHeight() - 85, 30);

		// Increase stat sert a rien ici car classe unique
		increaseStats(enemyCoef);

		// BOSS CAPACITY
		colorization = normalColor;
		BossBar.active = true;
		BossBar.enemy = this;
		BossBar.setBossName("BOSS #1 : " + Worlds.WOLRD_1.finalBoss);
	}

	@Override
	protected void enemies_initialisation()
	{
		// shootCooldwon = 0.2f;
		// m_shootPauseTime = 0f;
		// m_shootRunTime = 1f;

		bumpSensibility = false;
		maxJumpSpeedY = 7;

		if (Worlds.WOLRD_1.isCompleted())
		{
			m_goldValue = 1;
			m_goldQuantity = 10;
		} else
		{
			m_goldValue = 10;
			m_goldQuantity = 50;
		}

		increaseStats(0);
	}

	private enum State
	{
		TACKLE,
		WALK,
		PREPARATION;
	}

	private State	m_state;
	Timer			timerShoot		= new Timer(1, 3);
	Timer			timerCharge		= new Timer(5);
	Timer			timerMessage	= new Timer(0.4f);

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (attackAnimation && m_animation.isAnimationFinished(animationStateTime))
		{
			setAttackFalse();
		}

		if (timerShoot.doAction(delta) && getActions().size == 0)
		{
			shoot = true;
		} else
		{
			shoot = false;
		}

		// ETAT PREPARATION
		if (m_state == State.PREPARATION)
		{
			jump = true;
		}

		if (timerCharge.doAction(delta))
		{
			jump = false;
			walk = false;
			float lenght = 800;

			// PREVENT TACKLE
			colorization = tackleColor;
			SequenceAction action = new SequenceAction();
			m_state = State.PREPARATION;

			S.c().play(TyrianSound.soundEffect_boss1_stomp);

			action.addAction(Actions.delay(0.7f));

			action.addAction(new RunnableAction()
			{

				@Override
				public void run()
				{
					S.c().play(TyrianSound.soundEffect_boss1_dash);
					m_state = State.TACKLE;
					walk = false;
				}

			});

			if (direction == Direction.LEFT_DIRECTION)
			{
				action.addAction(Actions.moveTo(getX() - lenght, getY(), 0.5f));
			} else
			{
				action.addAction(Actions.moveTo(getX() + lenght, getY(), 0.5f));
			}

			action.addAction(new RunnableAction()
			{

				@Override
				public void run()
				{
					colorization = normalColor;
					m_state = State.WALK;

				}

			});
			addAction(action);
		}

		if (getActions().size > 0 && m_state == State.TACKLE)
		{
			DrawableAnimation drawableAnimation = new DrawableAnimation(0.05f, R.c().jumpUpFxAnimation);
			Ressource ressource = new Ressource(drawableAnimation, getCenterX(), getY(), 40, ActionFactory.getRemoveAction(drawableAnimation.getAnimationDuration() - 0.05f));
			GlobalController.fxController.addActor(ressource);
		}

		if (m_state == State.PREPARATION)
		{
			if (timerMessage.doAction(delta))
			{
				GlobalController.fxController.addActor(new PopMessage(this, MessageType.BOSS_CRY_1));
			}
		} else
		{
			timerMessage.resetForLunchImmediatly();
		}

		if (timerSpeak.doAction(delta))
		{
			S.c().play(TyrianSound.soundEffect_boss1_laugh);
		}

		if (timerSpit.doAction(delta) && shoot)
		{

			S.c().play(TyrianSound.soundEffect_boss1_burp);
		}

	}

	@Override
	public void shootEngine()
	{
		super.shootEngine();
		for (int i = 0; i < Projectiles.ENEMY_BOSS_1.quantityPerShoot; i++)
		{
			Projectile p = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
			p.construct(Projectiles.ENEMY_BOSS_1);
			p.init(this);
			GlobalController.bulletControllerEnemy.addActor(p);
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
			setAttackTrue(); // PERMET DE LANCER LACTION
			// Fait perdre de la vie au player
			player.setLosingLifeEvent(true);
			player.setLoosingLiveValueEvent(getAttackPower());

			if (!soundSlashPlaying)
			{
				// soundSlash.loop(MusicManager.sfxVolume);
				soundSlashPlaying = true;
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
			if (!attackAnimation && getActions().size == 0)
			{
				setWalk(true);
				soundSlashPlaying = false;
				// soundSlash.stop();
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

	public void setAttackTrue()
	{
		if (!attackAnimation)
		{
			attackAnimation = true;
			animationStateTime = 0;
		}
	}

	public void setAttackFalse()
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

	@Override
	public boolean remove()
	{
		if (super.remove())
		{
			BossBar.active = false;
			BossBar.enemy = null;
			return super.remove();
		}
		return super.remove();
	}

}
