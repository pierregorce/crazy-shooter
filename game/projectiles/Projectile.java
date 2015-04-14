package game.projectiles;

import game.entitiy.Character;
import game.entitiy.Enemies;
import game.entitiy.PhysicalEntity;
import globals.Projectiles;

import java.util.Random;

import ressources.Drawable;
import ressources.DrawableAnimation;
import ressources.DrawableSprite;
import screen.MyGdxGame;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends PhysicalEntity
{
	// Varable non modifiée
	private Drawable	drawable;
	public Projectiles	projectilesType;

	// Variables modifiée a chaque init
	public boolean		active		= true;
	private float		m_xStart	= 0;
	public float		precision	= 0;
	public float		rotation	= 0;
	public int			rebound		= 0;

	public Projectile(Projectiles projectilesType)
	{
		this.projectilesType = projectilesType;
		if (projectilesType.animated)
		{
			drawable = new DrawableAnimation(0.1f, projectilesType.projectileTextureRegion);
		} else
		{
			drawable = new DrawableSprite(projectilesType.projectileTextureRegion[new Random().nextInt(projectilesType.projectileTextureRegion.length)]);
		}
		setWidth(drawable.getWidth() * MyGdxGame.PIXEL_SIZE);
		setHeight(drawable.getHeight() * MyGdxGame.PIXEL_SIZE);
	}

	public void init(Character characterSender)
	{
		setVisible(true);
		active = true;

		if (characterSender != null)
		{
			if (characterSender.direction == Direction.RIGHT_DIRECTION)
			{
				setX(characterSender.getX() + projectilesType.characterAnchor.x);
				direction = Direction.RIGHT_DIRECTION;
				m_xStart = getRight();
			} else
			{
				direction = Direction.LEFT_DIRECTION;
				float x = characterSender.getRight() - projectilesType.characterAnchor.x;
				setX(x - getWidth());
				m_xStart = x - getWidth();
			}

			setY(characterSender.getY() + projectilesType.characterAnchor.y);
		}

		if (characterSender == null)
		{
			setX(projectilesType.characterAnchor.x);
			setY(projectilesType.characterAnchor.y);
			m_xStart = 0;
		}
		projectilesType.projectileComportement.comportement_init(this, characterSender);
	}

	/**
	 * Permet d'avoir un target, utile pour les laser,bolt pour mettre un offset sur les projectiles Rend egalement non visible
	 */
	public void setTarget(Character characterSender, Vector2 target)
	{
		setVisible(false);
		if (characterSender.direction == Direction.LEFT_DIRECTION)
		{
			setX(getX() - target.x + 70);
		} else
		{
			setX(getX() + target.x - 70);
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (Math.abs(m_xStart - getX()) > projectilesType.lenghtAlive)
		{
			remove();
		}

		if (getY() < 0)
		{
			remove();
		}

		if (projectilesType.animated)
		{
			DrawableAnimation drawableAnimation = (DrawableAnimation) drawable;
			if (drawableAnimation.isAnimationFinished(drawableAnimation.time))
			{
				remove();
			}
		}

		projectilesType.projectileComportement.comportement_act(this);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		batch.setColor(getColor());
		drawable.setRotation(rotation);
		drawable.setBounds(getX(), getY(), getWidth(), getHeight());
		drawable.setOrigin(getWidth() / 2, getHeight() / 2);
		drawable.draw(batch, getColor().a);
		batch.setColor(Color.WHITE);
	}

	public int getPower()
	{
		int modulation = new Random().nextInt(projectilesType.damageModulation);
		return projectilesType.damage + modulation;
	}

	public void doEnddingEffect(Character characterReceiving)
	{
		getActions().clear(); // Enleve au cas ou les actions en cours
		enemyBumping(characterReceiving, direction);
		characterReceiving.explosionByBullet(this);
		projectilesType.projectileComportement.comportement_endingEffect(this, characterReceiving);
	}

	private void enemyBumping(Character character, Direction bumpDirection)
	{
		// Vérifie qu'il y a bien un character pour prendre l'action, car par exemple les grenables eclate avec un character_receiving null.
		if (character instanceof Enemies)
		{
			Enemies enemy = (Enemies) character;

			if (enemy.isBumpSensibility())
			{
				if (direction == Direction.RIGHT_DIRECTION)
				{
					enemy.setX(enemy.getX() + projectilesType.bumpAmount);
				} else
				{
					enemy.setX(enemy.getX() - projectilesType.bumpAmount);
				}
			}
		}

	}
}
