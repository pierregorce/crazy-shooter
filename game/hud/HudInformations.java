package game.hud;

import globals.PlayerStats;
import ressources.R;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oasix.crazyshooter.GameStage;

public class HudInformations extends Actor
{

	private BitmapFont	font;
	private GameStage	gameStage;

	private String		xp		= "XP";
	private String		life	= "LIFE";
	private String		level	= "LEVEL";
	private String		fps		= "FPS";

	public HudInformations(GameStage gameStage)
	{
		this.gameStage = gameStage;
		font = R.c().fontTypeBasicThin;

	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		// font.draw(batch, life, 180, MyGdxGame.TRUE_HEIGHT);
		// font.draw(batch, xp, 100, 880);
		// font.draw(batch, level, 20, MyGdxGame.TRUE_HEIGHT - 125);
		font.draw(batch, fps, 20, 300);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		life = "" + gameStage.getPlayerLife() + "/" + PlayerStats.getMaxLife();
		xp = "XP: " + PlayerStats.currentXP + "/" + PlayerStats.getRequiertXP();
		level = "LEVEL: " + PlayerStats.level;
		fps = "FPS :" + Gdx.graphics.getFramesPerSecond();
	}
}
