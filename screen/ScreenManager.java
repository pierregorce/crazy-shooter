package screen;

import screen.level.LevelData;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.utils.IntMap;

public final class ScreenManager
{

	private IntMap<MovableScreen> screens;
	private MyGdxGame game;
	private static ScreenManager uniqueInstance;// Stockage de l'unique instance de cette classe.
	private LevelData levelSelected;
	private MovableScreen m_currentScreen;

	private ScreenManager()
	{
		super();
		screens = new IntMap<MovableScreen>();

	}

	public void initialize(MyGdxGame game)
	{
		this.game = game;
	}

	public static ScreenManager getInstance()
	{

		if (uniqueInstance == null)
		{
			uniqueInstance = new ScreenManager();
		}
		return uniqueInstance;
	}

	public boolean fade = false;

	public void show(final ScreenEnum screenEnum)
	{
		// Si le screen n'existe pas, alors création.
		if (!screens.containsKey(screenEnum.ordinal()))
		{
			screens.put(screenEnum.ordinal(), screenEnum.getScreenInstance());
		}

		if (m_currentScreen == null)
		{
			// Initialisation.
			game.setScreen(screens.get(screenEnum.ordinal()));
			m_currentScreen = screens.get(screenEnum.ordinal());
		} else
		{
			// Autres cas.
			if (screenEnum != ScreenEnum.GAME)
			{
				System.out.println("Lancement d'un transition screen de : " + m_currentScreen.toString() + " vers : " + screenEnum.toString());
				game.setScreen(new TransitionScreen(game, m_currentScreen, screens.get(screenEnum.ordinal())));
				m_currentScreen = screens.get(screenEnum.ordinal());
			} else
			{
				System.out.println("Chargement du gameScreen");
				GameScreen gameScreen = (GameScreen) screens.get(screenEnum.ordinal()); // Recupère l'instance du game
				// Initialize le gameScreen
				gameScreen.initialize();
				// Set le gameScreen
				// game.setScreen(gameScreen);
				game.setScreen(new TransitionScreen(game, m_currentScreen, gameScreen)); // TODO TESTER
				m_currentScreen = gameScreen;
			}
		}
		screenEnum.setMusic();
	}

	public void dispose(final ScreenEnum screenEnum)
	{
		if (!screens.containsKey(screenEnum.ordinal()))
		{
			return;
		}
		screens.remove(screenEnum.ordinal()).dispose();
	}

	public void dispose()
	{
		for (com.badlogic.gdx.Screen screen : screens.values())
		{
			screen.dispose();
		}
		screens.clear();
		uniqueInstance = null;
	}

	public LevelData getLevelSelected()
	{
		return levelSelected;
	}

	public void setLevelSelected(LevelData currentLevel)
	{
		this.levelSelected = currentLevel;
	}

}
