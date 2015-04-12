package screen;

import screen.ending.EndingGameStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class EndingScreen implements MovableScreen
{

	private EndingGameStage endingGameStage = new EndingGameStage();

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		endingGameStage.act(delta);
		endingGameStage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		endingGameStage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		System.out.println("EndingScreen Show");
		endingGameStage = new EndingGameStage();
		// Set processeur
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(endingGameStage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void hide()
	{
		System.out.println("EndingScreen Hidden");
	}

	@Override
	public void pause()
	{
		System.out.println("EndingScreen Pause");
	}

	@Override
	public void resume()
	{
		System.out.println("EndingScreen Resume");
	}

	@Override
	public void dispose()
	{
		System.out.println("EndingScreen Dispose");
	}

	@Override
	public Stage getStage()
	{
		return endingGameStage;
	}

}
