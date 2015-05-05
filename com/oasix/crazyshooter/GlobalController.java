package com.oasix.crazyshooter;

import files.Files;
import game.ennemies.Enemy_ExplosiveBarrel;
import game.ennemies.Enemy_NapalmBarrel;
import game.entitiy.Enemies;
import game.fx.BloodParticle;
import game.fx.LimaceParticle;
import game.fx.MetalParticle;
import game.hud.BossBar;
import game.hud.HudMessages;
import game.object.Coin;
import game.object.LifeBox;
import game.pop.PopMessage;
import game.pop.PopMessage.MessageType;
import game.pop.PopXP;
import game.projectiles.Projectile;
import globals.PlayerStats;
import globals.Worlds;

import java.util.Random;

import ressources.DrawableSprite;
import ressources.R;
import ressources.Ressource;
import ressources.S;
import ressources.S.TyrianSound;
import screen.ScreenManager;
import screen.level.Levels;
import utilities.enumerations.GameStatesEnum;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.SnapshotArray;

public class GlobalController
{
	private GameStage					m_gameStage;

	// ----------------------------- Gestion des vagues

	private int							m_wave						= 0;							// Index de la wave en cours
	private float						m_waveTime;												// Durée de la wave en cours
	private int							m_waveCount;												// Nombre de wave dans le level
	private float[][]					m_waveDetails;												// Contient l'ensemble des lignes de la wave en cours
	private Array<EnemyFactory>			m_factoryArray				= new Array<EnemyFactory>();	// Contient l'ensemble des factory et les controlles.
	private LevelParser					m_levelParser;
	private float						m_time						= 0;
	public static int					nombreRestantEnnemies		= 0;
	// ------------------------------ Groups
	private Player						player;
	public static BlockController		blockController;
	// Groupes static permettant de délocaliser la gestion du tir à la classe character et la gestion des factory enemies
	public static Group					bulletControllerEnemy		= new Group();
	public static Group					bulletControllerFriendly	= new Group();
	public static Group					ammosController				= new Group();
	public static Group					enemyController				= new Group();
	public static Group					particleController			= new Group();					// Blood et metal particle
	public static Group					limaceParticleController	= new Group();					// Limace particle, Flaque faisant ralentir (limace spit)
	public static Group					fxController				= new Group();					// Contient les popDamages, popXp, coins qui s'envolent
	private static Group				coinsController				= new Group();					// Coins
	private static Group				lifeBoxController			= new Group();					// Lifebox

	private Timer						lifeBoxTimerPoping;
	private Timer						explosiveBoxTimerPoping;
	// ------------------------------ Pools

	public static Pool<BloodParticle>	bloodParticlePool;
	public static Pool<MetalParticle>	metalParticlePool;
	public static Pool<Ammos>			ammosPool;

