package game.hud;

import screen.MyGdxGame;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.oasix.crazyshooter.GameStage;

public class Hud extends Stage
{

	private GameStage gameStage;
	private HudInformations hudDebuger;
	private HudInterface hudInterface;
	private BossBar bossBar;

	public Hud(final GameStage gameStage)
	{
		setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));
		this.gameStage = gameStage;

		hudInterface = new HudInterface(gameStage);
		addActor(hudInterface);

		hudDebuger = new HudInformations(gameStage);
		addActor(hudDebuger);

		HudBars bar = new HudBars(gameStage);
		addActor(bar);

		HudWave hudWave = new HudWave(gameStage);
		addActor(hudWave);

		bossBar = new BossBar();
		addActor(bossBar);

	}

	private void showMessage()
	{
		for (int i = 0; i < GameStage.hudMessages.size(); i++)
		{
			if (GameStage.hudMessages.get(i).getChildren().size > 0)
			{
				// Il y a des actors qui se drawent donc ajout comme actor puis return, en effet, il 'agit de d'assets avec une action removeActor
				addActor(GameStage.hudMessages.get(i));
				return;
			}
		}

	}

	boolean leftDown = false;
	boolean rightDown = false;

	@Override
	public boolean keyDown(int keyCode)
	{
		// System.out.println(keyCode);

		if (keyCode == 22)
		{
			gameStage.moveRight();
			gameStage.walk(true);
			rightDown = true;
		}
		if (keyCode == 21)
		{
			gameStage.moveLeft();
			gameStage.walk(true);
			leftDown = true;
		}
		if (keyCode == 19)
		{
			gameStage.jump();
		}

		if (keyCode == 62)
		{
			gameStage.shoot(true);
		}

		return super.keyDown(keyCode);
	}

	@Override
	public boolean keyUp(int keyCode)
	{

		if (keyCode == 22)
		{
			rightDown = false;
		}

		if (keyCode == 21)
		{
			leftDown = false;
		}

		if (!leftDown && !rightDown)
		{
			gameStage.walk(false);
		}

		if (keyCode == 62)
		{
			gameStage.shoot(false);
		}

		return super.keyUp(keyCode);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		showMessage();
	}
}
