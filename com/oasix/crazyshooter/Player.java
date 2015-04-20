package com.oasix.crazyshooter;

import game.entitiy.Character;
import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.fx.Beam;
import game.fx.BloodParticle;
import game.fx.Bolt;
import game.fx.ParticleColors;
import game.hud.HudMessages;
import game.pop.PopMessage;
import game.pop.PopMessage.MessageType;
import game.projectiles.Projectile;
import game.sound.MusicManager;
import globals.PlayerStats;
import globals.Projectiles;
import globals.Weapons;

import java.util.Comparator;
import java.util.Random;

import ressources.DrawableAnimation;
import ressources.R;
import ressources.Ressource;
import ressources.S;
import screen.MyGdxGame;
import utilities.Methods;
import utilities.enumerations.Direction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.Sort;

public class Player extends Character
{

	// ENEMIES GESTION
	float								immuneTime				= 0;
	private boolean						event					= false;
	private boolean						bumpingLeftEvent		= false;
	private boolean						bumpingRightEvent		= false;
	private boolean						reduceSpeedEvent		= false;
	private boolean						losingLifeEvent			= false;
	private int							loosingLiveValueEvent	= 0;

	// CONSTANT CREATION
	private final static TextureRegion	m_region				= R.c().player_walk[0];
	private final static int			HEIGHT					= 100;

	private static float				realHeight				= m_region.getRegionHeight() * MyGdxGame.PIXEL_SIZE;
	public static float					heightCoef				= HEIGHT / realHeight;

	// private final static int HEIGHT = m_region.getRegionHeight() * 4;

	public Player()
	{
		super(PlayerStats.getMaxLife(), PlayerStats.getMoveSpeed(), HEIGHT, R.c().player_walk);

		// Vector2 position = EnemyPopConstants.getInstance().getPlayerPopPosition();
		Vector2 position = EnemyPopConstants.getInstance().getGroundBlockPosition();
		position.x = 1900;
		setPosition(position.x, position.y);

		jumpDownFxAnimation = new DrawableAnimation(0.05f, R.c().jumpDownFxAnimation);
		jumpUpFxAnimation = new DrawableAnimation(0.05f, R.c().jumpUpFxAnimation);

		editWalkAnimation(R.c().player_walk, R.c().player_weapons[PlayerStats.weaponsType]);
		maxJumpSpeedY = PlayerStats.getJumpHeight();
		editCollisionBox(30, 15, 10);
		// TODO FAIRE AUSSI EDIT BOUNCING BOX...
	}

	@Override
	protected void character_initialisation()
	{
		shootCooldwon = PlayerStats.getCurrentWeapons().fireRate;
		m_shootPauseTime = 0f;
		m_shootRunTime = 1f;
		defineTimer();
	}

	Blinker	blinker	= new Blinker();

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (immuneTime < PlayerStats.getImuneTime() && immuneTime != 0)
		{
			if (blinker.shouldBlink(Gdx.graphics.getDeltaTime()))
			{
				super.draw(batch, parentAlpha);
			}
		} else
		{
			super.draw(batch, parentAlpha);
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		xp();
		eventEngine(delta);
		if (getX() < 60)
		{
			setX(60);
		}
		if (getRight() > MyGdxGame.VIRTUAL_WORLD_WIDTH)
		{
			setX(MyGdxGame.VIRTUAL_WORLD_WIDTH - getWidth());
		}

		if (isShoot())
		{
			if (direction == Direction.LEFT_DIRECTION)
			{
				// setX(getX() + 0.2f);
			} else
			{
				// setX(getX() - 0.2f);
			}
		}
		updateBeams();
		updateBolt();
	}

	public void xp()
	{
		if (PlayerStats.currentXP >= PlayerStats.getRequiertXP())
		{
			PlayerStats.level++;
			PlayerStats.currentXP = 0;
			GameStage.hudMessages.add(new HudMessages("Level UP !", "Next Level in " + PlayerStats.getRequiertXP()));

			DrawableAnimation drawableAnimationLevel = new DrawableAnimation(0.1f, R.c().playerLevelUp);
			Ressource levelupAnimation = new Ressource(drawableAnimationLevel, getX() - 200, getY() - 110, 400, MyGdxGame.VIRTUAL_HEIGHT, new SequenceAction(
					Actions.delay(drawableAnimationLevel.getAnimationDuration() - 0.1f), Actions.removeActor()));
			getParent().addActor(levelupAnimation);

			GameStage.cameraShake(30);
			setLife(PlayerStats.getMaxLife());

			S.c().soundEffect_player_levelUp.play(MusicManager.sfxVolume_Player);
		}
	}

