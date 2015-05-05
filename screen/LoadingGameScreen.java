package screen;

import java.util.TreeMap;

import ressources.R;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LoadingGameScreen implements MovableScreen
{
	private Stage						loadingStage	= new Stage();
	private boolean						done			= false;

	private Group						group			= new Group();
	private Label						loadingLabel;
	private Label						explainLabel;
	private Image						backgroundImage;

	private TreeMap<Integer, String>	hash			= new TreeMap<Integer, String>();

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		loadingStage.act(delta);
		loadingStage.draw();

		loadingLabel.setPosition(0, MyGdxGame.VIRTUAL_HEIGHT / 2 + 30);
		loadingLabel.setAlignment(Align.center);
		loadingLabel.setWidth(MyGdxGame.VIRTUAL_WIDTH);
		// loadingLabel.setText("Loading : " + (int) (S.c().soundManager.getProgress() * 100) + "");

		loadingLabel.setText("Loading");

		explainLabel.setPosition(200, MyGdxGame.VIRTUAL_HEIGHT / 2 - 30);
		explainLabel.setAlignment(Align.center);
		explainLabel.setWidth(MyGdxGame.VIRTUAL_WIDTH - 400);
		explainLabel.setWrap(true);
		explainLabel.setDebug(false);

		if (!done)
		{
			Action a = Actions.delay(0.8f);
			Action b = new RunnableAction()
			{
				@Override
				public void run()
				{
					// S.c().construct();
				}
			};
			Action c = Actions.delay(1f);

			Action d = new RunnableAction()
			{
				@Override
				public void run()
				{
					ScreenManager.getInstance().show(ScreenEnum.GAME);
				}
			};

			loadingStage.addAction(Actions.sequence(a, b, c, d));

			done = true;
		}

		// // On itère sur la map pour trouver le poucentage sur lequel on est
		// for (Integer key : hash.keySet())
		// {
		// if (key <= ((int) (S.c().soundManager.getProgress() * 100)))
		// {
		// explainLabel.setText(hash.get(key));
		// }
		// }
		//
		// if (GameStage.debugRapidAndLevels)
		// {
		// S.c().soundManager.finishLoading();
		// }
		//
		// if (loadingStage.getRoot().getActions().size == 0 && !done)
		// {
		// System.out.println("Chargement du game en cours");
		// if (S.c().soundManager.update())
		// {
		// S.c().generateSound();
		// done = true;
		// }
		// }

	}

	@Override
	public void resize(int width, int height)
	{
		loadingStage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		System.out.println("LoadingGameScreen Show");
		done = false;
		// Set processeur
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(loadingStage);
		Gdx.input.setInputProcessor(multiplexer);

		loadingStage.addActor(group);
		loadingStage.setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));

		float factor = 2.66f;

		backgroundImage = new Image(new Texture(Gdx.files.internal("images/options/option-bg.png")));
		backgroundImage.setSize(backgroundImage.getWidth() * factor, backgroundImage.getHeight() * factor);
		group.addActor(backgroundImage);

		loadingLabel = new Label("", new LabelStyle(R.c().EarlyGameBoyFont_38, Color.WHITE));
		group.addActor(loadingLabel);

		explainLabel = new Label("", new LabelStyle(R.c().EarlyGameBoyFont_32, Color.WHITE));
		group.addActor(explainLabel);

		hash.put(0, "Loading sounds");
		hash.put(20, "Loading weapons");
		hash.put(35, "Loading ennemies");
		hash.put(50, "Loading munitions");
		hash.put(65, "Loading blood");
		hash.put(75, "Process world");
		hash.put(85, "Process finishing");
	}

	@Override
	public void hide()
	{
		System.out.println("LoadingGameScreen Hidden");
		// Lancement d'une action
	}

	@Override
	public void pause()
	{
		System.out.println("LoadingGameScreen Pause");
	}

	@Override
	public void resume()
	{
		System.out.println("LoadingGameScreen Resume");
	}

	@Override
	public void dispose()
	{
		System.out.println("LoadingGameScreen Dispose");
	}

	@Override
	public Stage getStage()
	{
		return loadingStage;
	}

}
