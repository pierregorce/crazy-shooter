package screen.cheat;

import ressources.R;
import screen.MyGdxGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class CheatingStage extends Stage
{

	private TextField	textField;
	private Image		backgroundImage;

	public CheatingStage()
	{
		setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));

		float factor = 2.66f;
		backgroundImage = new Image(R.c().upgrade_background);
		backgroundImage.setSize(backgroundImage.getWidth() * factor, backgroundImage.getHeight() * factor);
		addActor(backgroundImage);

		TextFieldStyle s = new TextFieldStyle();
		s.font = R.c().getEarlyGameBoyFont(40);
		s.fontColor = Color.WHITE;
		s.cursor = new TextureRegionDrawable(R.c().primitive_square);
		// s.selection = new TextureRegionDrawable(R.c().primitive_square);
		s.background = new TextureRegionDrawable(R.c().bossBarFull);

		// Enter Code Please
		textField = new TextField("a", s);
		// addActor(textField);
		// Gdx.input.setOnscreenKeyboardVisible(true);
		Table t = new Table();
		t.add(textField);
		addActor(t);
		t.setPosition(500, 500);
		t.setWidth(500);
		textField.setWidth(500);
		t.debug();
		textField.debug();

		Button b = new Button(new TextureRegionDrawable(R.c().upgrade_button_train_comp_active));

		t.add(b);

	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		// TODO Auto-generated method stub
		// textField.setPosition(500, 500);

		textField.setWidth(700);

		// textField.setDebug(true);
	}
}