	Timer	timerPlayerMessage	= new Timer(6f);
	boolean	playerMessage		= false;

	public void eventEngine(float delta)
	{
		// demarrage de l'event
		if (timerPlayerMessage.doAction(delta))
		{
			if (playerMessage)
			{
				GlobalController.fxController.addActor(new PopMessage(this, MessageType.PLAYER_HIT));
				playerMessage = false;
			}
		}

		if (bumpingLeftEvent || bumpingRightEvent || losingLifeEvent)
		{
			event = true;
		}

		if (losingLifeEvent && immuneTime == 0)
		{
			loseLife(loosingLiveValueEvent);
			GlobalController.fxController.addActor(new PopMessage("" + loosingLiveValueEvent, this, MessageType.PLAYER_LOSE_LIFE));
			playerMessage = true;

			// Genere du sang
			for (int i = 0; i < 2; i++)
			{
				BloodParticle bloodParticle = GlobalController.bloodParticlePool.obtain();
				bloodParticle.init(getCenterX(), getCenterY(), ParticleColors.getInstance().getBloodColor());
				GlobalController.particleController.addActor(bloodParticle);
			}
		}

		if (bumpingRightEvent && immuneTime == 0)
		{
			clear();
			MoveToAction action1 = new MoveToAction();
			action1.setPosition(getX() + 50, getY() + 10);
			action1.setDuration(0.1f);
			MoveToAction action2 = new MoveToAction();
			action2.setPosition(getX() + 100, getY());
			action2.setDuration(0.1f);
			SequenceAction sequenceAction = new SequenceAction(action1, action2);
			addAction(sequenceAction);
		}

		if (bumpingLeftEvent && immuneTime == 0)
		{
			clear();
			MoveToAction action1 = new MoveToAction();
			action1.setPosition(getX() - 50, getY() + 10);
			action1.setDuration(0.1f);
			MoveToAction action2 = new MoveToAction();
			action2.setPosition(getX() - 100, getY());
			action2.setDuration(0.1f);
			SequenceAction sequenceAction = new SequenceAction(action1, action2);
			addAction(sequenceAction);
		}

		if (event && immuneTime == 0)
		{
			immuneTime += delta;
			blinker = new Blinker();
			blinker.setBlinking(true);
		}

		// cooldown de l'event
		if (immuneTime < PlayerStats.getImuneTime() && immuneTime != 0)
		{
			immuneTime += delta;

			// IMMUNE TIME - REMET A ZERO LES EVENTS CAR ILS NE PEUVENT SE PRODUIRE
			losingLifeEvent = false;
			loosingLiveValueEvent = 0;
			bumpingLeftEvent = false;
			bumpingRightEvent = false;
			event = false;
		} else
		{
			immuneTime = 0;
			blinker.setBlinking(false);
			timerPlayerMessage.resetForLunchImmediatly();
			playerMessage = false;
		}

		// Slow la vitesse en fonction de si on est dans la flaque ou pas
		if (reduceSpeedEvent)
		{
			// Add a blink on colorisation
			if (colorizationBlinkTime < colorizationBlinkTiming)
			{
				colorization = Color.GREEN;
			} else
			{
				colorization = null;
			}
			if (colorizationBlinkTime > colorizationBlinkTiming * 2)
			{
				colorizationBlinkTime = 0;
			} else
			{
				colorizationBlinkTime += delta;
			}

			maxVelocityX = PlayerStats.getMoveSpeedReduce();
		} else
		{
			maxVelocityX = PlayerStats.getMoveSpeed();
			colorization = null;
		}

	}

	private float	colorizationBlinkTime	= 0;
	private float	colorizationBlinkTiming	= 0.1f;

	public void addXP(int xp)
	{
		PlayerStats.currentXP += xp;
	}

	public boolean isBumpingLeftEvent()
	{
		return bumpingLeftEvent;
	}

	public void setBumpingLeftEvent(boolean bumpingLeftEvent)
	{
		this.bumpingLeftEvent = bumpingLeftEvent;
	}

