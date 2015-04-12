package game.panels;

import globals.PlayerStats;
import globals.Weapons;
import ressources.DrawableSprite;
import ressources.R;
import ressources.Ressource;
import screen.MyGdxGame;
import screen.ScreenManager;
import utilities.Methods;
import utilities.enumerations.GameStatesEnum;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.oasix.crazyshooter.GameStage;
import com.oasix.crazyshooter.Timer;

public class GameBreakMenu extends Group
{
	private float			panelWidth				= 380;
	private Button			m_buttonResume;
	private ButtonStyle		m_buttonStyleResume		= new ButtonStyle();
	private Button			m_buttonUpgrade;
	private ButtonStyle		m_buttonStyleUpgrade	= new ButtonStyle();
	private Button			m_buttonRetry;
	private ButtonStyle		m_buttonStyleRetry		= new ButtonStyle();
	private Button			m_buttonMenu;
	private ButtonStyle		m_buttonStyleMenu		= new ButtonStyle();
	private Button			m_buttonNext;
	private ButtonStyle		m_buttonStyleNext		= new ButtonStyle();

	private GameStatesEnum	breakType;

	public GameBreakMenu(GameStage gameStage, GameStatesEnum breakType)
	{
		this.breakType = breakType;
		initButton();
		initPanel(breakType);
	}

	private void initButton()
	{
		// Button prev
		TextureRegionDrawable resume = new TextureRegionDrawable(R.c().game_iconResume);
		m_buttonStyleResume.up = resume;

		TextureRegionDrawable upgrade = new TextureRegionDrawable(R.c().screens_iconGoToUpgradeScreen);
		m_buttonStyleUpgrade.up = upgrade;

		TextureRegionDrawable retry = new TextureRegionDrawable(R.c().game_iconRetry);
		m_buttonStyleRetry.up = retry;

		TextureRegionDrawable menu = new TextureRegionDrawable(R.c().screens_iconGoToMenuScreen);
		m_buttonStyleMenu.up = menu;

		TextureRegionDrawable next = new TextureRegionDrawable(R.c().game_iconGoToNextLevel);
		m_buttonStyleNext.up = next;

		m_buttonResume = new Button(m_buttonStyleResume);
		m_buttonResume.addListener(new Ecouteur());
		m_buttonResume.setSize(Methods.scaleByHeight(150, m_buttonResume.getWidth(), m_buttonResume.getHeight()), 150);

		m_buttonUpgrade = new Button(m_buttonStyleUpgrade);
		m_buttonUpgrade.addListener(new Ecouteur());
		m_buttonUpgrade.setSize(Methods.scaleByHeight(150, m_buttonUpgrade.getWidth(), m_buttonUpgrade.getHeight()), 150);

		m_buttonMenu = new Button(m_buttonStyleMenu);
		m_buttonMenu.addListener(new Ecouteur());
		m_buttonMenu.setSize(Methods.scaleByHeight(150, m_buttonMenu.getWidth(), m_buttonMenu.getHeight()), 150);

		m_buttonRetry = new Button(m_buttonStyleRetry);
		m_buttonRetry.addListener(new Ecouteur());
		m_buttonRetry.setSize(Methods.scaleByHeight(150, m_buttonRetry.getWidth(), m_buttonRetry.getHeight()), 150);

		m_buttonNext = new Button(m_buttonStyleNext);
		m_buttonNext.addListener(new Ecouteur());
		m_buttonNext.setSize(Methods.scaleByHeight(150, m_buttonNext.getWidth(), m_buttonNext.getHeight()), 150);
	}

