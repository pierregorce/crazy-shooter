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

import java.util.Random;

import ressources.DrawableAnimation;
import ressources.R;
import ressources.Ressource;
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
		if (getX() < 0)
		{
			setX(0);
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

			R.c().soundEffect_player_levelUp.play(MusicManager.sfxVolume_Player);
		}
	}

	Timer	timerPlayerMessage	= new Timer(4f);
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
		weaponsBumpBack(weapons.recoilStrenght);

		// ------------------Add ammmo
		if (weapons.ammo)
		{
			Ammos ammos = GlobalController.ammosPool.obtain();
			ammos.init(this);
			GlobalController.ammosController.addActor(ammos);
		}

		// Cas particulier du laser -- TODO Change projectile
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

		// Cas particulier du lighting gun
		if (PlayerStats.weaponsType == 11)
		{
			if (bolt == null)
			{
				bolt = new Bolt(this);
				bolt.setVisible(true);
				GlobalController.fxController.addActor(bolt);
			} else
			{
				bolt.setVisible(true);
			}
		}

		// Animation de tir
		// addWeaponsAnimation(RessoucesController.getInstance().weapons_w1_shootFx, 20, projectiles.characterAnchor, projectiles.characterAnchor, 0.05f);
		// a stocker dans l'enum weaponsprojectile
		// Sound HOW TO
		// RessoucesController.getInstance().soundEffect_weapons_pistol[new Random().nextInt(RessoucesController.getInstance().soundEffect_weapons_pistol.length)]
		// .play(MusicManager.sfxVolume_Weapons);
		// a stocker dans projectile
		// -------------------------------------------
		// -------------------------------------------
		// -------------------------------------------
		// -------------------------------------------
		// -------------------------------------------
		// -------------------------------------------

		// int yOffsetCanon = 40;
		// Projectile bullet = null; // Projectile throws
		// Vector2 vector = null; // Stock la position d'apparition de la bullet (diffère selon l'arme)
		// boolean ammo = true;
		// // Fx shoot
		//
		// switch (weaponsType) {
		// case 0:
		//
		// vector = new Vector2(getRight() - 20, getCenterY() - 15);
		// bullet = ProjectilePoolFactory.getInstance().player_w1_projectilePool.obtain();
		// bullet.init(this, vector);
		// GlobalController.bulletControllerFriendly.addActor(bullet);
		// // Animation
		// addWeaponsAnimation(RessoucesController.getInstance().weapons_w1_shootFx, 20, vector.x - 3, vector.y - 6, 0.05f);
		// // Music
		// RessoucesController.getInstance().soundEffect_weapons_pistol[new Random().nextInt(RessoucesController.getInstance().soundEffect_weapons_pistol.length)]
		// .play(MusicManager.sfxVolume_Weapons);
		//
		// break;
		//
		// case 1:
		// // Lance une balle de type w1
		// vector = new Vector2(getRight() - 20, getCenterY() - 15);
		// bullet = ProjectilePoolFactory.getInstance().player_w1_projectilePool.obtain();
		// bullet.init(this, vector);
		// GlobalController.bulletControllerFriendly.addActor(bullet);
		// // Animation
		// addWeaponsAnimation(RessoucesController.getInstance().weapons_w2_shootFx, 20, vector.x - 3, vector.y - 6, 0.05f);
		//
		// // Lance une balle de type w2
		// Vector2 vector_w2 = new Vector2(getX(), getCenterY() - 15);
		// Projectile bullet_w2 = ProjectilePoolFactory.getInstance().player_w2_projectilePool.obtain();
		// bullet_w2.init(this, vector_w2);
		// GlobalController.bulletControllerFriendly.addActor(bullet_w2);
		//
		// // Animation
		// TextureRegion[] text = RessoucesController.invertAnimation(RessoucesController.getInstance().weapons_w2_shootFx, 0.05f).getKeyFrames();
		// addWeaponsAnimation(text, 20, getX() - 50 - 3, vector.y - 6, 0.05f);
		// // Music
		// RessoucesController.getInstance().soundEffect_weapons_pistol[new Random().nextInt(RessoucesController.getInstance().soundEffect_weapons_pistol.length)]
		// .play(MusicManager.sfxVolume_Weapons);
		// addAction(new SequenceAction(Actions.delay(0.05f), new RunnableAction()
		// {
		// @Override
		// public void run()
		// {
		// RessoucesController.getInstance().soundEffect_weapons_pistol[new Random().nextInt(RessoucesController.getInstance().soundEffect_weapons_pistol.length)]
		// .play(MusicManager.sfxVolume_Weapons);
		// }
		// }));
		//
		// break;
		// case 2:
		// // Lance une balle de type w3
		// for (int i = 0; i < 4; i++)
		// {
		// int xRandomization = new Random().nextInt(100);
		// int yRandomization = new Random().nextInt(20);
		// vector = new Vector2(getRight() - 20 + xRandomization, getCenterY() - 15 + yRandomization);
		// bullet = ProjectilePoolFactory.getInstance().player_w3_projectilePool.obtain();
		// bullet.init(this, vector);
		// GlobalController.bulletControllerFriendly.addActor(bullet);
		//
		// }
		//
		// // Animation
		// addWeaponsAnimation(RessoucesController.getInstance().weapons_w3_shootFx, 20, getRight() - 20, getCenterY() - 15, 0.05f);
		// weaponsBumpBack(25);
		// GameStage.cameraShake(10);
		//
		// // Music
		// RessoucesController.getInstance().soundEffect_weapons_shotGun[new Random().nextInt(RessoucesController.getInstance().soundEffect_weapons_shotGun.length)]
		// .play(MusicManager.sfxVolume_Weapons);
		//
		// break;
		// case 3:
		// // Lance une balle de type w4
		// vector = new Vector2(getRight() - 23, getY() - 8 + yOffsetCanon);
		// bullet = ProjectilePoolFactory.getInstance().player_w4_projectilePool.obtain();
		// bullet.init(this, vector);
		// GlobalController.bulletControllerFriendly.addActor(bullet);
		//
		// // Animation
		// addWeaponsAnimation(RessoucesController.getInstance().weapons_w4_shootFx, 70, vector.x - 15, vector.y - 20, 0.05f);
		// weaponsBumpBack(10);
		// // Music
		// RessoucesController.getInstance().soundEffect_weapons_bazookaFire[new Random()
		// .nextInt(RessoucesController.getInstance().soundEffect_weapons_bazookaFire.length)].play(MusicManager.sfxVolume_Weapons);
		// break;
		//
		// case 4:
		// // Lance une balle de type w5
		// vector = new Vector2(getRight(), getY() + yOffsetCanon - 10);
		// bullet = ProjectilePoolFactory.getInstance().player_w5_projectilePool.obtain();
		// bullet.init(this, vector);
		// GlobalController.bulletControllerFriendly.addActor(bullet);
		//
		// vector = new Vector2(getRight(), getY() + yOffsetCanon + 10);
		// bullet = ProjectilePoolFactory.getInstance().player_w5_projectilePool.obtain();
		// bullet.init(this, vector);
		// GlobalController.bulletControllerFriendly.addActor(bullet);
		// // Animation
		// // addWeaponsAnimation(RessoucesController.getInstance().weapons_w5_shootFx, 70, vector.x - 15, vector.y - 20, 0.05f);
		// // Music
		// RessoucesController.getInstance().soundEffect_weapons_machineGun[new Random()
		// .nextInt(RessoucesController.getInstance().soundEffect_weapons_machineGun.length)].play(MusicManager.sfxVolume_Weapons);
		// break;
		// case 5:
		// // Lance une balle de type w6
		// for (int i = 0; i < 4; i++)
		// {
		// vector = new Vector2(getRight(), getY() + yOffsetCanon);
		// bullet = ProjectilePoolFactory.getInstance().player_w6_projectilePool.obtain();
		// bullet.init(this, vector);
		// GlobalController.bulletControllerFriendly.addActor(bullet);
		// }
		// // Music
		// if (!soundPlaying)
		// {
		// weaponSound = RessoucesController.getInstance().soundEffect_weapons_flameThrower_Loop;
		// weaponSound.loop(MusicManager.sfxVolume_Weapons);
		// soundPlaying = true;
		// }
		//
		// ammo = false;
		//
		// // Pas d'animation pour le lance flamme
		// break;
		// case 6:
		// vector = new Vector2(getRight() - 23 + 15, getY() - 8 + yOffsetCanon + 1);
		// float dist = getFirstEnemyInDirection();
		// if (beam == null)
		// {
		// beam = new Beam();
		// beam.init(vector, dist, this);
		// beam.setVisible(true);
		// GlobalController.fxController.addActor(beam);
		// } else
		// {
		// beam.setVisible(true);
		// }
		// // Lance une bullet qui ressemble a une w1 mais invisible et avec des dmg differents.
		// vector.set(vector.x + dist, vector.y);
		// for (int i = 0; i < 3; i++)
		// {
		// bullet = ProjectilePoolFactory.getInstance().player_w7_projectilePool.obtain();
		// bullet.init(this, vector);
		// GlobalController.bulletControllerFriendly.addActor(bullet);
		// }
		//
		// // Music
		// if (!soundPlaying)
		// {
		// weaponSound = RessoucesController.getInstance().soundEffect_weapons_laserGun_Loop[new Random()
		// .nextInt(RessoucesController.getInstance().soundEffect_weapons_laserGun_Loop.length)];
		// weaponSound.loop(MusicManager.sfxVolume_Weapons);
		// soundPlaying = true;
		// }
		//
		// ammo = false;
		// break;
		//
		// case 7:
		//
		// for (int i = 0; i < 1; i++)
		// {
		// vector = new Vector2(getRight() - 15, getY() + yOffsetCanon);
		// bullet = new Player_w8_Projectile();
		// bullet.init(this, vector);
		// GlobalController.bulletControllerFriendly.addActor(bullet);
		// }
		//
		// break;
		// default:
		// break;
		// }

		// // Music
		// if (!soundPlaying)
		// {
		// weaponSound = RessoucesController.getInstance().soundEffect_weapons_laserGun_Loop[new Random()
		// .nextInt(RessoucesController.getInstance().soundEffect_weapons_laserGun_Loop.length)];
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

	private Bolt	bolt;	; // Lightning gun

	private void updateBolt()
	{
		if (bolt != null && bolt.isVisible())
		{
			Character c = getXClosestEnemyInDirection();
			if (c == null)
			{
				bolt.setObjective(getRight() + 500, getTop());
			} else
			{
				bolt.setObjective(c.getCenterX(), c.getCenterY());
			}
		}
	}

	private Character getXClosestEnemyInDirection()
	{
		float distance = Projectiles.PLAYER_LASER.lenghtAlive; // Distance max w11
		SnapshotArray<Actor> enemyArray = GlobalController.enemyController.getChildren();

		for (Actor actor : enemyArray)
		{
			Enemies enemy = (Enemies) actor;
			// Caclul de la distance avec l'enemy,
			float tempDistance = enemy.getCenterX() - getCenterX();
			// positif si l'enemy est a gauche du player
			// négatif si l'enemy est a droite du player
			if (direction == Direction.LEFT_DIRECTION && tempDistance < 0)
			{
				if (Math.abs(tempDistance) < distance)
				{
					distance = Math.abs(tempDistance);
				}
				return enemy;
			}
			if (direction == Direction.RIGHT_DIRECTION && tempDistance > 0)
			{
				if (tempDistance < distance)
				{
					distance = tempDistance;
				}
				return enemy;
			}
		}
		return null;
	}

	/**
	 * Retourne la distance du premier enemy face au player et ayant le même x. La distance est positive.
	 */
	private float getFirstEnemyInDirection()
	{

		SnapshotArray<Actor> enemyArray = GlobalController.enemyController.getChildren();
		float distance = 600; // Distance max w7

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
			if (bolt != null)
			{
				bolt.setVisible(false);
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
		R.c().soundEffect_player_jump[new Random().nextInt(R.c().soundEffect_player_jump.length)].play(MusicManager.sfxVolume_Player);
	}

	@Override
	public void loseLife(int quantity)
	{
		super.loseLife(quantity);
		R.c().soundEffect_player_gettingHit[new Random().nextInt(R.c().soundEffect_player_gettingHit.length)].play(MusicManager.sfxVolume_Player);
	}

}
