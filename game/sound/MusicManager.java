package game.sound;

import java.util.HashMap;
import java.util.Map;

import screen.ScreenManager;
import screen.level.LevelGroup_old;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class MusicManager
{
	private MusicManager()
	{
		// Cannot be instancied
	}

	public static Map<String, FileHandle> musics = new HashMap<String, FileHandle>();
	public static float higherMusicVolume = 0.3f;
	public static float lowerMusicVolume = 0f;

	// Surtout les enemies
	public static float sfxVolume = 0.9f;
	// Evenement a faible occurence et les armes
	public static float sfxVolume_Weapons = 0.85f;
	public static float sfxVolume_Player = 1f;
	public static float sfxVolume_BossLow = 0.9f;

	public static String currentMusicKey = "";
	public static Music currentMusic;

	static
	{
		musics.put("Menu", Gdx.files.internal("music/crazy shooter menu theme v1.1.mp3"));
		// musics.put("BG1", Gdx.files.internal("music/crazy spooky theme v1.5.ogg"));
		musics.put("BG1", Gdx.files.internal("music/crazy spooky theme v1.5.mp3"));
		musics.put("BG2", Gdx.files.internal("music/Crazy shooter underground.mp3"));
		musics.put("BG3", Gdx.files.internal("music/Crazy Shooter Nature Theme v1.2.mp3"));
	}

	public static void play_Menu_Music()
	{
		loadAndPlay("Menu");
	}

	public static void play_Game_Music()
	{
		int[] m_boss_level = LevelGroup_old.m_boss_level_from_1; // Récupère les niveau des boss (commence à 1)
		int level = ScreenManager.getInstance().getLevelSelected().levelIndex; // Recupère le level (commence à 1)

		// Charge le monde en fonction des niveau des boss
		if (level <= m_boss_level[0])
		{
			loadAndPlay("BG1");
		} else if (level > m_boss_level[1])
		{
			loadAndPlay("BG3");
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
