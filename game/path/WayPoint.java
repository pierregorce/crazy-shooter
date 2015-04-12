package game.path;

import game.entitiy.Character;

import java.util.ArrayList;

import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oasix.crazyshooter.GameStage;

public class WayPoint extends Actor
{

	public enum Action
	{
		JUMP,
		WALK_LEFT,
		WALK_RIGHT,
		NONE;
	}

	private Rectangle			startBounds;
	private ShapeRenderer		shapeRenderer;
	public ArrayList<Action>	actions;
	public boolean				selected	= false;
	public boolean				active		= false;

	public WayPoint(float x, float y, float size)
	{
		this(x, y, size, size);
	}

	public WayPoint(float x, float y, float width, float height)
	{
		startBounds = new Rectangle(x, y, width, height);
		setBounds(startBounds.getX(), startBounds.getY(), startBounds.getWidth(), startBounds.getHeight());

		if (GameStage.debug)
		{
			shapeRenderer = new ShapeRenderer();
		}

	}

	public void setActions(Action... actions)
	{
		this.actions = new ArrayList<Action>();
		for (Action action : actions)
		{
			this.actions.add(action);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		if (!selected)
		{
			setColor(Color.RED);
		} else
		{
			setColor(Color.BLUE);
		}

		if (GameStage.debug)
		{
			batch.end();
			shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(getColor());
			shapeRenderer.rect(startBounds.getX(), startBounds.getY(), startBounds.getWidth(), startBounds.getHeight());
			shapeRenderer.end();
			batch.begin();
		}
	}

	public boolean isInWayPoint(Character character)
	{
		// Check connexion pour savoir si on est dans le départ du wayPoint
		return Intersector.overlaps(startBounds, character.getCollisionBox());
	}

	/**
	 * Transmet a un character des actions (walk, jump...) qui l'ameneront au bout du wayPoint
	 * 
	 * @param character
	 */
	public void giveAction(Character character)
	{
		character.setWalk(false); // Si jamais on a pas de walk

		if (actions == null)
		{
			System.out.println("OOO je n'ai pas eu d'action set");
		}

		for (Action action : actions)
		{
			switch (action) {
			case JUMP:
				character.setJump(true);
				break;
			case WALK_LEFT:
				character.setWalk(true);
				character.direction = Direction.LEFT_DIRECTION;
				break;
			case WALK_RIGHT:
				character.setWalk(true);
				character.direction = Direction.RIGHT_DIRECTION;
				break;
			default:
				break;
			}
		}
	}
}
