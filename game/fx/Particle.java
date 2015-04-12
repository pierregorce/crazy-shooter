package game.fx;

import game.entitiy.PhysicalEntity;

import java.util.Random;

import ressources.R;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Une particule est un objet physicalEntity qui contient un sprite calé sur la physical entity
 * 
 * @author Pierre
 *
 */
public abstract class Particle extends PhysicalEntity implements Poolable
{
	public float		defaultLifetime;
	private float		lifetime;
	protected Sprite	sprite	= new Sprite(R.c().whiteSquareImage);

	public Particle() // FOR POOLING
	{

	}

	public void init(float x, float y, Color[] color)
	{
		lifetime = 0;
		jump = true;
		walk = true;
		sprite.setColor(color[new Random().nextInt(color.length)]);
		setX(x);
		setY(y);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		lifetime += delta;

		if (lifetime > defaultLifetime)
		{
			remove();
		}

		if (getCollisionBlock() != null || getY() < 0)
		{
			maxVelocityX = 0;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		sprite.setBounds(getX(), getY(), getWidth(), getHeight());
		sprite.draw(batch);
	}

	@Override
	public void reset()
	{

	}

}
