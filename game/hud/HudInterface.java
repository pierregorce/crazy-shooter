package game.hud;

import ressources.R;
import screen.MyGdxGame;
import screen.settings.SettingsStage;
import utilities.enumerations.GameStatesEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.oasix.crazyshooter.GameStage;

public class HudInterface extends Group
{
	private final GameStage		gameStage;
	private static final int	fullHudButton	= 200;
	private static final int	middleHudButton	= 125;
	private static final int	yHudButton		= 40;

	private static final int	entraxe			= 220;
	private static final int	x1				= 40;
	private static final int	x2				= 1440;

	private Button				m_buttonLeft;
	private Button				m_buttonRight;
	private Button				m_buttonPause;
	private Button				m_buttonShoot;
	private Button				m_buttonJump;

	private Rectangle			buttonLeft;
	private Rectangle			buttonRight;
	private Rectangle			buttonShoot;
	private Rectangle			buttonJump;
	boolean						rightDown		= false;
	boolean						leftDown		= false;
	boolean						shoot			= false;

	/**
	 * Renvoie la largeur calculé pour une hauteur voulue et les dimmensions initiales
	 */
	public float scaleByHeight(float finalHeight, float initialWidth, float initialHeight)
	{
		return initialWidth * finalHeight / initialHeight;
	}

	/**
	 * Renvoie la hauteur calculée pour une largeur voulue et les dimmensions initiales
	 */
	public float scaleByWidth(float finalWidth, float initialWidth, float initialHeight)
	{
		return initialHeight * finalWidth / initialWidth;
	}

	public HudInterface(GameStage gameStage)
	{
		super();
		this.gameStage = gameStage;

		// Button Left
		TextureRegionDrawable buttonLeftUp = new TextureRegionDrawable(R.c().hud_iconGoLeft);
		TextureRegionDrawable buttonLeftDown = new TextureRegionDrawable(R.c().hud_iconGoLeft);
		m_buttonLeft = new Button(buttonLeftUp, buttonLeftDown);
		m_buttonLeft.setPosition(x1, yHudButton);
		m_buttonLeft.setWidth(scaleByHeight(fullHudButton, m_buttonLeft.getWidth(), m_buttonLeft.getHeight()));
		m_buttonLeft.setHeight(fullHudButton);
		// m_buttonLeft.addListener(new ButtonChange());
		addActor(m_buttonLeft);

		// Button Right
		TextureRegion textureRegion = new TextureRegion(R.c().hud_iconGoLeft);
		textureRegion.flip(true, false);
		TextureRegionDrawable buttonRightUp = new TextureRegionDrawable(textureRegion);
		TextureRegionDrawable buttonRightDown = new TextureRegionDrawable(textureRegion);
		m_buttonRight = new Button(buttonRightUp, buttonRightDown);
		m_buttonRight.setPosition(x1 + entraxe, yHudButton);
		m_buttonRight.setWidth(scaleByHeight(fullHudButton, m_buttonRight.getWidth(), m_buttonRight.getHeight()));
		m_buttonRight.setHeight(fullHudButton);
		// m_buttonRight.addListener(new ButtonChange());
		addActor(m_buttonRight);

		// Button Pause
		TextureRegionDrawable buttonPauseUp = new TextureRegionDrawable(R.c().hud_iconPause);
		TextureRegionDrawable buttonPauseDown = new TextureRegionDrawable(R.c().hud_iconPause);
		m_buttonPause = new Button(buttonPauseUp, buttonPauseDown);
		m_buttonPause.setPosition(MyGdxGame.VIRTUAL_WIDTH / 2 - middleHudButton / 2, yHudButton);
		m_buttonPause.setWidth(scaleByHeight(middleHudButton, m_buttonPause.getWidth(), m_buttonPause.getHeight()));
		m_buttonPause.setHeight(middleHudButton);
		m_buttonPause.addListener(new ButtonChange());
		addActor(m_buttonPause);

		// Button Shoot
		TextureRegionDrawable buttonShootUp = new TextureRegionDrawable(R.c().hud_iconShoot);
		TextureRegionDrawable buttonShootDown = new TextureRegionDrawable(textureRegion);
		m_buttonShoot = new Button(buttonShootUp, buttonShootDown);
		m_buttonShoot.setPosition(x2, yHudButton);
		m_buttonShoot.setWidth(scaleByHeight(fullHudButton, m_buttonShoot.getWidth(), m_buttonShoot.getHeight()));
		m_buttonShoot.setHeight(fullHudButton);
		// m_buttonShoot.addListener(new ButtonChange());
		addActor(m_buttonShoot);

		// Button Jump
		TextureRegionDrawable buttonJumpUp = new TextureRegionDrawable(R.c().hud_iconJump);
		TextureRegionDrawable buttonJumpDown = new TextureRegionDrawable(textureRegion);
		m_buttonJump = new Button(buttonJumpUp, buttonJumpDown);
		m_buttonJump.setPosition(x2 + entraxe, yHudButton);
		m_buttonJump.setWidth(scaleByHeight(fullHudButton, m_buttonJump.getWidth(), m_buttonJump.getHeight()));
		m_buttonJump.setHeight(fullHudButton);
		// m_buttonJump.addListener(new ButtonChange());
		addActor(m_buttonJump);

		SettingsStage.placeButtons(m_buttonLeft, m_buttonRight, m_buttonShoot, m_buttonJump);

		buttonLeft = new Rectangle(m_buttonLeft.getX(), m_buttonLeft.getY(), m_buttonLeft.getWidth(), m_buttonLeft.getHeight());
		buttonRight = new Rectangle(m_buttonRight.getX(), m_buttonRight.getY(), m_buttonRight.getWidth(), m_buttonRight.getHeight());
		buttonShoot = new Rectangle(m_buttonShoot.getX(), m_buttonShoot.getY(), m_buttonShoot.getWidth(), m_buttonShoot.getHeight());
		buttonJump = new Rectangle(m_buttonJump.getX(), m_buttonJump.getY(), m_buttonJump.getWidth(), m_buttonJump.getHeight());
	}

