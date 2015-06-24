package utilities;

import ressources.S;
import ressources.S.TyrianSound;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonUtilities extends Button
{
	// Effet de décalage vertical
	protected float	currentY;
	public float	offset	= -10;

	// Sound au click sur le button
	private boolean	playSound;

	public ButtonUtilities()
	{
	}

	public ButtonUtilities(TextureRegionDrawable textureRegionDrawable)
	{
		super(textureRegionDrawable);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		if (isPressed())
		{
			if (!playSound)
			{
				S.c().play(TyrianSound.soundEffect_menu_clickOnLevel, null, null);
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
