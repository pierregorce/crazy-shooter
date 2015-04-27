package game.fx;

import game.entitiy.Character;
import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Timer;

public class Beam extends Actor
{
	private Sprite		sprite;
	private Sprite		spriteMiddle;
	private Sprite		spriteRound1;
	private Sprite		spriteRoundWhite1;
	private Sprite		spriteRound2;
	private Sprite		spriteRoundWhite2;
	private float		spriteRoundSizeStart		= 15;
	private float		spriteRoundWhiteSizeStart	= 5;

	private float		spriteRoundSizeEnd			= 25;
	private float		spriteRoundWhiteSizeEnd		= 10;

	private float		thickness					= 8;
	private Timer		thicknessTimerEffect;
	private float		thicknessVariation			= 3;
	private Timer		blinckingTimer;

	private Character	m_character;

	public Beam()
	{
		super();
		sprite = new Sprite(R.c().whiteSquareImage);
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		sprite.setColor(Color.RED);
		sprite.setAlpha(0.7f);

		spriteMiddle = new Sprite(R.c().whiteSquareImage);
		spriteMiddle.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		spriteMiddle.setColor(Color.valueOf("a52e2e"));
		spriteMiddle.setAlpha(0.7f);

		spriteRound1 = new Sprite(R.c().whiteRoundImage);
		spriteRound1.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		spriteRound1.setColor(Color.RED);
		spriteRound1.setAlpha(0.9f);

		spriteRoundWhite1 = new Sprite(R.c().whiteRoundImage);
		spriteRoundWhite1.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		spriteRoundWhite1.setColor(Color.valueOf("a52e2e"));
		spriteRoundWhite1.setAlpha(0.8f);

		spriteRound2 = new Sprite(R.c().whiteRoundImage);
		spriteRound2.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		spriteRound2.setColor(Color.RED);
		spriteRound2.setAlpha(0.9f);

		spriteRoundWhite2 = new Sprite(R.c().whiteRoundImage);
		spriteRoundWhite2.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		spriteRoundWhite2.setColor(Color.valueOf("a52e2e"));
		spriteRoundWhite2.setAlpha(0.8f);

		thicknessTimerEffect = new Timer(0.1f);
		blinckingTimer = new Timer(0.04f, 0.04f);

		GlobalController.fxController.addActor(this);
	}

	public void setRobotSpecification()
	{
		sprite.setColor(Color.RED);
		spriteMiddle.setColor(Color.valueOf("a52e2e"));
		spriteRound1.setColor(Color.RED);
		spriteRoundWhite1.setColor(Color.valueOf("a52e2e"));
		spriteRound2.setColor(Color.RED);
		spriteRoundWhite2.setColor(Color.valueOf("a52e2e"));
	}

	public void setScientistSpecification()
	{

		sprite.setColor(Color.BLUE);
		spriteMiddle.setColor(Color.WHITE);
		spriteRound1.setColor(Color.BLUE);
		spriteRoundWhite1.setColor(Color.WHITE);
		spriteRound2.setColor(Color.BLUE);
		spriteRoundWhite2.setColor(Color.WHITE);
		spriteRoundSizeStart = 50;
		spriteRoundWhiteSizeStart = 35;
		spriteRoundSizeEnd = 35;
		spriteRoundWhiteSizeEnd = 20;
		thickness = 20;
		thicknessVariation = 5;
	}

	public void setPlayerSpecification()
	{
		sprite.setColor(Color.MAGENTA);
		spriteMiddle.setColor(Color.WHITE);
		spriteRound1.setColor(Color.MAGENTA);
		spriteRoundWhite1.setColor(Color.WHITE);
		spriteRound2.setColor(Color.MAGENTA);
		spriteRoundWhite2.setColor(Color.WHITE);
		spriteRoundSizeStart = 50;
		spriteRoundWhiteSizeStart = 35;
		spriteRoundSizeEnd = 35;
		spriteRoundWhiteSizeEnd = 20;
		thickness = 15;
		thicknessVariation = 5;

	}

	public void init(Vector2 playerAnchor, float lenght, Character character)
	{
		m_character = character;
		if (character.direction == Direction.LEFT_DIRECTION)
		{
			float x = character.getRight() - playerAnchor.x;
			float playerX = character.getX() + x;
			setBounds(playerX - lenght, playerAnchor.y, lenght, thickness);

		} else
		{
			setBounds(playerAnchor.x, playerAnchor.y, lenght, thickness);
		}

	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		batch.end();
		batch.setBlendFunction(GL20.GL_BLEND, GL20.GL_ONE);
		batch.begin();

		if (blinckingTimer.doAction(Gdx.graphics.getDeltaTime()))
		{
			spriteRound1.draw(batch);
			spriteRound2.draw(batch);
			sprite.draw(batch);
			spriteMiddle.draw(batch);
			spriteRoundWhite1.draw(batch);
			spriteRoundWhite2.draw(batch);
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (thicknessTimerEffect.doAction(delta))
		{
			thicknessVariation = thicknessVariation * -1;
			setHeight(thickness + thicknessVariation);
			setY(getY() + (thicknessVariation * -1) / 2);
		}

		sprite.setBounds(getX(), getY(), getWidth(), getHeight());
		spriteMiddle.setBounds(getX(), getY() + getHeight() / 2 - 5 / 2, getWidth(), 5);

		// Start or end round
		if (m_character.direction == Direction.LEFT_DIRECTION)
		{

			spriteRound1.setBounds(getRight() - spriteRoundSizeStart / 2, getY() + getHeight() / 2 - spriteRoundSizeStart / 2, spriteRoundSizeStart, spriteRoundSizeStart);
			spriteRoundWhite1.setBounds(getRight() - spriteRoundWhiteSizeStart / 2, getY() + getHeight() / 2 - spriteRoundWhiteSizeStart / 2, spriteRoundWhiteSizeStart, spriteRoundWhiteSizeStart);

			spriteRound2.setBounds(getX() - spriteRoundSizeEnd / 2, getY() + getHeight() / 2 - spriteRoundSizeEnd / 2, spriteRoundSizeEnd, spriteRoundSizeEnd);
			spriteRoundWhite2.setBounds(getX() - spriteRoundWhiteSizeEnd / 2, getY() + getHeight() / 2 - spriteRoundWhiteSizeEnd / 2, spriteRoundWhiteSizeEnd, spriteRoundWhiteSizeEnd);

		} else
		{
			spriteRound1.setBounds(getX() - spriteRoundSizeStart / 2, getY() + getHeight() / 2 - spriteRoundSizeStart / 2, spriteRoundSizeStart, spriteRoundSizeStart);
			spriteRoundWhite1.setBounds(getX() - spriteRoundWhiteSizeStart / 2, getY() + getHeight() / 2 - spriteRoundWhiteSizeStart / 2, spriteRoundWhiteSizeStart, spriteRoundWhiteSizeStart);

			spriteRound2.setBounds(getRight() - spriteRoundSizeEnd / 2, getY() + getHeight() / 2 - spriteRoundSizeEnd / 2, spriteRoundSizeEnd, spriteRoundSizeEnd);
			spriteRoundWhite2.setBounds(getRight() - spriteRoundWhiteSizeEnd / 2, getY() + getHeight() / 2 - spriteRoundWhiteSizeEnd / 2, spriteRoundWhiteSizeEnd, spriteRoundWhiteSizeEnd);
		}
	}
}
