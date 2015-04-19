package screen.level;

import ressources.R;
import utilities.ButtonScreen;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class LevelGroup extends Group
{
	private float			factor	= 2.66f;

	Image					image2;

	private ButtonScreen	buttonScreenUpgrade;
	private ButtonScreen	buttonScreenMenu;
	private ButtonScreen	buttonScreenEvaluate;
	private ButtonScreen	buttonScreenQuickPlay;

	public LevelGroup()
	{
		Image image = new Image(R.c().upgrade_background);
		image.setSize(image.getWidth() * factor, image.getHeight() * factor);
		// image.setPosition(174 * factor, (screenHeight - 145) * factor);
		addActor(image);

		image2 = new Image(R.c().world_header);
		image2.setSize(image2.getWidth() * factor, image2.getHeight() * factor);
		// image.setPosition(174 * factor, (screenHeight - 145) * factor);
		addActor(image2);

		Image image3 = new Image(R.c().world_blakc_rectangle);
		image3.setSize(image3.getWidth() * factor, image3.getHeight() * factor);
		// image.setPosition(174 * factor, (screenHeight - 145) * factor);
		addActor(image3);

		initScreenButtons();

		buttonScreenEvaluate.setPosition(400, 120);
		buttonScreenUpgrade.setPosition(1450, 120);
		buttonScreenMenu.setPosition(1800, 1010);
		buttonScreenQuickPlay.setPosition(1750, 140);
		image2.setPosition(500, 970);

		addActor(new LevelPathGroup());
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
		buttonScreenUpgrade = new ButtonScreen(this, ScreenEnum.UPGRADE);
		buttonScreenUpgrade.putStyle(R.c().screens_iconGoToUpgradeScreen);
		buttonScreenMenu = new ButtonScreen(this, ScreenEnum.WORLD);
		buttonScreenMenu.putStyle(R.c().screens_iconGoToMenuScreen);
		buttonScreenQuickPlay = new ButtonScreen(this, ScreenEnum.GAMELOADING);
		buttonScreenQuickPlay.putStyle(R.c().screens_iconGoToQuickGame);

		buttonScreenEvaluate.putSize();
		buttonScreenEvaluate.putEffect();
		buttonScreenMenu.putEffect();
		buttonScreenUpgrade.putSize();
		buttonScreenMenu.putSize();
		buttonScreenQuickPlay.putSize();
	}
}