	public GlobalController(GameStage gameStage, LevelParser levelParser)
	{
		initPools();

		m_levelParser = levelParser;
		m_gameStage = gameStage;

		// New for static
		enemyController = new Group();
		bulletControllerEnemy = new Group();
		bulletControllerFriendly = new Group();
		ammosController = new Group();
		particleController = new Group(); // Blood et metal particle
		limaceParticleController = new Group(); // Limace particle
		fxController = new Group(); // Contient les popDamages, popXp, coins qui s'envolent
		coinsController = new Group();
		lifeBoxController = new Group();
		blockController = new BlockController();

		// BackGround
		gameStage.addActor(new Background(this));
		// Block
		blockController = new BlockController();
		gameStage.addActor(blockController);
		// Player
		player = new Player();
		gameStage.addActor(player);
		// Enemy
		gameStage.addActor(enemyController);
		// Particle
		gameStage.addActor(particleController);
		// Puddle
		gameStage.addActor(limaceParticleController);
		// Ammos
		gameStage.addActor(ammosController);
		// Bullet
		gameStage.addActor(bulletControllerEnemy);
		gameStage.addActor(bulletControllerFriendly);
		// Fx
		gameStage.addActor(coinsController);
		gameStage.addActor(lifeBoxController);
		gameStage.addActor(fxController);

		// wayPointController = new WayPointController();
		// gameStage.addActor(wayPointController);

		// Initialisation pour le level control
		m_waveTime = m_levelParser.getWaveTime(GameStage.levelData.levelIndex, m_wave);
		m_waveCount = m_levelParser.getWaveCount(GameStage.levelData.levelIndex);
		// GameStage.hudMessages.add(new HudMessages("New wave :" + m_wave, "Wave remaining : " + m_waveCount));
		m_waveDetails = m_levelParser.getpopForWave(GameStage.levelData.levelIndex, m_wave); // Donne les caractéristique de la wave

		// Création des enemies

		for (int i = 0; i < m_waveDetails.length; i++)
		{
			// Boucle sur la liste des lignes
			// Récupère les données pour la ligne en cours
			// Envoie les données de la ligne à l'objet enemyFactory qui s'occupera d'ajouter les enemies au bon moment
			m_factoryArray.add(new EnemyFactory(enemyController, player, m_waveDetails[i]));
		}

		int level = ScreenManager.getInstance().getLevelSelected().levelIndex; // Recupère le level (commence à 1)

		// Charge les timer en fonction des niveau des boss

		if (Worlds.getWorldNumber(level) == 0)
		{
			lifeBoxTimerPoping = new Timer(9f);
			explosiveBoxTimerPoping = new Timer(11f);
		}
		if (Worlds.getWorldNumber(level) == 1)
		{
			lifeBoxTimerPoping = new Timer(11f);
			explosiveBoxTimerPoping = new Timer(11f);
		}
		if (Worlds.getWorldNumber(level) == 2)
		{
			lifeBoxTimerPoping = new Timer(11f);
			explosiveBoxTimerPoping = new Timer(11f);
		}

		if (Worlds.getWorldNumber(level) == 3)
		{
			lifeBoxTimerPoping = new Timer(11f);
			explosiveBoxTimerPoping = new Timer(11f);
		}

		if (Worlds.getWorldNumber(level) == 4)
		{
			lifeBoxTimerPoping = new Timer(13f);
			explosiveBoxTimerPoping = new Timer(11f);
		}
		if (Worlds.getWorldNumber(level) == 5)
		{
			lifeBoxTimerPoping = new Timer(15f);
			explosiveBoxTimerPoping = new Timer(11f);
		}

		nombreRestantEnnemies = getTotalEnemiesCount();

		if (GameStage.debugRapidAndLevels)
		{
			levelParser.sumByLevel();
		}
	}

	private void initPools()
	{
		Pools.get(Projectile.class).clear();

		bloodParticlePool = new Pool<BloodParticle>(150)
		{
			@Override
			protected BloodParticle newObject()
			{
				return new BloodParticle();
			}
		};

		metalParticlePool = new Pool<MetalParticle>(25)
		{
			@Override
			protected MetalParticle newObject()
			{
				return new MetalParticle();
			}
		};

		ammosPool = new Pool<Ammos>(100)
		{
			@Override
			protected Ammos newObject()
			{
				return new Ammos();
			}
		};
	}

	public void control(float delta)
	{
		friendlyBulletCollision();
		enemyBulletCollision(delta);
		objectCollision();
		fxCollision();
		levelController(delta);
		objectPoping(delta);

		// System.out.println("x :" + player.getX() + " | y :" + player.getY());
		// Gere la mort
		if (player.getLife() <= 0)
		{
			player.setLife(0);
			GameStage.gameState = GameStatesEnum.GAME_LOOSE;
		}

		if (player.getY() <= 0)
		{
			GameStage.gameState = GameStatesEnum.GAME_LOOSE;
		}
	}

	private void objectPoping(float delta)
	{
		if (lifeBoxTimerPoping.doAction(delta))
		{
			lifeBoxController.addActor(new LifeBox());
		}

		if (explosiveBoxTimerPoping.doAction(delta) && !BossBar.active)
		{
			int level = ScreenManager.getInstance().getLevelSelected().levelIndex;

			if (Worlds.getWorldNumber(level) < 3)
			{
				enemyController.addActor(new Enemy_ExplosiveBarrel(player, 0));
			} else
			{
				enemyController.addActor(new Enemy_NapalmBarrel(player, 0));
			}
		}

	}

	private void enemyBulletCollision(float delta)
	{
		SnapshotArray<Actor> enemyBulletArray = bulletControllerEnemy.getChildren();

		for (Actor bullets : enemyBulletArray)
		{
			Projectile bullet = (Projectile) bullets;

			if (bullet.active && player.getBouncingBox().overlaps(bullet.getBouncingBox()))
			{
				// ------------------------- Lose life

				player.setLosingLifeEvent(true);
				player.setLoosingLiveValueEvent(bullet.getPower());

				// Delete Bullet
				bullet.doEnddingEffect(player);
				bullet.active = false;
				if (bullet.projectilesType.removeOnCollision)
				{
					bullet.remove();
				}
				return;
			}
		}
	}

