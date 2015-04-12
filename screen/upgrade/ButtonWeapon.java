package screen.upgrade;

import globals.PlayerStats;
import globals.Weapons;
import ressources.ButtonRessource;
import ressources.R;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonWeapon extends ButtonRessource
{

	public boolean			equiped	= false;

	public TextureRegion	weapon_textureRegion;
	private float			factor;
	private Label			weapon_gold_cost;

	public Weapons			weapons;

	public ButtonStyle		disableWeapon;
	public ButtonStyle		activeWeapon;
	public ButtonStyle		equipedWeapon;

	public ButtonWeapon(Group group)
	{
		super(group);

		activeWeapon = new ButtonStyle();
		activeWeapon.up = new TextureRegionDrawable(R.c().upgrade_weapons_passive);
		activeWeapon.checked = new TextureRegionDrawable(R.c().upgrade_weapons_selected);

		equipedWeapon = new ButtonStyle();
		equipedWeapon.up = new TextureRegionDrawable(R.c().upgrade_weapons_equiped);
		equipedWeapon.checked = new TextureRegionDrawable(R.c().upgrade_weapons_equiped);

		disableWeapon = new ButtonStyle();
		disableWeapon.up = new TextureRegionDrawable(R.c().upgrade_weapons_lock);
		disableWeapon.checked = new TextureRegionDrawable(R.c().upgrade_weapons_lock_selected);

		setStyle(disableWeapon);
		setSize(getPrefWidth(), getPrefHeight());

	}

	public ButtonRessource putWeapon(Weapons weapons)
	{
		this.weapons = weapons;
		LabelStyle labelStyle = new LabelStyle(R.c().getEarlyGameBoyFont((int) (5 * factor)), Color.valueOf("fbd00f"));
		this.weapon_gold_cost = new Label(weapons.cost + "", labelStyle);
		if (weapons.cost == 0)
		{
			// weapon_gold_cost.setText("FREE");
		}
		getParent().addActor(weapon_gold_cost);
		return this;
	}

	public ButtonRessource putWeaponImage(TextureRegion textureRegion, float factor)
	{
		this.weapon_textureRegion = textureRegion;
		this.factor = factor;
		return this;
	}

	public ButtonRessource putStyle()
	{
		if (weapons.isBuy)
		{
			setStyle(activeWeapon);
		} else
		{
			setStyle(disableWeapon);
		}

		if (PlayerStats.weaponsType == weapons.ordinal())
		{
			setStyle(equipedWeapon);
		}
		setSize(getWidth(), getHeight());
		return this;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		if (weapon_textureRegion != null)
		{
			float width = getWidth();
			float w = width - weapon_textureRegion.getRegionWidth() * factor;

			float height = getHeight();
			float h = height - weapon_textureRegion.getRegionHeight() * factor;

			batch.draw(weapon_textureRegion, getX() + w / 2, getY() + h / 2 + 10, weapon_textureRegion.getRegionWidth() * factor, weapon_textureRegion.getRegionHeight() * factor);
		}
		if (weapon_gold_cost != null)
		{
			weapon_gold_cost.setPosition(getX() + 190 - weapon_gold_cost.getTextBounds().width, getY() + 15);
		}

		putStyle();

	}

}
