package screen;

import ressources.R;
import ressources.S;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LoadingGameScreen implements MovableScreen
{
	private Stage	loadingStage	= new Stage();
	private boolean	done			= false;

	private Group	group			= new Group();
	private Label	loadingLabel;
	private Image	backgroundImage;

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		loadingStage.act(delta);
		loadingStage.draw();

		loadingLabel.setPosition(500, 500);
		loadingLabel.setText("Loading : " + S.c().soundManager.getProgress() + "");
		// S.c().soundManager.finishLoading();

		if (loadingStage.getRoot().getActions().size == 0 && !done)
		{
			System.out.println("Chargement du game en cours");
			if (S.c().soundManager.update())
			{
				S.c().generateSound();
				ScreenManager.getInstance().show(ScreenEnum.GAME);
				done = true;
			}
		}

	}

	@Override
	public void resize(int width, int height)
	{
		loadingStage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		System.out.println("LoadingGameScreen Show");
		done = false;
		// Set processeur
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(loadingStage);
		Gdx.input.setInputProcessor(multiplexer);

		loadingStage.addActor(group);
		loadingStage.setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));

		float factor = 2.66f;
		backgroundImage = new Image(R.c().upgrade_background);
		backgroundImage.setSize(backgroundImage.getWidth() * factor, backgroundImage.getHeight() * factor);
		group.addActor(backgroundImage);

		BitmapFont b = R.c().EarlyGameBoyFont_32;
		loadingLabel = new Label("", new LabelStyle(b, Color.WHITE));
		group.addActor(loadingLabel);

	}

	@Override
	public void hide()
	{
		System.out.println("LoadingGameScreen Hidden");
		// Lancement d'une action

	}

	@Override
	public void pause()
	{
		System.out.println("LoadingGameScreen Pause");
	}

	@Override
	public void resume()
	{
		System.out.println("LoadingGameScreen Resume");
	}

	@Override
	public void dispose()
	{
		System.out.println("LoadingGameScreen Dispose");
	}

	@Override
	public Stage getStage()
	{
		return loadingStage;
	}

}
