package game.fx;

import game.entitiy.Character;

import java.util.Random;

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class LimaceParticle extends Particle
{

	public LimaceParticle()
	{
		sprite = new Sprite(R.c().whiteRoundImage);
	}

	public void init(Character character, Vector2 anchor, Color[] color)
	{
		this.direction = character.direction;

		// Size
		int maxSize = 35;
		int minSize = 15;
		int size = new Random().nextInt(maxSize - minSize) + minSize;
		setWidth(size);
		size = new Random().nextInt(maxSize - minSize) + minSize;
		setHeight(size);

		if (character.direction == Direction.RIGHT_DIRECTION)
		{
			init(anchor.x, anchor.y, color);
		} else
		{
			float x = character.getRight() - anchor.x;
			init(character.getX() + x - getWidth(), anchor.y, color);
		}
	}

	@Override
	public void init(float x, float y, Color[] color)
	{
		// Hauteur d'envol.
		maxJumpSpeedY = new Random().nextInt(5) + 12;
		// Vitesse
		maxVelocityX = new Random().nextInt(6) + 10;

		// Durée de vie
		float minLifeTime = 8f;
		float maxLifeTime = 10f;
		defaultLifetime = new Random().nextFloat() * (maxLifeTime - minLifeTime) + minLifeTime;
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

	@Override
	public void act(float delta)
	{
		super.act(delta);
		if (getCollisionBlock() != null)
		{
			setHeight(7);
		}

	}

}
