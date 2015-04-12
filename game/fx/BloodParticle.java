package game.fx;

import java.util.Random;

import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Color;
import com.oasix.crazyshooter.GlobalController;

public class BloodParticle extends Particle
{
	public BloodParticle()
	{

	}

	public BloodParticle(float x, float y)
	{
		// super(x, y, ExplosionColor.getInstance().getBloodColor());
		init(x, y, ParticleColors.getInstance().getBloodColor());
	}

	@Override
	public void init(float x, float y, Color[] color)
	{
		int size = new Random().nextInt(12) + 8;
		setWidth(size);

		size = new Random().nextInt(12) + 3;
		setHeight(size);

		defaultLifetime = new Random().nextFloat() * 10 + 5f;
		direction = Direction.values()[new Random().nextInt(Direction.values().length)];
		maxVelocityX = new Random().nextInt(8);
		maxJumpSpeedY = new Random().nextInt(12) + 5;
		super.init(x, y, color);
	}

	@Override
	public boolean remove()
	{
		if (super.remove())
		{
			GlobalController.bloodParticlePool.free(this);
			return true;
		}
		return false;
	}

}
