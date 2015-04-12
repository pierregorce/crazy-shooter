package game.fx;

import ressources.R;
import utilities.Methods;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Line extends Actor
{

	private Vector2	end;
	private Vector2	start;
	private float	thickness;

	private float	length;
	private float	rotation;

	private Sprite	sprite;		// Background
	private Sprite	startImageLeft;
	private Sprite	endImageRight;

	// A and B are the line's endpoints.

	public Line(Vector2 start, Vector2 end, int thickness)
	{
		super();
		this.start = start;
		this.thickness = thickness;

		length = start.dst(end);
		rotation = (float) Math.atan2(end.y - start.y, end.x - start.x);

		sprite = new Sprite(R.c().primitive_beam);
		// sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// sprite.setBounds(start.x, start.y, length, thickness);
		sprite.setBounds(start.x, start.y - thickness / 2, length, thickness);
		sprite.setOrigin(0, thickness / 2);
		sprite.setRotation((float) (Math.toDegrees(rotation)));

		// sprite.setColor(Color.BLUE);
		// sprite.setAlpha(0.7f);

		startImageLeft = new Sprite(R.c().primitive_beam_left);
		float initialWidth = R.c().primitive_beam_left.getRegionWidth();
		float initialHeight = R.c().primitive_beam_left.getRegionHeight();
		float finalWidth = Methods.scaleByHeight(thickness, initialWidth, initialHeight);
		startImageLeft.setBounds(start.x - finalWidth, start.y - thickness / 2, finalWidth, thickness);

		endImageRight = new Sprite(R.c().primitive_beam_right);
		endImageRight.setBounds(end.x, end.y - thickness / 2, Methods.scaleByHeight(thickness, initialWidth, initialHeight), thickness);

		startImageLeft.setOrigin(startImageLeft.getWidth() / 2, startImageLeft.getHeight() / 2);
		startImageLeft.setRotation((float) (Math.toDegrees(rotation)));
		endImageRight.setOrigin(endImageRight.getWidth() / 2, endImageRight.getHeight() / 2);
		endImageRight.setRotation((float) (Math.toDegrees(rotation)));
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		// batch.end(); // actual drawing is done on end(); if we do not end, we contaminate previous rendering.
		// batch.setBlendFunction(GL20.GL_BLEND, GL20.GL_ONE);
		// batch.begin();

		sprite.draw(batch);
		startImageLeft.draw(batch);
		endImageRight.draw(batch);
		// sprite2.draw(batch);

	}

	public void setCoordinate(Vector2 start, Vector2 end)
	{
		// LA rotation ne changema jamais car le truc est droit
		length = start.dst(end);
		sprite.setBounds(start.x, start.y, length, thickness);
		setBounds(start.x, start.y, length, thickness);
		setBounds(start.x, start.y, length, thickness);
		sprite.setBounds(start.x, start.y, length, thickness);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
	}

}
