package screen;

import screen.worlds.WorldsGroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class WorldScreen implements MovableScreen
{

	private Stage	stage	= new Stage();

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		System.out.println("WorldScreen Show");
		stage = new Stage();
		stage.setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));
		stage.addActor(new WorldsGroup());
		// Set processeur
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void hide()
	{
		System.out.println("WorldScreen Hidden");
	}

	@Override
	public void pause()
	{
		System.out.println("WorldScreen Pause");
	}

	@Override
	public void resume()
	{
		System.out.println("WorldScreen Resume");
	}

	@Override
	public void dispose()
	{
		System.out.println("WorldScreen Dispose");
	}

	@Override
	public Stage getStage()
	{
		return stage;
	}

}
