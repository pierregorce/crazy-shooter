package screen.cheat;

import files.Files;
import globals.Cheat;
import globals.PlayerStats;
import ressources.ButtonRessource;
import ressources.R;
import screen.MyGdxGame;
import utilities.ButtonScreen;
import utilities.Methods;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class CheatingStage extends Stage
{

	private TextField		textField;

	// Idee un cheat code pourrait n'être valable qu'une fois...
	private String			home	= "Type your cheat code here :";
	private String			success	= "Cheat code correct, effect done";
	private String			error	= "Cheat code incorrect, try again";

	private Image			backgroundImage;
	private TextureRegion	tapPanel;
	private ButtonRessource	buttonCheck;
	private ButtonScreen	buttonGoBack;
	private Label			labelHeader;
	private Label			labelResult;

	public CheatingStage()
	{
		setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));

		float factor = 2.66f;
		backgroundImage = new Image(new TextureRegion(new Texture(Gdx.files.internal("images/options/option-bg.png"))));
		backgroundImage.setSize(backgroundImage.getWidth() * factor, backgroundImage.getHeight() * factor);
		addActor(backgroundImage);

		buttonCheck = new ButtonRessource(this);
		buttonCheck.putUpDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/cheat/cheat-button-check.png")))).putSize(factor);
		buttonCheck.addListener(new ButtonCheat());

		buttonGoBack = new ButtonScreen(this, ScreenEnum.LOADING);
		buttonGoBack.putStyle(new TextureRegion(new Texture(Gdx.files.internal("images/cheat/cheat-button-goBack.png"))));
		buttonGoBack.putSize();
		buttonGoBack.addListener(new ButtonCheat());

		TextFieldStyle s = new TextFieldStyle();
		s.font = R.c().EarlyGameBoyFont_40;
		s.fontColor = Color.WHITE;
		s.cursor = new TextureRegionDrawable(R.c().primitive_square);
		tapPanel = new TextureRegion(new Texture(Gdx.files.internal("images/cheat/cheat-tap-panel.png")));
		s.background = new TextureRegionDrawable(tapPanel);
		s.background.setLeftWidth(30);
		s.background.setRightWidth(100);
		textField = new TextField("", s);

		addActor(textField);

		LabelStyle l = new LabelStyle(R.c().EarlyGameBoyFont_40, Color.valueOf("f9f4a2"));
		LabelStyle l2 = new LabelStyle(R.c().EarlyGameBoyFont_32, Color.valueOf("f9f4a2"));
		labelHeader = new Label(home, l);
		labelResult = new Label("", l2);
		addActor(labelHeader);
		addActor(labelResult);

		// for effect
		buttonCheck.setPosition(1250, 755);
		buttonGoBack.setPosition(1600, 270);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		textField.setBlinkTime(0.2f);

		textField.setPosition(400, 750);
		textField.setWidth(800);
		textField.setHeight(Methods.scaleByWidth(800, tapPanel.getRegionWidth(), tapPanel.getRegionHeight()));

		labelHeader.setPosition(400, 860);
		labelResult.setPosition(420, 700);

	}

	class ButtonCheat extends ClickListener
	{
		@Override
		public void clicked(InputEvent event, float x, float y)
		{
			super.clicked(event, x, y);
			labelResult.setText(error);
			if (textField.getText().equals(Cheat.ADD_5000_GOLD))
			{
				PlayerStats.ressource += 5000;
				Files.playerDataWrite();
				textField.setText("");
				labelResult.setText(success);
			}
			if (textField.getText().equals(Cheat.ADD_1_LEVEL))
			{
				PlayerStats.level += 1;
				Files.playerDataWrite();
				textField.setText("");
				labelResult.setText(success);
			}
			if (textField.getText().equals(Cheat.FOR_TEST_PURPOSE))
			{
				PlayerStats.ressource += 80000;
				PlayerStats.level += 50;
				Files.playerDataWrite();
				textField.setText("");
				labelResult.setText(success);
			}

			Gdx.input.setOnscreenKeyboardVisible(false);
		}
	}
}
