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

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.SnapshotArray;
import com.oasix.crazyshooter.GameStage;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_Boss_20_Kraken extends Enemies
{

	/*
	 * Ne peut tomber dans un trou ! DISABLE PHYSICS
	 * 
	 * BOSS 4, tous les 25% se decompose en plein de petit a tuer puis repop du boss On peut par exemple afficher un bandeau de text pour dire ce qu'il faut faire... Tant
	 * que tous les petits pop ne sont pas mort, sera invincible et immobile.
	 * 
	 * Lorsque qu'il n'y a plus de petits, navigate over world à la manière des wasp pour changer de level, tir des bullets à la manière du boss 1 mais qui reste au sol
	 * (tapie)
	 */

	private final static int				MAX_LIFE		= 6800;
	private final static int				XP_GAIN_ON_KILL	= 5000;
	private final static int				ATTACK_POWER	= 55;
	private final static float				MOVE_SPEED_MIN	= 5;
	private final static float				MOVE_SPEED_MAX	= 6;

	private final static TextureRegion[]	walk_frames		= R.c().enemy_kraken_boss_walk;
	private final static int				PIXEL_SIZE		= 4;
	private final static int				WIDTH			= walk_frames[0].getRegionWidth() * PIXEL_SIZE;

	private float							enemyCoef;

	public Enemy_Boss_20_Kraken(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, walk_frames);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getGroundBlockPosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);
		increaseStats(enemyCoef);

		disablePhysics();

		BossBar.active = true;
		BossBar.enemy = this;
		BossBar.setBossName("BOSS #4 : " + Worlds.WOLRD_4.finalBoss);
		bumpSensibility = false;

		// EDIT BOUNCING BOX CAR TROP SIMPLE A TOUCHER VOIR BOSS2
	}

	@Override
	protected void enemies_initialisation()
	{
		// shootCooldwon = 0.2f;
		// m_shootPauseTime = 0f;
		// m_shootRunTime = 1f;

		if (Worlds.WOLRD_4.isCompleted())
		{
			m_goldValue = 1;
			m_goldQuantity = 10;
		} else
		{
			m_goldValue = 60;
			m_goldQuantity = 20;
		}
		increaseStats(0);
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

	// --------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Phase 1 correspond à la pahse, patrol-shoot-change offset | Phase 2 invulnérable
	 */
	enum Phases
	{
		PHASE_1,
		PHASE_2
	}

	private Timer			changeOffsetTimer	= new Timer(6);
	private Timer			shootBullet			= new Timer(3);
	private boolean			changingOffset		= false;
	private Phases			phase				= Phases.PHASE_1;
	private final Integer[]	POPS				= { 7, 8, 10, 15 };
	private final float[]	PHASES				= { 0.85f, 0.60f, 0.4f, 0.2f };
	private final boolean[]	POP_DONE			= { false, false, false, false };
	private Animation		kraken_invulnerable	= new Animation(0.1f, R.c().enemy_kraken_boss_invulnerable);

	@Override
	public void act(float delta)
	{
		super.act(delta);

		EnemyComportements.physicalAttack(this, player);

		if (phase == Phases.PHASE_1)
		{
			phase_1(delta);
		}
		if (phase == Phases.PHASE_2)
		{
			phase_2(delta);
		}

		if (!changingOffset)
		{
			// Un changement de phase, n'interomperrera pas un changement d'offset.
			phaseControlleur(delta);
		}

		shoot = true;
		m_shootPauseTime = 0;

	}

	private void phaseControlleur(float delta)
	{
		for (int i = 0; i < POP_DONE.length; i++)
		{
			if (POP_DONE[i] == false)
			{
				// Un pop est n' a pas été fait.
				// Verifions la vie pour savoir si on peut le faire
				if (life < maxLife * PHASES[i])
				{
					doPop(i);
					POP_DONE[i] = true;
					phase = Phases.PHASE_2;
				}
			}
		}

		if (countMiniKraken() == 0)
		{
			phase = Phases.PHASE_1;
		}

	}

	private void doPop(int i)
	{
		GameStage.cameraShake(GameStage.HIGH_SHAKE);

		for (int j = 0; j < POPS[i]; j++)
		{
			GlobalController.enemyController.addActor(new Enemy_21_MiniKraken(player, 0, new Vector2(getCenterX(), getCenterY())));
			GlobalController.fxController.addActor(new PopMessage(this, MessageType.BOSS_CRY_3));
		}
	}

	private int countMiniKraken()
	{
		SnapshotArray<Actor> actors = GlobalController.enemyController.getChildren();
		for (Actor actor : actors)
		{
			if (actor instanceof Enemy_21_MiniKraken)
			{
				return 1; // On ne va pas plus loin, il y en a 1.
			}
		}
		return 0;
	}

	private void phase_2(float delta)
	{
		shoot = false;
		walk = false;
		protection = true;
		faireFaceTo(player);
	}

	@Override
	public Animation getCurrentAnimation()
	{
		if (phase == Phases.PHASE_2)
		{
			return kraken_invulnerable;
		} else
		{
			return super.getCurrentAnimation();
		}
	}

	private void phase_1(float delta)
	{
		protection = false;
		if (!changingOffset)
		{
			if (shootBullet.doAction(delta))
			{
				faireFaceTo(player);
				shoot = true;
			} else
			{
				shoot = false;
			}
			EnemyComportements.navigateOverWorld(this, player); // TODO FAIRE AVEC WIDTH REDUIT POUR ETRE CENTRER
		}

		if (changeOffsetTimer.doAction(delta))
		{
			walk = false;
			shoot = false;
			changingOffset = true;
			Vector2 position = EnemyPopConstants.getInstance().getFlyPosition();
			addAction(Actions.sequence(Actions.moveTo(getX(), position.y, 0.7f), new RunnableAction()
			{
				@Override
				public void run()
				{
					changingOffset = false;
				}
			}));
		}
	}

	@Override
	public void shootEngine()
	{
		Projectile p = new Projectile(Projectiles.ENEMY_BOSS_4);
		p.init(this);
		GlobalController.bulletControllerEnemy.addActor(p);
	}

}
