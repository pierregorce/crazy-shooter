package com.oasix.crazyshooter;

import ressources.DrawableSprite;
import ressources.R;
import ressources.Ressource;
import screen.MyGdxGame;
import screen.ScreenManager;
import screen.level.LevelGroup_old;
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

		int[] m_boss_level = LevelGroup_old.m_boss_level_from_1; // Récupère les niveau des boss (commence à 1)
		int level = ScreenManager.getInstance().getLevelSelected().levelIndex; // Recupère le level (commence à 1)

		// Charge le monde en fonction des niveau des boss
		Array<Texture> textures = null;

		if (level <= m_boss_level[0])
		{
			// BG1 - butcher
			textures = R.c().backGroundLayer_level0;

		} else if (level > m_boss_level[0] && level <= m_boss_level[1])
		{
			// BG2 - bee
			textures = R.c().backGroundLayer_level1;

		} else if (level > m_boss_level[1] && level < m_boss_level[2])
		{
			// BG3 - invocator
			textures = R.c().backGroundLayer_level2;
		} else if (level > m_boss_level[2] && level < m_boss_level[3])
		{
			// BG4 - town
			// textures = R.c().backGroundLayer_level3;
			textures = R.c().backGroundLayer_level4;
		} else if (level > m_boss_level[3] && level < m_boss_level[4])
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

		if (globalController.getPlayer().direction == Direction.LEFT_DIRECTION && globalController.getPlayer().isWalk() && globalController.getPlayer().getX() > 10
				&& GameStage.cameraMovingX)
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
