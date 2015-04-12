package game.object;

import game.entitiy.PhysicalEntity;

import java.util.Random;

import ressources.DrawableAnimation;
import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Object drop by an enemy. It contain a certain quantity of point. Contient un nombre de point, une sprite et fait un petit saut a son apparition avant de retomber au
 * sol.
 * 
 * 
 * @author Pierre
 */
public class Coin extends PhysicalEntity
{
	private float				m_defaultLifetime	= 7;
	private float				m_lifetime			= 0;

	private DrawableAnimation	m_drawableAnimation;

	public int					m_value;

	public Coin(float x, float y, int value)
	{
		m_value = value;
		jump = true;
		walk = true;

		// run entre 0.09 et 0.11;
		float min = 0.08f;
		float max = 0.15f;
		float ecart = max - min;

		float variablePart = new Random().nextFloat() / (100 * ecart);

		float run = variablePart + min;

		m_drawableAnimation = new DrawableAnimation(run, R.c().iconCoin);

		setX(x);
		setY(y);

		int size = 35;
		setWidth(size);
		setHeight(size);

		maxJumpSpeedY = new Random().nextInt(12) + 5;
		direction = Direction.values()[new Random().nextInt(Direction.values().length)];
		maxVelocityX = new Random().nextInt(8);
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
			maxVelocityX = 0;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		m_drawableAnimation.setBounds(getX(), getY(), getWidth(), getHeight());
		m_drawableAnimation.draw(batch);

	}

}
