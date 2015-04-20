package screen;

import game.sound.MusicManager;
import ressources.R;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Game;
import com.oasix.crazyshooter.AdsController;

public class MyGdxGame extends Game
{

	public static final int		VIRTUAL_WIDTH			= 1920;
	public static final int		VIRTUAL_WORLD_WIDTH		= 1920 * 2 + 200;
	public static final int		VIRTUAL_HEIGHT			= 1080;
	public static final int		VIRTUAL_WORLD_HEIGHT	= 1080 * 2;

	public static final int		PIXEL_SIZE				= 4;
	public static final String	TITLE					= "Crazy Shooter";

	public static AdsController	adsController;

	public MyGdxGame(AdsController adsController)
	{
		MyGdxGame.adsController = adsController;
	}

	@Override
	public void create()
	{
		System.out.println("GameCreated");
		ScreenManager.getInstance().initialize(this); // Ne sert qu'ici
		// Ajout d'un stage avec image + texte
		ScreenManager.getInstance().show(ScreenEnum.LOADING);
	}

	@Override
	public void dispose()
	{
		super.dispose();
		// test
		R.c().dispose();
		R.c().deleteInstance();
		ScreenManager.getInstance().dispose();
		// adsController = null;
		// MusicManager.stop();
		System.out.println("MyGame Dispose");
	}

	@Override
	public void pause()
	{
		System.out.println("MyGame Pause");
		super.pause();
	}

	@Override
	public void resume()
	{
		System.out.println("MyGame Resume");

		// RessoucesController.getInstance().finishLoading();
		// RessoucesController.getInstance().generateTextures();
		// RessoucesController.getInstance().generateFonts();
		// RessoucesController.getInstance().generateSound();

		super.resume();
	}

	@Override
	public void render()
	{

		if (MusicManager.currentMusic != null && MusicManager.currentMusic.isPlaying())
		{
		} else
		{
			MusicManager.replay();
			System.out.println("musique replay");
		}

		// loadAd();
		super.render();
	}

	public static void loadAd()
	{
		if (adsController != null && adsController.isWifiConnected())
		{
			// boolean loaded = adsController.loadInterstitial();
			// System.out.println("Ad loaded ? : " + loaded);

			/*
			 * if (adsController.isWifiConnected()) { // System.out.println("Envoie d'une demande de load de l'ad"); }
			 */
		}
	}

	public static void showAd()
	{

		if (adsController != null && adsController.isWifiConnected())
		{
			// adsController.showInterstitial();
			System.out.println("Ad show");

			if (adsController.isWifiConnected())
			{
				// System.out.println("Envoie d'une demande de load de l'ad");
			}
		}

	}
}
