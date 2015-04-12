package screen;

import screen.settings.SettingsStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class SettingsScreen implements MovableScreen
{

	private SettingsStage	settingsScreen	= new SettingsStage();

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		settingsScreen.act(delta);
		settingsScreen.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		settingsScreen.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		System.out.println("EndingScreen Show");
		settingsScreen = new SettingsStage();
		// Set processeur
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(settingsScreen);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void hide()
	{
		System.out.println("SettingsStage Hidden");
	}

	@Override
	public void pause()
	{
		System.out.println("SettingsStage Pause");
	}

	@Override
	public void resume()
	{
		System.out.println("SettingsStage Resume");
	}

	@Override
	public void dispose()
	{
		System.out.println("SettingsStage Dispose");
	}

	@Override
	public Stage getStage()
	{
		return settingsScreen;
	}

}
