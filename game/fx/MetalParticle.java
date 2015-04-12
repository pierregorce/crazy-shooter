package game.fx;

import java.util.Random;

import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Color;
import com.oasix.crazyshooter.GlobalController;

public class MetalParticle extends Particle
{
	public MetalParticle()
	{

	}

	public MetalParticle(float x, float y)
	{
		// super(x, y, ExplosionColor.getInstance().getBloodColor());
		init(x, y, ParticleColors.getInstance().getIceColor());
	}

	@Override
	public void init(float x, float y, Color[] color)
	{
		int size = new Random().nextInt(10) + 3;
		setWidth(size);
		setHeight(size);
		defaultLifetime = (0.4f);
		direction = Direction.values()[new Random().nextInt(Direction.values().length)];
		maxVelocityX = new Random().nextInt(1);
		maxJumpSpeedY = new Random().nextInt(5) + 5;
		super.init(x, y, color);
	}

	@Override
	public boolean remove()
	{
		if (super.remove())
		{
			GlobalController.metalParticlePool.free(this);
			return true;
		}
		return false;
	}

}
