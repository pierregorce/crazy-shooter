package game.hud;

import globals.PlayerStats;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import ressources.DrawableAnimation;
import ressources.R;
import ressources.Ressource;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.oasix.crazyshooter.GameStage;

public class HudWave extends Group
{

	private GameStage gameStage;

	int yTop = 1000;
	int x = 1250;
	int x2 = x + 220;

	private DecimalFormat decimalFormat;
	private BitmapFont m_bitmapFont;

	public HudWave(GameStage gameStage)
	{
		this.gameStage = gameStage;

		// ADD BOUNDS
		decimalFormat = new DecimalFormat("0.0");
		decimalFormat.setRoundingMode(RoundingMode.DOWN);

		// Coin
		DrawableAnimation drawableAnimation = new DrawableAnimation(0.1f, R.c().iconCoin);
		Ressource ressource = new Ressource(drawableAnimation, x, yTop, 40);
		addActor(ressource);

		// Wave
		DrawableAnimation drawableAnimation2 = new DrawableAnimation(0.1f, R.c().hudWaveIcon);
		Ressource ressource2 = new Ressource(drawableAnimation2, x2, yTop, 40);
		addActor(ressource2);

		m_bitmapFont = R.c().fontTypeBasicBlod;
		m_bitmapFont.setScale(6);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		float waveDuration = gameStage.getGlobalController().getRemainingTime();
		int waveRemaining = gameStage.getGlobalController().getWaveRemaining();
		m_bitmapFont.draw(batch, "" + PlayerStats.ressource, x + 50, yTop + 116);
		m_bitmapFont.draw(batch, "" + decimalFormat.format(waveDuration) + " - " + waveRemaining + "/" + gameStage.getGlobalController().getWaveCount(), x2 + 70,
				yTop + 116);
	}

}