	public boolean isBumpingRightEvent()
	{
		return bumpingRightEvent;
	}

	public void setBumpingRightEvent(boolean bumpingRightEvent)
	{
		this.bumpingRightEvent = bumpingRightEvent;
	}

	public boolean isReduceSpeedEvent()
	{
		return reduceSpeedEvent;
	}

	public void setReduceSpeedEvent(boolean reduceSpeedEvent)
	{
		this.reduceSpeedEvent = reduceSpeedEvent;
	}

	public boolean isLosingLifeEvent()
	{
		return losingLifeEvent;
	}

	public void setLosingLifeEvent(boolean losingLifeEvent)
	{
		this.losingLifeEvent = losingLifeEvent;
	}

	public void setLoosingLiveValueEvent(int loosingLiveValueEvent)
	{
		this.loosingLiveValueEvent = loosingLiveValueEvent;
	}

	public float getImmuneTime()
	{
		return immuneTime;
	}

	private Sound	weaponSound;
	private boolean	soundPlaying	= false;

	@Override
	public void throwProjectile()
	{
		int weaponsType = PlayerStats.weaponsType;
		Weapons weapons = Weapons.values()[weaponsType];
		Projectiles projectiles = weapons.projectileType;

		// Création du projectile
		if (PlayerStats.weaponsType != 11)
		{
			for (int i = 0; i < projectiles.quantityPerShoot; i++)
			{
				Projectile projectile = new Projectile(projectiles);
				projectile.init(this);
				if (PlayerStats.weaponsType == 6)
				{
					projectile.setTarget(this, new Vector2(getFirstEnemyInDirection(), getY()));
				}
				GlobalController.bulletControllerFriendly.addActor(projectile);
			}
		} else
		{
			// Envoie 3 projectile mais à des Y et X endroits différents
			for (int i = 0; i < 3; i++)
			{
				Character c = getClosestEnemyInAllDirection(i);
				if (c != null)
				{
					Projectile projectile = new Projectile(projectiles);
					projectile.init(this);
					projectile.setTarget(this, c);
					GlobalController.bulletControllerFriendly.addActor(projectile);
				}
			}
		}
		weaponsBumpBack(weapons.recoilStrenght);

		// Add ammmo --------------------------------------------------------
		if (weapons.ammo)
		{
			Ammos ammos = GlobalController.ammosPool.obtain();
			ammos.init(this);
			GlobalController.ammosController.addActor(ammos);
		}

		// Cas particulier du laser -----------------------------------------
		if (PlayerStats.weaponsType == 6)
		{
			float dist = getFirstEnemyInDirection();

			if (beam == null)
			{
				beam = new Beam();
				beam.setPlayerSpecification();
				beam.init(Projectiles.PLAYER_LASER.characterAnchor, dist, this);
				beam.setVisible(true);
				GlobalController.fxController.addActor(beam);
			} else
			{
				beam.setVisible(true);
			}
		}

		// Cas particulier du lighting gun ----------------------------------
		if (PlayerStats.weaponsType == 11)
		{
			if (bolt_1 == null)
			{
				bolt_1 = new Bolt(this);
				bolt_1.setVisible(true);
				GlobalController.fxController.addActor(bolt_1);
			} else
			{
				bolt_1.setVisible(true);
			}
			if (bolt_2 == null)
			{
				bolt_2 = new Bolt(this);
				bolt_2.setVisible(true);
				GlobalController.fxController.addActor(bolt_2);
			} else
			{
				bolt_2.setVisible(true);
			}
			if (bolt_3 == null)
			{
				bolt_3 = new Bolt(this);
				bolt_3.setVisible(true);
				GlobalController.fxController.addActor(bolt_3);
			} else
			{
				bolt_3.setVisible(true);
			}
		}

		// S.c().soundEffect_weapons_pistol[new Random().nextInt(S.c().soundEffect_weapons_pistol.length)].play(MusicManager.sfxVolume_Weapons);
		// S.c().soundEffect_weapons_shotGun[new Random().nextInt(S.c().soundEffect_weapons_shotGun.length)].play(MusicManager.sfxVolume_Weapons);
		// S.c().soundEffect_weapons_bazookaFire[new Random().nextInt(S.c().soundEffect_weapons_bazookaFire.length)].play(MusicManager.sfxVolume_Weapons);
		// S.c().soundEffect_weapons_machineGun[new Random().nextInt(S.c().soundEffect_weapons_machineGun.length)].play(MusicManager.sfxVolume_Weapons);
		//
		// // Weapon animation ?
		// // GameStage.cameraShake(10);
		//
		// // Music
		// if (!soundPlaying)
		// {
		// weaponSound = S.c().soundEffect_weapons_flameThrower_Loop;
		// weaponSound.loop(MusicManager.sfxVolume_Weapons);
		// soundPlaying = true;
		// }
		//
		// // Music
		// if (!soundPlaying)
		// {
		// weaponSound = S.c().soundEffect_weapons_laserGun_Loop[new Random().nextInt(S.c().soundEffect_weapons_laserGun_Loop.length)];
		// weaponSound.loop(MusicManager.sfxVolume_Weapons);
		// soundPlaying = true;
		// }

	}

