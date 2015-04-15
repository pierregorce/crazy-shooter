package game.entitiy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oasix.crazyshooter.Block;
import com.oasix.crazyshooter.GameStage;

public class Entities extends Actor
{

	private Rectangle		collisionBox;
	private Rectangle		bouncingBox;
	private ShapeRenderer	shapeRenderer;
	private Block			collisionBlock	= null;
	private Vector2			position;

	protected boolean		isBlock			= false;

	public Entities()
	{
		super();
		collisionBox = new Rectangle(getX(), getY(), getWidth(), getHeight());
		bouncingBox = new Rectangle(getX(), getY(), getWidth(), getHeight());
		if (GameStage.debug)
		{
			shapeRenderer = new ShapeRenderer();
		}
		// setDebug(GameStage.debug);

	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		if (GameStage.debug)
		{
			batch.end();
			shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect(bouncingBox.getX(), bouncingBox.getY(), bouncingBox.getWidth(), bouncingBox.getHeight());
			shapeRenderer.setColor(Color.PINK);
			shapeRenderer.rect(collisionBox.getX(), collisionBox.getY(), collisionBox.getWidth(), collisionBox.getHeight());
			shapeRenderer.end();
			batch.begin();
		}

	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		if (!isBlock)
		{
			collisionBox.setSize(collisionBoxWidth, collisionBoxHeight); // size uniquement car la position est géré par la classe fille qui gere la direction
			if (bouncingBoxWidth == 0)
			{
				bouncingBox.set(getX(), getY(), getWidth(), getHeight());
			} else
			{
				bouncingBox.setSize(bouncingBoxWidth, bouncingBoxHeight);
			}
		} else
		{
			if (bouncingBoxWidth == 0)
			{
				bouncingBox.set(getX(), getY(), getWidth(), getHeight());
			} else
			{
				bouncingBox.set(getX(), getY() + bouncingBoxLeftOffset, bouncingBoxWidth, bouncingBoxHeight);
			}
		}
	}

	protected float	collisionBoxWidth		= getWidth();
	protected float	collisionBoxHeight		= getHeight();
	protected float	collisionBoxLeftOffset	= 0;			// Decalage lorsque le player regarde vers la droite

	/**
	 * 
	 * @param width
	 * @param height
	 * @param offset
	 *            Decalage lorsque le player regarde vers la droite
	 */
	public void editCollisionBox(float width, float height, float offset)
	{
		collisionBoxWidth = width;
		collisionBoxHeight = height;
		collisionBoxLeftOffset = offset;
	}

	protected float	bouncingBoxWidth;
	protected float	bouncingBoxHeight;
	protected float	bouncingBoxLeftOffset	= 0;	// Decalage lorsque le player regarde vers la droite

	public void editBouncingBox(float width, float height, float offset)
	{
		bouncingBoxWidth = width;
		bouncingBoxHeight = height;
		bouncingBoxLeftOffset = offset;
	}

	public Rectangle getCollisionBox()
	{
		return collisionBox;
	}

	public Rectangle getBouncingBox()
	{
		return bouncingBox;
	}

	public void setCollisionBlock(Block collisionBlock)
	{
		this.collisionBlock = collisionBlock;
	}

	public Block getCollisionBlock()
	{
		return collisionBlock;
	}

	public Vector2 getPosition()
	{
		if (position == null)
		{
			position = new Vector2(getX(), getY());
		}
		position.set(getX(), getY());
		return position;
	}
}
