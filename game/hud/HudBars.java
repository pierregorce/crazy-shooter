package game.hud;

import globals.PlayerStats;
import ressources.DrawableSprite;
import ressources.R;
import ressources.Ressource;
import ressources.Ressource.Dimension;
import utilities.Methods;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.oasix.crazyshooter.GameStage;

public class HudBars extends Group
{

	private DrawableSprite lifeBarEmpty;
	private DrawableSprite lifeBarFull;
	private Ressource r_lifeBarEmpty;
	private Ressource r_lifeBarFull;
	private Sprite s_lifeBarSpriteFlash;

	private DrawableSprite xpBarEmpty;
	private DrawableSprite xpBarFull;
	private Ressource r_xpBarEmpty;
	private Ressource r_xpBarFull;

	private Label m_labelXp;
	private Label m_labelLevel;
	private Label m_labelLevelText;
	private Label m_labelLife;

	int xLifeBar = 50;
	int yLifeBar = 1000;
	int xXpBar = 50;
	int yXpBar = 930;

	int width = 700;

	private GameStage gameStage;

	// TODO ADD CLIGNOTEMENT BLANC LORS DE DEGAT
	// TODO ADD EASING LORS DU REMPLISSAGE

	public HudBars(GameStage gameStage)
	{
		this.gameStage = gameStage;

		// ----------LIFE BAR---------------
		// Recupération des textures regions
		lifeBarEmpty = new DrawableSprite(R.c().lifeBarEmpty);
		// Création d'une nouvelle textureRegion pour la barre full pour pouvoir crop
		TextureRegion textureRegionLifeBar = new TextureRegion(R.c().lifeBarFull);
		lifeBarFull = new DrawableSprite(textureRegionLifeBar);
		// Mise en place de ressources
		r_lifeBarEmpty = new Ressource(lifeBarEmpty, xLifeBar, yLifeBar, width, Dimension.WIDTH);
		addActor(r_lifeBarEmpty);
		r_lifeBarFull = new Ressource(lifeBarFull, xLifeBar, yLifeBar, width, Dimension.WIDTH);
		addActor(r_lifeBarFull);

		// ----------XP BAR---------------
		// Recupération des textures regions
		xpBarEmpty = new DrawableSprite(R.c().xpBarEmpty);
		// Création d'une nouvelle textureRegion pour la barre full pour pouvoir crop
		TextureRegion textureRegionXpBar = new TextureRegion(R.c().xpBarFull);
		xpBarFull = new DrawableSprite(textureRegionXpBar);
		// Mise en place de ressources
		r_xpBarEmpty = new Ressource(xpBarEmpty, xXpBar, yXpBar, width, Dimension.WIDTH);
		addActor(r_xpBarEmpty);
		r_xpBarFull = new Ressource(xpBarFull, xXpBar, yXpBar, width, Dimension.WIDTH);
		addActor(r_xpBarFull);

		// TEXT OFFSET
		int y = 75;
		// ----------XP BAR LABEL---------------
		BitmapFont bitmapFontXp = R.c().fontTypeViolet;
		m_labelXp = new Label("", new LabelStyle(bitmapFontXp, null));
		addActor(m_labelXp);
		m_labelXp.setFontScale(4);
		m_labelXp.setPosition(r_xpBarEmpty.getCenterX() - m_labelXp.getTextBounds().width / 2, r_xpBarEmpty.getY() + y);

		// ------------LEVEL LABEL----------------
		BitmapFont bitmapFontLevel = R.c().fontTypeViolet;
		m_labelLevel = new Label("0", new LabelStyle(bitmapFontLevel, null));
		addActor(m_labelLevel);
		m_labelLevel.setFontScale(8);
		m_labelLevel.setPosition(r_xpBarEmpty.getRight() + 30, r_xpBarEmpty.getY() + m_labelLevel.getTextBounds().height + 30);

		m_labelLevelText = new Label("Level", new LabelStyle(bitmapFontLevel, null));
		addActor(m_labelLevelText);
		m_labelLevelText.setFontScale(5);
		m_labelLevelText.setPosition(r_xpBarEmpty.getRight() + 30 - 150, r_xpBarEmpty.getY() + m_labelLevel.getTextBounds().height + 30 - 60);

		// ------------LIFE LABEL----------------
		BitmapFont bitmapFontLife = R.c().fontTypeLightGreen;
		m_labelLife = new Label("", new LabelStyle(bitmapFontLife, null));
		addActor(m_labelLife);
		m_labelLife.setFontScale(4);
		m_labelLife.setPosition(r_lifeBarEmpty.getCenterX() - m_labelLife.getTextBounds().width / 2, r_lifeBarEmpty.getY() + y);

		// Ajout d'une bar superposante pour faire un flash
		s_lifeBarSpriteFlash = new Sprite(R.c().lifeBarEmpty);
		float initialWidth = R.c().lifeBarEmpty.getRegionWidth();
		float initialHeight = R.c().lifeBarEmpty.getRegionHeight();
		s_lifeBarSpriteFlash.setBounds(xLifeBar, yLifeBar, width, Methods.scaleByWidth(width, initialWidth, initialHeight));
		s_lifeBarSpriteFlash.setColor(0, 0, 0, 1);

	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		// s_lifeBarSpriteFlash.draw(batch);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		float life = gameStage.getGlobalController().getPlayer().getLife();
		float xp = PlayerStats.currentXP;

		m_labelLevel.setText("" + PlayerStats.level);
		m_labelLife.setText("" + (int) life + "/" + PlayerStats.getMaxLife());
		m_labelLife.setX(r_lifeBarEmpty.getCenterX() - m_labelLife.getTextBounds().width / 2);
		m_labelXp.setText("" + (int) xp + "/" + PlayerStats.getRequiertXP());
		m_labelXp.setX(r_xpBarEmpty.getCenterX() - m_labelXp.getTextBounds().width / 2);

		// TODO EASING
		setBarWidth(xLifeBar, yLifeBar, r_lifeBarFull.getHeight(), PlayerStats.getMaxLife(), life, r_lifeBarFull, lifeBarFull, r_lifeBarEmpty, lifeBarEmpty);
		setBarWidth(xXpBar, yXpBar, r_xpBarFull.getHeight(), PlayerStats.getRequiertXP(), xp, r_xpBarFull, xpBarFull, r_xpBarEmpty, xpBarEmpty);

	}

	public void setBarWidth(float x, float y, float height, float max, float current, Ressource ressourceFull, DrawableSprite drawableFull, Ressource ressourceEmpty,
			DrawableSprite drawableEmpty)
	{
		float maxLife = max; // 100%
		float currentLife = current;

		// La ressource n'a pas la taille de la textureRegion
		float maxLifeWidth = ressourceEmpty.getWidth(); // 100%
		float currentLifeWidth = maxLifeWidth * currentLife / maxLife;

		// Il faut reprendre la taille exacte de la texture region et lui affecter la valeur
		float maxLifeTextureWidth = drawableEmpty.getRegionWidth(); // 100%
		float currentLifeTextureWidth = maxLifeTextureWidth * currentLife / maxLife;

		drawableFull.setRegionWidth((int) currentLifeTextureWidth);
		ressourceFull.setBounds(x, y, currentLifeWidth, height);
	}
}