	private Beam	beam;	// Laser Beam

	private void updateBeams()
	{
		if (beam != null && beam.isVisible())
		{
			Vector2 vector = new Vector2(getRight() - 23 + 15, getY() - 8 + 40 + 1);
			float dist = getFirstEnemyInDirection();
			beam.init(vector, dist, this);
		}
	}

	private Bolt	bolt_1;	// 3 ligthing en même temps !
	private Bolt	bolt_2;	// 3 ligthing en même temps !
	private Bolt	bolt_3;	// 3 ligthing en même temps !

	private void updateBolt()
	{
		if (bolt_1 != null && bolt_1.isVisible())
		{
			Character c = getClosestEnemyInAllDirection(0);
			if (c == null)
			{
				bolt_1.setObjective(getCorretedX(290), getCenterY());
			} else
			{
				bolt_1.setObjective(c.getCenterX(), c.getCenterY());
			}
		}
		if (bolt_2 != null && bolt_2.isVisible())
		{
			Character c = getClosestEnemyInAllDirection(1);
			if (c == null)
			{
				bolt_2.setObjective(getCorretedX(250), getCenterY() + 30);
			} else
			{
				bolt_2.setObjective(c.getCenterX(), c.getCenterY());
			}
		}
		if (bolt_3 != null && bolt_3.isVisible())
		{
			Character c = getClosestEnemyInAllDirection(2);
			if (c == null)
			{
				bolt_3.setObjective(getCorretedX(250), getCenterY() - 30);
			} else
			{
				bolt_3.setObjective(c.getCenterX(), c.getCenterY());
			}
		}

	}

	/**
	 * Retourne le n-ième enemy le plus prés 1 pour avoir l'enemy le plus prés (x et y) [Sert pour le lighting gun] Attention, ce doit être dans le même sens que le
	 * player.... n démmarre à 0
	 */
	private Character getClosestEnemyInAllDirection(int n)
	{
		float distance = Projectiles.PLAYER_LIGHTING_GUN.lenghtAlive;
		SnapshotArray<Actor> enemyArray = GlobalController.enemyController.getChildren();
		final Vector2 v = getPosition();

		Comparator<Actor> c = new Comparator<Actor>()
		{
			@Override
			public int compare(Actor o1, Actor o2)
			{

				if (v.dst(o1.getX(), o1.getY()) > v.dst(o2.getX(), o2.getY()))
				{
					return 1;
				} else
				{
					return -1;
				}
			}
		};

		Sort.instance().sort(enemyArray, c);

		try
		{
			Character character = (Character) enemyArray.get(n);

			if (v.dst(character.getCenterX(), character.getCenterY()) > distance)
			{
				return null;
			} else
			{
				return character;
			}

		} catch (Exception e)
		{
			return null;
		}

	}

