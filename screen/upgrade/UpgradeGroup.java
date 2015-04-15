package screen.upgrade;

import files.Files;
import game.sound.MusicManager;
import globals.PlayerStats;
import globals.Upgrades;
import globals.Weapons;

import java.text.DecimalFormat;

import ressources.ButtonRessource;
import ressources.R;
import ressources.S;
import screen.MyGdxGame;
import utilities.ButtonScreen;
import utilities.Methods;
import utilities.enumerations.ScreenEnum;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.oasix.crazyshooter.GameStage;

public class UpgradeGroup extends Group
{
	private float			screenHeight	= 405;
	private float			factor			= 2.66f;
	private DecimalFormat	df				= new DecimalFormat("0.0");

	private Image			image2;
	private Image			image3;
	private Image			image4;
	private ButtonScreen	buttonScreen;

	public UpgradeGroup()
	{
		// Preferences prefs = Gdx.app.getPreferences(PREFERENCE_FILE);

		Image image = new Image(R.c().upgrade_background);
		image.setSize(image.getWidth() * factor, image.getHeight() * factor);
		// image.setPosition(174 * factor, (screenHeight - 145) * factor);
		addActor(image);

		image3 = new Image(R.c().upgrade_profil_panel);
		image3.setSize(image3.getWidth() * factor, image3.getHeight() * factor);
		addActor(image3);

		image4 = new Image(R.c().upgrade_comp_panel);
		image4.setSize(image4.getWidth() * factor, image4.getHeight() * factor);
		addActor(image4);

		image2 = new Image(R.c().upgrade_weapons_panel);
		image2.setSize(image2.getWidth() * factor, image2.getHeight() * factor);
		addActor(image2);

		buttonScreen = new ButtonScreen(this, ScreenEnum.LEVEL);
		buttonScreen.putStyle(R.c().screens_iconGoToMenuScreen);
		buttonScreen.putEffect();

		initialisationWeapons();
		initialisationProfil();
		initialisationCompetences();

	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		image2.setPosition(7 * factor, (screenHeight - 386) * factor);
		image3.setPosition(308 * factor, (screenHeight - 170) * factor);
		image4.setPosition(334 * factor, (screenHeight - 397) * factor);

		buttonScreen.setPosition(675 * factor, (screenHeight - 25) * factor);

		weaponTitle.setPosition(31 * factor, (screenHeight - 270) * factor);
		weaponDamage.setPosition(209 * factor, (screenHeight - 296) * factor);
		weaponRange.setPosition(209 * factor, (screenHeight - 322) * factor);
		weaponAttackSpeed.setPosition(209 * factor, (screenHeight - 349) * factor);

		float w2 = weaponCost.getTextBounds().width;
		weaponCost.setPosition(160 + 42 * factor - w2 / 2, (screenHeight - 346) * factor);

		weaponsCoinImage.setPosition(140 * factor, (screenHeight - 354) * factor);

		float width = 400;
		float w = width - weaponImage.getWidth();
		weaponImage.setPosition(50 + w / 2, 200);

		profilGolds.setPosition(395 * factor, (screenHeight - 62) * factor);
		profilStars.setPosition(400 * factor, (screenHeight - 124) * factor);
		profilLevel.setPosition(589 * factor, (screenHeight - 17) * factor);
		profilXp.setPosition(650 * factor, (screenHeight - 141) * factor);
		profilHealth.setPosition(582 * factor, (screenHeight - 47) * factor);
		profilCriticalChance.setPosition(582 * factor, (screenHeight - 72) * factor);
		profilHealthPack.setPosition(582 * factor, (screenHeight - 99) * factor);
		profilDamage.setPosition(582 * factor, (screenHeight - 122) * factor);
		profilAttackSpeed.setPosition(582 * factor, (screenHeight - 144) * factor);
		profilAttackSpeedSubtitle.setPosition(582 * factor, (screenHeight - 153) * factor);
		profilStarsForNextLevel.setPosition(435 * factor, (screenHeight - 147) * factor);

		float maxXp = 125;
		float currentXp = maxXp * PlayerStats.currentXP / PlayerStats.getRequiertXP();
		profilLevelBarImage.setWidth(currentXp);
		profilLevelBarImage.setPosition(650 * factor, (screenHeight - 131) * factor);

		compIconImage.setPosition(405 * factor, (screenHeight - 281) * factor);
		float offset = 16;
		comptenceIcon.setPosition(compIconImage.getX() + offset, compIconImage.getY() + offset);
		competenceTitle.setPosition(470 * factor, (screenHeight - 240) * factor);
		competenceDescription.setPosition(470 * factor, (screenHeight - 250) * factor);

	}

