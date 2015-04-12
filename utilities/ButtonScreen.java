package utilities;

import screen.ScreenManager;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonScreen extends Button
{
	private float	height	= 100;

	public ButtonScreen(Group group, ScreenEnum screen)
	{
		group.addActor(this);
		addListener(new ButtonScreenAction(screen));
	}

	public void putSize()
	{
		setSize(Methods.scaleByHeight(height, getWidth(), getHeight()), height);
	}

	public void putStyle(TextureRegion up, TextureRegion down)
	{
		ButtonStyle b = new ButtonStyle();
		b.up = new TextureRegionDrawable(up);
		b.down = new TextureRegionDrawable(down);
		setStyle(b);
		setSize(getPrefWidth(), getPrefHeight());
		putSize();
	}

	public void putStyle(TextureRegion up)
	{
		ButtonStyle b = new ButtonStyle();
		b.up = new TextureRegionDrawable(up);
		setStyle(b);
		setSize(getPrefWidth(), getPrefHeight());
		putSize();
	}

	public void putEffect()
	{
		setOrigin(getWidth() / 2, getHeight() / 2);
		Action action1 = Actions.sizeBy(20, 20, 0.3f);
		Action action2 = Actions.delay(0.07f);
		Action action3 = Actions.sizeBy(-20, -20, 0.3f);
		Action action4 = Actions.delay(0.07f);

		SequenceAction s = new SequenceAction(action1, action2, action3, action4);
		RepeatAction r = new RepeatAction();
		r.setAction(s);
		r.setCount(RepeatAction.FOREVER);

		addAction(r);
	}

	@Override
	public void setPosition(float x, float y)
	{
		// super.setPosition(x, y);
		super.setPosition(x - getWidth() / 2, y - getWidth() / 2);
	}
}

class ButtonScreenAction extends ClickListener
{
	private ScreenEnum	screen;

	public ButtonScreenAction(ScreenEnum screen)
	{
		super();
		this.screen = screen;
	}

	@Override
	public void clicked(InputEvent event, float x, float y)
	{
		super.clicked(event, x, y);
		ScreenManager.getInstance().show(screen);
	}
}
