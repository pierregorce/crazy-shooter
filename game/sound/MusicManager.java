package game.sound;

import globals.Worlds;

import java.util.HashMap;
import java.util.Map;

import screen.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class MusicManager
{
	private MusicManager()
	{
		// Cannot be instancied
	}

	public static Map<String, FileHandle>	musics				= new HashMap<String, FileHandle>();
	public static float						higherMusicVolume	= 0.3f;
	public static float						lowerMusicVolume	= 0f;

	public static String					currentMusicKey		= "";
	public static Music						currentMusic;

	static
	{
		musics.put("Menu", Gdx.files.internal("music/crazy shooter menu theme v1.1.mp3"));
		musics.put("BG1", Gdx.files.internal("music/crazy spooky theme v1.5.mp3"));
		musics.put("BG2", Gdx.files.internal("music/Crazy shooter underground.mp3"));
		musics.put("BG3", Gdx.files.internal("music/Crazy Shooter Nature Theme v1.2.mp3"));
		musics.put("BG4", Gdx.files.internal("music/World4 - Sin City.mp3"));
		musics.put("BG5", Gdx.files.internal("music/World5 - The Depths of Putricides.mp3"));
	}

	public static void play_Menu_Music()
	{
		loadAndPlay("Menu");
	}

	public static void play_Game_Music()
	{
		int level = ScreenManager.getInstance().getLevelSelected().levelIndex;

		// Charge le monde en fonction des niveau des boss
		if (Worlds.getWorldNumber(level) == 0)
		{
			loadAndPlay("BG1");
		} else if (Worlds.getWorldNumber(level) == 1)
		{
			loadAndPlay("BG2");
		} else if (Worlds.getWorldNumber(level) == 2)
		{
			loadAndPlay("BG3");
		} else if (Worlds.getWorldNumber(level) == 3)
		{
			loadAndPlay("BG4");
		} else if (Worlds.getWorldNumber(level) == 4)
		{
			loadAndPlay("BG5");
		} else
		{
			loadAndPlay("BG2");
		}
	}

	private static synchronized void loadAndPlay(String musicName)
	{
		// Add Transition over time
		// Create 2 actors and use the alpha for set volume.

		if (!currentMusicKey.equals(musicName))
		{
			System.out.println("-------------------------CHANGEMENT DE MUSIQUE-------------------------");
			System.out.println("Current Music key = " + currentMusicKey);
			System.out.println("Final Music key = " + musicName);

			currentMusicKey = musicName;
			stop();
			currentMusic = Gdx.audio.newMusic(musics.get(musicName));
			play();
		}

	}

	public static void replay()
	{
		stop();
		currentMusic = Gdx.audio.newMusic(musics.get(currentMusicKey));
		play();
	}

	public static void play()
	{
		if (currentMusic != null)
		{
			currentMusic.setVolume(higherMusicVolume);
			if (currentMusicKey == "Menu")
			{
				currentMusic.setVolume(higherMusicVolume + 0.3f);
			}
			// currentMusic.setLooping(true);
			currentMusic.play();
		}
	}

	public static void stop()
	{
		if (currentMusic != null)
		{
			currentMusic.dispose();
		}
	}

}
