package screen.start;

import ressources.DrawableSprite;
import ressources.R;
import ressources.Ressource;
import screen.MyGdxGame;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LoadingGameStage extends Stage
{
	public LoadingGameStage()
	{
		setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));
		Group group = new Group();
		addActor(group);

		DrawableSprite drawableSprite = new DrawableSprite(R.c().loading_loadingScreen);
		group.addActor(new Ressource(drawableSprite, 0, 0, MyGdxGame.VIRTUAL_HEIGHT));
	}

}