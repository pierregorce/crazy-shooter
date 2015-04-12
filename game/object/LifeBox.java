package game.object;

import game.entitiy.PhysicalEntity;
import globals.PlayerStats;

import java.util.Random;

import ressources.DrawableSprite;
import ressources.R;
import screen.MyGdxGame;
import utilities.Methods;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Object drop by an enemy. It contain a certain quantity of point. Contient un nombre de point, une sprite et fait un petit saut a son apparition avant de retomber au
 * sol.
 * 
 * 
 * @author Pierre
 */
public class LifeBox extends PhysicalEntity
{
	private float			m_defaultLifetime	= 12;
	private float			m_lifetime			= 0;
	public int				m_value;

	private DrawableSprite	m_drawableSprite;

	public LifeBox(int value)
	{
		m_value = value;
		jump = true;
		walk = true;

		m_drawableSprite = new DrawableSprite(R.c().iconLifeBox);

		setX(new Random().nextInt(MyGdxGame.VIRTUAL_WORLD_WIDTH));
		setY(MyGdxGame.VIRTUAL_HEIGHT);

		float initialWidth = ((Sprite) m_drawableSprite).getRegionWidth();
		float initialHeight = ((Sprite) m_drawableSprite).getRegionHeight();
		float finalWidth = 60;
		float finalHeight = Methods.scaleByWidth(finalWidth, initialWidth, initialHeight);
		setWidth(finalWidth);
		setHeight(finalHeight);

		maxJumpSpeedY = 1;
		direction = Direction.values()[new Random().nextInt(Direction.values().length)];
		maxVelocityX = 0;
		gravity = gravity - gravity * 0.90f;
	}

	public LifeBox()
	{
		this(PlayerStats.getLifeBoxHp());
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		m_lifetime += delta;
		if (m_lifetime > m_defaultLifetime)
		{
			remove();
		}

		if (getCollisionBlock() != null)
		{
			jump = true;
		}

	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		m_drawableSprite.setBounds(getX(), getY(), getWidth(), getHeight());
		m_drawableSprite.draw(batch);

	}

}
