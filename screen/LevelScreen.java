package screen;

import screen.level.LevelStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LevelScreen implements MovableScreen
{
	private LevelStage levelStage = new LevelStage();

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		levelStage.act(delta);
		levelStage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		levelStage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		System.out.println("LevelScreen Show");
		levelStage = new LevelStage();
		// Set processeur
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(levelStage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void hide()
	{
		System.out.println("LevelScreen Hidden");
	}

	@Override
	public void pause()
	{
		System.out.println("LevelScreen Pause");
	}

	@Override
	public void resume()
	{
		System.out.println("LevelScreen Resume");
	}

	@Override
	public void dispose()
	{
		System.out.println("LevelScreen Dispose");
	}

	@Override
	public Stage getStage()
	{
		return levelStage;
	}

}
