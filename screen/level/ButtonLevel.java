package screen.level;

import globals.Worlds;
import ressources.R;
import screen.ScreenManager;
import utilities.ButtonScreen;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class ButtonLevel extends ButtonScreen
{
	private Label	levelLabel;

	public ButtonLevel(Group group, final LevelData levelData, final Levels allLevels)
	{
		super(group, new RunnableAction()
		{
			@Override
			public void run()
			{
				boolean availability;

				if (levelData.levelIndex > 0)
				{
					availability = allLevels.level[levelData.levelIndex - 1].levelComplete.equals("true");
				} else
				{
					availability = true;
				}

				if (availability || true)
				{
					// Stock le niveau dans le controlleur de screen
					ScreenManager.getInstance().setLevelSelected(levelData);
					ScreenManager.getInstance().show(ScreenEnum.GAMELOADING);
					Gdx.input.setInputProcessor(null); // empeche des actions durant le changement de screen
				}
			}
		});

		LabelStyle levelText = new LabelStyle(R.c().EarlyGameBoyFont_32, Color.valueOf("ece1c6"));
		levelLabel = new Label("" + levelData.levelIndex, levelText);
		addActor(levelLabel);

		putStyle(R.c().level_icon_new);

		// Level done
		if (levelData.levelComplete.equals("true"))
		{
			putStyle(R.c().level_icon_done);
		}

		// Level lock
		if (levelData.levelIndex > 0)
		{
			if (allLevels.level[levelData.levelIndex - 1].levelComplete.equals("false"))
			{
				putStyle(R.c().level_icon_lock);
			}
		}

		// Level boss
		for (Worlds world : Worlds.values())
		{
			if (levelData.levelIndex == world.endWorld)
			{
				putStyle(R.c().level_icon_boss);
			}
		}

		putSize(2.66f);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		levelLabel.setPosition(30, 35);
		levelLabel.setAlignment(Align.center);
		levelLabel.setWrap(true);
		levelLabel.setWidth(85);
		levelLabel.setDebug(false);

	}

}
