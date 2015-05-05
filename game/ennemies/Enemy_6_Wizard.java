package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;
import game.projectiles.Projectile;
import globals.Projectiles;

import java.util.Random;

import ressources.DrawableAnimation;
import ressources.R;
import ressources.Ressource;
import ressources.S;
import ressources.S.TyrianSound;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Pools;
import com.oasix.crazyshooter.Ammos;
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
public class Enemy_6_Wizard extends Enemies
{
	private final static int	MAX_LIFE		= 60;
	private final static int	XP_GAIN_ON_KILL	= 70;
	private final static int	ATTACK_POWER	= 20;
	private final static float	MOVE_SPEED_MIN	= 1;
	private final static float	MOVE_SPEED_MAX	= 2;
	private final static int	WIDTH			= 110;

	private float				enemyCoef;

	public Enemy_6_Wizard(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_wizard_walk);

		this.player = player;
		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getGroundBlockPosition();
		setPosition(position.x, position.y);

		direction = Direction.RIGHT_DIRECTION;
		walk = true;
		editCollisionBox(40, 20, 0);

	}

	@Override
	protected void enemies_initialisation()
	{
		shootCooldwon = 0.10f;
		m_shootPauseTime = 0.7f;
		m_shootRunTime = 0.15f;
		m_goldQuantity = 4;
		m_goldValue = 8;
		increaseStats(enemyCoef);
	}

	private Timer	laughingSound	= new Timer(4f);

	@Override
	public void enemyDirectionEngine()
	{
		super.enemyDirectionEngine();

		// Avance jusqu'a 200px de player.
		// Si le player plus prés alors recule
		// Sinon avance

		walk = false;
		shoot = false;
		float distance = Math.abs(getX() - player.getX());

		if (distance >= 600)
		{
			// Avance
			walk = true;
			faireFaceTo(player);

		}
		if (distance <= 400)
		{
			// Recule
			walk = true;
			tournerLeDosAuPlayer(player);
		}

		// Si l'on est dans la bande on l'enemy ne walk pas
		if (!walk)
		{
			faireFaceTo(player);
			shoot = true;
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		if (laughingSound.doAction(delta))
		{
			S.c().play(TyrianSound.soundEffect_enemies_witchLauging);
		}
	}

	@Override
	public void shootEngine()
	{
		Projectile p = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
		p.construct(Projectiles.ENEMY_WIZARD);
		p.init(this);
		GlobalController.bulletControllerEnemy.addActor(p);

		// SFX
		S.c().play(TyrianSound.soundEffect_enemies_witchShoot);

		// ADD HALO EFFECT
		float haloHeight = 20;
		DrawableAnimation drawableAnimation = new DrawableAnimation(0.05f, R.c().fx_pop);
		Ressource assets = null;

		if (direction == Direction.RIGHT_DIRECTION)
		{
			assets = new Ressource(drawableAnimation, getCenterX(), getCenterY() - haloHeight / 2, haloHeight, new SequenceAction(Actions.delay(0.05f), Actions.removeActor())); // FIXME
		} else
		{
			assets = new Ressource(drawableAnimation, getX(), getCenterY() - haloHeight / 2 + 5, haloHeight, new SequenceAction(Actions.delay(0.05f), Actions.removeActor())); // FIXME
		}
		GlobalController.fxController.addActor(assets);

		// ------------------Add ammmo --POSIBILITE DE REMONTER CE TRUC ET DE NE PAS FAIRE UNE METHODE ABSCRACT
		Ammos ammos = GlobalController.ammosPool.obtain();
		ammos.init(this);
		GlobalController.ammosController.addActor(ammos);
	}

	@Override
	public void setShootAnimation()
	{
		shootRight = new Animation(0.07f, R.c().enemy_wizard_shoot);
		shootLeft = R.invertAnimation(R.c().enemy_wizard_shoot, 0.07f);

	}

}