	// ----------------------------------------------------------COMPETENCE
	// ----------------------------------------------------------COMPETENCE
	// ----------------------------------------------------------COMPETENCE
	// ----------------------------------------------------------COMPETENCE
	// ----------------------------------------------------------COMPETENCE

	private Label			competenceTitle;
	private Label			competenceDescription;
	private Image			comptenceIcon;
	private ButtonRessource	competenceButtonTrain;
	private ButtonRessource	competenceButtonReset;
	private TextureRegion[]	competenceIcons;
	private Image			compIconImage;
	private Upgrades		selectedUpgrade	= Upgrades.values()[0];

	public void initialisationCompetences()
	{
		competenceIcons = new TextureRegion[5];
		competenceIcons[0] = R.c().upgrade_icon_comp_life;
		competenceIcons[1] = R.c().upgrade_icon_comp_jump;
		competenceIcons[2] = R.c().upgrade_icon_comp_speed;
		competenceIcons[3] = R.c().upgrade_icon_comp_heath;
		competenceIcons[4] = R.c().upgrade_icon_comp_precision;

		ButtonGroup buttonGroup = new ButtonGroup();

		for (int i = 0; i < 5; i++)
		{
			ButtonComp buttonComp = new ButtonComp(this);
			buttonComp.putDrawable(R.c().upgrade_button_comp_frame, R.c().upgrade_button_comp_frame_selected);
			buttonComp.putIconImage(competenceIcons[i], factor);
			buttonComp.putStarsCost(Upgrades.values()[i]);
			buttonComp.putPosition(386 + i * 65, 385, factor);
			buttonComp.addListener(new CompetenceButtonAction());
			buttonGroup.add(buttonComp);
			if (i == 0)
			{
				buttonComp.setChecked(true);
			}
		}

		LabelStyle labelStyleTitle = new LabelStyle(R.c().getEarlyGameBoyFont((int) (12 * factor)), Color.WHITE);
		LabelStyle labelStyleDescription = new LabelStyle(R.c().getEarlyGameBoyFont((int) (8 * factor)), Color.WHITE);

		competenceTitle = new Label(selectedUpgrade.title + "", labelStyleTitle);
		addActor(competenceTitle);
		competenceDescription = new Label(selectedUpgrade.stringDescription + "", labelStyleDescription);
		addActor(competenceDescription);

		compIconImage = new Image();
		compIconImage.setDrawable(new TextureRegionDrawable(R.c().upgrade_button_comp_simple_frame));
		compIconImage.setSize(R.c().upgrade_button_comp_simple_frame.getRegionWidth() * factor, R.c().upgrade_button_comp_simple_frame.getRegionHeight() * factor);
		addActor(compIconImage);

		comptenceIcon = new Image();
		comptenceIcon.setDrawable(new TextureRegionDrawable(competenceIcons[0]));
		comptenceIcon.setSize(competenceIcons[0].getRegionWidth() * factor, competenceIcons[0].getRegionHeight() * factor);
		addActor(comptenceIcon);

		competenceButtonTrain = new ButtonRessource(this);
		competenceButtonTrain.putUpDisableDrawable(R.c().upgrade_button_train_comp_active, R.c().upgrade_button_train_comp_lock);
		competenceButtonTrain.putPosition(300, 200, factor);
		competenceButtonTrain.addListener(new CompetenceButtonEvent());

		competenceButtonReset = new ButtonRessource(this);
		competenceButtonReset.putUpDrawable(R.c().upgrade_button_reset_comp);
		competenceButtonReset.putPosition(400, 200, factor);
		competenceButtonReset.addListener(new CompetenceButtonEvent());

		competenceButtonReset.putOnlyPosition(597, 215, factor);
		competenceButtonTrain.putOnlyPosition(474, 307, factor);

	}

