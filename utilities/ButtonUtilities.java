package utilities;

import game.sound.MusicManager;
import ressources.S;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ButtonUtilities extends Button
{
	// Effet de décalage vertical
	protected float	currentY;
	public float	offset	= -10;

	// Sound au click sur le button
	private boolean	playSound;
	public Sound	sound	= S.basicButtonClickSound;

	@Override
	public void act(float delta)
	{
		super.act(delta);
		if (isPressed())
		{
			if (!playSound)
			{
				sound.play(MusicManager.higherMusicVolume);
				playSound = true;
			}
			setY(currentY + offset);
		} else
		{
			setY(currentY);
			playSound = false;
		}

	}

	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		currentY = getY();
	}

}
