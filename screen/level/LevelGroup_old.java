package screen.level;

import java.io.BufferedReader;
import java.io.IOException;

import ressources.DrawableSprite;
import ressources.R;
import ressources.Ressource;
import ressources.Ressource.Dimension;
import ressources.S;
import screen.MyGdxGame;
import screen.ScreenManager;
import utilities.Methods;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import files.Files;
import game.sound.MusicManager;

public class LevelGroup_old extends Group
{

	// ----------------------------------------Level Path
	private final int			TAILLE_LIGNE					= 5;
	private final int			NOMBRE_DE_PAGE					= 5;
	private int					m_page							= 0;													// Page courrante des levels
	private int[]				m_levelsPage					= { 7, 13, 18, 23 };
	private final int			NOMBRE_LIGNES					= TAILLE_LIGNE * NOMBRE_DE_PAGE + NOMBRE_DE_PAGE - 1;

	private String[][]			levelPath;																				// Objet contenant le path
	private Levels				m_levels;																				// Objet contenant un tableau des levels

	private ButtonStyle			m_buttonStyleGoNext				= new ButtonStyle();
	private ButtonStyle			m_buttonStyleGoPrev				= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelLock_1		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelNew_1			= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelDone_1		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelBoss_1		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelLock_2		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelNew_2			= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelDone_2		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelBoss_2		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelLock_3		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelNew_3			= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelDone_3		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelBoss_3		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelLock_4		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelNew_4			= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelDone_4		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelBoss_4		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleHorizontalLink		= new ButtonStyle();
	private ButtonStyle			m_buttonStyleLevelVerticalLink	= new ButtonStyle();

	private final char			ICON_PREV						= 'A';
	private final char			ICON_NEXT						= 'B';
	private final char			ICON_LINK_HORIZONTAL			= 'C';
	private final char			ICON_LINK_VERTICAL				= 'D';
	private final int			ICON_SIZE						= 100;
	private final Vector2		ICON_ANCHOR						= new Vector2(350, 770);

	public static final int[]	m_boss_level_from_0				= { 9, 17, 24, 31, 999 };								// Commence à 0. Endroid unique ou ecrire les
																														// niveau des boss (avec le level.xml et le //
																														// level-config.json).
	public static final int[]	m_boss_level_from_1				= { 10, 18, 25, 32, 999 };								// Commence à 1 - idem que le fichier level.xml
	public static final int		m_last_level_from_1				= 32;

	private Group				m_groupPath						= new Group();
	private ButtonGroup			m_buttonGroup;

	// ---------------------------------------Hud

	private Label				m_labelFontA;
	private Label				m_labelFontB;
	private final Vector2		FONT_ANCHOR						= new Vector2(150, 1090);
	private Button				m_startButton;
	private Button				m_upgradeButton;
	private ButtonStyle			m_buttonStyleStart				= new ButtonStyle();
	private ButtonStyle			m_buttonStyleUpgrade			= new ButtonStyle();
	private Vector2				LEVEL_PANEL_ANCHOR				= new Vector2(100, 900);

	// Rate my app
	private Button				m_rateButton;
	private Button				m_exitButton;
	// Exit app
	private ButtonStyle			m_buttonStyleRate				= new ButtonStyle();
	private ButtonStyle			m_buttonStyleExit				= new ButtonStyle();

	public LevelGroup_old()
	{
		// R.c().getEarlyGameBoyFont(40);
		// R.c().getEarlyGameBoyFont(40);
		// R.c().getEarlyGameBoyFont(40);
		// R.c().getEarlyGameBoyFont(40);
		DrawableSprite drawableSprite = new DrawableSprite(R.c().level_background);
		addActor(new Ressource(drawableSprite, 0, 0, MyGdxGame.VIRTUAL_HEIGHT));
		addActor(m_groupPath);
		initButtonStyle();// Mise en place des buttonStyle pour buttons
		loadLevelPath(); // Recupère le path des niveaux
		m_levels = Files.levelDataRead();
		setPage();
		initPath(); // Initialise les éléments composant le path
		initHud(); // Mise en place du hud
	}