	class CompetenceButtonAction extends ClickListener
	{
		@Override
		public void clicked(InputEvent event, float x, float y)
		{
			super.clicked(event, x, y);
			ButtonComp b = (ButtonComp) event.getListenerActor();

			if (PlayerStats.getTalentPointsRemaining() >= b.upgrades.cost)
			{
				competenceButtonTrain.setDisabled(false);
			} else
			{
				competenceButtonTrain.setDisabled(true);
			}

			competenceTitle.setText(b.upgrades.title);
			competenceDescription.setText(b.upgrades.stringDescription);
			comptenceIcon.setDrawable(new TextureRegionDrawable(competenceIcons[b.upgrades.ordinal()]));
			selectedUpgrade = b.upgrades;
		}
	}

	class CompetenceButtonEvent extends ClickListener
	{
		@Override
		public void clicked(InputEvent event, float x, float y)
		{
			super.clicked(event, x, y);
			if (competenceButtonTrain == event.getListenerActor())
			{
				if (PlayerStats.getTalentPointsRemaining() >= selectedUpgrade.cost)
				{
					selectedUpgrade.point += selectedUpgrade.cost;
				}
			}

			if (competenceButtonReset == event.getListenerActor())
			{
				Upgrades.reset();
			}

			profilUpdateLabels();
			if (PlayerStats.getTalentPointsRemaining() >= selectedUpgrade.cost)
			{
				competenceButtonTrain.setDisabled(false);
			} else
			{
				competenceButtonTrain.setDisabled(true);
			}
			Files.playerUpgradeWrite();
		}
	}

	// ----------------------------------------------------------PROFIL
	// ----------------------------------------------------------PROFIL
	// ----------------------------------------------------------PROFIL
	// ----------------------------------------------------------PROFIL
	// ----------------------------------------------------------PROFIL

	private Label	profilGolds;
	private Label	profilStars;
	private Label	profilLevel;
	private Label	profilXp;
	private Label	profilHealth;
	private Label	profilCriticalChance;
	private Label	profilHealthPack;
	private Label	profilDamage;
	private Label	profilAttackSpeed;
	private Label	profilAttackSpeedSubtitle;
	private Label	profilStarsForNextLevel;
	private Image	profilLevelBarImage;

