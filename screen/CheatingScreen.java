package screen;

import screen.cheat.CheatingStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CheatingScreen implements MovableScreen
{

	private CheatingStage	cheatingStage	= new CheatingStage();

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cheatingStage.act(delta);
		cheatingStage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		cheatingStage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		System.out.println("CheatingScreen Show");
		cheatingStage = new CheatingStage();
		// Set processeur
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(cheatingStage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void hide()
	{
		System.out.println("CheatingScreen Hidden");
	}

	@Override
	public void pause()
	{
		System.out.println("CheatingScreen Pause");
	}

	@Override
	public void resume()
	{
		System.out.println("CheatingScreen Resume");
	}

	@Override
	public void dispose()
	{
		System.out.println("CheatingScreen Dispose");
	}

	@Override
	public Stage getStage()
	{
		return cheatingStage;
	}

}
