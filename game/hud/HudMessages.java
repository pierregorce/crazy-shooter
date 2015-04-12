package game.hud;

import ressources.ActionFactory;
import ressources.R;
import screen.MyGdxGame;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class HudMessages extends Group
{
	int yTop = 770;
	int yUnder = yTop - 100;

	public static enum MessageType
	{
		DEFAULT, NEW_WAVE, LEVEL_UP;
	}

	public HudMessages(String message1, String message2)
	{
		this(message1, message2, 6);
	}

	public HudMessages(String message1, String message2, int scale)
	{
		BitmapFont bitmapFont = R.c().fontTypeGreen;

		Label label = new Label(message1, new LabelStyle(bitmapFont, null));
		addActor(label);
		label.setFontScale(scale);
		label.setPosition(-MyGdxGame.VIRTUAL_WIDTH, yTop);

		SequenceAction a = new SequenceAction(ActionFactory.getGameAnimationFromRight(yTop, label.getTextBounds().width), new RemoveActorAction());
		label.addAction(a);

		Label label2 = new Label(message2, new LabelStyle(bitmapFont, null));
		addActor(label2);
		label2.setFontScale(scale - 2);
		label2.setPosition(MyGdxGame.VIRTUAL_WIDTH * 2, yUnder);

		SequenceAction b = new SequenceAction(ActionFactory.getGameAnimationFromLeft(yUnder, label2.getTextBounds().width), new RemoveActorAction());
		label2.addAction(b);
	}

}