	private void setPage()
	{
		int level = 0;

		// Calcul le premier niveau non terminé
		for (LevelData levelData : m_levels.level)
		{
			level++;
			if (levelData.levelComplete.equals("false"))
			{
				break;
			}
		}
		System.out.println("Le level a faire est le : " + level);

		// Cacul de la page en cours
		int page = 0;

		for (int i = 0; i < m_levelsPage.length; i++)
		{
			if (level > m_levelsPage[i] + 1)
			{
				page++;
			}
		}

		System.out.println("La page est donc la page : " + m_page);
		m_page = page;
	}

	private void initButtonStyle()
	{
		// Button prev
		TextureRegionDrawable prev = new TextureRegionDrawable(R.c().level_iconPathPrev);
		m_buttonStyleGoPrev.up = prev;

		// Button next
		TextureRegionDrawable next = new TextureRegionDrawable(R.c().level_iconPathNext);
		m_buttonStyleGoNext.up = next;

		TextureRegionDrawable lock_1 = new TextureRegionDrawable(R.c().level_iconPathLock_1);
		m_buttonStyleLevelLock_1.up = lock_1;
		TextureRegionDrawable lock_s_1 = new TextureRegionDrawable(R.c().level_iconPathLockSelected_1);
		m_buttonStyleLevelLock_1.checked = lock_s_1;
		TextureRegionDrawable new_1 = new TextureRegionDrawable(R.c().level_iconPathNew_1);
		m_buttonStyleLevelNew_1.up = new_1;
		TextureRegionDrawable new_s_1 = new TextureRegionDrawable(R.c().level_iconPathNewSelected_1);
		m_buttonStyleLevelNew_1.checked = new_s_1;
		TextureRegionDrawable done_1 = new TextureRegionDrawable(R.c().level_iconPathDone_1);
		m_buttonStyleLevelDone_1.up = done_1;
		TextureRegionDrawable done_s_1 = new TextureRegionDrawable(R.c().level_iconPathDoneSelected_1);
		m_buttonStyleLevelDone_1.checked = done_s_1;
		TextureRegionDrawable boss_1 = new TextureRegionDrawable(R.c().level_iconPathBoss_1);
		m_buttonStyleLevelBoss_1.up = boss_1;
		TextureRegionDrawable boss_s_1 = new TextureRegionDrawable(R.c().level_iconPathBossSelected_1);
		m_buttonStyleLevelBoss_1.checked = boss_s_1;

		TextureRegionDrawable lock_2 = new TextureRegionDrawable(R.c().level_iconPathLock_2);
		m_buttonStyleLevelLock_2.up = lock_2;
		TextureRegionDrawable lock_s_2 = new TextureRegionDrawable(R.c().level_iconPathLockSelected_2);
		m_buttonStyleLevelLock_2.checked = lock_s_2;
		TextureRegionDrawable new_2 = new TextureRegionDrawable(R.c().level_iconPathNew_2);
		m_buttonStyleLevelNew_2.up = new_2;
		TextureRegionDrawable new_s_2 = new TextureRegionDrawable(R.c().level_iconPathNewSelected_2);
		m_buttonStyleLevelNew_2.checked = new_s_2;
		TextureRegionDrawable done_2 = new TextureRegionDrawable(R.c().level_iconPathDone_2);
		m_buttonStyleLevelDone_2.up = done_2;
		TextureRegionDrawable done_s_2 = new TextureRegionDrawable(R.c().level_iconPathDoneSelected_2);
		m_buttonStyleLevelDone_2.checked = done_s_2;
		TextureRegionDrawable boss_2 = new TextureRegionDrawable(R.c().level_iconPathBoss_2);
		m_buttonStyleLevelBoss_2.up = boss_2;
		TextureRegionDrawable boss_s_2 = new TextureRegionDrawable(R.c().level_iconPathBossSelected_2);
		m_buttonStyleLevelBoss_2.checked = boss_s_2;

		TextureRegionDrawable lock_3 = new TextureRegionDrawable(R.c().level_iconPathLock_3);
		m_buttonStyleLevelLock_3.up = lock_3;
		TextureRegionDrawable lock_s_3 = new TextureRegionDrawable(R.c().level_iconPathLockSelected_3);
		m_buttonStyleLevelLock_3.checked = lock_s_3;
		TextureRegionDrawable new_3 = new TextureRegionDrawable(R.c().level_iconPathNew_3);
		m_buttonStyleLevelNew_3.up = new_3;
		TextureRegionDrawable new_s_3 = new TextureRegionDrawable(R.c().level_iconPathNewSelected_3);
		m_buttonStyleLevelNew_3.checked = new_s_3;
		TextureRegionDrawable done_3 = new TextureRegionDrawable(R.c().level_iconPathDone_3);
		m_buttonStyleLevelDone_3.up = done_3;
		TextureRegionDrawable done_s_3 = new TextureRegionDrawable(R.c().level_iconPathDoneSelected_3);
		m_buttonStyleLevelDone_3.checked = done_s_3;
		TextureRegionDrawable boss_3 = new TextureRegionDrawable(R.c().level_iconPathBoss_3);
		m_buttonStyleLevelBoss_3.up = boss_3;
		TextureRegionDrawable boss_s_3 = new TextureRegionDrawable(R.c().level_iconPathBossSelected_3);
		m_buttonStyleLevelBoss_3.checked = boss_s_3;

		TextureRegionDrawable lock_4 = new TextureRegionDrawable(R.c().level_iconPathLock_4);
		m_buttonStyleLevelLock_4.up = lock_4;
		TextureRegionDrawable lock_s_4 = new TextureRegionDrawable(R.c().level_iconPathLockSelected_4);
		m_buttonStyleLevelLock_4.checked = lock_s_4;
		TextureRegionDrawable new_4 = new TextureRegionDrawable(R.c().level_iconPathNew_4);
		m_buttonStyleLevelNew_4.up = new_4;
		TextureRegionDrawable new_s_4 = new TextureRegionDrawable(R.c().level_iconPathNewSelected_4);
		m_buttonStyleLevelNew_4.checked = new_s_4;
		TextureRegionDrawable done_4 = new TextureRegionDrawable(R.c().level_iconPathDone_4);
		m_buttonStyleLevelDone_4.up = done_4;
		TextureRegionDrawable done_s_4 = new TextureRegionDrawable(R.c().level_iconPathDoneSelected_4);
		m_buttonStyleLevelDone_4.checked = done_s_4;
		TextureRegionDrawable boss_4 = new TextureRegionDrawable(R.c().level_iconPathBoss_4);
		m_buttonStyleLevelBoss_4.up = boss_4;
		TextureRegionDrawable boss_s_4 = new TextureRegionDrawable(R.c().level_iconPathBossSelected_4);
		m_buttonStyleLevelBoss_4.checked = boss_s_4;

		// Button Link Horizontal
		TextureRegionDrawable horiz = new TextureRegionDrawable(R.c().level_iconPathLinkHorizontal);
		m_buttonStyleHorizontalLink.up = horiz;

		// Button Link Vertical
		TextureRegionDrawable vert = new TextureRegionDrawable(R.c().level_iconPathLinkVertical);
		m_buttonStyleLevelVerticalLink.up = vert;

		// Start Button
		TextureRegionDrawable start = new TextureRegionDrawable(R.c().screens_iconGoToGameScreen);
		m_buttonStyleStart.up = start;
		TextureRegionDrawable start1 = new TextureRegionDrawable(R.c().screens_iconGoToGameScreenLock);
		m_buttonStyleStart.disabled = start1;

		// Upgrade Button
		TextureRegionDrawable upgrade = new TextureRegionDrawable(R.c().screens_iconGoToUpgradeScreen);
		m_buttonStyleUpgrade.up = upgrade;
		m_buttonStyleUpgrade.pressedOffsetY = 50; // FIXME
		m_buttonStyleUpgrade.unpressedOffsetY = 0;

		// Rate my app
		TextureRegionDrawable rate = new TextureRegionDrawable(R.c().screens_iconGoToRating);
		m_buttonStyleRate.up = rate;
		// Exit app
		TextureRegionDrawable exit = new TextureRegionDrawable(R.c().screens_iconGoToExit);
		m_buttonStyleExit.up = exit;

	}

