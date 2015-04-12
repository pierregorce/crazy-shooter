package screen.level;

import screen.MyGdxGame;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LevelStage extends Stage
{
	private LevelGroup levelGroup;

	public LevelStage()
	{
		setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));
		levelGroup = new LevelGroup();
		addActor(levelGroup);
	}
}
