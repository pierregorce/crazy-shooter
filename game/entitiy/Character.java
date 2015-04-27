package game.entitiy;

import game.projectiles.Projectile;

import java.util.Random;

import ressources.DrawableAnimation;
import ressources.R;
import ressources.Ressource;
import screen.MyGdxGame;
import utilities.Methods;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Timer;

/**
 * Entité physique disposant de life, maxLife, shoot et des animations.
 * 
 * @author Pierre
 *
 */
public abstract class Character extends PhysicalEntity
{
	protected int			life;
	protected int			maxLife;
	public boolean			protection			= false;

	// Animations list....................................................
	private Animation		walkLeft;
	private Animation		walkRight;
	protected Animation		shootLeft;
	protected Animation		shootRight;

	private TextureRegion	m_textureRegion_weapons_right;
	private TextureRegion	m_textureRegion_weapons_left;

	// Gestion des animations
	protected Animation		m_animation;
	protected float			animationStateTime	= 0;

	// Gestion de la colorimétrie
	protected Color			colorization		= null;

	public Character(int maxLife, float moveSpeed, float height, TextureRegion[] walkAnimation)
	{
		this.maxLife = maxLife;
		life = maxLife;
		maxVelocityX = moveSpeed;

		// Gere la taille de l'actor puis dans draw des animations de mouvement (setwidth , getwidth)
		setHeight(height); // Ne change pas tout au long de la vie de l'acteur.

		character_initialisation();
		setShootAnimation();

		// Initialisation des animations walk
		editWalkAnimation(walkAnimation, null);
	}

	protected abstract void character_initialisation();

	@Override
	public void act(float delta)
	{
		super.act(delta);
		animationStateTime += delta;
		shoot(delta);

		if (getX() + 10 < 0)
		{
			// setX(0 + 10);
		}

		if (getRight() - 10 > MyGdxGame.VIRTUAL_WORLD_WIDTH)
		{
			// setX(MyGdxGame.VIRTUAL_WORLD_WIDTH - getWidth() - 10);
		}
	}

	protected void defineTimer()
	{
		// Re-crée des timer avec les variables que les classes enfants ont set
		// Appelé par la classe enfant
		m_timerShootTiming = new Timer(m_shootRunTime, m_shootPauseTime);
		m_timerShootCooldownInRunTime = new Timer(shootCooldwon);
	}

	// Gestion du tir ------------------------------------------------------------------------------------------------

	protected boolean	shoot				= false;
	protected float		shootCooldwon		= 0.08f;
	protected float		m_shootPauseTime	= 0.5f;
	protected float		m_shootRunTime		= 0.5f;
	private Timer		m_timerShootTiming;
	private Timer		m_timerShootCooldownInRunTime;

	private void shoot(float delta)
	{
		if (shoot)
		{
			if (m_timerShootTiming.doAction(delta))
			{
				if (m_timerShootCooldownInRunTime.doAction(delta))
				{
					throwProjectile();
				}
			}
		} else
		{
			// Gere le fait que si on ne tire plus ça doit être direct disponible une fois le cd passé
			m_timerShootTiming.resetForLunchImmediatly();
			if (m_timerShootCooldownInRunTime.doAction(delta))
			{
				m_timerShootCooldownInRunTime.resetForLunchImmediatly();
			}
		}
	}

	/**
	 * Methode a polymorpher pour customizer le comportement des bullets. Car pratiquemlent tous les characters peuvent tirer et si ils ne peuvent pas, laisser vide.
	 */
	protected abstract void throwProjectile();

	/**
	 * A personnalider pour personnaliser les animations de tir
	 */
	protected void setShootAnimation()
	{
	}

	// -------------------------------------------------------------------------------------------------------------------

