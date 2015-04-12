package screen;

import screen.upgrade.UpgradeStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class UpgradeScreen implements MovableScreen
{

	private UpgradeStage upgradeStage = new UpgradeStage();

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		upgradeStage.act(delta);
		upgradeStage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		upgradeStage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		System.out.println("UpgradeStage Show");
		upgradeStage = new UpgradeStage();
		// Set processeur
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(upgradeStage);
		Gdx.input.setInputProcessor(multiplexer);

	}

	@Override
	public void hide()
	{
		System.out.println("UpgradeStage Hidden");
	}

	@Override
	public void pause()
	{
		System.out.println("UpgradeStage Pause");
	}

	@Override
	public void resume()
	{
		System.out.println("UpgradeStage Resume");
	}

	@Override
	public void dispose()
	{
		System.out.println("UpgradeStage Dispose");
	}

	@Override
	public Stage getStage()
	{
		return upgradeStage;
	}

}
