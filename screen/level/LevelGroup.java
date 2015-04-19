package screen.level;

import files.Files;
import globals.Worlds;
import ressources.R;
import screen.MyGdxGame;
import screen.ScreenManager;
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

	private Image			blackSquareBottom;
	private Image			blackSquareMiddle;

	private Worlds			world	= Worlds.getWorldByLevel(ScreenManager.getInstance().getLevelSelected().levelIndex);

	public LevelGroup()
	{
		Image image = new Image(R.c().upgrade_background);
		image.setSize(image.getWidth() * factor, image.getHeight() * factor);
		addActor(image);

		image2 = new Image(R.c().level_header);
		image2.setSize(image2.getWidth() * factor, image2.getHeight() * factor);
		addActor(image2);

		blackSquareBottom = new Image(R.c().world_black_rectangle);
		blackSquareBottom.setSize(MyGdxGame.VIRTUAL_WIDTH, 65 * factor);
		addActor(blackSquareBottom);

		blackSquareMiddle = new Image(R.c().world_black_rectangle);
		blackSquareMiddle.setSize(600 * factor, 280 * factor);
		addActor(blackSquareMiddle);

		initScreenButtons();
		initLevels();

		buttonScreenEvaluate.setPosition(400, 120);
		buttonScreenUpgrade.setPosition(1450, 120);
		buttonScreenMenu.setPosition(1800, 1010);
		image2.setPosition(500, 970);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		blackSquareBottom.setPosition(0, 0);
		blackSquareMiddle.setPosition(170, 200);
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

		buttonScreenEvaluate.putSize();
		buttonScreenEvaluate.putEffect();
		buttonScreenMenu.putEffect();
		buttonScreenUpgrade.putSize();
		buttonScreenMenu.putSize();
	}

	private void initLevels()
	{
		Levels levels = Files.levelDataRead();

		int nombreColonne = 5;
		int nombreLigne = 3;
		int topX = 600;
		int topY = 750;

		int k = world.startWorld;
		for (int i = 0; i < nombreLigne; i++)
		{
			for (int j = 0; j < nombreColonne; j++)
			{
				if (k <= world.endWorld)
				{
					ButtonLevel buttonLevel = new ButtonLevel(this, levels.level[k], levels);
					buttonLevel.setPosition(topX + j * 180, topY - i * 190);
					addActor(buttonLevel);
				}
				k++;
			}
		}

	}
}