	private void initPanel(GameStatesEnum breakType)
	{
		int yRetreat = MyGdxGame.VIRTUAL_HEIGHT; // Pour venir du haut

		// --------------------Panel centré sur x et sur y
		DrawableSprite drawableSprite = new DrawableSprite(R.c().gamePanel);
		Ressource panel = new Ressource(drawableSprite, 0, 0, panelWidth);

		float panelX = MyGdxGame.VIRTUAL_WIDTH / 2 - panel.getWidth() / 2;
		float panelY = MyGdxGame.VIRTUAL_HEIGHT / 2 - panel.getHeight() / 2;
		panel.setPosition(panelX, panelY + yRetreat);

		MoveToAction action = new MoveToAction();
		action.setDuration(0.6f);
		action.setPosition(panelX, panelY); // se place au centre
		action.setInterpolation(Interpolation.swingOut);
		panel.addAction(action);

		addActor(panel);

		// -------------------------------------------INFO
		int offsetXMessage = 60;
		int offsetYMessage = 90;
		DrawableSprite weaponsAvailable = null;

		if (isTalentAvailable())
		{
			weaponsAvailable = new DrawableSprite(R.c().gamePanel_talentAvailable);
		}

		if (isWeaponsAvailable())
		{
			weaponsAvailable = new DrawableSprite(R.c().gamePanel_weaponsAvailable);
		}

		if (weaponsAvailable != null)
		{
			Ressource weaponsAvailableRessource = new Ressource(weaponsAvailable, panelX + offsetXMessage, panelY + yRetreat, 75);
			weaponsAvailableRessource.setTimer(new Timer(0.2f, 0.1f));
			MoveToAction action6 = new MoveToAction();
			action6.setDuration(0.6f);
			action6.setPosition(panelX + offsetXMessage, panelY - offsetYMessage); // se place au centre
			action6.setInterpolation(Interpolation.swingOut);
			weaponsAvailableRessource.addAction(action6);
			addActor(weaponsAvailableRessource);
		}

		// -------------------Buttons
		int spacing = 70;
		float buttonY = 400;
		float buttonX = panelX + spacing;
		// Commun button
		addActor(m_buttonMenu);
		m_buttonMenu.setPosition(buttonX, buttonY + yRetreat);
		MoveToAction action2 = new MoveToAction();
		action2.setDuration(0.65f);
		action2.setPosition(m_buttonMenu.getX(), buttonY); // se place au centre
		action2.setInterpolation(Interpolation.swingOut);
		m_buttonMenu.addAction(action2);

		if (breakType == GameStatesEnum.GAME_WIN)
		{
			addActor(m_buttonNext);
			m_buttonNext.setPosition(m_buttonMenu.getRight() + spacing, m_buttonMenu.getY());
			MoveToAction action3 = new MoveToAction();
			action3.setDuration(0.70f);
			action3.setPosition(m_buttonNext.getX(), buttonY); // se place au centre
			action3.setInterpolation(Interpolation.swingOut);
			m_buttonNext.addAction(action3);

			addActor(m_buttonUpgrade);
			m_buttonUpgrade.setPosition(m_buttonNext.getRight() + spacing, m_buttonMenu.getY());
			MoveToAction action4 = new MoveToAction();
			action4.setDuration(0.75f);
			action4.setPosition(m_buttonUpgrade.getX(), buttonY); // se place au centre
			action4.setInterpolation(Interpolation.swingOut);
			m_buttonUpgrade.addAction(action4);

			float initialHeight = R.c().game_panelVictoryText.getRegionHeight();
			float initialWidth = R.c().game_panelVictoryText.getRegionWidth();
			float height = 100;
			float width = Methods.scaleByHeight(height, initialWidth, initialHeight);

			float x = panelX - width / 2 + panelWidth / 2 + panelX / 2;

			Ressource ressource = new Ressource(new DrawableSprite(R.c().game_panelVictoryText), x, 600 + yRetreat, height);
			addActor(ressource);
			MoveToAction action5 = new MoveToAction();
			action5.setDuration(0.75f);
			action5.setPosition(x, 600); // se place au centre
			action5.setInterpolation(Interpolation.swingOut);
			ressource.addAction(action5);

		}
		if (breakType == GameStatesEnum.GAME_PAUSE)
		{
			addActor(m_buttonResume);
			m_buttonResume.setPosition(m_buttonMenu.getRight() + spacing, m_buttonMenu.getY());
			MoveToAction action3 = new MoveToAction();
			action3.setDuration(0.70f);
			action3.setPosition(m_buttonResume.getX(), buttonY); // se place au centre
			action3.setInterpolation(Interpolation.swingOut);
			m_buttonResume.addAction(action3);

			addActor(m_buttonUpgrade);
			m_buttonUpgrade.setPosition(m_buttonResume.getRight() + spacing, m_buttonMenu.getY());
			MoveToAction action4 = new MoveToAction();
			action4.setDuration(0.75f);
			action4.setPosition(m_buttonUpgrade.getX(), buttonY); // se place au centre
			action4.setInterpolation(Interpolation.swingOut);
			m_buttonUpgrade.addAction(action4);

			float initialHeight = R.c().game_panelPauseText.getRegionHeight();
			float initialWidth = R.c().game_panelPauseText.getRegionWidth();
			float height = 100;
			float width = Methods.scaleByHeight(height, initialWidth, initialHeight);

			float x = panelX - width / 2 + panelWidth / 2 + panelX / 2;

			Ressource ressource = new Ressource(new DrawableSprite(R.c().game_panelPauseText), x, 600 + yRetreat, height);
			addActor(ressource);
			MoveToAction action5 = new MoveToAction();
			action5.setDuration(0.75f);
			action5.setPosition(x, 600); // se place au centre
			action5.setInterpolation(Interpolation.swingOut);
			ressource.addAction(action5);

		}
		if (breakType == GameStatesEnum.GAME_LOOSE)
		{
			addActor(m_buttonRetry);
			m_buttonRetry.setPosition(m_buttonMenu.getRight() + spacing, m_buttonMenu.getY());
			MoveToAction action3 = new MoveToAction();
			action3.setDuration(0.70f);
			action3.setPosition(m_buttonRetry.getX(), buttonY); // se place au centre
			action3.setInterpolation(Interpolation.swingOut);
			m_buttonRetry.addAction(action3);

			addActor(m_buttonUpgrade);
			m_buttonUpgrade.setPosition(m_buttonRetry.getRight() + spacing, m_buttonMenu.getY());
			MoveToAction action4 = new MoveToAction();
			action4.setDuration(0.75f);
			action4.setPosition(m_buttonUpgrade.getX(), buttonY); // se place au centre
			action4.setInterpolation(Interpolation.swingOut);
			m_buttonUpgrade.addAction(action4);

			float initialHeight = R.c().game_panelFailText.getRegionHeight();
			float initialWidth = R.c().game_panelFailText.getRegionWidth();
			float height = 100;
			float width = Methods.scaleByHeight(height, initialWidth, initialHeight);

			float x = panelX - width / 2 + panelWidth / 2 + panelX / 2;

			Ressource ressource = new Ressource(new DrawableSprite(R.c().game_panelFailText), x, 600 + yRetreat, height);
			addActor(ressource);
			MoveToAction action5 = new MoveToAction();
			action5.setDuration(0.75f);
			action5.setPosition(x, 600); // se place au centre
			action5.setInterpolation(Interpolation.swingOut);
			ressource.addAction(action5);
		}

	}

