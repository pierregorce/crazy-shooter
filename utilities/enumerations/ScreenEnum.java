package utilities.enumerations;

import game.sound.MusicManager;
import screen.CheatingScreen;
import screen.EndingScreen;
import screen.GameScreen;
import screen.LevelScreen;
import screen.LoadingGameScreen;
import screen.LoadingScreen;
import screen.MovableScreen;
import screen.SettingsScreen;
import screen.UpgradeScreen;
import screen.WorldScreen;

public enum ScreenEnum
{
	GAMELOADING
	{
		@Override
		public MovableScreen getScreenInstance()
		{
			return new LoadingGameScreen();
		}

		@Override
		public void setMusic()
		{
			MusicManager.play_Game_Music();
		}
	},
	LEVEL
	{
		@Override
		public MovableScreen getScreenInstance()
		{
			return new LevelScreen();
		}

		@Override
		public void setMusic()
		{
			MusicManager.play_Menu_Music();
		}
	},
	UPGRADE
	{
		@Override
		public MovableScreen getScreenInstance()
		{
			return new UpgradeScreen();
		}

		@Override
		public void setMusic()
		{
			MusicManager.play_Menu_Music();
		}
	},
	GAME
	{
		@Override
		public MovableScreen getScreenInstance()
		{
			return new GameScreen();
		}

		@Override
		public void setMusic()
		{
			MusicManager.play_Game_Music();
		}
	},
	LOADING
	{
		@Override
		public MovableScreen getScreenInstance()
		{
			return new LoadingScreen();
		}

		@Override
		public void setMusic()
		{
			MusicManager.play_Menu_Music();
		}
	},
	SETTINGS
	{
		@Override
		public MovableScreen getScreenInstance()
		{
			return new SettingsScreen();
		}

		@Override
		public void setMusic()
		{
			MusicManager.play_Menu_Music();
		}
	},
	CHEATS
	{
		@Override
		public MovableScreen getScreenInstance()
		{
			return new CheatingScreen();
		}

		@Override
		public void setMusic()
		{
			MusicManager.play_Menu_Music();
		}
	},
	WORLD
	{
		@Override
		public MovableScreen getScreenInstance()
		{
			return new WorldScreen();
		}

		@Override
		public void setMusic()
		{
			MusicManager.play_Menu_Music();
		}
	},
	ENDING
	{
		@Override
		public MovableScreen getScreenInstance()
		{
			return new EndingScreen();
		}

		@Override
		public void setMusic()
		{
			MusicManager.play_Menu_Music();
		}
	};

	public abstract MovableScreen getScreenInstance();

	public abstract void setMusic();

}
