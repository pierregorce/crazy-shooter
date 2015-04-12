package screen.upgrade;

import globals.Upgrades;
import ressources.ButtonRessource;
import ressources.R;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonComp extends ButtonRessource
{

	private TextureRegion	icon_textureRegion;

	private TextureRegion	starsCost_textureRegion	= R.c().upgrade_button_comp_cost;
	private Label			starsCost;
	private Label			points;

	private float			factor;

	public Upgrades			upgrades;

	public ButtonComp(Group group)
	{
		super(group);
	}

	public ButtonRessource putStarsCost(Upgrades upgrades)
	{
		this.upgrades = upgrades;
		LabelStyle labelStyle = new LabelStyle(R.c().getEarlyGameBoyFont((int) (8 * factor)), Color.valueOf("fbd00f"));
		this.starsCost = new Label(upgrades.cost + "", labelStyle);
		getParent().addActor(starsCost);

		LabelStyle labelStyle2 = new LabelStyle(R.c().getEarlyGameBoyFont((int) (14 * factor)), Color.valueOf("f9f4a2"));
		points = new Label("", labelStyle2);
		getParent().addActor(points);
		return this;
	}

	public ButtonRessource putIconImage(TextureRegion textureRegion, float factor)
	{
		this.icon_textureRegion = textureRegion;
		this.factor = factor;
		return this;
	}

	public ButtonRessource putDrawable(TextureRegion up, TextureRegion checked)
	{
		ButtonStyle b = new ButtonStyle();
		b.up = new TextureRegionDrawable(up);
		b.checked = new TextureRegionDrawable(checked);
		setStyle(b);
		setSize(getPrefWidth(), getPrefHeight());
		return this;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		if (icon_textureRegion != null)
		{
			batch.draw(icon_textureRegion, getX() + 15, getY() + 85, icon_textureRegion.getRegionWidth() * factor, icon_textureRegion.getRegionHeight() * factor);
			batch.draw(starsCost_textureRegion, getX() + 65, getY() + 55, starsCost_textureRegion.getRegionWidth() * factor, starsCost_textureRegion.getRegionHeight() * factor);
		}

		starsCost.setAlignment(Align.center);
		starsCost.setPosition(getX() + 110, getY() + 68);
		starsCost.setText("" + upgrades.cost);
		points.setPosition(getX() + 75 - points.getTextBounds().width / 2, getY() + 35);
		points.setText(upgrades.point + "");

	}

}