	private void loadLevelPath()
	{
		levelPath = new String[NOMBRE_LIGNES][];
		try
		{
			FileHandle file = Gdx.files.internal("level-path.txt");
			BufferedReader bufferedReader = file.reader(1);
			String ligne = null;
			ligne = bufferedReader.readLine();
			int i = 0;

			while (ligne != null)
			{
				levelPath[i] = ligne.split(" ");
				i++;
				ligne = bufferedReader.readLine();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void initPath()
	{

		m_groupPath.clear();

		// ----------------------------------------------------------------------------------------------
		// Variation des indices X (pour choisir la page)
		int indiceStart = m_page * TAILLE_LIGNE + m_page;
		int indiceEnd = indiceStart + TAILLE_LIGNE;

		// Creation d'un groupe de bouton pour le checked status
		m_buttonGroup = new ButtonGroup();
		m_buttonGroup.setMaxCheckCount(1);
		m_buttonGroup.setMinCheckCount(0);

		// Boucle de creation

		for (int j = 0; j < levelPath[0].length; j++)
		{
			// Variable pour le placement a l'écran
			int ligneCount = 0;

			for (int i = indiceStart; i < indiceEnd; i++)
			{
				// Boucle sur le tableau de levels
				Button button = null;
				if (isInteger(levelPath[i][j]))
				{
					int level = Integer.parseInt(levelPath[i][j]) - 1;

					boolean nextLevelComplete = Boolean.parseBoolean(m_levels.level[level + 1].levelComplete);
					boolean currentLevelComplete = Boolean.parseBoolean(m_levels.level[level].levelComplete);

					if (nextLevelComplete)
					{
						button = new ButtonLevel(m_levels.level[level], getButtonStyle(level, ButtonStyles.DONE));
					}
					if (currentLevelComplete && !nextLevelComplete)
					{
						button = new ButtonLevel(m_levels.level[level], getButtonStyle(level, ButtonStyles.NEW));
					}
					if (!currentLevelComplete)
					{
						button = new ButtonLevel(m_levels.level[level], getButtonStyle(level, ButtonStyles.LOCK));
					}
					button.addListener(new ButtonLevelListenner());
					m_buttonGroup.add(button);

				} else
				{
					char icon = levelPath[i][j].charAt(0);

					switch (icon) {
					case ICON_PREV:
						button = new Button(m_buttonStyleGoPrev);
						button.addListener(new ButtonPagePrevListenner());
						break;
					case ICON_NEXT:
						button = new Button(m_buttonStyleGoNext);
						button.addListener(new ButtonPageNextListenner());
						break;
					case ICON_LINK_HORIZONTAL:
						button = new Button(m_buttonStyleHorizontalLink);
						break;
					case ICON_LINK_VERTICAL:
						button = new Button(m_buttonStyleLevelVerticalLink);
						break;
					default:
						break;
					}
				}

				// Ajout du bouton
				if (button != null)
				{
					m_groupPath.addActor(button);
					button.setSize(ICON_SIZE, ICON_SIZE);
					button.setPosition(ICON_ANCHOR.x + j * ICON_SIZE, ICON_ANCHOR.y - ligneCount * ICON_SIZE);
				}
				ligneCount++; // incrémente lorsque l'on change de ligne
			}
		}
	}

	private enum ButtonStyles
	{
		LOCK,
		NEW,
		DONE;
	}

	private ButtonStyle getButtonStyle(int level, ButtonStyles buttonStyles)
	{

		if (level < m_boss_level_from_0[0])
		{
			if (buttonStyles == ButtonStyles.NEW)
			{
				return m_buttonStyleLevelNew_1;

			}
			if (buttonStyles == ButtonStyles.DONE)
			{
				return m_buttonStyleLevelDone_1;
			}
			if (buttonStyles == ButtonStyles.LOCK)
			{
				return m_buttonStyleLevelLock_1;
			}
		} else if (level > m_boss_level_from_0[0] && level < m_boss_level_from_0[1])
		{
			if (buttonStyles == ButtonStyles.NEW)
			{
				return m_buttonStyleLevelNew_2;

			}
			if (buttonStyles == ButtonStyles.DONE)
			{
				return m_buttonStyleLevelDone_2;
			}
			if (buttonStyles == ButtonStyles.LOCK)
			{
				return m_buttonStyleLevelLock_2;
			}
		} else if (level > m_boss_level_from_0[1] && level < m_boss_level_from_0[2])
		{
			if (buttonStyles == ButtonStyles.NEW)
			{
				return m_buttonStyleLevelNew_3;

			}
			if (buttonStyles == ButtonStyles.DONE)
			{
				return m_buttonStyleLevelDone_3;
			}
			if (buttonStyles == ButtonStyles.LOCK)
			{
				return m_buttonStyleLevelLock_3;
			}
		} else if (level > m_boss_level_from_0[2] && level < m_boss_level_from_0[3])
		{
			if (buttonStyles == ButtonStyles.NEW)
			{
				return m_buttonStyleLevelNew_4;

			}
			if (buttonStyles == ButtonStyles.DONE)
			{
				return m_buttonStyleLevelDone_4;
			}
			if (buttonStyles == ButtonStyles.LOCK)
			{
				return m_buttonStyleLevelLock_4;
			}
		} else
		{
			// BOSS
			if (level == m_boss_level_from_0[0])
			{
				return m_buttonStyleLevelBoss_1;
			}
			if (level == m_boss_level_from_0[1])
			{
				return m_buttonStyleLevelBoss_2;
			}
			if (level == m_boss_level_from_0[2])
			{
				return m_buttonStyleLevelBoss_3;
			}
			if (level == m_boss_level_from_0[3])
			{
				return m_buttonStyleLevelBoss_4;
			}
		}

		return null;
	}

	private boolean isInteger(String value)
	{
		try
		{
			Integer.parseInt(value);
			return true;
		} catch (Exception e)
		{
			return false;
		}
	}

	// ---------------------------------Liste des écouteurs du panel Level

	class ButtonPageNextListenner extends ClickListener
	{

		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			if (m_page < NOMBRE_DE_PAGE)
			{
				m_page++;
				initPath();
			}
			super.touchUp(event, x, y, pointer, button);
		}

	}

	class ButtonPagePrevListenner extends ClickListener
	{

		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			if (m_page > 0)
			{
				m_page--;
				initPath();
			}
			super.touchUp(event, x, y, pointer, button);
		}

	}

	class ButtonLevelListenner extends ClickListener
	{
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			S.basicButtonClickSound.play(MusicManager.sfxVolume);
			// Recupere le level data contenu dans le buttonLevel
			ButtonLevel buttonLevel = (ButtonLevel) event.getTarget();
			// Change les labels de niveau sur l'écran
			m_labelFontA.setText("Level " + buttonLevel.getLevelData().levelIndex);
			m_labelFontB.setText(buttonLevel.getLevelData().levelName);

			boolean availability = m_levels.level[buttonLevel.getLevelData().levelIndex - 1].levelComplete.equals("true");

			if (availability)
			{
				// Stock le niveau dans le controlleur de screen
				ScreenManager.getInstance().setLevelSelected(buttonLevel.getLevelData());
				// Change le style du button Start
				m_startButton.setDisabled(false);
				m_startButton.clearListeners();
				m_startButton.addListener(new ButtonStartListenner());
			} else
			{
				m_startButton.setDisabled(true);
			}
			super.touchUp(event, x, y, pointer, button);
		}
	}

