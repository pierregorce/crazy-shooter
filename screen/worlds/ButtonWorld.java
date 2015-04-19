package screen.worlds;

import files.Files;
import globals.Worlds;
import ressources.ButtonRessource;
import ressources.R;
import screen.ScreenManager;
import screen.level.Levels;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ButtonWorld extends ButtonRessource
{
	public Worlds	world;

	private Label	titleLabel;
	private Label	wavesLabel;
	private Label	bossLabel;
	private Label	plateformLabel;
	private Label	completedLabel;
	private Label	starsLabel;

	private Image	starsImage;
	private Image	worldImage;

	public ButtonWorld(Group group, Worlds world)
	{
		super(group);
		this.world = world;

		LabelStyle big = new LabelStyle(R.c().EarlyGameBoyFont_32, Color.WHITE);
		LabelStyle medium = new LabelStyle(R.c().EarlyGameBoyFont_26, Color.WHITE);
		LabelStyle small = new LabelStyle(R.c().EarlyGameBoyFont_22, Color.valueOf("fbd00f"));

		titleLabel = new Label("" + world.name, medium);
		wavesLabel = new Label("" + world.startWorld + " - " + world.endWorld, big);
		bossLabel = new Label("" + world.finalBoss, big);
		plateformLabel = new Label("" + world.plateformType, big);
		completedLabel = new Label("", small);
		starsLabel = new Label("", small);

		addActor(titleLabel);
		addActor(wavesLabel);
		addActor(bossLabel);
		addActor(plateformLabel);
		addActor(completedLabel);
		addActor(starsLabel);

		float factor = 2.66f;
		starsImage = new Image(R.c().world_star);
		starsImage.setSize(R.c().world_star.getRegionWidth() * factor, R.c().world_star.getRegionHeight() * factor);
		addActor(starsImage);

		worldImage = new Image(world.level);
		worldImage.setSize(world.level.getRegionWidth() * factor, world.level.getRegionHeight() * factor);
		addActor(worldImage);

		if (world.isCompleted())
		{
			putUpDrawable(R.c().world_frame_completed);
		}
		putUpDrawable(R.c().world_frame_completed);

		addListener(new ButtonScreenAction());
	}

	private void positionning()
	{
		titleLabel.setPosition(195, 295);
		titleLabel.setWrap(true);
		titleLabel.setAlignment(Align.center);
		titleLabel.setDebug(false);
		titleLabel.setWidth(385);
		wavesLabel.setPosition(250, 200);
		bossLabel.setPosition(250, 130);
		plateformLabel.setPosition(250, 61);
		completedLabel.setPosition(240, 26);
		starsLabel.setPosition(490, 26);
		starsImage.setPosition(510, 13);
		worldImage.setPosition(22, 40);
	}

	@Override
	public void act(float delta)
	{
		positionning();
		super.act(delta);
		if (world.isCompleted())
		{
			completedLabel.setText("Completed");
			starsLabel.setText("");
			starsImage.setVisible(false);
		} else
		{
			completedLabel.setText("On complete");
			starsLabel.setText("" + world.onComplete);
			starsImage.setVisible(true);
		}
	}

	class ButtonScreenAction extends ClickListener
	{
		@Override
		public void clicked(InputEvent event, float x, float y)
		{
			super.clicked(event, x, y);

			Levels levels = Files.levelDataRead();
			ScreenManager.getInstance().setLevelSelected(levels.level[world.startWorld]);
			ScreenManager.getInstance().show(ScreenEnum.LEVEL);
			Gdx.input.setInputProcessor(null); // empeche des actions durant le changement de screen
		}
	}
}
