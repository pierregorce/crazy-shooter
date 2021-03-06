package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.pop.PopMessage;
import game.pop.PopMessage.MessageType;
import game.projectiles.Projectile;
import globals.Projectiles;

import java.util.Random;

import ressources.R;
import ressources.S;
import ressources.S.TyrianSound;
import utilities.enumerations.Direction;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.oasix.crazyshooter.GameStage;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;

public class Enemy_NapalmBarrel extends Enemies
{

	/*
	 * Walking,Jumping basic enemy Try to catch player
	 */

	private final static int	MAX_LIFE		= 1;
	private final static int	XP_GAIN_ON_KILL	= 0;
	private final static int	ATTACK_POWER	= 0;
	private final static float	MOVE_SPEED		= 0;
	private final static int	WIDTH			= 70;

	public Enemy_NapalmBarrel(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, MOVE_SPEED, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().napalmBarrel);

		m_goldQuantity = 0;

		jump = true;
		walk = false;

		Vector2 position = EnemyPopConstants.getInstance().getObjectPosition();
		setX(position.x);
		setY(position.y);

		maxJumpSpeedY = 0;
		maxVelocityX = 0;
		direction = Direction.values()[new Random().nextInt(Direction.values().length)];
		gravity = gravity - gravity * 0.88f;
		// colorization = Color.BLACK;
	}

	@Override
	protected void enemies_initialisation()
	{
		shootCooldwon = 0.07f;
		m_shootPauseTime = 0f;
		m_shootRunTime = 1f;
		m_goldQuantity = 10;
		m_goldValue = 10;
	}

	private boolean	ending	= false;

	@Override
	public boolean remove()
	{
		if (super.remove())
		{

			for (int i = 0; i < Projectiles.BARREL_NAPALM.quantityPerShoot; i++)
			{
				int xRandomization = new Random().nextInt(180) - 90;
				int yRandomization = new Random().nextInt(100);

				Projectile projectileExplosion = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
				projectileExplosion.construct(Projectiles.BARREL_NAPALM);

				projectileExplosion.init(this);
				projectileExplosion.setX(this.getCenterX() + xRandomization);
				projectileExplosion.setY(this.getCenterY() + yRandomization);
				GlobalController.bulletControllerFriendly.addActor(projectileExplosion);
			}
			GameStage.cameraShake(GameStage.HIGH_SHAKE);

			return true;
		} else
		{
			return false;
		}
	}

	@Override
	protected void explosionOnLosingLife()
	{
		// super.explosionOnLosingLife();
	}

	@Override
	public void popDamageOnLosingLife(int bulletDamage, boolean crit)
	{
		// super.popDamageOnLosingLife(bulletDamage, crit);

		GlobalController.fxController.addActor(new PopMessage(this, MessageType.EXPLOSION));
		S.c().play(TyrianSound.soundEffect_player_barrelExplosion, player, this);

	}

}