	/**
	 * Retourne la distance du premier enemy face au player et ayant le même y. La distance est positive. [Sert pour le laser]
	 */
	private float getFirstEnemyInDirection()
	{

		SnapshotArray<Actor> enemyArray = GlobalController.enemyController.getChildren();
		float distance = Projectiles.PLAYER_LASER.lenghtAlive;

		// Si la distance à l'enemy est inférieure à la variable distance alors stocke la distance.

		// Boucle sur le tableau d'enemy.
		for (Actor actor : enemyArray)
		{
			Enemies enemy = (Enemies) actor;

			// Si l'enemy à le même y que le player (chauve souris)
			if ((getY() <= enemy.getTop() && getTop() >= enemy.getTop()) || (getTop() >= enemy.getY() && getY() <= enemy.getY()))
			{
				// Caclul de la distance avec l'enemy,
				float tempDistanceRight = enemy.getCenterX() - getRight();
				float tempDistanceLeft = enemy.getCenterX() - getX();

				// positif si l'enemy est a gauche du player
				// négatif si l'enemy est a droite du player

				if (direction == Direction.LEFT_DIRECTION && tempDistanceLeft < 0)
				{
					if (Math.abs(tempDistanceLeft) < distance)
					{
						distance = Math.abs(tempDistanceLeft);
					}
				}

				if (direction == Direction.RIGHT_DIRECTION && tempDistanceRight > 0)
				{
					if (tempDistanceRight < distance)
					{
						distance = tempDistanceRight;
					}
				}
			}

		}

		return distance;

	}

	public void addWeaponsAnimation(TextureRegion[] textureRegion, float animationHeight, float xAnimationPosition, float yAnimationPosition, float animationFrameTime)
	{

		DrawableAnimation drawableAnimation;
		Ressource fx = null;

		if (direction == Direction.RIGHT_DIRECTION)

		{
			drawableAnimation = new DrawableAnimation(animationFrameTime, textureRegion);

			fx = new Ressource(drawableAnimation, xAnimationPosition, yAnimationPosition, animationHeight, new SequenceAction(Actions.delay(drawableAnimation.getAnimationDuration(), Actions.removeActor())));

		} else
		{

			drawableAnimation = new DrawableAnimation(animationFrameTime, R.invertAnimation(textureRegion, animationFrameTime).getKeyFrames());

			float initialWidth = drawableAnimation.getWidth();
			float initialHeight = drawableAnimation.getHeight();
			float animationWidth = Methods.scaleByHeight(animationHeight, initialWidth, initialHeight);

			float x = getRight() - xAnimationPosition;
			fx = new Ressource(drawableAnimation, getX() + x - animationWidth, yAnimationPosition, animationHeight, new SequenceAction(Actions.delay(drawableAnimation.getAnimationDuration(), Actions.removeActor())));

		}

		GlobalController.fxController.addActor(fx);
	}

	private void weaponsBumpBack(int strength)
	{
		if (direction == Direction.RIGHT_DIRECTION)
		{
			setX(getX() - strength);
		} else
		{
			setX(getX() + strength);
		}
	}

	/*
	 * @Override public void setShootAnimation() { // shootRight = new Animation(0.07f, RessoucesController.getInstance().wizard_shoot); // shootLeft =
	 * RessoucesController.invertAnimation(RessoucesController.getInstance().wizard_shoot, 0.07f); }
	 */

	@Override
	public void setShoot(boolean shoot)
	{
		super.setShoot(shoot);

		if (shoot == false)
		{
			if (beam != null)
			{
				beam.setVisible(false);
			}

			if (bolt_1 != null)
			{
				bolt_1.setVisible(false);
			}
			if (bolt_2 != null)
			{
				bolt_2.setVisible(false);
			}
			if (bolt_3 != null)
			{
				bolt_3.setVisible(false);
			}

			if (weaponSound != null)
			{
				weaponSound.stop();
			}
			soundPlaying = false;
		}
	}

	@Override
	public void setJump(boolean jump)
	{
		super.setJump(jump);
		S.c().soundEffect_player_jump[new Random().nextInt(S.c().soundEffect_player_jump.length)].play(MusicManager.sfxVolume_Player);
	}

	@Override
	public void loseLife(int quantity)
	{
		super.loseLife(quantity);
		S.c().soundEffect_player_gettingHit[new Random().nextInt(S.c().soundEffect_player_gettingHit.length)].play(MusicManager.sfxVolume_Player);
	}

	/**
	 * Retourne pour une position X du bon coté du player
	 * 
	 * @rightAnchor position a droite
	 */
	public float getCorretedX(float rightAnchor)
	{
		if (direction == Direction.RIGHT_DIRECTION)
		{
			return getX() + rightAnchor;
		} else
		{
			return getRight() - rightAnchor;
		}
	}

}
