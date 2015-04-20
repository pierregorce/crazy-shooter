package screen;

import java.util.ArrayList;
import java.util.Random;

import ressources.R;
import screen.loading.LoadingGroup;
import utilities.ButtonScreen;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import files.Files;
import globals.Worlds;

/**
 * Ne peut être utilisé que lors du chargement initial, prévoir un autre screen pour le game LOADING
 * 
 * @author Pierre
 *
 */
public class LoadingScreen implements MovableScreen
{
	private Stage				m_stage		= new Stage();
	private boolean				m_loading	= false;

	private LoadingGroup		loadingGroup;
	private Label				loadingLabel;
	private Label				versionLabel;

	private BitmapFont			bitmapTips;
	private ArrayList<String>	tipsTable	= new ArrayList<String>();
	private String				tips;

	private String				version		= "version 1.2";
	private long				time		= System.currentTimeMillis();

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		m_stage.act(delta);
		m_stage.draw();

		// Tant que le load n'est pas fait alors alors on ne passe pas au changement de screen

		R.c().assetManager.finishLoading();

		if (R.c().assetManager.update(0) && !m_loading)
		{
			m_loading = true;
			R.c().generateTextures();
			// Generation des fonts
			R.c().generateFonts();
			// R.c().generateSound();

			System.out.println("Initialisation des variables globales concernant le joueur...");
			// Stock les données d'upgrades dans la classe Upgrades - FICHIER player-upgrade
			Files.playerUpgradeRead();
			// Stock les données concernant le player level, l'xp du joueur, l'arme du joueur - FICHIER player-globals
			Files.playerDataRead();
			Files.playerWeaponsRead();
			// Stock les données concernannt le game level
			ScreenManager.getInstance().setLevelSelected(Worlds.getLastLevelUnlock());
			// Initialisation des screens, ils peuvent utiliser les variables précedement chargées
			System.out.println("Done.");
			addButtons();
			System.out.println("Temps de chargement : " + (System.currentTimeMillis() - time));
		}

		// System.out.println("Temps de chargement = " + (b));

		float percent = R.c().assetManager.getProgress() * 100;

		if (percent < 100)
		{
			loadingLabel.setText("LOADING : " + (int) percent + " %");
			loadingLabel.setPosition(700, 430);
		} else
		{
			loadingLabel.remove();
		}

		m_stage.getBatch().begin();
		float width = 1500;
		float position = MyGdxGame.VIRTUAL_WIDTH - width - (MyGdxGame.VIRTUAL_WIDTH - width) / 2;
		bitmapTips.drawWrapped(m_stage.getBatch(), tips, position, 200, width, HAlignment.CENTER);
		m_stage.getBatch().end();
		bitmapTips.setScale(4f);

		versionLabel.setText(version);
		versionLabel.setPosition(1700, 50);

	}

	private void addButtons()
	{
		float y = 340;
		ButtonScreen b = new ButtonScreen(loadingGroup, ScreenEnum.WORLD);
		b.putStyle(new TextureRegion(new Texture(Gdx.files.internal("images/loading/loading-button-goToPlay.png"))));
		b.putSize();
		b.setPosition(-MyGdxGame.VIRTUAL_WIDTH, 280 + y); // place a sa position final - yRetreat
		b.addAction(Actions.moveTo(MyGdxGame.VIRTUAL_WIDTH / 2, 280 + y, 0.4f, Interpolation.sine));

		ButtonScreen b2 = new ButtonScreen(loadingGroup, ScreenEnum.SETTINGS);
		b2.putStyle(new TextureRegion(new Texture(Gdx.files.internal("images/loading/loading-button-goToSettings.png"))));
		b2.setPosition(-MyGdxGame.VIRTUAL_WIDTH, 160 + y); // place a sa position final - yRetreat
		b2.putSize();
		Action a = Actions.moveTo(MyGdxGame.VIRTUAL_WIDTH / 2, 160 + y, 0.4f, Interpolation.sine);
		b2.addAction(Actions.delay(0.3f, a));

		ButtonScreen b3 = new ButtonScreen(loadingGroup, ScreenEnum.CHEATS);
		b3.putStyle(new TextureRegion(new Texture(Gdx.files.internal("images/loading/loading-button-goToCheatsCode.png"))));
		b3.putSize();
		b3.setPosition(-MyGdxGame.VIRTUAL_WIDTH, 40 + y); // place a sa position final - yRetreat
		Action a2 = Actions.moveTo(MyGdxGame.VIRTUAL_WIDTH / 2, 40 + y, 0.4f, Interpolation.sine);
		b3.addAction(Actions.delay(0.6f, a2));
	}

	@Override
	public void resize(int width, int height)
	{
		m_stage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{

		// Set processeur
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(m_stage);
		Gdx.input.setInputProcessor(multiplexer);

		m_stage.setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));

		// Generation des textures
		R.c();
		tipsTable.add("TIPS : Be aware from bat, they are vicious !");
		tipsTable.add("TIPS : Lighting gun is AWESOME");
		tipsTable.add("TIPS : There is more than 20 differents enemy type...");
		tipsTable.add("TIPS : Improve your skill on switching plateform, it will be usefull !");
		tipsTable.add("TIPS : You should increase you life as first stats !");
		tipsTable.add("TIPS : Choose the flamme thrower depending the ennemies your affronting. It could be monstrous or inefiscient...");
		tips = tipsTable.get(new Random().nextInt(tipsTable.size()));

		loadingGroup = new LoadingGroup();
		m_stage.addActor(loadingGroup);
		loadingLabel = new Label("", new LabelStyle(R.c().getEarlyGameBoyFont(60), Color.WHITE));
		versionLabel = new Label("", new LabelStyle(R.c().getWendyFont(35), Color.WHITE));
		loadingGroup.addActor(loadingLabel);
		loadingGroup.addActor(versionLabel);
		bitmapTips = R.c().getEarlyGameBoyFont(8);

		if (m_loading)
		{
			addButtons();
		}
	}

	@Override
	public void hide()
	{
		m_loading = false;
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void dispose()
	{
		m_loading = false;
	}

	@Override
	public Stage getStage()
	{
		return m_stage;
	}

}
