package screen.worlds;

import globals.Worlds;

import java.text.DecimalFormat;

import ressources.R;
import screen.MyGdxGame;
import screen.ScreenManager;
import utilities.ButtonScreen;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class WorldsGroup extends Group
{
	private float			screenHeight	= 405;
	private float			factor			= 2.66f;
	private DecimalFormat	df				= new DecimalFormat("0.0");

	private ButtonScreen	buttonScreenUpgrade;
	private ButtonScreen	buttonScreenMenu;
	private ButtonScreen	buttonScreenExit;
	private ButtonScreen	buttonScreenEvaluate;
	private ButtonScreen	buttonScreenQuickPlay;

	private Label			currentProgressionValue;

	Image					image2;
	Image					image4;
	Image					image3;

	public WorldsGroup()
	{
		ScreenManager.getInstance().setLevelSelected(Worlds.getLastLevelUnlock());

		Image image = new Image(R.c().upgrade_background);
		image.setSize(image.getWidth() * factor, image.getHeight() * factor);
		// image.setPosition(174 * factor, (screenHeight - 145) * factor);
		addActor(image);

		image2 = new Image(R.c().world_header);
		image2.setSize(image2.getWidth() * factor, image2.getHeight() * factor);
		// image.setPosition(174 * factor, (screenHeight - 145) * factor);
		addActor(image2);

		image3 = new Image(R.c().world_black_rectangle);
		image3.setSize(image3.getWidth() * factor, image3.getHeight() * factor);
		// image.setPosition(174 * factor, (screenHeight - 145) * factor);
		addActor(image3);

		image4 = new Image(R.c().world_progression);
		image4.setSize(image4.getWidth() * factor, image4.getHeight() * factor);
		// image.setPosition(174 * factor, (screenHeight - 145) * factor);
		addActor(image4);

		currentProgressionValue = new Label("" + Worlds.getProgression(), new LabelStyle(R.c().EarlyGameBoyFont_32, Color.valueOf("fbd00f")));
		addActor(currentProgressionValue);

		initScreenButtons();
		initWorldButtons();

		buttonScreenEvaluate.setPosition(400, 120);
		buttonScreenExit.setPosition(150, 90);
		buttonScreenUpgrade.setPosition(1450, 120);
		buttonScreenMenu.setPosition(1800, 1010);
		buttonScreenQuickPlay.setPosition(1750, 140);
		image2.setPosition(500, 970);
		image4.setPosition(550, 55);
		currentProgressionValue.setPosition(1100, 77);
	}

	private void initScreenButtons()
	{
		buttonScreenEvaluate = new ButtonScreen(this, new RunnableAction()
		{
			@Override
			public void run()
			{
				Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.oasix.crazyshooter.android");
			}
		});
		buttonScreenEvaluate.putStyle(R.c().screens_iconGoToRating);
		buttonScreenExit = new ButtonScreen(this, new RunnableAction()
		{
			@Override
			public void run()
			{
				Gdx.app.exit();
			}
		});
		buttonScreenExit.putStyle(R.c().screens_iconGoToExit);
		buttonScreenUpgrade = new ButtonScreen(this, ScreenEnum.UPGRADE);
		buttonScreenUpgrade.putStyle(R.c().screens_iconGoToUpgradeScreen);
		buttonScreenMenu = new ButtonScreen(this, ScreenEnum.LOADING);
		buttonScreenMenu.putStyle(R.c().screens_iconGoToMenuScreen);
		buttonScreenQuickPlay = new ButtonScreen(this, ScreenEnum.GAMELOADING);
		buttonScreenQuickPlay.putStyle(R.c().screens_iconGoToQuickGame);

		buttonScreenEvaluate.putSize();
		buttonScreenEvaluate.putEffect();
		buttonScreenExit.putSize();
		buttonScreenMenu.putEffect();
		buttonScreenUpgrade.putSize();
		buttonScreenMenu.putSize();
		buttonScreenQuickPlay.putSize();
	}

	private void initWorldButtons()
	{
		int k = 0;
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				ButtonWorld b = new ButtonWorld(this, Worlds.values()[k]);
				b.putUpDrawable(R.c().world_frame_uncompleted);
				b.putSize(factor);
				k++;
				b.setPosition(30 + j * 630, 580 - i * 380);
			}
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		image3.setSize(MyGdxGame.VIRTUAL_WIDTH, 65 * factor);
	}

}
