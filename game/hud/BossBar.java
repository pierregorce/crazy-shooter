package game.hud;

import game.entitiy.Enemies;
import ressources.DrawableSprite;
import ressources.R;
import ressources.Ressource;
import ressources.Ressource.Dimension;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class BossBar extends Group
{
	public static boolean active = false;
	public static Enemies enemy = null;
	public static String name = "";

	private DrawableSprite lifeBarEmpty;
	private DrawableSprite lifeBarFull;
	private Ressource r_lifeBarEmpty;
	private Ressource r_lifeBarFull;

	private Label m_labelLife;
	private static Label m_labelNameBoss;

	int width = 850;
	// int xLifeBar = MyGdxGame.VIRTUAL_WIDTH / 2 - width / 2;
	int xLifeBar = 1000;
	int yLifeBar = 850;

	public static void setBossName(String name)
	{
		if (m_labelNameBoss != null) // Uniquement pour le level parseur !
		{
			m_labelNameBoss.setText(name);
		}
	}

	public BossBar()
	{

		// ----------LIFE BAR---------------
		// Recupération des textures regions
		lifeBarEmpty = new DrawableSprite(R.c().bossBarEmpty);
		// Création d'une nouvelle textureRegion pour la barre full pour pouvoir crop
		TextureRegion textureRegionLifeBar = new TextureRegion(R.c().bossBarFull);
		lifeBarFull = new DrawableSprite(textureRegionLifeBar);
		// ((Sprite) lifeBarFull).setColor(new Color(194 / 255f, 96 / 255f, 202 / 255f, 1));
		// Mise en place de ressources
		r_lifeBarEmpty = new Ressource(lifeBarEmpty, xLifeBar, yLifeBar, width, Dimension.WIDTH);
		addActor(r_lifeBarEmpty);
		r_lifeBarFull = new Ressource(lifeBarFull, xLifeBar, yLifeBar, width, Dimension.WIDTH);
		addActor(r_lifeBarFull);

		// TEXT OFFSET
		int y = 100;

		// ------------LIFE LABEL----------------
		BitmapFont bitmapFontLife = R.c().fontTypeBasicBlod;
		m_labelLife = new Label("", new LabelStyle(bitmapFontLife, null));
		addActor(m_labelLife);
		m_labelLife.setFontScale(3.9f);
		m_labelLife.setPosition(r_lifeBarEmpty.getCenterX() - m_labelLife.getTextBounds().width / 2, r_lifeBarEmpty.getY() + y);
		// m_labelLife.setColor(new Color(194 / 255f, 96 / 255f, 202 / 255f, 1));
		active = false;
		// ------------BOSS NAME LABEL----------------
		m_labelNameBoss = new Label("", new LabelStyle(bitmapFontLife, null));
		addActor(m_labelNameBoss);
		m_labelNameBoss.setFontScale(3.9f);
		m_labelNameBoss.setPosition(r_lifeBarEmpty.getX() - m_labelLife.getTextBounds().width / 2, r_lifeBarEmpty.getY() + y + 50);
		// m_labelNameBoss.setColor(new Color(194 / 255f, 96 / 255f, 202 / 255f, 1));
		active = false;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (active)
		{
			super.draw(batch, parentAlpha);
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (active)
		{
			float life = enemy.getLife();

			m_labelLife.setText("" + (int) life + "/" + enemy.getMaxLife());
			m_labelLife.setX(r_lifeBarEmpty.getCenterX() - m_labelLife.getTextBounds().width / 2);

			// TODO EASING
			setBarWidth(xLifeBar, yLifeBar, r_lifeBarFull.getHeight(), enemy.getMaxLife(), life, r_lifeBarFull, lifeBarFull, r_lifeBarEmpty, lifeBarEmpty);
		}
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
