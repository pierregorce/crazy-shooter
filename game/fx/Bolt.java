package game.fx;

import globals.Projectiles;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Bolt extends Actor
{

	private Timer			timerBoltChange	= new Timer(0.02f);
	private Timer			timerShow		= new Timer(0.22f, 0.10f);
	private Player			player;
	private ArrayList<Line>	lineArray		= new ArrayList<Line>();
	private ArrayList<Line>	lineArray2		= new ArrayList<Line>();
	private ArrayList<Line>	lineArray3		= new ArrayList<Line>();
	private Vector2			objective		= new Vector2(0, 0);

	public Bolt(Player player)
	{
		this.player = player;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		if (timerBoltChange.doAction(delta) || true)
		{
			Vector2 v = new Vector2(player.getCorretedX(Projectiles.PLAYER_LIGHTING_GUN.characterAnchor.x), player.getY() + Projectiles.PLAYER_LIGHTING_GUN.characterAnchor.y);
			lineArray3 = createBolt(v.cpy(), objective.cpy(), 7);
			lineArray = createBolt(v.cpy(), objective.cpy(), 15);
			lineArray2 = createBolt(v.cpy(), objective.cpy(), 10);
		}
	}

	public void setObjective(float x, float y)
	{
		objective.x = x;
		objective.y = y;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		if (timerShow.doAction(Gdx.graphics.getDeltaTime()))
		{
			for (Line line2 : lineArray)
			{
				line2.draw(batch, parentAlpha);
			}
			for (Line line2 : lineArray2)
			{
				line2.draw(batch, parentAlpha);
			}
			for (Line line2 : lineArray3)
			{
				line2.draw(batch, parentAlpha);
			}
		}
	}

	private ArrayList<Line> createBolt(Vector2 source, Vector2 dest, int thickness)
	{
		float length = source.dst(dest);
		int divisionLength = 35;
		int subdivisionCount = (int) (length / divisionLength);
		float subdivisisionSize = length / subdivisionCount;
		int offset = 35;
		ArrayList<Vector2> pointList = new ArrayList<Vector2>();

		Vector2 tangent = dest.sub(source);
		tangent.nor();
		Vector2 normal = new Vector2(tangent.y, -tangent.x);
		normal.nor();

		for (int i = 0; i <= subdivisionCount; i++)
		{
			Vector2 point = tangent.cpy();
			point.scl(subdivisisionSize * i);
			point.add(source);

			if (i > 1 && i < subdivisionCount)
			{
				int offsetRandomized = new Random().nextInt(offset) - offset / 2;
				Vector2 test = normal.cpy();
				test.scl(offsetRandomized);
				point.add(test);
			}
			pointList.add(point);
		}

		ArrayList<Line> results = new ArrayList<Line>();
		for (int i = 1; i < pointList.size(); i++)
		{
			results.add(new Line(pointList.get(i - 1), pointList.get(i), thickness));
		}
		return results;

	}

}