	private void friendlyBulletCollision()
	{
		SnapshotArray<Actor> playerBulletArray = bulletControllerFriendly.getChildren();
		SnapshotArray<Actor> enemyArray = enemyController.getChildren();

		for (Actor bullets : playerBulletArray)
		{
			Projectile bullet = (Projectile) bullets;

			for (Actor actor : enemyArray)
			{
				Enemies enemy = (Enemies) actor;

				if (bullet.active && bullet.getBouncingBox().overlaps(enemy.getBouncingBox()))
				{
					// Check if it will be critical hit ?
					int criticalVerification = new Random().nextInt(100) + 1; // Une valeur de 0 à 100
					float critChance = PlayerStats.getCriticalChance() * 100; // % de chance de crit
					int bulletDamage = bullet.getPower();
					bulletDamage += PlayerStats.getDamage();
					boolean crit = false;
					if (criticalVerification <= critChance)
					{
						bulletDamage *= 1.8f;
						bulletDamage += new Random().nextInt(4);
						crit = true;
					}

					// ------------------------- Lose life & Add explosion
					enemy.loseLife(bulletDamage);
					// -------------------------- Pop Damage
					enemy.popDamageOnLosingLife(bulletDamage, crit);
					// ---------------------------Add weapons effect
					bullet.doEnddingEffect(enemy);

					// -------------------------- Delete enemy if dead
					if (enemy.getLife() <= 0)
					{
						player.addXP(enemy.getXpGainOnKill());
						// ------------------------- Add popXp
						if (enemy.getXpGainOnKill() > 0)
						{
							PopXP popXP = new PopXP(enemy.getXpGainOnKill() + " XP", enemy);
							fxController.addActor(popXP);
						}

						// ------------------------- Add object
						for (int i = 0; i < enemy.m_goldQuantity; i++)
						{
							coinsController.addActor(new Coin(enemy.getCenterX(), enemy.getTop(), enemy.m_goldValue));
						}

						enemy.remove();
						// enemyArray.removeValue(enemy, false);
					}
					// Delete Bullet
					bullet.active = false;

					if (bullet.projectilesType.removeOnCollision)
					{
						bullet.remove();
					}
				}

			}

		}
	}

	private void objectCollision()
	{

		SnapshotArray<Actor> coinsArray = coinsController.getChildren();
		SnapshotArray<Actor> lifeBoxArray = lifeBoxController.getChildren();

		for (Actor actor : coinsArray)
		{
			Coin coin = (Coin) actor;

			if (player.getBouncingBox().overlaps(coin.getBouncingBox()))
			{
				// Ajout de la ressource au player
				PlayerStats.ressource += coin.m_value;
				// Suppression de l'objet
				coin.remove();
				// Sfx
				S.c().play(TyrianSound.soundEffect_player_coinPickup);
				// Affichage de particle

				Vector2 initialPosition = new Vector2(coin.getX(), coin.getY());
				Vector3 cameraPosition = m_gameStage.getCamera().position;

				MoveToAction action1 = new MoveToAction();
				action1.setDuration(0.1f);
				action1.setInterpolation(Interpolation.exp10);
				action1.setPosition(cameraPosition.x, cameraPosition.y);
				MoveToAction action2 = new MoveToAction();
				action2.setDuration(0.5f);
				action2.setInterpolation(Interpolation.exp10);
				action2.setPosition(cameraPosition.x + 600, 900); // se place à sa position finale
				SequenceAction sequenceAction = new SequenceAction(action1, action2, new RemoveActorAction());

				fxController.addActor(new Ressource(new DrawableSprite(R.c().iconCoinFx), initialPosition.x, initialPosition.y, 45, sequenceAction));
			}
		}

		for (Actor actor : lifeBoxArray)
		{
			LifeBox lifeBox = (LifeBox) actor;

			if (player.getBouncingBox().overlaps(lifeBox.getBouncingBox()))
			{
				// Ajout de la vie au player
				player.addLife(lifeBox.m_value);
				fxController.addActor(new PopMessage("" + lifeBox.m_value, player, MessageType.HEAL));
				// Suppression de l'objet
				lifeBox.remove();
				// Sfx
				S.c().play(TyrianSound.soundEffect_player_healthPackPickup);
			}
		}

	}

