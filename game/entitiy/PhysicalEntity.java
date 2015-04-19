package game.entitiy;

import ressources.ActionFactory;
import ressources.DrawableAnimation;
import ressources.R;
import ressources.Ressource;
import utilities.enumerations.Direction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.oasix.crazyshooter.Block;
import com.oasix.crazyshooter.GlobalController;

/**
 * Physical permet d'être utilisé pour les particles, munitions sans necessiter de drawables, dispose d'une méthod disablePhysics pour les enemies volants. Attention a
 * bien definier les width et height cas d'utilisation sans character (image) Les x et y sont toujours définis par la classe concrete (player, enemyType)
 * 
 * @author Pierre
 *
 */
public abstract class PhysicalEntity extends Entities
{
	// POUR Y
	protected float				gravity					= -80f;

	// POUR X
	private float				ACCELERATION			= 80f;
	private final float			DAMP					= 0.6f;

	public float				maxVelocityX			= 15f;
	public float				maxJumpSpeedY			= 30f;
	public final static float	speedCoefficient		= 0.017f;

	public Direction			direction				= Direction.RIGHT_DIRECTION;

	protected Vector2			acceleration			= new Vector2();
	protected Vector2			velocity				= new Vector2();

	protected boolean			jump					= false;
	protected boolean			inAir					= false;
	protected boolean			walk					= false;

	private boolean				disablePhysics			= false;
	private boolean				disableBlockCollision	= false;

	protected DrawableAnimation	jumpUpFxAnimation		= null;
	protected DrawableAnimation	jumpDownFxAnimation		= null;

	public void disablePhysics()
	{
		disablePhysics = true;
	}

	public void enablePhysics()
	{
		disablePhysics = false;
	}

	private void xMove(float delta)
	{
		if (walk)
		{
			if (direction == Direction.LEFT_DIRECTION)
			{
				acceleration.x = -ACCELERATION;
			} else
			{
				acceleration.x = ACCELERATION;
			}
		} else
		{
			acceleration.x = 0;
			velocity.x *= DAMP;
			// velocity.x=0;
		}

		// acceleration.x *= delta;
		velocity.add(acceleration.x, 0);

		if (acceleration.x == 0)
		{
			// velocity.x *= DAMP;
		}
		if (velocity.x > maxVelocityX)
		{
			velocity.x = maxVelocityX;
		}
		if (velocity.x < -maxVelocityX)
		{
			velocity.x = -maxVelocityX;
		}

		setX(getX() + (velocity.x / PhysicalEntity.speedCoefficient) * delta);
	}

	private void yMove(float delta)
	{

		// ----------------------------- Lancement du saut
		if (jump && !inAir)
		{
			jump = false;
			inAir = true;
			velocity.y = maxJumpSpeedY; // Non dependant du fps, le set y sera ainsi a la meme hauteur.
			setCollisionBlock(null); // Si départ d'un block alors plus de colision block

			if (jumpUpFxAnimation != null) // FIXME METTRE METHODE ABSTRACT DANS CHARACTER
			{
				DrawableAnimation drawableAnimation = new DrawableAnimation(0.05f, R.c().jumpUpFxAnimation);
				Ressource ressource = new Ressource(drawableAnimation, getCenterX(), getY(), 40, ActionFactory.getRemoveAction(drawableAnimation.getAnimationDuration() - 0.05f));
				GlobalController.fxController.addActor(ressource);
			}

		}

		acceleration.y = gravity * delta; // Acceleration is set to GRAVITY. This is because the gravity is a constant and we start from here.
		velocity.add(0, acceleration.y); // Current velocity gets updated with his acceleration

		// Fin du saut block
		if (getCollisionBlock() != null && getY() + velocity.y <= getCollisionBlock().getTop() && velocity.y < 0)
		{
			velocity.y = 0;
			setY(getCollisionBlock().getTop());

			if (inAir)
			{
				inAir = false;
				if (jumpDownFxAnimation != null)
				{
					DrawableAnimation drawableAnimation = new DrawableAnimation(0.05f, R.c().jumpDownFxAnimation);
					Ressource ressource = new Ressource(drawableAnimation, getCenterX(), getY(), 40, ActionFactory.getRemoveAction(drawableAnimation.getAnimationDuration() - 0.05f));
					GlobalController.fxController.addActor(ressource);
				}
			}
		}

		// Gestion de la chutte des blocks
		if (getCollisionBlock() != null)
		{
			if (getCollisionBox().getX() > getCollisionBlock().getX() + getCollisionBlock().getWidth() || getCollisionBox().getX() + getCollisionBox().getWidth() < getCollisionBlock().getX())
			{
				setCollisionBlock(null);
				inAir = true;
			}
		}

		setY(getY() + velocity.y * delta / PhysicalEntity.speedCoefficient);
	}

	private void checkCollision()
	{
		SnapshotArray<Actor> blockArray = GlobalController.blockController.getChildren();

		for (Actor blocks : blockArray)
		{
			Block block = (Block) blocks;

			// si disable est TRUE alors on check que le sol
			if ((disableBlockCollision && block.isGround) || !disableBlockCollision)
			{
				float movementYIncrement = velocity.y * Gdx.graphics.getDeltaTime() / PhysicalEntity.speedCoefficient;

				Rectangle futurCollisionBox = new Rectangle(getCollisionBox().x, getCollisionBox().y + movementYIncrement, getCollisionBox().width, getCollisionBox().height);

				if (getCollisionBox().overlaps(block.getBouncingBox()))
				{
					setCollisionBlock(block);
				}

				if (futurCollisionBox.overlaps(block.getBouncingBox()))
				{
					setCollisionBlock(block);
				}
			}
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		checkCollision();

		xMove(delta);

		if (!disablePhysics)
		{
			yMove(delta);
		}

		if (getY() < 0)
		{
			remove();
		}

		// Change la position de la collision box pour qu'elle soit toujours placé à l'arrière (dos) du player.
		if (direction == Direction.RIGHT_DIRECTION)
		{
			// Aucun changement
			getCollisionBox().setPosition(getX() + collisionBoxLeftOffset, getY());

			if (bouncingBoxWidth != 0)
			{
				getBouncingBox().setPosition(getX() + bouncingBoxLeftOffset, getY());
			}

		} else
		{
			float x = getRight() - collisionBoxWidth;
			getCollisionBox().setPosition(x - collisionBoxLeftOffset, getY());

			if (bouncingBoxWidth != 0)
			{
				float x2 = getRight() - bouncingBoxWidth;
				getBouncingBox().setPosition(x2 - bouncingBoxLeftOffset, getY());
			}
		}

	}

	public Vector2 getVelocity()
	{
		return velocity;
	}

	public boolean isJump()
	{
		return jump;
	}

	public void setJump(boolean jump)
	{
		this.jump = jump;
	}

	public boolean isInAir()
	{
		return inAir;
	}

	public void setInAir(boolean inAir)
	{
		this.inAir = inAir;
	}

	public boolean isWalk()
	{
		return walk;
	}

	public void setWalk(boolean walk)
	{
		this.walk = walk;
	}

	/**
	 * Retourne la direction opposé à la direction current
	 */
	public Direction getOpositeDirection()
	{
		if (direction == Direction.LEFT_DIRECTION)
		{
			return Direction.RIGHT_DIRECTION;
		} else
		{
			return Direction.LEFT_DIRECTION;
		}
	}

	public float getMaxVelocityX()
	{
		return maxVelocityX;
	}

	public void setDisableBlockCollision(boolean disableBlockCollision)
	{
		this.disableBlockCollision = disableBlockCollision;
	}

}