	public void initialisationProfil()
	{
		LabelStyle labelStyleFat = new LabelStyle(R.c().getEarlyGameBoyFont((int) (14 * factor)), Color.valueOf("f9f4a2"));
		LabelStyle labelStyleSmall = new LabelStyle(R.c().getEarlyGameBoyFont((int) (10 * factor)), Color.valueOf("f9f4a2"));
		LabelStyle labelStyleMedium = new LabelStyle(R.c().getEarlyGameBoyFont((int) (12 * factor)), Color.WHITE);
		LabelStyle labelStyleWendy10 = new LabelStyle(R.c().getWendyFont((int) (10 * factor)), Color.WHITE);

		profilGolds = new Label("", labelStyleFat);
		profilStars = new Label("", labelStyleFat);
		profilLevel = new Label("", labelStyleFat);
		profilXp = new Label("", labelStyleWendy10);
		profilHealth = new Label("", labelStyleMedium);
		profilCriticalChance = new Label("", labelStyleMedium);
		profilHealthPack = new Label("", labelStyleMedium);
		profilDamage = new Label("", labelStyleMedium);
		profilAttackSpeed = new Label("", labelStyleMedium);
		profilAttackSpeedSubtitle = new Label("", labelStyleWendy10);
		profilStarsForNextLevel = new Label("", labelStyleSmall);

		addActor(profilGolds);
		addActor(profilStars);
		addActor(profilLevel);
		addActor(profilXp);
		addActor(profilHealth);
		addActor(profilCriticalChance);
		addActor(profilHealthPack);
		addActor(profilDamage);
		addActor(profilAttackSpeed);
		addActor(profilAttackSpeedSubtitle);
		addActor(profilStarsForNextLevel);

		profilUpdateLabels();

		profilLevelBarImage = new Image();
		profilLevelBarImage.setDrawable(new TextureRegionDrawable(R.c().primitive_square));
		profilLevelBarImage.setColor(Color.valueOf("3daf73"));
		profilLevelBarImage.setSize(1 * factor, 5 * factor);
		addActor(profilLevelBarImage);

	}

	public void profilUpdateLabels()
	{
		profilGolds.setText("" + PlayerStats.ressource);
		profilStars.setText("" + PlayerStats.getTalentPointsRemaining());
		profilLevel.setText("" + PlayerStats.level);
		profilXp.setText("" + PlayerStats.currentXP + "/" + PlayerStats.getRequiertXP());
		profilHealth.setText("" + PlayerStats.getMaxLife());
		profilCriticalChance.setText("" + df.format(PlayerStats.getCriticalChance()) + "%");
		profilHealthPack.setText("" + PlayerStats.getLifeBoxHp() + "HP");
		profilDamage.setText("+" + PlayerStats.getDamage() + "");
		profilAttackSpeed.setText(df.format(PlayerStats.getAttackSpeed()) + "");
		profilAttackSpeedSubtitle.setText("zqdqzd");
		profilStarsForNextLevel.setText("" + 1);
	}

	// ----------------------------------------------------------WEAPONS
	// ----------------------------------------------------------WEAPONS
	// ----------------------------------------------------------WEAPONS
	// ----------------------------------------------------------WEAPONS
	// ----------------------------------------------------------WEAPONS

	private ButtonRessource	buy;
	private ButtonRessource	equip;
	private Weapons			selectedWeapon;
	private Label			weaponTitle;
	private Label			weaponDamage;
	private Label			weaponRange;
	private Label			weaponAttackSpeed;
	private Label			weaponCost;
	private Image			weaponImage;
	private Image			weaponsCoinImage;