	private void fxCollision()
	{
		SnapshotArray<Actor> objetsArray = limaceParticleController.getChildren();

		for (Actor actor : objetsArray)
		{
			LimaceParticle limaceParticle = (LimaceParticle) actor;

			if (player.getCollisionBox().overlaps(limaceParticle.getBouncingBox()))
			{
				player.setReduceSpeedEvent(true);
				return;
			} else
			{
				player.setReduceSpeedEvent(false);
			}
		}
	}

	private void levelController(float delta)
	{
		// Boucle pour avancer dans les vagues

		// Si le temps de la vague n'est pas finit
		if (m_time < m_waveTime)
		{
			m_time += delta;
		} else
		{
			// Avance d'une vague
			if (m_wave < m_waveCount - 1)
			{

			}
			// Mise a jout des variables si possible (c'est a dire que l'on soit sur une vague qui existe)
			if (m_wave < m_waveCount - 1)
			{
				m_time = 0;
				m_wave++;
				GameStage.hudMessages.add(new HudMessages("New wave :" + m_wave, "They will be back !"));
				m_waveTime = m_levelParser.getWaveTime(GameStage.levelData.levelIndex, m_wave);
				m_waveDetails = m_levelParser.getpopForWave(GameStage.levelData.levelIndex, m_wave); // Donne les caractéristique de la wave

				// Création des factory pour deleguer le traitement de la gestion du pop
				for (int i = 0; i < m_waveDetails.length; i++)
				{
					// Boucle sur la liste des lignes
					// Récupère les données pour la ligne en cours
					// Envoie les données de la ligne à l'objet enemyFactory qui s'occupera d'ajouter les enemies au bon moment
					m_factoryArray.add(new EnemyFactory(enemyController, player, m_waveDetails[i]));
				}
			}
		}

		// System.out.println("m_wave = " + m_wave);
		// System.out.println("m_waveCount = " + m_waveCount);

		// Controlle la fin du niveau (si l'on est sur la dernière vague + 1)
		if (m_wave == m_waveCount - 1)
		{
			// si plus d'enemy, plus de factory et plus d'objet alors demarage de la fin du niveau
			if (isThereNoMoreEnemy() && m_factoryArray.size == 0 && coinsController.getChildren().size == 0 && !finDuNviveauLance)
			{
				System.out.println("Fin du niveau runnable action lunched.");
				finDuNiveauDelayed();
				finDuNviveauLance = true;
			} else
			{
				// System.out.println("Derniere vague, En attente de la mort des enemy et des pieces pour lancer la fin du niveau");
			}
		}

		// Controlle les factory.
		for (EnemyFactory enemyFactory : m_factoryArray)
		{
			enemyFactory.control(delta);
			if (enemyFactory.isComplete())
			{
				m_factoryArray.removeValue(enemyFactory, false);
			}
		}
	}

	private boolean	finDuNviveauLance	= false;

	private boolean isThereNoMoreEnemy()
	{
		for (Actor enemy : enemyController.getChildren())
		{
			Enemies enemies = (Enemies) enemy;

			if (enemies.getXpGainOnKill() > 0)
			{
				return false;
			}
		}
		return true;
	}

	private void finDuNiveauDelayed()
	{

		RunnableAction runAction = new RunnableAction()
		{

			@Override
			public void run()
			{
				GameStage.gameState = GameStatesEnum.GAME_WIN;
				Levels levels = Files.levelDataRead();
				int currentLevel = ScreenManager.getInstance().getLevelSelected().levelIndex;
				levels.level[currentLevel].levelComplete = "true";
				ScreenManager.getInstance().setLevelSelected(levels.level[currentLevel + 1]);

				Files.levelDataWrite(levels);
				Files.playerDataWrite();
				Files.playerUpgradeWrite();
			}

		};

		m_gameStage.addAction(new SequenceAction(Actions.delay(1.5f), runAction));
	}

	// -----------------------------Getters and Setters

	public Player getPlayer()
	{
		return player;
	}

	public int getWaveRemaining()
	{
		return m_waveCount - m_wave - 1;
	}

	public float getRemainingTime()
	{
		return m_waveTime - m_time;
	}

	public int getWaveCount()
	{
		return m_waveCount;
	}

	public int getTotalEnemiesCount()
	{
		int count = 0;
		for (int i = 0; i < getWaveCount(); i++)
		{
			float[][] popForLevel = m_levelParser.getpopForWave(GameStage.levelData.levelIndex, i);
			for (int j = 0; j < popForLevel.length; j++)
			{
				count += popForLevel[j][1];
			}
		}
		return count;
	}

	public int getNombreRestantEnnemies()
	{
		return nombreRestantEnnemies;
	}

}
