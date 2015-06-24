package game.panels;

import globals.Worlds;
import ressources.S;
import ressources.S.TyrianSound;
import screen.MyGdxGame;
import screen.ScreenManager;
import utilities.enumerations.GameStatesEnum;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.oasix.crazyshooter.GameStage;

public class GameMenu extends Stage
{
	private GameBreakMenu	gameBreakMenu	= null;
	private GameStage		gameStage;
	private boolean			shown			= false;

	public GameMenu(GameStage gameStage)
	{
		setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));
		this.gameStage = gameStage;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (!shown)
		{
			if (GameStage.gameState == GameStatesEnum.GAME_PAUSE)
			{
				shown = true;
				gameBreakMenu = new GameBreakMenu(gameStage, GameStatesEnum.GAME_PAUSE);

				addActor(gameBreakMenu);
			}
			if (GameStage.gameState == GameStatesEnum.GAME_WIN)
			{
				if (ScreenManager.getInstance().getLevelSelected().levelIndex <= Worlds.LAST_LEVEL)
				{
					shown = true;
					gameBreakMenu = new GameBreakMenu(gameStage, GameStatesEnum.GAME_WIN);
					addActor(gameBreakMenu);
					S.c().play(TyrianSound.soundEffect_player_win, null, null);
				} else
				{
					ScreenManager.getInstance().show(ScreenEnum.ENDING);
				}
			}
			if (GameStage.gameState == GameStatesEnum.GAME_LOOSE)
			{
				shown = true;
				gameBreakMenu = new GameBreakMenu(gameStage, GameStatesEnum.GAME_LOOSE);
				addActor(gameBreakMenu);
				S.c().play(TyrianSound.soundEffect_player_loose, null, null);
			}
		}
		if (getActors().size == 0)
		{
			shown = false;
		}

	}
}