	class Ecouteur extends ClickListener
	{
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			if (event.getTarget() == m_buttonResume)
			{
				GameStage.gameState = GameStatesEnum.GAME_RUN;
				remove();
			}
			if (event.getTarget() == m_buttonMenu)
			{
				ScreenManager.getInstance().show(ScreenEnum.LEVEL);
				if (breakType != GameStatesEnum.GAME_PAUSE)
				{
					showAd();
				}
			}
			if (event.getTarget() == m_buttonRetry)
			{
				ScreenManager.getInstance().show(ScreenEnum.GAMELOADING);
				if (breakType != GameStatesEnum.GAME_PAUSE)
				{
					// showAd();
				}
			}
			if (event.getTarget() == m_buttonUpgrade)
			{
				ScreenManager.getInstance().show(ScreenEnum.UPGRADE);
				if (breakType != GameStatesEnum.GAME_PAUSE)
				{
					showAd();
				}
			}
			if (event.getTarget() == m_buttonNext)
			{
				ScreenManager.getInstance().show(ScreenEnum.GAMELOADING);
				showAd();
			}

			super.touchUp(event, x, y, pointer, button);
		}
	}

	private boolean isWeaponsAvailable()
	{

		boolean retour = false;

		for (Weapons weapon : Weapons.values())
		{
			if (!weapon.isBuy && weapon.cost <= PlayerStats.ressource)
			{
				retour = true;
			}
		}

		return retour;
	}

	private boolean isTalentAvailable()
	{
		return PlayerStats.getTalentPointsRemaining() > 0;
	}

	private void showAd()
	{
		MyGdxGame.showAd();
	}

}
