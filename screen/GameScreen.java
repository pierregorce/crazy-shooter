package screen;

import game.hud.Hud;
import game.panels.GameMenu;
import utilities.enumerations.GameStatesEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.oasix.crazyshooter.GameStage;

public class GameScreen implements MovableScreen
{

	// Ensemble des stages formant le gameScreen
	private GameStage	gameStage;
	private Hud			hud;
	private GameMenu	gameMenu;

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (GameStage.gameState == GameStatesEnum.GAME_RUN)
		{
			gameStage.act(delta);
			hud.act(delta);
		}

		gameStage.draw();
		hud.draw();
		gameMenu.act(delta);
		gameMenu.draw();

		// try
		// {
		// Thread.sleep(40);
		// } catch (InterruptedException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	@Override
	public void resize(int width, int height)
	{
		gameStage.getViewport().update(width, height, true);
		hud.getViewport().update(width, height, true);
		gameMenu.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		System.out.println("GameScreen Show");
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		// Set processeur
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(hud);
		multiplexer.addProcessor(gameMenu);
		Gdx.input.setInputProcessor(multiplexer);

	}

	public void initialize()
	{
		System.out.println("Initilisation des objets du gameScreen");
		// Generation des stages
		gameStage = new GameStage();
		hud = new Hud(gameStage);
		gameMenu = new GameMenu(gameStage);
	}

	@Override
	public void hide()
	{
		System.out.println("GameScreen Hidden");
		// GameStage.gameState = GameStatesEnum.GAME_RUN;
	}

	@Override
	public void pause()
	{
		System.out.println("GameScreen Paused");
	}

	@Override
	public void resume()
	{
		System.out.println("GameScreen Resume");
	}

	@Override
	public void dispose()
	{
		System.out.println("GameScreen Dispose");
	}

	@Override
	public Stage getStage()
	{
		return gameMenu;
	}

}
