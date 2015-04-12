package game.entitiy;

import java.util.ArrayList;
import java.util.Random;

import screen.MyGdxGame;

import com.badlogic.gdx.math.Vector2;
import com.oasix.crazyshooter.Block;
import com.oasix.crazyshooter.BlockController;

/**
 * Retourne une position de pop en fonction du type d'enemy
 * 
 */
public class EnemyPopConstants
{
	// Un enemy peut pop sur chaque block.
	// Un enemy peut pop sur chaque block sauf le sol (teleporteur...)
	// Un enemy peut pop uniquement au sol a droite et gauche, ou sur le gros block du haut. -> Array SOL dans le block controlleur. (Limace, Golem)
	// Un enemy peut pop uniquement sur les coté aux levels des blocks (bat, wasp...)
	// Apparait en hauteur et tombe ainsi que au sol

	private static EnemyPopConstants	enemyPopConstants;

	public static synchronized EnemyPopConstants getInstance()
	{
		if (enemyPopConstants == null)
		{
			enemyPopConstants = new EnemyPopConstants();
		}
		return enemyPopConstants;

	}

	/**
	 * Renvoie une position soit au sol, soit sur le bigBlock (enemy qui fait uniquement du patrol/follow)
	 * 
	 */
	public Vector2 getPopablePosition()
	{
		ArrayList<Block> blockCollection = new ArrayList<Block>();
		blockCollection.addAll(BlockController.bigBlock);
		blockCollection.addAll(BlockController.groundBlock);
		return blockCollection.get(new Random().nextInt(blockCollection.size())).getPopPosition();
	}

	/**
	 * Renvoie une position a droite ou a gauche du monde et a une hauteur correspondant a un des block + marge pour lui tirer dessus
	 */
	public Vector2 getFlyPosition()
	{
		float flyMargin = 5;
		ArrayList<Block> blockCollection = new ArrayList<Block>();
		blockCollection.addAll(BlockController.bigBlock);
		blockCollection.addAll(BlockController.smallBlock);

		// Get All Block Y Levels
		Vector2 vectorSelected = blockCollection.get(new Random().nextInt(blockCollection.size())).getPopPosition();

		// Edit X for Fly (Only Wolrd Right or Left)
		float[] xPosition = { 0, MyGdxGame.VIRTUAL_WORLD_WIDTH };
		vectorSelected.x = xPosition[new Random().nextInt(xPosition.length)];
		vectorSelected.y += flyMargin;

		return vectorSelected;
	}

	/**
	 * Renvoie une position au sol.
	 */
	public Vector2 getGroundBlockPosition()
	{
		ArrayList<Block> blockCollection = new ArrayList<Block>();
		blockCollection.addAll(BlockController.groundBlock);
		return blockCollection.get(new Random().nextInt(blockCollection.size())).getPopPosition();
	}

	/**
	 * Retourne une position sur tous les smalls block.
	 */
	public Vector2 getSmallBlocksPosition()
	{
		ArrayList<Block> blockCollection = new ArrayList<Block>();
		blockCollection.addAll(BlockController.smallBlock);
		return blockCollection.get(new Random().nextInt(blockCollection.size())).getPopPosition();
	}

	/**
	 * Renvoie une position sur le big-block.
	 */
	public Vector2 getBigBlockPosition()
	{
		ArrayList<Block> blockCollection = new ArrayList<Block>();
		blockCollection.addAll(BlockController.bigBlock);
		return blockCollection.get(new Random().nextInt(blockCollection.size())).getPopPosition();
	}

	/**
	 * Renvoie une position a tous les niveaux, sol compris.
	 */
	public Vector2 getAllBlockPosition()
	{
		ArrayList<Block> blockCollection = new ArrayList<Block>();
		blockCollection.addAll(BlockController.bigBlock);
		blockCollection.addAll(BlockController.groundBlock);
		blockCollection.addAll(BlockController.smallBlock);
		return blockCollection.get(new Random().nextInt(blockCollection.size())).getPopPosition();
	}

	/**
	 * Renvoie une position en hauteur du monde et les mob tombe du haut pour pop.
	 */
	public Vector2 getFallPosition()
	{
		ArrayList<Block> blockCollection = new ArrayList<Block>();
		blockCollection.addAll(BlockController.fictivesBlock);
		return blockCollection.get(new Random().nextInt(blockCollection.size())).getPopPosition();
	}

	public Vector2 getPlayerPopPosition()
	{
		return getAllBlockPosition();
	}
}
