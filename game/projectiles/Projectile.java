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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.oasix.crazyshooter.Timer;

public class Projectile extends PhysicalEntity implements Poolable
{
	public static final int	PROJECTILE_POOL_SIZE	= 250;
	// Varable non modifiée
	private Drawable		drawable;
	private Drawable		left_drawable;
	private Drawable		right_drawable;

	public Projectiles		projectilesType;

	// Variables modifiée a chaque init
	public boolean			active					= true;
	private float			m_xStart				= 0;
	public float			precision				= 0;
	public float			rotation				= 0;
	public int				rebound					= 0;
	public Timer			effect;

	public Projectile()
	{
	}

	public void construct(Projectiles projectilesType)
	{
		getActions().clear(); // Enleve au cas ou les actions en cours
		this.projectilesType = projectilesType;
		if (projectilesType.animated)
		{
			right_drawable = new DrawableAnimation(0.1f, projectilesType.projectileTextureRegion);
		} else
		{
			right_drawable = new DrawableSprite(projectilesType.projectileTextureRegion[new Random().nextInt(projectilesType.projectileTextureRegion.length)]);
		}

		if (projectilesType.animated)
		{
			left_drawable = new DrawableAnimation(0.1f, projectilesType.projectileTextureRegion);
		} else
		{
			left_drawable = new DrawableSprite(new TextureRegion(projectilesType.projectileTextureRegion[new Random().nextInt(projectilesType.projectileTextureRegion.length)]));
			left_drawable.flip(true);
		}

		setWidth(right_drawable.getWidth() * MyGdxGame.PIXEL_SIZE);
		setHeight(right_drawable.getHeight() * MyGdxGame.PIXEL_SIZE);
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
				drawable = right_drawable;
				m_xStart = getRight();
			} else
			{
				direction = Direction.LEFT_DIRECTION;
				float x = characterSender.getRight() - projectilesType.characterAnchor.x;
				setX(x - getWidth());
				drawable = left_drawable;
				m_xStart = x - getWidth();
			}

			setY(characterSender.getY() + projectilesType.characterAnchor.y);
		}

		if (characterSender == null)
		{
			setX(projectilesType.characterAnchor.x);
			setY(projectilesType.characterAnchor.y);
			drawable = right_drawable;
			m_xStart = 0;
		}

		projectilesType.projectileComportement.comportement_init(this, characterSender);
	}

	public void init(Direction direction, Vector2 position)
	{
		this.direction = direction;
		setX(position.x);
		setY(position.y);
		m_xStart = 0;
		projectilesType.projectileComportement.comportement_init(this, null);
		drawable = right_drawable;
	}

	/**
	 * Permet d'avoir un offset, utile pour les laser pour mettre un offset sur les projectiles Rend egalement non visible
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

	/**
	 * Permet d'avoir un target, utile pour les bolt pour mettre un offset sur les projectiles Rend egalement non visible
	 */
	public void setTarget(Character characterSender, Character target)
	{
		setVisible(false);
		if (characterSender.direction == Direction.LEFT_DIRECTION)
		{
			setX(target.getX() + 70);
		} else
		{
			setX(target.getX() - 70);
		}
		setY(target.getCenterY());
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
		// getActions().clear(); // Enleve au cas ou les actions en cours FIX ME A QUI CA SERT CE PUTAIN DE TRUC
		enemyBumping(characterReceiving, direction);
		if (characterReceiving != null)
		{
			characterReceiving.explosionByBullet(this);
		}
		projectilesType.projectileComportement.comportement_endingEffect(this, characterReceiving);
	}

	private void enemyBumping(Character character, Direction bumpDirection)
	{
		// Vérifie qu'il y a bien un character pour prendre l'action, car par exemple les grenables eclate avec un character_receiving null.
		if (character != null)
		{
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
			} else
			{
				if (direction == Direction.RIGHT_DIRECTION)
				{
					character.setX(character.getX() + projectilesType.bumpAmount);
				} else
				{
					character.setX(character.getX() - projectilesType.bumpAmount);
				}
			}
		}
	}

	@Override
	public void reset()
	{
		System.out.println("reset");
		active = true;
		m_xStart = 0;
		rotation = 0;
		rebound = 0;
		effect = null;
		setColor(Color.WHITE);
		setX(0);
		setY(0);
		disablePhysics();
	}

	@Override
	public boolean remove()
	{
		if (super.remove())
		{
			Pools.free(this);
			return true;
		}
		return false;
	}

}
