package game.hud;

import globals.PlayerStats;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import ressources.DrawableAnimation;
import ressources.R;
import ressources.Ressource;
import utilities.Methods;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.oasix.crazyshooter.GameStage;

public class HudWave extends Group
{

	private GameStage		gameStage;

	int						yTop	= 1000;
	int						x		= 1250;
	int						x2		= x + 220;

	private DecimalFormat	decimalFormat;
	private BitmapFont		m_bitmapFont;

	Ressource				ressource;
	Ressource				ressource2;
	Label					label;
	Label					label2;
	Label					label3;

	public HudWave(GameStage gameStage)
	{
		this.gameStage = gameStage;

		// ADD BOUNDS
		decimalFormat = new DecimalFormat("0.0");
		decimalFormat.setRoundingMode(RoundingMode.DOWN);

		// Coin
		DrawableAnimation drawableAnimation = new DrawableAnimation(0.1f, R.c().iconCoin);
		ressource = new Ressource(drawableAnimation, x, yTop, 40);

		// Wave
		DrawableAnimation drawableAnimation2 = new DrawableAnimation(0.1f, R.c().hudWaveIcon);
		ressource2 = new Ressource(drawableAnimation2, x2, yTop, 40);
		addActor(ressource2);

		m_bitmapFont = R.c().fontTypeBasicBlod;
		m_bitmapFont.setScale(6);

		label = new Label("", new LabelStyle(R.c().EarlyGameBoyFont_32, Color.WHITE));
		label2 = new Label("", new LabelStyle(R.c().EarlyGameBoyFont_32, Color.WHITE));
		label3 = new Label("", new LabelStyle(R.c().getWendyFont(30), Color.WHITE));
		addActor(label);
		addActor(label2);
		addActor(label3);
		addActor(ressource);

		enemiesTotal = gameStage.getGlobalController().getTotalEnemiesCount();
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		float size = 650;
		ressource2.setSize(size, Methods.scaleByWidth(size, R.c().hudWaveIcon.getRegionWidth(), R.c().hudWaveIcon.getRegionHeight()));
	}

	int	enemiesTotal;
	int	enemiesCurrent;

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		float waveDuration = gameStage.getGlobalController().getRemainingTime();
		int waveRemaining = gameStage.getGlobalController().getWaveRemaining();

		enemiesCurrent = gameStage.getGlobalController().getNombreRestantEnnemies();

		label.setPosition(1226, 1004);
		label2.setPosition(1520, 1005);
		label3.setPosition(1666, 968);
		label.setText("" + PlayerStats.ressource);
		label2.setText("" + enemiesCurrent + "~" + enemiesTotal);
		label3.setText("" + decimalFormat.format(waveDuration));
		ressource2.setPosition(1150, 950);
		ressource.setPosition(1180, 985);
		// m_bitmapFont.draw(batch, "" + PlayerStats.ressource, x + 50, yTop + 116);
		// m_bitmapFont.draw(batch, "" + decimalFormat.format(waveDuration) + " - " + waveRemaining + "/" + gameStage.getGlobalController().getWaveCount(), x2 + 70, yTop
		// + 116);
	}
}
