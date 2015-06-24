package files;

import globals.PlayerStats;
import globals.Upgrades;
import globals.Weapons;
import screen.level.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class Files
{
	private final static String	SALT_KEY				= "PGE";
	private final static String	version					= "";
	public final static String	levelConfigName			= "levels-config" + version + ".json";
	public final static String	levelConfigNameFrame	= "levels-config-v2" + version + ".json";
	public final static String	playerUpgradeName		= "player-upgrades" + version + ".json";
	public final static String	playerGlobalName		= "player-globals" + version + ".json";
	public final static String	playerWeaponsName		= "player-weapons" + version + ".json";

	// ------------Gestion des fichiers pour la sauvergarde des LEVELS

	public static Levels levelDataRead()
	{
		// Definition du fichier a retrouver
		FileHandle localFile = Gdx.files.local(levelConfigName);

		if (!localFile.exists())
		{
			// Ecriture si le fichier n'existe pas
			FileHandle internalfile = Gdx.files.internal(levelConfigNameFrame);
			String levelsConfig = internalfile.readString();
			localFile.writeString(levelsConfig, false);
		}

		// Retrieve du fichier écrit
		String levelsConfig = localFile.readString();
		Levels levels = new Json().fromJson(Levels.class, levelsConfig);

		// Vérification si le fichier récupéré n'est pas d'une vielle version.
		try
		{
			if (levels.level[0].levelIndex != 0)
			{
				// On est sur l'ancien mode
				// Récupération du dernier niveau
				int lastUnlock = getLastLevelUnlock(levels);
				// System.out.println("Le dernier niveau était le " + lastUnlock);

				// Récupération d'un nouveau fichier de niveau
				Levels newLevels = new Json().fromJson(Levels.class, Gdx.files.internal(levelConfigNameFrame));
				// System.out.println("qzdzdqzd      " + newLevels.level[0].levelIndex + "   " + newLevels.level[0].levelName);

				// Affectation des niveau complété.
				for (int i = 0; i < newLevels.level.length; i++)
				{
					if (i <= lastUnlock)
					{
						newLevels.level[i].levelComplete = "true";
						// System.out.println("affection du niveau a complété");
					}
				}
				levels = newLevels;

				// Ecriture du fichier en remplacement
				localFile.writeString(new Json().toJson(levels), false);
				// System.out.println("PAF ECRIT !");
			}

		} catch (Exception e)
		{
			// On ne fait rien car on est sur la bonne structure
		}

		return levels;
	}

	public static int getLastLevelUnlock(Levels level)
	{
		for (int i = 0; i < level.level.length; i++)
		{
			if (i > 0 && level.level[i - 1].levelComplete.equals("true") && level.level[i].levelComplete.equals("false"))
			{
				return i;
			}
		}
		return 0;
	}

	public static void levelDataWrite(Levels level)
	{
		// Ecriture
		FileHandle localFile = Gdx.files.local(levelConfigName);
		Json json = new Json();
		String string = json.toJson(level);
		localFile.writeString(string, false);
	}

	// ------------Gestion des fichiers pour la sauvergarde des UPGRADES
	// Stock les données dans l'enum Upgrade

	public static void playerUpgradeRead()
	{
		// Definition du fichier a retrouver
		FileHandle localFile = Gdx.files.local(playerUpgradeName);

		if (!localFile.exists())
		{
			playerUpgradeWrite();
		} else
		{
			// Lecture des données
			String savedState = localFile.readString();
			// savedState = savedState.replace(SALT_KEY, "");
			// savedState = Base64Coder.decodeString(savedState);

			Json json = new Json();
			Upgrades.retrieve(json.fromJson(Integer[].class, savedState));
		}

	}

	public static void playerUpgradeWrite()
	{
		// Ecriture des données
		Json json = new Json();
		String savedState = json.toJson(Upgrades.send());
		FileHandle localFile = Gdx.files.local(playerUpgradeName);
		// savedState = Base64Coder.encodeString(savedState);
		// localFile.writeString(SALT_KEY + savedState, false);
		localFile.writeString(savedState, false);
	}

	// ------------Gestion des fichiers pour la sauvergarde du PLAYER
	// Stock les données dans la classe static de player Upgrade

	public static void playerDataRead()
	{
		FileHandle localFile = Gdx.files.local(playerGlobalName);

		if (!localFile.exists())
		{
			// Ecriture dans un premier temps si le fichier n'existe pas
			Json json = new Json();
			PlayerStats.PlayerSavedDatas playerSavedDatas = new PlayerStats.PlayerSavedDatas();
			playerSavedDatas.currentXp = 0;
			playerSavedDatas.ressource = 0;
			playerSavedDatas.level = 1;
			playerSavedDatas.weaponsType = 0;
			String string = json.toJson(playerSavedDatas);
			localFile.writeString(string, false);
		} else
		{
			// Lecture des données
			PlayerStats.PlayerSavedDatas playerSavedData;
			String savedState = localFile.readString();
			Json json = new Json();
			playerSavedData = json.fromJson(PlayerStats.PlayerSavedDatas.class, savedState);
			PlayerStats.level = playerSavedData.level;
			PlayerStats.currentXP = playerSavedData.currentXp;
			PlayerStats.ressource = playerSavedData.ressource;
			PlayerStats.weaponsType = playerSavedData.weaponsType;
		}
	}

	public static void playerDataWrite()
	{
		FileHandle localFile = Gdx.files.local(playerGlobalName);
		// Ecriture
		Json json = new Json();
		PlayerStats.PlayerSavedDatas playerSavedDatas = new PlayerStats.PlayerSavedDatas();
		playerSavedDatas.currentXp = PlayerStats.currentXP;
		playerSavedDatas.level = PlayerStats.level;
		playerSavedDatas.ressource = PlayerStats.ressource;
		playerSavedDatas.weaponsType = PlayerStats.weaponsType;
		;
		String string = json.toJson(playerSavedDatas);

		localFile.writeString(string, false);
	}

	// ------------Gestion des fichiers pour la sauvergarde des WEAPONS
	// Stock les données dans l'enum Weapons

	public static void playerWeaponsRead()
	{
		// Definition du fichier a retrouver
		FileHandle localFile = Gdx.files.local(playerWeaponsName);

		if (!localFile.exists())
		{
			// Ecriture si le fichier n'existe pas
			Json json = new Json();
			String savedState = json.toJson(Weapons.send());
			localFile.writeString(savedState, false);

		} else
		{
			// Lecture des données
			String savedState = localFile.readString();
			Json json = new Json();
			Weapons.retrieve(json.fromJson(Boolean[].class, savedState));
		}

	}

	public static void playerWeaponsWrite()
	{
		// Ecriture des données
		Json json = new Json();
		String savedState = json.toJson(Weapons.send());
		FileHandle localFile = Gdx.files.local(playerWeaponsName);
		localFile.writeString(savedState, false);
	}

}
