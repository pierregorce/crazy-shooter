package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.fx.BloodParticle;
import game.fx.ParticleColors;
import game.projectiles.Projectile;
import globals.Projectiles;

import java.util.Random;

import ressources.R;
import ressources.S;
import ressources.S.TyrianSound;
import screen.MyGdxGame;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

/**
 * Wizard : Can shoot, Can walk, Can't jump, Se deplace jusqu'a une distance de X px du player puis attend. Si le player est au même Y alors shoot. Si le player est trop
 * prés alors recule //TODO
 * 
 * @author Pierre
 *
 */
public class Enemy_18_Turrel extends Enemies
{
	private final static int				MAX_LIFE		= 500;
	private final static int				XP_GAIN_ON_KILL	= 70;
	private final static int				ATTACK_POWER	= 20;
	private final static float				MOVE_SPEED_MIN	= 1;
	private final static float				MOVE_SPEED_MAX	= 2;

	private final static TextureRegion[]	textures		= R.c().enemy_turrel_idle;
	private final static int				HEIGHT			= textures[0].getRegionHeight() * MyGdxGame.PIXEL_SIZE;

	private float							enemyCoef;

	public Enemy_18_Turrel(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, HEIGHT, textures);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getSmallBlocksPosition();
		setPosition(position.x, position.y);

		editCollisionBox(40, 20, 0);
		direction = Direction.RIGHT_DIRECTION;
		walk = false;
		shoot = true;
		bumpSensibility = false;
	}

	Timer	timer_shooting	= new Timer(1f, 2f);	// for shoot animation

	@Override
	protected void enemies_initialisation()
	{
		shootCooldwon = 0.2f;
		m_shootPauseTime = 0f;
		m_shootRunTime = 1f;
		m_goldQuantity = 10;
		m_goldValue = 25;
		increaseStats(enemyCoef);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		faireFaceTo(player);
		if (timer_shooting.doAction(delta))
		{
			shoot = true;
		} else
		{
			shoot = false;
		}
	}

	@Override
	public void shootEngine()
	{
		Projectile p = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
		p.construct(Projectiles.ENEMY_TURREL);
		p.init(this);
		GlobalController.bulletControllerEnemy.addActor(p);
		S.c().play(TyrianSound.soundEffect_enemies_towerShoot, player, this);
	}

	@Override
	protected void explosionOnLosingLife()
	{
		// super.explosionOnLosingLife();

		for (int i = 0; i < 3; i++)
		{
			BloodParticle bloodParticle = GlobalController.bloodParticlePool.obtain();
			bloodParticle.init(getCenterX(), getCenterY(), ParticleColors.getInstance().getGreenWaste());
			GlobalController.particleController.addActor(bloodParticle);
		}
	}

	@Override
	public void setShootAnimation()
	{
		shootRight = new Animation(0.1f, R.c().enemy_turrel_idle);
		shootLeft = R.invertAnimation(R.c().enemy_turrel_idle, 0.1f);
	}
}
