package game.ennemies;

import game.entitiy.Enemies;
import game.entitiy.EnemyPopConstants;

import java.util.Random;

import ressources.R;
import ressources.S;
import ressources.S.TyrianSound;
import utilities.enumerations.Direction;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.oasix.crazyshooter.GameStage;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class Enemy_2_Bat_Basic extends Enemies
{

	// Flying basic enemy
	// Move left then right, don't care about player

	private final static int	MAX_LIFE		= 35;
	private final static int	XP_GAIN_ON_KILL	= 35;
	private final static int	ATTACK_POWER	= 15;
	private final static float	MOVE_SPEED_MIN	= 3;
	private final static float	MOVE_SPEED_MAX	= 6;
	private final static float	MOVE_SPEED		= new Random().nextFloat() * (MOVE_SPEED_MAX - MOVE_SPEED_MIN) + MOVE_SPEED_MIN;
	private final static int	WIDTH			= 80;

	private float				enemyCoef;

	public Enemy_2_Bat_Basic(Player player, float enemyCoef)
	{
		super(player, MAX_LIFE, MOVE_SPEED, ATTACK_POWER, XP_GAIN_ON_KILL, WIDTH, R.c().enemy_bat_walk);

		this.enemyCoef = enemyCoef;

		Vector2 position = EnemyPopConstants.getInstance().getFlyPosition();
		setPosition(position.x, position.y);
		direction = Direction.RIGHT_DIRECTION;
		setWalk(true);
		disablePhysics();
		increaseStats(enemyCoef);
	}

	@Override
	protected void enemies_initialisation()
	{
		m_goldQuantity = 2;
		m_goldValue = 4;
		increaseStats(enemyCoef);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		pick(delta);
		EnemyComportements.navigateOverWorld(this, player);
	}

	private Timer	timerPick	= new Timer(2f);
	private boolean	canPick		= false;
	private boolean	pickReady	= true;

	@Override
	public void physicalAttackEngine()
	{
		super.physicalAttackEngine();

		// si player est en dessous
		// si player non immune
		// alors la chauve souris attaque

		// player perd de la vie

		if (getRight() > player.getBouncingBox().getX() - 20 && getX() < player.getBouncingBox().getX() + player.getBouncingBox().getWidth() + 20 && player.getImmuneTime() == 0 && player.getY() < getY())
		{
			canPick = true;
		} else
		{
			canPick = false;
		}
	}

	private void pick(float delta)
	{
		// CHECK PICK STATUS
		if (timerPick.doAction(delta))
		{
			pickReady = true; // Repasse ready toute les Xs
		}

		// DO THE PICK
		if (canPick && pickReady)
		{
			pickAction(delta);
			pickReady = false;
		}
	}

	private void pickAction(float delta)
	{

		if (getActions().size == 0)
		{
			MoveToAction action1 = Actions.moveTo(player.getX(), player.getY(), 0.15f);
			final Enemy_2_Bat_Basic me = this;

			RunnableAction action2 = new RunnableAction()
			{

				@Override
				public void run()
				{
					GameStage.cameraShake(15);
					S.c().play(TyrianSound.soundEffect_enemies_batAttack, player, me);
					// player.setWalk(false);

					// Fait perdre de la vie au player
					player.setLosingLifeEvent(true);
					player.setLoosingLiveValueEvent(getAttackPower());
				}

			};

			MoveToAction action3 = null;

			if (direction == Direction.LEFT_DIRECTION)
			{
				action3 = Actions.moveTo(getX() - 200, getY(), 0.15f);
			}
			if (direction == Direction.RIGHT_DIRECTION)
			{
				action3 = Actions.moveTo(getX() + 200, getY(), 0.15f);
			}

			SequenceAction sequenceAction = new SequenceAction(action1, action2, action3);
			addAction(sequenceAction);
		}
	}

}