	class ButtonStartListenner extends ClickListener
	{
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			if (!m_startButton.isDisabled())
			{
				System.out.println("clikk start");
				ScreenManager.getInstance().show(ScreenEnum.GAMELOADING);
				// R.c().soundEffect_player_barrelExplosion.play(MusicManager.sfxVolume);
			}
		}
	}

	class ButtonRatingListenner extends ClickListener
	{
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.oasix.crazyshooter.android");
		}
	}

	class ButtonExitListenner extends ClickListener
	{
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			Gdx.app.exit();
		}
	}

	private void initHud()
	{
		// ----------------------------------BUTTONS
		int yRetreatBottom = -MyGdxGame.VIRTUAL_HEIGHT; // Pour venir du bas
		int yRetreatUp = MyGdxGame.VIRTUAL_HEIGHT; // Pour venir du haut

		// ******Start Button
		m_startButton = new Button(m_buttonStyleStart);
		m_startButton.setDisabled(true);
		m_startButton.setPosition(1700, 50 + yRetreatBottom); // place a sa position final - yRetreat
		m_startButton.setSize(Methods.scaleByHeight(150, m_startButton.getWidth(), m_startButton.getHeight()), 150);
		m_startButton.addListener(new ButtonStartListenner());
		addActor(m_startButton);

		MoveToAction action = new MoveToAction();
		action.setDuration(0.45f);
		action.setInterpolation(Interpolation.swingOut);
		action.setPosition(1700, 50); // se place à sa position finale
		m_startButton.addAction(action);

		// *******Upgrade Button
		m_upgradeButton = new Button(m_buttonStyleUpgrade);
		m_upgradeButton.setPosition(1350, 50 + yRetreatBottom);
		m_upgradeButton.setSize(Methods.scaleByHeight(150, m_upgradeButton.getWidth(), m_upgradeButton.getHeight()), 150);
		m_upgradeButton.addListener(new ClickListener()
		{

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				ScreenManager.getInstance().show(ScreenEnum.UPGRADE);
				super.touchUp(event, x, y, pointer, button);
			}

		});
		addActor(m_upgradeButton);

		MoveToAction action2 = new MoveToAction();
		action2.setDuration(0.5f);
		action2.setInterpolation(Interpolation.swingOut);
		action2.setPosition(1350, 50); // se place à sa position finale
		m_upgradeButton.addAction(action2);

		// *******Rate Button
		m_rateButton = new Button(m_buttonStyleRate);
		m_rateButton.setPosition(400, 50 + yRetreatBottom); // place a sa position final - yRetreat
		m_rateButton.setSize(Methods.scaleByHeight(160, m_rateButton.getWidth(), m_rateButton.getHeight()), 160);
		m_rateButton.addListener(new ButtonRatingListenner());
		addActor(m_rateButton);

		MoveToAction actionRate = new MoveToAction();
		actionRate.setDuration(0.45f);
		actionRate.setInterpolation(Interpolation.swingOut);
		actionRate.setPosition(400, 50); // se place à sa position finale
		m_rateButton.addAction(actionRate);

		// *******Exit Button
		m_exitButton = new Button(m_buttonStyleExit);
		m_exitButton.setPosition(100, 50 + yRetreatBottom); // place a sa position final - yRetreat
		m_exitButton.setSize(Methods.scaleByHeight(140, m_exitButton.getWidth(), m_exitButton.getHeight()), 140);
		m_exitButton.addListener(new ButtonExitListenner());
		addActor(m_exitButton);

		MoveToAction actionExit = new MoveToAction();
		actionExit.setDuration(0.45f);
		actionExit.setInterpolation(Interpolation.swingOut);
		actionExit.setPosition(100, 50); // se place à sa position finale
		m_exitButton.addAction(actionExit);

		// -------------------------------------PANELS
		DrawableSprite drawableSprite = new DrawableSprite(R.c().level_panelSelection);
		MoveToAction action3 = new MoveToAction();
		action3.setDuration(0.45f);
		action3.setInterpolation(Interpolation.swingOut);
		action3.setPosition(LEVEL_PANEL_ANCHOR.x, LEVEL_PANEL_ANCHOR.y); // se place à sa position finale
		Ressource ressource = new Ressource(drawableSprite, LEVEL_PANEL_ANCHOR.x, LEVEL_PANEL_ANCHOR.y + yRetreatUp, 400, new SequenceAction(action3));
		addActor(ressource);

		DrawableSprite drawableSpriteTitle = new DrawableSprite(R.c().level_panelTitle);
		int width = 400;
		MoveToAction action4 = new MoveToAction();
		action4.setDuration(0.45f);
		action4.setInterpolation(Interpolation.swingOut);
		action4.setPosition(MyGdxGame.VIRTUAL_WIDTH / 2 - width / 2, -100); // se place à sa position finale
		Ressource ressource1 = new Ressource(drawableSpriteTitle, MyGdxGame.VIRTUAL_WIDTH / 2 - width / 2, 100 + yRetreatBottom, width, new SequenceAction(action4), Dimension.WIDTH);
		addActor(ressource1);

		// -------------------------------------FONT

		BitmapFont bitmapFont = R.c().fontTypeLightGreen;
		LabelStyle labelStyle = new LabelStyle(bitmapFont, null);
		// Font A

		// Select the first button selectable
		m_labelFontA = new Label("Please select level", labelStyle);

		m_labelFontA.setPosition(FONT_ANCHOR.x, FONT_ANCHOR.y);
		m_labelFontA.setFontScale(5);
		addActor(m_labelFontA);
		// Font B
		m_labelFontB = new Label("", labelStyle);
		m_labelFontB.setPosition(FONT_ANCHOR.x, FONT_ANCHOR.y - 60);
		m_labelFontB.setFontScale(5);
		addActor(m_labelFontB);

	}

}