	/**
	 * Méthode appelée au constructeur mais pouvant être éditée notement pour le player (customization des armes)
	 * 
	 * @param walkAnimation
	 */
	public void editWalkAnimation(TextureRegion[] walkAnimation, TextureRegion weapons)
	{
		float frame = 0.1f;
		walkRight = new Animation(frame, walkAnimation);
		walkLeft = R.invertAnimation(walkAnimation, frame);

		if (weapons != null)
		{
			m_textureRegion_weapons_right = weapons;
			m_textureRegion_weapons_left = new TextureRegion(weapons);
			m_textureRegion_weapons_left.flip(true, false);
		}
	};

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		m_animation = getCurrentAnimation();
		// Scale l'animation en fonction de la hauteur de celle-ci.
		// La hauteur de l'acteur ne change jamais.
		float initialWidth = m_animation.getKeyFrame(animationStateTime, true).getRegionWidth();
		float initialHeight = m_animation.getKeyFrame(animationStateTime, true).getRegionHeight();
		setWidth(Methods.scaleByHeight(getHeight(), initialWidth, initialHeight));

		if (colorization != null)
		{
			batch.end();
			batch.begin();
			batch.setColor(colorization);
			batch.draw(m_animation.getKeyFrame(animationStateTime, true), getX(), getY(), getWidth(), getHeight());
			batch.end();
			batch.begin();
			batch.setColor(Color.WHITE);
		} else
		{
			batch.draw(m_animation.getKeyFrame(animationStateTime, true), getX(), getY(), getWidth(), getHeight());
		}

		if (m_textureRegion_weapons_right != null)
		{
			if (direction == Direction.LEFT_DIRECTION)
			{

				batch.draw(m_textureRegion_weapons_left, getX(), getY(), getWidth(), getHeight());
			} else
			{
				batch.draw(m_textureRegion_weapons_right, getX(), getY(), getWidth(), getHeight());
			}
		}

	}

	/**
	 * Retourne la bonne animation en fonction de l'état du character Methode a override et la pas lancer le super tant que l'on veux personnaliser le contenu !
	 * 
	 * @return
	 */
	public Animation getCurrentAnimation()
	{
		if (walk)
		{
			return getWalkAnimation();
		}

		if (shoot)
		{
			if (shootRight == null)
			{
				animationStateTime = 0;
			}
			return getShootAnimation();
		}

		if (!walk && !shoot)
		{
			animationStateTime = 0;
			return getWalkAnimation();
		}

		/**
		 * Specifique player
		 */
		if (inAir && !walk)
		{
			animationStateTime = 0.11f;
			return getWalkAnimation();
		}

		return null;

	}

	// ------------------------------Getters for animation---------------------------------------------------

	public Animation getWalkAnimation()
	{
		if (direction == Direction.LEFT_DIRECTION)
		{
			return walkLeft;
		} else
		{
			return walkRight;
		}
	}

	public Animation getShootAnimation()
	{
		if (shootRight == null)
		{
			return getWalkAnimation();
		}

		if (direction == Direction.LEFT_DIRECTION)
		{
			return shootLeft;
		} else
		{
			return shootRight;
		}
	}

	// ------------------------------------------------------------------------------------------------------

	public void loseLife(int quantity)
	{
		life -= quantity;
	}

	public void addLife(int quantity)
	{
		if (life + quantity <= maxLife)
		{
			life += quantity;
		} else
		{
			life = maxLife;
		}
	}

	public int getLife()
	{
		return life;
	}

	public int getMaxLife()
	{
		return maxLife;
	}

	public void setLife(int life)
	{
		this.life = life;
	}

	public void setMaxLife(int maxLife)
	{
		this.maxLife = maxLife;
	}

	public boolean isShoot()
	{
		return shoot;
	}

	public void setShoot(boolean shoot)
	{
		this.shoot = shoot;
	}

	public void explosionByBullet(Projectile p)
	{
		DrawableAnimation drawableAnimation;
		Ressource fx = null;

		drawableAnimation = new DrawableAnimation(0.1f, R.c().fx_pop);

		float min = 60;
		float max = 80;
		float dimension = new Random().nextFloat() * (max - min) + min;

		fx = new Ressource(drawableAnimation, p.getCenterX(), p.getCenterY(), dimension, new SequenceAction(Actions.delay(drawableAnimation.getAnimationDuration()), Actions.removeActor()));

		if (protection)
		{
			fx.setColor(1, 1, 1, 0.45f);
		} else
		{
			fx.setColor(1, 1, 1, 0.75f);
		}

		GlobalController.fxController.addActor(fx);
	}
}
