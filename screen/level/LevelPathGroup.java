package screen.level;

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;

import files.Files;

public class LevelPathGroup extends Group
{

	private final int	TAILLE_LIGNE	= 5;

	private String[][]	levelPath;
	private Levels		levels;

	public LevelPathGroup()
	{
		loadLevelPath();
		levels = Files.levelDataRead();
	}

	private void loadLevelPath()
	{
		levelPath = new String[50][];
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

		for (int j = 0; j < levelPath[0].length; j++)
		{

		}

	}

}