	Vector3	touchPoint	= new Vector3();
	boolean	keyboard	= false;

	@Override
	public void act(float delta)
	{
		if (MyGdxGame.adsController == null)
		{
			keyboard = true;
		} else
		{
			keyboard = false;
		}

		super.act(delta);
		if (!keyboard)
		{
			rightDown = false;
			leftDown = false;
			shoot = false;

			for (int i = 0; i < 4; i++)
			{
				if (Gdx.input.isTouched(i))
				{
					getStage().getCamera().unproject(touchPoint.set(Gdx.input.getX(i), Gdx.input.getY(i), 0));
					if (buttonLeft.contains(touchPoint.x, touchPoint.y))
					{
						gameStage.moveLeft();
						gameStage.walk(true);
						leftDown = true;
					}
					if (buttonRight.contains(touchPoint.x, touchPoint.y))
					{
						gameStage.moveRight();
						gameStage.walk(true);
						rightDown = true;
					}
					if (buttonShoot.contains(touchPoint.x, touchPoint.y))
					{
						gameStage.shoot(true);
						shoot = true;
					}
					if (buttonJump.contains(touchPoint.x, touchPoint.y))
					{
						gameStage.jump();
					}
				}

			}

			if (!leftDown && !rightDown)
			{
				gameStage.walk(false);
			}
			if (!shoot)
			{
				gameStage.shoot(false);
			}
		}

		// Gdx.input.vibrate(milliseconds);
		/*
		 * System.out.println("overLeft " + m_buttonLeft.isOver() + "  ///   " + "overRight " + m_buttonRight.isOver());
		 * 
		 * leftDown = false; rightDown = false;
		 * 
		 * if (m_buttonLeft.isOver()) { gameStage.moveLeft(); gameStage.walk(true); leftDown = true; }
		 * 
		 * if (m_buttonRight.isOver()) { gameStage.moveRight(); gameStage.walk(true); rightDown = true; }
		 * 
		 * if (!leftDown && !rightDown) { gameStage.walk(false); }
		 */

	}

	class ButtonChange extends ClickListener
	{
		// Ecouteur d'un seul bouton

		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
		{
			// System.out.println("down" + pointer);
			if (event.getTarget() == m_buttonLeft)
			{
				gameStage.moveLeft();
				gameStage.walk(true);
				leftDown = true;
			}
			if (event.getTarget() == m_buttonRight)
			{
				gameStage.moveRight();
				gameStage.walk(true);
				rightDown = true;
			}

			if (event.getTarget() == m_buttonPause)
			{
				GameStage.gameState = GameStatesEnum.GAME_PAUSE;
			}
			if (event.getTarget() == m_buttonShoot)
			{
				gameStage.shoot(true);
			}
			if (event.getTarget() == m_buttonJump)
			{
				gameStage.jump();
			}

			return super.touchDown(event, x, y, pointer, button);
		}

		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			// System.out.println("up" + pointer);

			if (event.getTarget() == m_buttonRight)
			{
				gameStage.walk(false);
			}
			if (event.getTarget() == m_buttonLeft)
			{
				gameStage.walk(false);
			}
			if (event.getTarget() == m_buttonShoot)
			{
				gameStage.shoot(false);
			}
			super.touchUp(event, x, y, pointer, button);
		}

		@Override
		public void touchDragged(InputEvent event, float x, float y, int pointer)
		{
			// Si l'on drag est que l'on arrive sur l'un des boutons alors ça doit aussi fonctionner
			// System.out.println("drag" + pointer);

			if (event.getTarget() == m_buttonLeft)
			{
				gameStage.moveLeft();
				gameStage.walk(true);
				// leftDown = true;
			}
			if (event.getTarget() == m_buttonRight)
			{
				gameStage.moveRight();
				gameStage.walk(true);
				// rightDown = true;
			}

			if (event.getTarget() == m_buttonPause)
			{
				GameStage.gameState = GameStatesEnum.GAME_PAUSE;
			}
			if (event.getTarget() == m_buttonShoot)
			{
				gameStage.shoot(true);
			}
			if (event.getTarget() == m_buttonJump)
			{
				gameStage.jump();
			}

			super.touchDragged(event, x, y, pointer);
		}
	}

	public GameStage getGameStage()
	{
		return gameStage;
	}

}
