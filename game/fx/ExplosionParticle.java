package game.fx;

import java.util.Random;

import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Color;

public class ExplosionParticle extends Particle
{
	public ExplosionParticle()
	{

	}

	public ExplosionParticle(float x, float y)
	{
		// super(x, y, ExplosionColor.getInstance().getBloodColor());
		init(x, y, ParticleColors.getInstance().getExplosionColor());
	}

	@Override
	public void init(float x, float y, Color[] color)
	{
		int size = new Random().nextInt(12) + 5;

		size = new Random().nextInt(8) + 4;
		setWidth(size);
		setHeight(size);

		defaultLifetime = new Random().nextFloat() * 2 + 0.5f;
		direction = Direction.values()[new Random().nextInt(Direction.values().length)];
		maxVelocityX = new Random().nextInt(8);
		maxJumpSpeedY = new Random().nextInt(10) + 8;
		super.init(x, y, color);
	}

	@Override
	public boolean remove()
	{
		if (super.remove())
		{
			// GlobalController.bloodParticlePool.free(this);
			return true;
		}
		return false;
	}

}
