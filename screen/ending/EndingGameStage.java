package screen.ending;

import ressources.DrawableSprite;
import ressources.R;
import ressources.Ressource;
import screen.MyGdxGame;
import screen.ScreenManager;
import utilities.Methods;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class EndingGameStage extends Stage
{
	private BitmapFont bitmap;
	private Button m_rateButton;
	private ButtonStyle m_buttonStyleRate = new ButtonStyle();

	public EndingGameStage()
	{
		setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));
		Group group = new Group();
		addActor(group);

		DrawableSprite drawableSprite = new DrawableSprite(R.c().ending_gameImage);
		group.addActor(new Ressource(drawableSprite, 0, 0, MyGdxGame.VIRTUAL_HEIGHT));
		group.addListener(new ListennerScreen());

		// Load text
		FreeTypeFontGenerator basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("slkscr.ttf"));
		FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontParameter();
		freeTypeFontParameter.size = 45;
		bitmap = basicFontGenerator.generateFont(freeTypeFontParameter);

		// Rate my app
		TextureRegionDrawable rate = new TextureRegionDrawable(R.c().screens_iconGoToRating);
		m_buttonStyleRate.up = rate;
		// *******Rate Button
		m_rateButton = new Button(m_buttonStyleRate);
		m_rateButton.setPosition(400, 50); // place a sa position final - yRetreat
		m_rateButton.setSize(Methods.scaleByHeight(160, m_rateButton.getWidth(), m_rateButton.getHeight()), 160);
		m_rateButton.addListener(new ButtonRatingListenner());
		addActor(m_rateButton);

	}

	String a = "Congratulation";
	String b = "YOU HAVE REACHED THE END OF THE GAME";
	String c = "Please, evaluate application.";
	String d = "Good comments help me staying ";
	String e = "motivated for new levels update.";
	String i = "\n";
	String global = a + i + b + i + i + c + i + i + d + i + e;

	@Override
	public void draw()
	{
		super.draw();
		getBatch().begin();
		bitmap.drawMultiLine(getBatch(), global, 900, 700, 0, HAlignment.CENTER);
		getBatch().end();
		m_rateButton.setPosition(MyGdxGame.VIRTUAL_WIDTH / 2 - m_rateButton.getWidth() / 2, 150);
	}

	class ListennerScreen extends ClickListener
	{

		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			ScreenManager.getInstance().show(ScreenEnum.LEVEL);
			super.touchUp(event, x, y, pointer, button);
		}

	}

	class ButtonRatingListenner extends ClickListener
	{
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.oasix.crazyshooter.android");
		}
	}

}