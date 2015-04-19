package com.oasix.crazyshooter;

import globals.Worlds;
import ressources.DrawableSprite;
import ressources.R;
import ressources.Ressource;
import screen.MyGdxGame;
import screen.ScreenManager;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class Background extends Group
{

	private Ressource			layer1;
	private Ressource			layer2;
	private Ressource			layer3;
	private Ressource			layer4;

	private float				layer2speed	= 0.7f;
	private float				layer3speed	= 0.5f;
	private float				layer4speed	= 0.3f;

	private GlobalController	globalController;

	/**
	 * Layer1 : le SOCLE : ne bouge pas Layer2 : 1er plan Layer3 : 2eme plan Layer4 : 3eme plan
	 * 
	 * @param globalController
	 */
	public Background(GlobalController globalController)
	{
		this.globalController = globalController;

		int level = ScreenManager.getInstance().getLevelSelected().levelIndex; // Recupère le level

		// Charge le monde en fonction des niveau des boss
		Array<Texture> textures = null;

		System.out.println(level);
		System.out.println("le niveau " + level + " correspond a world : " + Worlds.getWorldNumber(level));

		if (Worlds.getWorldNumber(level) == 0)
		{
			textures = R.c().backGroundLayer_level0;
		} else if (Worlds.getWorldNumber(level) == 1)
		{
			textures = R.c().backGroundLayer_level1;
		} else if (Worlds.getWorldNumber(level) == 2)
		{
			textures = R.c().backGroundLayer_level2;
		} else if (Worlds.getWorldNumber(level) == 3)
		{
			textures = R.c().backGroundLayer_level3;
			textures = R.c().backGroundLayer_level4;
		} else if (Worlds.getWorldNumber(level) == 4)
		{
			textures = R.c().backGroundLayer_level5;
		}

		int offset = 400 - 270;
		int off = offset * 4;
		int oo = 70 * 4;

		int xOffset = -120;

		float offsetX = -0 * MyGdxGame.PIXEL_SIZE;
		float offsetY = -70 * MyGdxGame.PIXEL_SIZE;

		layer1 = new Ressource(new DrawableSprite(textures.get(0)), offsetX, offsetY, textures.get(0).getHeight() * 4);
		layer2 = new Ressource(new DrawableSprite(textures.get(1)), offsetX, offsetY, textures.get(0).getHeight() * 4);
		layer3 = new Ressource(new DrawableSprite(textures.get(2)), offsetX, offsetY, textures.get(0).getHeight() * 4);
		layer4 = new Ressource(new DrawableSprite(textures.get(3)), offsetX, offsetY, textures.get(0).getHeight() * 4);

		// layer1 = new Ressource(new DrawableSprite(textures.get(0)), 0, 0, textures.get(0).getHeight() * MyGdxGame.PIXEL_SIZE);
		// layer2 = new Ressource(new DrawableSprite(textures.get(1)), 0, 0, textures.get(0).getHeight() * MyGdxGame.PIXEL_SIZE);
		// layer3 = new Ressource(new DrawableSprite(textures.get(2)), 0, 0, textures.get(0).getHeight() * MyGdxGame.PIXEL_SIZE);
		// layer4 = new Ressource(new DrawableSprite(textures.get(3)), 0, 0, textures.get(0).getHeight() * MyGdxGame.PIXEL_SIZE);

		addActor(layer4);
		addActor(layer3);
		addActor(layer2);
		addActor(layer1);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (globalController.getPlayer().direction == Direction.LEFT_DIRECTION && globalController.getPlayer().isWalk() && globalController.getPlayer().getX() > 10 && GameStage.cameraMovingX)
		{
			layer4.setX(layer4.getX() + layer4speed);
			layer3.setX(layer3.getX() + layer3speed);
			layer2.setX(layer2.getX() + layer2speed);
		}

		if (globalController.getPlayer().direction == Direction.RIGHT_DIRECTION && globalController.getPlayer().isWalk() && GameStage.cameraMovingX)
		{
			layer4.setX(layer4.getX() - layer4speed);
			layer3.setX(layer3.getX() - layer3speed);
			layer2.setX(layer2.getX() - layer2speed);
		}
	}
}
