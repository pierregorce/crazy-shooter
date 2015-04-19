package com.oasix.crazyshooter;

import game.entitiy.Entities;
import game.path.WayPoint;

import java.util.ArrayList;

import ressources.R;
import screen.MyGdxGame;
import screen.ScreenManager;
import screen.level.LevelGroup_old;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Block extends Entities
{

	public static int			height			= 65;

	private Sprite				sprite;

	// -------------------------------------------------------------------
	public boolean				isGround		= false;
	public boolean				active			= false;
	public String				debugNumber		= "X";
	private ShapeRenderer		shapeRenderer;
	private int					wayPointSize	= 20;
	public WayPoint				left;
	public WayPoint				middle;
	public WayPoint				right;
	private ArrayList<Block>	blockToCome		= new ArrayList<Block>();
	private ArrayList<WayPoint>	wayPointToCome	= new ArrayList<WayPoint>();

	public Block(int x, int y, int type)
	{
		setX(x);
		setY(y);

		int[] m_boss_level = LevelGroup_old.m_boss_level_from_1; // Récupère les niveau des boss (commence à 1)
		int level = ScreenManager.getInstance().getLevelSelected().levelIndex; // Recupère le level (commence à 1)

		// Charge le monde en fonction des niveau des boss
		Array<Texture> textures = null;

		if (level <= m_boss_level[0])
		{
			textures = R.c().block_level0;
		} else if (level > m_boss_level[0] && level < m_boss_level[1])
		{
			textures = R.c().block_level1;
		} else if (level > m_boss_level[1] && level < m_boss_level[2])
		{
			textures = R.c().block_level2;
		} else if (level > m_boss_level[2] && level < m_boss_level[3])
		{
			textures = R.c().block_level3;
		}

		textures = R.c().block_level;
		sprite = new Sprite(textures.get(type));

		setX(x);
		setY(y);
		setHeight(height);
		setWidth(height * sprite.getWidth() / sprite.getHeight());

		sprite.setBounds(x, y, getWidth(), height);
		isBlock = true;
		editBouncingBox(getWidth(), 85, 40);
		// editCollisionBox(getWidth(), 10, 0);
		init();
	}

	public Block(float x, float y, float width, float height)
	{
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		sprite = new Sprite(R.c().whiteSquareImage);
		sprite.setBounds(x, y, width, height);
		isBlock = true;
		init();
	}

	public Block(float x, float y, float width, float height, boolean pixelMultiplictor)
	{
		this(x * MyGdxGame.PIXEL_SIZE, y * MyGdxGame.PIXEL_SIZE, width * MyGdxGame.PIXEL_SIZE, height * MyGdxGame.PIXEL_SIZE);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		// sprite.setColor(Color.valueOf("54deaa"));
		sprite.setColor(Color.valueOf("77c3f8")); // depend on level
		// sprite.draw(batch);

		if (GameStage.debug)
		{
			batch.end();
			shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.GREEN);
			Vector2 popPosition = getPopPosition();
			float size = 15;
			shapeRenderer.rect(popPosition.x - size / 2, popPosition.y - size / 2, size, size);
			shapeRenderer.end();
			batch.begin();

			R.c().fontTypeBasicThin.drawWrapped(batch, debugNumber, getCenterX() - 70, getTop() + 30, 0);
		}

	}

	private void init()
	{
		left = new WayPoint(getX(), getTop(), wayPointSize);
		middle = new WayPoint(getCenterX() - wayPointSize / 2, getTop(), wayPointSize);
		right = new WayPoint(getRight() - wayPointSize, getTop(), wayPointSize);
		if (GameStage.debug)
		{
			shapeRenderer = new ShapeRenderer();
			GlobalController.fxController.addActor(left);
			GlobalController.fxController.addActor(middle);
			GlobalController.fxController.addActor(right);
		}
	}

	/**
	 * Renvoie la position centrale du block
	 */
	public Vector2 getPopPosition()
	{
		return new Vector2(getCenterX(), getTop() + 30);
	}

	public void setBlocksLinked(Block... blocks)
	{
		for (Block block : blocks)
		{
			blockToCome.add(block);
		}
	}

	public void setWayPointToCome(WayPoint... wayPoints)
	{
		for (WayPoint wayPoint : wayPoints)
		{
			wayPointToCome.add(wayPoint);
		}
	}

	public ArrayList<Block> getBlocksLinked()
	{
		ArrayList<Block> blockLinkedActive = new ArrayList<Block>();
		for (Block block : blockToCome)
		{
			if (block.active)
			{
				blockLinkedActive.add(block);
			}
		}
		return blockLinkedActive;
	}

	public ArrayList<WayPoint> getWayPointToCome()
	{
		ArrayList<WayPoint> wayPointActive = new ArrayList<WayPoint>();
		for (WayPoint wayPoint : wayPointToCome)
		{
			if (wayPoint.active)
			{
				wayPointActive.add(wayPoint);
			}
		}
		return wayPointActive;
	}
}
