package screen;

import screen.start.LoadingGameStage;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LoadingGameScreen implements MovableScreen
{
	private LoadingGameStage loadingStage = new LoadingGameStage();
	private boolean done = false;

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		loadingStage.act(delta);
		loadingStage.draw();

		if (loadingStage.getRoot().getActions().size == 0 && !done)
		{
			done = true;
			System.out.println("Chargement du game en cours");

			ScreenManager.getInstance().show(ScreenEnum.GAME); // TODO ERREUR...car ça switch le premier puis lancer l'autre
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
		// Add Action

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
