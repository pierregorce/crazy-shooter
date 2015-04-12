package screen.loading;

import screen.MyGdxGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class MovableObject extends Actor
{
	private float		moveSpeed	= 0;
	private float		factor		= 4;

	private Sprite		sprite1;
	private Sprite		sprite2;							// Represent the following sprite on start

	private ResetMethod	resetMethod	= ResetMethod.RESET;

	static enum ResetMethod
	{
		RESET,
		LOOP
	}

	public MovableObject(Group group, Texture texture, ResetMethod resetMethod)
	{
		group.addActor(this);
		sprite1 = new Sprite(texture);
		sprite1.setBounds(getX(), getY(), getWidth(), getHeight());
		this.resetMethod = resetMethod;

		if (resetMethod == ResetMethod.LOOP)
		{
			sprite2 = new Sprite(texture);
			sprite2.setBounds(sprite1.getX() + sprite1.getWidth(), getY(), getWidth(), getHeight());
		}

		setSize(texture.getWidth() * factor, texture.getHeight() * factor);
	}

	public void putMoveSpeed(float moveSpeed)
	{
		this.moveSpeed = moveSpeed;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		if (resetMethod == ResetMethod.LOOP)
		{
			sprite1.draw(batch);
			sprite2.draw(batch);
		} else
		{
			sprite1.draw(batch);
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		setPosition(getX() + moveSpeed, getY());

		sprite1.setBounds(getX(), getY(), getWidth(), getHeight());

		if (resetMethod == ResetMethod.LOOP)
		{
			sprite2.setBounds(sprite1.getX() + sprite1.getWidth(), getY(), getWidth(), getHeight());
		}

		if (resetMethod == ResetMethod.LOOP)
		{

			if (getRight() < 0)
			{
				switchSpriteOnLeaveScreen();
			}
		} else
		{

			suppressionOnLeaveScreen();
		}

	}

	private void suppressionOnLeaveScreen()
	{
		// Se deplace vers la gauche
		if (getRight() < 0)
		{
			setX(MyGdxGame.VIRTUAL_WIDTH);
		}
		// Se deplace vers la droite
		if (getX() > MyGdxGame.VIRTUAL_WIDTH)
		{
			setX(0 - getWidth());
		}
	}

	private void switchSpriteOnLeaveScreen()
	{
		if (sprite1.getX() < sprite2.getX())
		{
			sprite1.setX(sprite2.getX() + sprite2.getWidth());
			setX(sprite2.getX());
			return;
		}

		if (sprite1.getX() > sprite2.getX())
		{
			sprite2.setX(sprite1.getX() + sprite1.getWidth());
			setX(sprite1.getX());
			return;
		}
	}

}
