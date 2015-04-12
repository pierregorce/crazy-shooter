package game.fx;

import game.entitiy.PhysicalEntity;

import java.util.Random;

import ressources.R;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Une particule est un objet physicalEntity qui contient un sprite calé sur la physical entity
 * 
 * @author Pierre
 *
 */
public class SquareParticleExplosion extends PhysicalEntity
{
	private float minTimeFrame = 0.03f;
	private float maxTimeFrame = 0.07f;
	protected float animationTime = new Random().nextFloat() * (maxTimeFrame - minTimeFrame) + minTimeFrame;
	protected Animation m_animation = new Animation(animationTime, R.c().squareExplosion_1);
	private float minSize = 70;
	private float maxSize = 3f;
	protected float size = new Random().nextFloat() * (maxSize - minSize) + minSize;

	private float stateTime = 0;

	public SquareParticleExplosion(float x, float y)
	{
		jump = true;
		setX(x);
		setY(y);
		int minSpeed = 10;
		int maxSpeed = 25;
		maxJumpSpeedY = new Random().nextInt(maxSpeed - minSpeed) + minSpeed;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		stateTime += delta;

		if (m_animation.isAnimationFinished(stateTime))
		{
			remove();
		}
	}

	int rotation = new Random().nextInt(2);
	float opacite = new Random().nextFloat();
	Color color = Color.MAGENTA;

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		batch.setColor(color.r, color.g, color.b, opacite);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		if (rotation == 0)
		{
			batch.draw(m_animation.getKeyFrame(stateTime), getX(), getY(), size, size);
		}
		if (rotation == 1)
		{
			batch.draw(m_animation.getKeyFrame(stateTime), getX(), getY(), size / 2, size / 2, size, size, 1f, 1f, 45f);
		}
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.setColor(Color.WHITE);
	}

}
