package game.ennemies;

import game.entitiy.Character;
import game.entitiy.Enemies;
import screen.MyGdxGame;
import utilities.enumerations.Direction;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oasix.crazyshooter.Block;
import com.oasix.crazyshooter.BlockController;
import com.oasix.crazyshooter.Player;

public class EnemyComportements
{
	/**
	 * Pour les 1er niveau sans trou
	 */
	public static void followPlayer(Enemies enemy, Player player)
	{
		enemy.setWalk(true);
		enemy.faireFaceTo(player);
	}

	/**
	 * Si l'enemy est sur un block alors va en direction du player Si l'enemy est au sol alors en fonction de la position du player decide quoi faire ------si le player
	 * est au sol sur le meme block : poursuit ------sinon : patrol
	 */
	public static void followPlayerAndPatrolOnGround(Enemies enemy, Player player)
	{
		enemy.setWalk(true);

		if (!isOnGround(enemy))
		{
			enemy.faireFaceTo(player);
		} else
		{
			if (isOnSameBlock(enemy, player))
			{
				enemy.faireFaceTo(player);
			} else
			{
				patrolOnBlock(enemy, player);
			}
		}
	}

	public static void followPlayerAndPatrolOnGroundAndShoot(Enemies enemy, Player player, float distanceShoot)
	{

		if (enemy.getPosition().dst(player.getPosition()) < distanceShoot)
		{
			enemy.faireFaceTo(player);
			enemy.setWalk(false);
			enemy.setShoot(true);
		} else
		{
			enemy.setShoot(false);
			followPlayerAndPatrolOnGround(enemy, player);
		}
	}

	/**
	 * Patrol si le player n'est pas sur le même block
	 */
	public static void followPlayerAndPatrol(Enemies enemy, Player player)
	{
		enemy.setWalk(true);

		if (isOnSameBlock(enemy, player))
		{
			enemy.faireFaceTo(player);
		} else
		{
			patrolOnBlock(enemy, player);
		}
	}

	public static void navigateOverWorld(Enemies enemy, Player player)
	{
		if (enemy.getRight() > MyGdxGame.VIRTUAL_WORLD_WIDTH)
		{
			enemy.direction = Direction.LEFT_DIRECTION;
		}
		if (enemy.getX() < 0)
		{
			enemy.direction = Direction.RIGHT_DIRECTION;
		}
	}

	public static void physicalAttack(Enemies enemy, Player player)
	{
		if (player.getBouncingBox().overlaps(enemy.getBouncingBox()))
		{
			// Fait perdre de la vie au player
			player.setLosingLifeEvent(true);
			player.setLoosingLiveValueEvent(enemy.getAttackPower());

			// TODO SOUND
			// Genere le bump
			if (player.getRight() > enemy.getX())
			{
				// player a droite de enemy
				// enemy va vers la droite
				player.setBumpingRightEvent(true); // TODO SET BUMP STRENGHT IN ENEMY
			}
			if (player.getX() < enemy.getX())
			{
				player.setBumpingLeftEvent(true);
				// player a gauche de enemy
				// enemy va a gauche
			}
			// Met l'enemy en collision -> arret
			enemy.setWalk(false);
		} else
		{
			// si plus de collision alors l'enemy repart
			enemy.setWalk(true);
		}
	}

	public static void patrolOnBlock(Enemies enemy, Player player)
	{
		Block block = enemy.getCollisionBlock();

		if (block != null)
		{
			enemy.setWalk(true);
			enemy.setShoot(false);

			if (enemy.getX() <= block.getX())
			{
				enemy.direction = Direction.RIGHT_DIRECTION;
			}
			if (enemy.getRight() >= block.getRight())
			{
				enemy.direction = Direction.LEFT_DIRECTION;
			}
		}
	}

	public static boolean isOnSameBlock(Enemies enemy, Player player)
	{
		if (enemy.getCollisionBlock() == player.getCollisionBlock())
		{
			return true;
		} else
		{
			return false;
		}
	}

	private static boolean areOnGround(Enemies enemy, Player player)
	{
		if (enemy.getY() == BlockController.groundLevel && player.getY() == BlockController.groundLevel)
		{
			return true;
		} else
		{
			return false;
		}
	}

	private static boolean isOnGround(Character character)
	{
		if (character.getY() == BlockController.groundLevel)
		{
			return true;
		} else
		{
			return false;
		}
	}

	public static void goToTarget(Enemies enemy, Actor target)
	{

		if (enemy.getCollisionBox().contains(target.getCenterX(), target.getCenterY()))
		{
			enemy.setWalk(false);
		} else
		{
			enemy.setWalk(true);
			enemy.faireFaceTo(target);
		}

	}

}
