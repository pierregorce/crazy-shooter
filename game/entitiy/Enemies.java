package game.entitiy;

import game.fx.BloodParticle;
import game.fx.ParticleColors;
import game.pop.PopMessage;
import game.pop.PopMessage.MessageType;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;

public abstract class Enemies extends Character
{

	// Variables communes à tous les enemies

	private int			attackPower;
	private int			xpGainOnKill;
	public int			m_goldQuantity;
	public int			m_goldValue;
	protected boolean	bumpSensibility	= true;
	protected Player	player;

	public Enemies(Player player, int maxLife, float moveSpeed, int attackPower, int xpGainOnKill, float height, TextureRegion[] walkAnimationRight)
	{
		super(maxLife, moveSpeed, height, walkAnimationRight);
		this.xpGainOnKill = xpGainOnKill;
		this.attackPower = attackPower;
		this.player = player;
	}

	@Override
	protected void character_initialisation()
	{
		enemies_initialisation();
		defineTimer();
	}

	protected abstract void enemies_initialisation();

	/**
	 * Augmente les stats de l'enemy ; concerne la vie et les dégats percent est entre 0 et 1 pour une augmentation des stat de 0 à 100%
	 * 
	 * @param percent
	 */
	protected void increaseStats(float percent)
	{
		maxLife = (int) (maxLife * (1 + percent));
		life = (int) (maxLife * (1 + percent));
		attackPower = (int) (attackPower * (1 + percent));
		m_goldValue = (int) (m_goldValue * (1 + percent));
		xpGainOnKill = (int) (xpGainOnKill * (1 + percent));

		if (percent > 0.2)
		{
			// color ?
			colorization = new Color(214 / 255f, 119 / 255f, 105 / 255f, 1);
		}
		if (percent > 0.5)
		{
			colorization = new Color(230 / 255f, 226 / 255f, 89 / 255f, 1);
		}
		if (percent > 0.8)
		{
			colorization = new Color(167 / 255f, 146 / 255f, 228 / 255f, 1);
		}
	}

	public void faireFaceTo(Actor player)
	{
		if (player.getX() > getRight())
		{
			// player a droite de enemy
			// enemy va vers la droite
			direction = Direction.RIGHT_DIRECTION;
		}
		if (player.getRight() < getX())
		{
			// player a gauche de enemy
			// enemy va a gauche
			direction = Direction.LEFT_DIRECTION;
		}
	}

	public void tournerLeDosAuPlayer(Player player)
	{
		if (player.getX() > getRight())
		{
			// player a droite de enemy
			// enemy va vers la gauche
			direction = Direction.LEFT_DIRECTION;
		}
		if (player.getRight() < getX())
		{
			// player a gauche de enemy
			// enemy va vers la gauche
			direction = Direction.RIGHT_DIRECTION;
		}
	}

	public int getAttackPower()
	{
		return attackPower;
	}

	public void setAttackPower(int attackPower)
	{
		this.attackPower = attackPower;
	}

	public int getXpGainOnKill()
	{
		return xpGainOnKill;
	}

	public void setXpGainOnKill(int xpGainOnKill)
	{
		this.xpGainOnKill = xpGainOnKill;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		enemyDirectionEngine();
		physicalAttackEngine();
	}

	public void enemyDirectionEngine()
	{
		// A override pour personaliser
	};

	public void shootEngine()
	{
		// A override pour personaliser
	};

	public void physicalAttackEngine()
	{
		// A override pour personaliser
	};

	@Override
	public void throwProjectile()
	{
		shootEngine();
	}

	public boolean isBumpSensibility()
	{
		return bumpSensibility;
	}

	@Override
	public void loseLife(int quantity)
	{
		if (!protection)
		{
			super.loseLife(quantity);
			explosionOnLosingLife();
		}

		if (life <= 0 && getXpGainOnKill() > 0)
		{
			GlobalController.nombreRestantEnnemies -= 1;
		}
	}

	@Override
	public boolean checkVerticalDeath()
	{
		if (super.checkVerticalDeath())
		{
			GlobalController.nombreRestantEnnemies -= 1;
			return true;
		} else
		{
			return false;
		}
	}

	protected void explosionOnLosingLife()
	{
		for (int i = 0; i < 3; i++)
		{
			BloodParticle bloodParticle = GlobalController.bloodParticlePool.obtain();
			bloodParticle.init(getCenterX(), getCenterY(), ParticleColors.getInstance().getBloodColor());
			GlobalController.particleController.addActor(bloodParticle);
		}
	}

	public void popDamageOnLosingLife(int bulletDamage, boolean crit)
	{
		if (!protection)
		{
			PopMessage popMessage = new PopMessage("" + bulletDamage, this, MessageType.ENEMY_LOSE_LIFE);
			if (crit)
			{
				popMessage.setCritical();
			}
			GlobalController.fxController.addActor(popMessage);
		} else
		{
			PopMessage popMessage = new PopMessage("" + 0, this, MessageType.ENEMY_LOSE_LIFE);
			if (crit)
			{
				popMessage.setCritical();
			}
			GlobalController.fxController.addActor(popMessage);
		}

	}

	public Player getPlayer()
	{
		return player;
	}

}
