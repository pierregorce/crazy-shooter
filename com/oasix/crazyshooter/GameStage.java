package com.oasix.crazyshooter;

import game.hud.HudMessages;
import globals.PlayerStats;

import java.util.ArrayList;
import java.util.Random;

import screen.MyGdxGame;
import screen.ScreenManager;
import screen.level.LevelData;
import utilities.enumerations.Direction;
import utilities.enumerations.GameStatesEnum;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GameStage extends Stage
{
	// Constantes
	// public static final int GROUND_HEIGHT = 260; // TODO REMOVE

	// Variables statics
	public static final boolean				debug			= false;
	public static LevelData					levelData		= ScreenManager.getInstance().getLevelSelected();
	public static GameStatesEnum			gameState		= GameStatesEnum.GAME_RUN;
	public static ArrayList<HudMessages>	hudMessages;

	private GlobalController				globalController;
	private LevelParser						levelParser;

	// Camera gestion --------------------------------------------------------------
	private Vector3							cameraPosition;
	private float							cameraSmooth	= 0.07f;
	public static boolean					cameraMovingX	= false;											// Permet de gerer le deplacement du background on
																												// fonction de si la caméra bouge ou non.
	// Initialisation du shake
	private static boolean					shake			= false;											// Permet de faire un shake de l'écran
	private static float					shakeRadius		= 25;
	public static int						SMALL_SHAKE		= 10;
	public static int						HIGH_SHAKE		= 30;
	private int								randomAngle;
	private Vector2							offsetVector	= new Vector2();

	public GameStage()
	{
		// --Initialisation des variables globales
		gameState = GameStatesEnum.GAME_RUN;
		levelData = ScreenManager.getInstance().getLevelSelected();
		hudMessages = new ArrayList<HudMessages>();

		System.out.println("--------------------------------------");
		System.out.println("GameStage Créé");
		System.out.println("Level joué : " + levelData.levelIndex);
		System.out.println("Pour gagner les 45 points de talents, il faut 45 niveau soit " + PlayerStats.getSumXpForLevel(45) + " xp.");
		System.out.println("Pour acheter les 6 armes il faut :" + (200 + 500 + 1500 + 3500 + 6500 * 2) + " golds");

		GameStage.hudMessages.add(new HudMessages("Level :" + ScreenManager.getInstance().getLevelSelected().levelIndex, ""
				+ ScreenManager.getInstance().getLevelSelected().levelName));

		setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));
		// setViewport(new FitViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));
		levelParser = new LevelParser();
		globalController = new GlobalController(this, levelParser);
		cameraPosition = getCamera().position;
		OrthographicCamera a = (OrthographicCamera) this.getCamera();
		a.zoom += 0.2;
		a.zoom -= 0.2;
		/*
		 * Line aaa = new Line(new Vector2(1200, 300), new Vector2(1500,600), 50); addActor(aaa);
		 * 
		 * Bolt bolt = new Bolt(); ArrayList<Line> aa = bolt.CreateBolt(new Vector2(globalController.getPlayer().getX(), globalController.getPlayer().getTop()), new
		 * Vector2( globalController.getPlayer().getX() + 500, globalController.getPlayer().getTop()), 10);
		 * 
		 * for (int i = 0; i < aa.size(); i++) { addActor(aa.get(i)); }
		 */
	}

	public static void cameraShake(int value)
	{
		shake = true;
		shakeRadius = value;
		// Gdx.input.vibrate(30);
	}

	@Override
	public void act(float delta)
	{

		super.act(delta);
		globalController.control(delta);

		// Controle de si il faut deplacer la camera et donc le background-------------------------------------------------------------------------------------
		if (globalController.getPlayer().getX() > MyGdxGame.VIRTUAL_WIDTH / 2
				&& globalController.getPlayer().getX() < MyGdxGame.VIRTUAL_WIDTH + MyGdxGame.VIRTUAL_WIDTH / 2)
		{
			cameraMovingX = true;
		} else
		{
			cameraMovingX = false;
		}

		// Controle le depalcement de la camera ----------------------------------------------------------------------------------------------------------
		if (!shake)
		{
			if (cameraMovingX) // controle sur X
			{
				// cameraPosition.x += (globalController.getPlayer().getX() - cameraPosition.x) * cameraSmooth; // si l'on peut deplacer la camera alors on le fait FIXME
				// cameraPosition.x += (globalController.getPlayer().getX() - cameraPosition.x); // si l'on peut deplacer la camera alors on le fait
			}

			// pas de controle sur Y

			// if (globalController.getPlayer().getY() > BlockController.OFFSET_2_HEIGHT)
			// {
			// cameraPosition.y += (globalController.getPlayer().getY() - GROUND_HEIGHT / 2 - cameraPosition.y) * cameraSmooth;
			// } else
			// {
			// cameraPosition.y += (MyGdxGame.VIRTUAL_HEIGHT / 2 - cameraPosition.y) * cameraSmooth;
			// }

			cameraPosition.x += (globalController.getPlayer().getX() - cameraPosition.x);
			cameraPosition.y += (globalController.getPlayer().getY() - cameraPosition.y) * cameraSmooth;
		}

		// Controle le shake ----------------------------------------------------------------------------------------------------------
		if (shake) // si shake
		{
			randomAngle += (180 + new Random().nextInt(180) - 60); // pick new angle
			offsetVector.set((float) (Math.sin(randomAngle) * shakeRadius), (float) (Math.cos(randomAngle) * shakeRadius)); // create offset 2d vector
			cameraPosition.x += offsetVector.x;
			cameraPosition.y += offsetVector.y;
			shakeRadius *= 0.9f; // diminish radius each frame
		}

		if (shakeRadius < 5)
		{
			shake = false;
		}
	}

	// HUD METHODES

	public void moveRight()
	{
		globalController.getPlayer().direction = Direction.RIGHT_DIRECTION;
	}

	public void moveLeft()
	{
		globalController.getPlayer().direction = Direction.LEFT_DIRECTION;
	}

	public void walk(boolean walk)
	{
		globalController.getPlayer().setWalk(walk);
	}

	public void shoot(boolean shoot)
	{
		globalController.getPlayer().setShoot(shoot);
		// shake = true;
	}

	public void jump()
	{
		if (!globalController.getPlayer().isInAir())
		{
			globalController.getPlayer().setJump(true);
		}
	}

	public boolean playerIsMoving()
	{
		return globalController.getPlayer().isWalk();
	}

	public int getPlayerLife()
	{
		return globalController.getPlayer().getLife();
	}

	public GlobalController getGlobalController()
	{
		return globalController;
	}

}
