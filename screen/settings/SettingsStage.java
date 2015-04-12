package screen.settings;

import java.text.DecimalFormat;

import ressources.ButtonRessource;
import ressources.R;
import screen.MyGdxGame;
import screen.ScreenManager;
import utilities.Methods;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class SettingsStage extends Stage
{

	private final static String	PREFERENCE_FILE				= "user_prefs_options";

	private final static String	button_left_x				= "button_left_x";
	private final static String	button_left_y				= "button_left_y";

	private final static String	button_right_x				= "button_right_x";
	private final static String	button_right_y				= "button_right_y";

	private final static String	button_shoot_x				= "button_shoot_x";
	private final static String	button_shoot_y				= "button_shoot_y";

	private final static String	button_jump_x				= "button_jump_x";
	private final static String	button_jump_y				= "button_jump_y";

	private final static String	button_scale				= "button_scale";

	private Button				m_buttonLeft;
	private Button				m_buttonRight;
	private Button				m_buttonShoot;
	private Button				m_buttonJump;

	private static final int	ORIGINAL_BUTTONS_WIDTH		= 200;
	private static final int	ORIGINAL_BUTTONS_Y			= 40;
	private static final int	ORIGINAL_ENTRAXE			= 220;
	private static final int	ORIGINAL_BUTTONS_X_LEFT		= 40;
	private static final int	ORIGINAL_BUTTONS_X_RIGHT	= 1440;

	Group						group;

	public SettingsStage()
	{
		setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));
		group = new Group();
		addActor(group);

		// Button Left
		TextureRegionDrawable buttonLeftUp = new TextureRegionDrawable(R.c().hud_iconGoLeft);
		m_buttonLeft = new Button(buttonLeftUp);
		m_buttonLeft.addListener(new DragListenner());
		addActor(m_buttonLeft);
		m_buttonLeft.setName("button_left");

		// Button Right
		TextureRegion textureRegion = new TextureRegion(R.c().hud_iconGoLeft);
		textureRegion.flip(true, false);
		TextureRegionDrawable buttonRightUp = new TextureRegionDrawable(textureRegion);
		m_buttonRight = new Button(buttonRightUp);
		m_buttonRight.addListener(new DragListenner());
		addActor(m_buttonRight);
		m_buttonRight.setName("button_right");

		// Button Shoot
		TextureRegionDrawable buttonShootUp = new TextureRegionDrawable(R.c().hud_iconShoot);
		m_buttonShoot = new Button(buttonShootUp);
		m_buttonShoot.addListener(new DragListenner());
		addActor(m_buttonShoot);
		m_buttonShoot.setName("button_shoot");

		// Button Jump
		TextureRegionDrawable buttonJumpUp = new TextureRegionDrawable(R.c().hud_iconJump);
		m_buttonJump = new Button(buttonJumpUp);
		m_buttonJump.addListener(new DragListenner());
		addActor(m_buttonJump);
		m_buttonJump.setName("button_jump");

		// other hud
		initHudSettings();

		// starting :
		placeButtons(m_buttonLeft, m_buttonRight, m_buttonShoot, m_buttonJump);

	}

	private ButtonRessource	addScale;
	private ButtonRessource	removeScale;
	private ButtonRessource	reset;
	private ButtonRessource	save;
	private Label			scaleLabel;								// TODO LABEL MIEUX AVEC FACTOR INTEGRER RESSOURCE LABEL ! //IDEm IMAGE //ADD EFFECT POPING !

	private float			scaleIncrement	= 0.1f;
	DecimalFormat			df				= new DecimalFormat("0.0");

	private void initHudSettings()
	{
		float factor = 2.66f;

		Image image = new Image(new Texture(Gdx.files.internal("images/options/option-bg.png")));
		image.setSize(image.getWidth() * factor, image.getHeight() * factor);
		group.addActor(image);

		Image image2 = new Image(new Texture(Gdx.files.internal("images/options/option-scale-panel.png")));
		image2.setSize(image2.getWidth() * factor, image2.getHeight() * factor);
		image2.setPosition(174 * factor, (405 - 145) * factor);
		group.addActor(image2);

		LabelStyle labelStyle = new LabelStyle(R.c().getEarlyGameBoyFont((int) (14 * factor)), Color.valueOf("fbd00f"));
		Preferences prefs = Gdx.app.getPreferences(PREFERENCE_FILE);
		if (!prefs.contains(button_scale))
		{
			prefs.putFloat(button_scale, 1);
		}
		scaleLabel = new Label(df.format(prefs.getFloat(button_scale)) + "", labelStyle);
		scaleLabel.setPosition(440 * factor, (405 - 128) * factor);
		group.addActor(scaleLabel);

		addScale = new ButtonRessource(group);
		addScale.putUpDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/options/option-button-add.png")))).putPosition(503, 134, factor);
		addScale.addListener(new HudButton());

		removeScale = new ButtonRessource(group);
		removeScale.putUpDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/options/option-button-remove.png")))).putPosition(379, 134, factor);
		removeScale.addListener(new HudButton());

		reset = new ButtonRessource(group);
		reset.putUpDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/options/option-button-reset.png")))).putPosition(202, 191, factor);
		reset.addListener(new HudButton());

		save = new ButtonRessource(group);
		save.putUpDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/options/option-button-save.png")))).putPosition(317, 191, factor);
		save.addListener(new HudButton());

	}

	/**
	 * Reset les preferences des boutons à la manière par defaut.
	 */
	private static void resetPrefs(Button m_buttonLeft, Button m_buttonRight, Button m_buttonShoot, Button m_buttonJump)
	{
		modification(m_buttonLeft);
		modification(m_buttonRight);
		modification(m_buttonShoot);
		modification(m_buttonJump);
		Preferences prefs = Gdx.app.getPreferences(PREFERENCE_FILE);
		prefs.putFloat(button_scale, 1);
		prefs.flush();
	}

	/**
	 * Reset les boutons à la manière par defaut.
	 */
	private static void resetButtons(Button m_buttonLeft, Button m_buttonRight, Button m_buttonShoot, Button m_buttonJump)
	{
		m_buttonLeft.setPosition(ORIGINAL_BUTTONS_X_LEFT, ORIGINAL_BUTTONS_Y);
		m_buttonLeft.setWidth(Methods.scaleByHeight(ORIGINAL_BUTTONS_WIDTH, m_buttonLeft.getWidth(), m_buttonLeft.getHeight()));
		m_buttonLeft.setHeight(ORIGINAL_BUTTONS_WIDTH);
		m_buttonLeft.setScale(1);

		m_buttonRight.setPosition(ORIGINAL_BUTTONS_X_LEFT + ORIGINAL_ENTRAXE, ORIGINAL_BUTTONS_Y);
		m_buttonRight.setWidth(Methods.scaleByHeight(ORIGINAL_BUTTONS_WIDTH, m_buttonRight.getWidth(), m_buttonRight.getHeight()));
		m_buttonRight.setHeight(ORIGINAL_BUTTONS_WIDTH);
		m_buttonRight.setScale(1);

		m_buttonShoot.setPosition(ORIGINAL_BUTTONS_X_RIGHT, ORIGINAL_BUTTONS_Y);
		m_buttonShoot.setWidth(Methods.scaleByHeight(ORIGINAL_BUTTONS_WIDTH, m_buttonShoot.getWidth(), m_buttonShoot.getHeight()));
		m_buttonShoot.setHeight(ORIGINAL_BUTTONS_WIDTH);
		m_buttonShoot.setScale(1);

		m_buttonJump.setPosition(ORIGINAL_BUTTONS_X_RIGHT + ORIGINAL_ENTRAXE, ORIGINAL_BUTTONS_Y);
		m_buttonJump.setWidth(Methods.scaleByHeight(ORIGINAL_BUTTONS_WIDTH, m_buttonJump.getWidth(), m_buttonJump.getHeight()));
		m_buttonJump.setHeight(ORIGINAL_BUTTONS_WIDTH);
		m_buttonJump.setScale(1);
	}

	/**
	 * Place les boutons de la manière définit par l'user ou de la manière par defaut Utilisé pour positionner le hud.
	 */
	public static void placeButtons(Button m_buttonLeft, Button m_buttonRight, Button m_buttonShoot, Button m_buttonJump)
	{
		resetButtons(m_buttonLeft, m_buttonRight, m_buttonShoot, m_buttonJump);

		Preferences prefs = Gdx.app.getPreferences(PREFERENCE_FILE);
		if (prefs.get().size() > 0)
		{
			// Retrieve preference if exist

			if (prefs.contains(button_left_x))
			{
				m_buttonLeft.setPosition(prefs.getFloat(button_left_x), prefs.getFloat(button_left_y));
			}

			if (prefs.contains(button_right_x))
			{
				m_buttonRight.setPosition(prefs.getFloat(button_right_x), prefs.getFloat(button_right_y));
			}

			if (prefs.contains(button_shoot_x))
			{
				m_buttonShoot.setPosition(prefs.getFloat(button_shoot_x), prefs.getFloat(button_shoot_y));
			}

			if (prefs.contains(button_jump_x))
			{
				m_buttonJump.setPosition(prefs.getFloat(button_jump_x), prefs.getFloat(button_jump_y));
			}

			if (prefs.contains(button_scale))
			{
				m_buttonLeft.setSize(m_buttonLeft.getWidth() * prefs.getFloat(button_scale), m_buttonLeft.getHeight() * prefs.getFloat(button_scale));
				m_buttonRight.setSize(m_buttonRight.getWidth() * prefs.getFloat(button_scale), m_buttonRight.getHeight() * prefs.getFloat(button_scale));
				m_buttonShoot.setSize(m_buttonShoot.getWidth() * prefs.getFloat(button_scale), m_buttonShoot.getHeight() * prefs.getFloat(button_scale));
				m_buttonJump.setSize(m_buttonJump.getWidth() * prefs.getFloat(button_scale), m_buttonJump.getHeight() * prefs.getFloat(button_scale));
			}
		}
	}

	public static void modification(Actor button)
	{
		Preferences prefs = Gdx.app.getPreferences(PREFERENCE_FILE);
		prefs.putFloat(button.getName() + "_x", button.getX());
		prefs.putFloat(button.getName() + "_y", button.getY());
		prefs.flush();
	}

	class DragListenner extends DragListener
	{
		@Override
		public void drag(InputEvent event, float x, float y, int pointer)
		{
			super.drag(event, x, y, pointer);
			event.getListenerActor().moveBy(x - event.getListenerActor().getWidth() / 2, y - event.getListenerActor().getHeight() / 2);
			modification(event.getListenerActor());
		}
	}

	class HudButton extends ClickListener
	{
		@Override
		public void clicked(InputEvent event, float x, float y)
		{
			super.clicked(event, x, y);

			Preferences prefs = Gdx.app.getPreferences(PREFERENCE_FILE);

			if (!prefs.contains(button_scale))
			{
				prefs.putFloat(button_scale, 1);
			}

			if (prefs.getFloat(button_scale) > 0 + scaleIncrement && prefs.getFloat(button_scale) <= 1.5f + scaleIncrement)
			{
				// TODO
			}

			if (addScale == event.getListenerActor())
			{
				prefs.putFloat(button_scale, prefs.getFloat(button_scale) + scaleIncrement);
			}

			if (removeScale == event.getListenerActor())
			{
				prefs.putFloat(button_scale, prefs.getFloat(button_scale) - scaleIncrement);
			}

			if (reset == event.getListenerActor())
			{
				resetButtons(m_buttonLeft, m_buttonRight, m_buttonShoot, m_buttonJump);
				resetPrefs(m_buttonLeft, m_buttonRight, m_buttonShoot, m_buttonJump);
			}

			scaleLabel.setText(df.format(prefs.getFloat(button_scale)) + "");
			placeButtons(m_buttonLeft, m_buttonRight, m_buttonShoot, m_buttonJump);

			if (save == event.getListenerActor())
			{
				ScreenManager.getInstance().show(ScreenEnum.LOADING);
			}
		}
	}

}