	public void initialisationWeapons()
	{
		selectedWeapon = Weapons.values()[0]; // TODO PLAyaer equiped

		ButtonGroup buttonGroup = new ButtonGroup();
		int k = 0;
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				if (k <= 11)
				{
					ButtonWeapon buttonWeapon = new ButtonWeapon(this);
					buttonWeapon.putWeaponImage(R.c().player_weapons_store[k], 5);
					buttonWeapon.putWeapon(Weapons.values()[k]);
					buttonWeapon.putPosition(33 + i * 93, 90 + j * 53, factor);
					buttonWeapon.addListener(new WeaponButton());
					buttonGroup.add(buttonWeapon);
					k++;
				}
			}
		}

		buy = new ButtonRessource(this);
		equip = new ButtonRessource(this);
		buy.putUpDisableDrawable(R.c().upgrade_button_buy_dollars_active, R.c().upgrade_button_buy_dollars_lock).putPosition(60, 396, factor);
		equip.putUpDisableDrawable(R.c().upgrade_button_equip_active, R.c().upgrade_button_equip_lock).putPosition(196, 396, factor);
		buy.addListener(new WeaponsUserActions());
		equip.addListener(new WeaponsUserActions());

		LabelStyle labelStyle = new LabelStyle(R.c().getEarlyGameBoyFont((int) (12 * factor)), Color.WHITE);
		LabelStyle labelStyle2 = new LabelStyle(R.c().getEarlyGameBoyFont((int) (12 * factor)), Color.valueOf("fbd00f"));

		weaponTitle = new Label("", labelStyle);
		weaponDamage = new Label("", labelStyle);
		weaponRange = new Label("", labelStyle);
		weaponAttackSpeed = new Label("", labelStyle);
		weaponCost = new Label("", labelStyle2);
		weaponsCoinImage = new Image();

		weaponsCoinImage = new Image();
		weaponsCoinImage.setDrawable(new TextureRegionDrawable(R.c().upgrade_weapons_icon_coin));
		weaponsCoinImage.setSize(R.c().upgrade_weapons_icon_coin.getRegionWidth() * factor, R.c().upgrade_weapons_icon_coin.getRegionHeight() * factor);

		weaponImage = new Image();
		selectWeapon(Weapons.values()[0]);

		addActor(weaponImage);
		addActor(weaponsCoinImage);
		addActor(weaponTitle);
		addActor(weaponDamage);
		addActor(weaponRange);
		addActor(weaponAttackSpeed);
		addActor(weaponCost);

	}

	public void selectWeapon(Weapons weapons)
	{
		if (weapons.cost <= PlayerStats.ressource)
		{
			buy.setDisabled(true);
		} else
		{
			buy.setDisabled(false);
		}

		if (weapons.isBuy)
		{
			buy.setDisabled(true);
			equip.setDisabled(false);
		} else
		{
			buy.setDisabled(false);
			equip.setDisabled(true);
		}

		weaponTitle.setText(weapons.name);
		if (weapons.projectileType.quantityPerShoot > 1)
		{
			weaponDamage.setText(weapons.projectileType.quantityPerShoot + "x" + weapons.projectileType.damage + "/HIT");
		} else
		{
			weaponDamage.setText(weapons.projectileType.damage + "/HIT");
		}

		weaponRange.setText(weapons.projectileType.lenghtAlive + " M");

		weaponAttackSpeed.setText(weapons.fireRate + " DPS");
		if (weapons.cost == 0)
		{
			weaponCost.setText("FREE");
		} else
		{

			weaponCost.setText(weapons.cost + "");
		}
	}

	class WeaponButton extends ClickListener
	{
		@Override
		public void clicked(InputEvent event, float x, float y)
		{
			super.clicked(event, x, y);
			ButtonWeapon weaponsButton = (ButtonWeapon) event.getTarget();
			selectedWeapon = weaponsButton.weapons;
			selectWeapon(weaponsButton.weapons);
			weaponImage.setDrawable(new TextureRegionDrawable(weaponsButton.weapon_textureRegion));
			weaponImage.setSize(weaponsButton.weapon_textureRegion.getRegionWidth() * 8, weaponsButton.weapon_textureRegion.getRegionHeight() * 8);
		}
	}

	class WeaponsUserActions extends ClickListener
	{

		@Override
		public void clicked(InputEvent event, float x, float y)
		{
			super.clicked(event, x, y);

			if (event.getListenerActor() == buy)
			{
				if (PlayerStats.ressource >= selectedWeapon.cost)
				{
					addActor(new PopupBuy(selectedWeapon));
				}

			}
			if (event.getListenerActor() == equip)
			{
				if (selectedWeapon.isBuy)
				{
					S.selectWeaponClickSound.play(MusicManager.sfxVolume);
					equipedMessage();
					// weaponsButton.setChecked(true);
					PlayerStats.weaponsType = selectedWeapon.ordinal();
					Files.playerDataWrite();
				}
			}
		}
	}

	// ----------------------------------------------------------POPUP
	// ----------------------------------------------------------POPUP
	// ----------------------------------------------------------POPUP
	// ----------------------------------------------------------POPUP

	private Label	m_labelEquiped;

	private void equipedMessage()
	{
		int y = 700;
		BitmapFont bitmapFont = R.c().fontTypeViolet;
		LabelStyle labelStyle = new LabelStyle(bitmapFont, null);
		m_labelEquiped = new Label("Equiped!", labelStyle);
		m_labelEquiped.setFontScale(10f);
		m_labelEquiped.setPosition(MyGdxGame.VIRTUAL_WIDTH / 2 - m_labelEquiped.getTextBounds().width / 2, y);

		SequenceAction a = new SequenceAction(Actions.delay(0.5f), Actions.removeActor());

		m_labelEquiped.addAction(a);
		addActor(m_labelEquiped);

		// CAMERA SHAKE
		UpgradeStage.cameraShake(GameStage.SMALL_SHAKE);
	}

	class PopupBuy extends Group
	{
		public PopupBuy(final Weapons weapons)
		{
			// ADD PANEL
			WindowStyle windowStyle = new WindowStyle(R.c().fontTypeBasicThin, Color.RED, new TextureRegionDrawable(R.c().upgrade_popup_panel));
			final Dialog dialog = new Dialog("", windowStyle);

			float initialWidth = R.c().upgrade_popup_panel.getRegionWidth();
			float initialHeight = R.c().upgrade_popup_panel.getRegionHeight();

			dialog.setSize(800, Methods.scaleByWidth(800, initialWidth, initialHeight));
			dialog.setPosition(500, 500);

			// ADD Label

			// BitmapFont bitmapFont = R.c().fontTypeLightGreen;
			// LabelStyle labelStyle = new LabelStyle(bitmapFont, null);
			// Font A

			// Select the first button selectable
			// dialog.row();

			// ADD BUTTON
			ButtonStyle buttonStyleOk = new ButtonStyle();
			TextureRegionDrawable okImage = new TextureRegionDrawable(R.c().upgrade_button_yes);
			buttonStyleOk.up = okImage;
			Button ok = new Button(buttonStyleOk);

			ok.addListener(new ClickListener()
			{
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button)
				{
					super.touchUp(event, x, y, pointer, button);
					System.out.println("click yes");

					S.buyWeaponClickSound.play(MusicManager.sfxVolume);
					// weaponsButton.setDisabled(false);
					// weaponsButton.weapons.setBuy(true);
					// weaponsButton.setChecked(true);

					weapons.isBuy = true;
					PlayerStats.weaponsType = weapons.ordinal();
					PlayerStats.ressource -= weapons.cost;

					// Weapons.values()[weapons.ordinal()].setBuy(true);

					// weaponsButton.setBuy(weaponsButton.weapons.ordinal());

					Files.playerWeaponsWrite();
					Files.playerDataWrite();
					profilUpdateLabels();
					selectWeapon(weapons);

					// buttonGroup.add(weaponsButton);
					equipedMessage();

					dialog.hide(Actions.fadeOut(0.05f));

				}

			});

			float initialButtonWidth = R.c().upgrade_button_yes.getRegionWidth();
			float initialButtonHeight = R.c().upgrade_button_yes.getRegionHeight();
			dialog.defaults().width(200);
			dialog.defaults().height(Methods.scaleByWidth(200, initialButtonWidth, initialButtonHeight));
			dialog.defaults().left();
			dialog.add(ok);
			// dialog.row();

			ButtonStyle buttonStyleNo = new ButtonStyle();
			TextureRegionDrawable noImage = new TextureRegionDrawable(R.c().upgrade_button_no);
			buttonStyleNo.up = noImage;
			Button no = new Button(buttonStyleNo);

			no.addListener(new ClickListener()
			{
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button)
				{
					super.touchUp(event, x, y, pointer, button);
					System.out.println("click no");
					dialog.hide(Actions.fadeOut(0.05f));
				}

			});

			dialog.add(no);
			// dialog.row();

			// dialog.debugAll();
			addActor(dialog);
		}

	}

}
