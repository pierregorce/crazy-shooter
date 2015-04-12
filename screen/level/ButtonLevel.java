package screen.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ButtonLevel extends Button
{

	private LevelData	m_levelData;

	public ButtonLevel(LevelData m_levelData, ButtonStyle buttonStyle)
	{
		super(buttonStyle);
		this.m_levelData = m_levelData;
		colorization = Color.RED;

	}

	public LevelData getLevelData()
	{
		return m_levelData;
	}

	protected Color	colorization	= Color.RED;

	@Override
	public void draw(Batch batch, float parentAlpha)
	{

		if (colorization != null)
		{
			batch.end();
			batch.begin();
			batch.setColor(colorization);
			super.draw(batch, parentAlpha);
			validate();
			batch.end();
			batch.begin();
			batch.setColor(Color.WHITE);
		} else
		{
			// super.draw(batch, parentAlpha);
		}
	}

	@Override
	protected void drawChildren(Batch batch, float parentAlpha)
	{
		if (colorization != null)
		{
			batch.end();
			batch.begin();
			batch.setColor(colorization);
			super.drawChildren(batch, parentAlpha);
			validate();
			batch.end();
			batch.begin();
			batch.setColor(Color.WHITE);
		} else
		{
			// super.draw(batch, parentAlpha);
		}
	}

	@Override
	protected void drawBackground(Batch batch, float parentAlpha, float x, float y)
	{
		if (colorization != null)
		{
			batch.end();
			batch.begin();
			batch.setColor(colorization);
			super.drawBackground(batch, parentAlpha, x, y);
			validate();
			batch.end();
			batch.begin();
			batch.setColor(Color.WHITE);
		} else
		{
			// super.draw(batch, parentAlpha);
		}
	}

}
