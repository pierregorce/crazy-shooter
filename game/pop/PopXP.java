package game.pop;

import game.entitiy.Entities;

import java.util.Random;

import ressources.ActionFactory;
import ressources.R;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class PopXP extends Group
{
	private Label label;

	public PopXP(String value, Entities entities)
	{
		BitmapFont bitmapFont = R.c().fontTypeBasicBlod;

		label = new Label("" + value, new LabelStyle(bitmapFont, null));
		label.setFontScale(5.1f);
		label.setPosition(entities.getCenterX(), entities.getTop());
		addActor(label);

		int xVariation = new Random().nextInt(40) - 20;
		int yVariation = 30;

		Vector2 dest = new Vector2(entities.getCenterX() + xVariation, entities.getTop() + yVariation + label.getTextBounds().height);
		SequenceAction moveAction = ActionFactory.getMoveToAction(dest, 0.5f);
		SequenceAction a = new SequenceAction(moveAction, new RemoveActorAction());
		label.addAction(a);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (getChildren().size == 0)
		{
			remove();
		}
	}

}
