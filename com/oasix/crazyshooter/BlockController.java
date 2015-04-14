package com.oasix.crazyshooter;

import game.entitiy.Character;
import game.path.WayPoint;
import game.path.WayPoint.Action;

import java.util.ArrayList;
import java.util.Random;

import screen.MyGdxGame;
import screen.ScreenManager;
import screen.level.LevelGroup;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

public class BlockController extends Group
{

	public static float				block_hauteur		= 15;
	public static float				background_offsetY	= -70;

	// ---------- Global variables
	public static float				groundLevel			= 000;
	public static ArrayList<Block>	bigBlock;
	public static ArrayList<Block>	smallBlock;
	public static ArrayList<Block>	groundBlock;
	public static ArrayList<Block>	fictivesBlock;				// Contient des blocks fictif pour une apparition en l'air

	public BlockController()
	{
		bigBlock = new ArrayList<Block>();
		smallBlock = new ArrayList<Block>();
		groundBlock = new ArrayList<Block>();
		fictivesBlock = new ArrayList<Block>();

		int[] m_boss_level = LevelGroup.m_boss_level_from_1; // Récupère les niveau des boss (commence à 1)
		int level = ScreenManager.getInstance().getLevelSelected().levelIndex; // Recupère le level (commence à 1)

		Block block_1 = new Block(0, 0, 0);
		Block block_2 = new Block(0, 0, 0);
		Block block_3 = new Block(0, 0, 0);
		Block block_4 = new Block(0, 0, 0);
		Block block_5 = new Block(0, 0, 0);
		Block block_6 = new Block(0, 0, 0);
		Block block_7 = new Block(0, 0, 0);
		Block block_8 = new Block(0, 0, 0);
		Block block_9 = new Block(0, 0, 0);
		Block block_10 = new Block(0, 0, 0);
		Block bigblock = new Block(0, 0, 0);
		Block groundLeft = new Block(0, 0, 0);
		Block groundRight = new Block(0, 0, 0);
		Block globalGround = new Block(0, 0, 0);

		// BOSS ::: |----------------0----------------1----------------2----------------3----------------4
		// LEVELS : |-------BG1------|-------BG2------|-------BG3------|-------BG4------|-------BG5------|

		// BG1 - 5 blocs
		// --------------------------Block de base
		// 50
		block_1 = new Block(308, 800 - 599 + background_offsetY - block_hauteur, 50, block_hauteur, true);
		block_1.debugNumber = "" + 1;
		addActor(block_1);
		smallBlock.add(block_1);
		block_1.active = true;

		block_2 = new Block(767, 800 - 577 + background_offsetY - block_hauteur, 50, block_hauteur, true);
		block_2.debugNumber = "" + 2;
		addActor(block_2);
		smallBlock.add(block_2);
		block_2.active = true;
		// 75
		block_3 = new Block(372, 800 - 576 + background_offsetY - block_hauteur, 75, block_hauteur, true);
		block_3.debugNumber = "" + 3;
		addActor(block_3);
		smallBlock.add(block_3);
		block_3.active = true;

		block_4 = new Block(667, 800 - 599 + background_offsetY - block_hauteur, 75, block_hauteur, true);
		block_4.debugNumber = "" + 4;
		addActor(block_4);
		smallBlock.add(block_4);
		block_4.active = true;

		block_6 = new Block(571, 800 - 539 + background_offsetY - block_hauteur, 120, block_hauteur, true);
		block_6.debugNumber = "" + 6;
		addActor(block_6);
		smallBlock.add(block_6);
		block_6.active = true;

		// BG2 - 6 blocs
		if (level > m_boss_level[0])
		{
			// 120
			block_5 = new Block(397, 800 - 517 + background_offsetY - block_hauteur, 120, block_hauteur, true);
			block_5.debugNumber = "" + 5;
			addActor(block_5);
			smallBlock.add(block_5);
			block_5.active = true;
		}

		// BG3 - 6 blocs + trou
		if (level > m_boss_level[1])
		{
			// Rien a rajouter pour les blocks, sol fait plus bas.
		}

		// BG4 - 10 blocs (+ big plateform) + trou
		if (level > m_boss_level[2])
		{
			// ------------------------------------- Block supplémentaire
			// 150
			block_7 = new Block(810, 800 - 503 + background_offsetY - block_hauteur, 150, block_hauteur, true);
			addActor(block_7);
			block_7.active = true;
			block_7.debugNumber = "" + 7;
			smallBlock.add(block_7);
			block_8 = new Block(203, 800 - 464 + background_offsetY - block_hauteur, 150, block_hauteur, true);
			addActor(block_8);
			smallBlock.add(block_8);
			block_8.active = true;
			block_8.debugNumber = "" + 8;
			// 75
			block_9 = new Block(267, 800 - 405 + background_offsetY - block_hauteur, 75, block_hauteur, true);
			addActor(block_9);
			block_9.active = true;
			smallBlock.add(block_9);
			block_9.debugNumber = "" + 9;
			block_10 = new Block(813, 800 - 444 + background_offsetY - block_hauteur, 75, block_hauteur, true);
			addActor(block_10);
			block_10.active = true;
			smallBlock.add(block_10);
			block_10.debugNumber = "" + 10;

			// 396
			bigblock = new Block(389, 800 - 405 + background_offsetY - block_hauteur, 396, block_hauteur, true)
			{
				@Override
				public Vector2 getPopPosition()
				{
					Block block = new Block(531, 800 - 405 + background_offsetY, 22, block_hauteur, true);
					return block.getPopPosition();
				}
			};
			addActor(bigblock);
			bigblock.debugNumber = "0-bigBlock";
			bigBlock.add(bigblock);
			bigblock.active = true;
		}

		// BG5 - Tout !
		if (level > m_boss_level[3])
		{
			// deplacé au bg4
		}

		// ------------------------------------------Gestion du sol

		final float hauteur = 20;

		if (level > m_boss_level[1])
		{
			// -------------------------------------------------------------- Sol avec 1 trou

			groundLeft = new Block(0, 800 - 665 + background_offsetY - hauteur, 523, hauteur, true)
			{
				@Override
				public Vector2 getPopPosition()
				{
					return new Vector2(0, (800 - 665 + background_offsetY) * MyGdxGame.PIXEL_SIZE);
				}
			};

			groundRight = new Block(565, 800 - 665 + background_offsetY - hauteur, 523 - (565 - 523), hauteur, true)
			{
				@Override
				public Vector2 getPopPosition()
				{
					return new Vector2(MyGdxGame.VIRTUAL_WORLD_WIDTH, (800 - 665 + background_offsetY) * MyGdxGame.PIXEL_SIZE);
				}
			};

			groundLeft.isGround = true;
			groundLeft.debugNumber = "0-left";
			groundRight.isGround = true;
			groundRight.debugNumber = "0-right";
			addActor(groundLeft);
			groundBlock.add(groundLeft);
			addActor(groundRight);
			groundBlock.add(groundRight);
			groundLevel = groundLeft.getTop();
			groundRight.active = true;
			groundLeft.active = true;

		} else
		{

			// -------------------------------------------------------------- Sol sans trou

			globalGround = new Block(0, 800 - 665 + background_offsetY - hauteur, 1000, hauteur, true)
			{
				@Override
				public Vector2 getPopPosition()
				{
					Vector2[] v = new Vector2[2];
					v[0] = new Vector2(0, (800 - 665 + background_offsetY) * MyGdxGame.PIXEL_SIZE);
					v[1] = new Vector2(MyGdxGame.VIRTUAL_WORLD_WIDTH, (800 - 665 + background_offsetY) * MyGdxGame.PIXEL_SIZE);
					return v[new Random().nextInt(v.length)];
				}
			};

			globalGround.isGround = true;
			globalGround.debugNumber = "0";
			addActor(globalGround);
			groundBlock.add(globalGround);
			groundLevel = globalGround.getTop();
			globalGround.active = true;

		}

		// --------------------------- Fictive Blocks, Only for Pop
		Block block_fictive_1 = new Block(177, 800 - 239 + background_offsetY - block_hauteur, 75, block_hauteur, true);
		fictivesBlock.add(block_fictive_1);
		Block block_fictive_2 = new Block(867, 800 - 239 + background_offsetY - block_hauteur, 75, block_hauteur, true);
		fictivesBlock.add(block_fictive_2);
		if (GameStage.debug)
		{
			GlobalController.fxController.addActor(block_fictive_1);
			GlobalController.fxController.addActor(block_fictive_2);
		}

		if (GameStage.debug)
		{
			GlobalController.fxController.addActor(new Block(531, 800 - 405 + background_offsetY, 22, block_hauteur, true));
		}

		// ------------------------------- Mise en place des wayPoint de départ (au sol)

		WayPoint groundWayPointLeft = new WayPoint(block_1.getX(), groundLevel, block_1.getWidth(), 30);
		groundWayPointLeft.setActions(Action.JUMP);
		groundWayPointLeft.active = true;
		WayPoint groundWayPointRight = new WayPoint(block_4.getX(), groundLevel, block_4.getWidth(), 30);
		groundWayPointRight.setActions(Action.JUMP);
		groundWayPointRight.active = true;
		if (GameStage.debug)
		{
			GlobalController.fxController.addActor(groundWayPointLeft);
			GlobalController.fxController.addActor(groundWayPointRight);
		}

		// -------------------------------------------------- Activation des waypoints
		// -------------------------------------------------- Activation des waypoints
		// -------------------------------------------------- Activation des waypoints

		// BG1 - 5 blocs
		block_1.middle.active = true;
		block_2.left.active = true;
		block_3.left.active = true;
		block_4.left.active = true;
		block_4.right.active = true;
		block_6.right.active = true;

		// BG2 - 6 blocs
		if (level > m_boss_level[0])
		{
			block_3.middle.active = true;
			block_5.right.active = true;
			block_6.left.active = true;
		}

		// BG4 - 10 blocs (il manque la big plateform) + trou
		if (level > m_boss_level[2])
		{
			block_2.right.active = true;
			block_5.left.active = true;

			block_7.left.active = true;
			block_7.middle.active = true;
			block_8.right.active = true;
			block_8.middle.active = true;
			block_9.left.active = true;
			block_10.right.active = true;

			// AJout du tout
			block_9.right.active = true;
			block_10.left.active = true;
			bigblock.left.active = true;
			bigblock.right.active = true;
		}

		// BG5 - Tout !
		if (level > m_boss_level[3])
		{
		}

		// ---------------------------------------------- Set block linked & WayPoint
		// ---------------------------------------------- Set block linked & WayPoint
		// ---------------------------------------------- Set block linked & WayPoint

		groundLeft.setBlocksLinked(block_1);
		groundRight.setBlocksLinked(block_4);

		block_1.setBlocksLinked(groundLeft, block_3);
		block_1.middle.setActions(Action.JUMP, Action.WALK_RIGHT);
		block_1.setWayPointToCome(groundWayPointLeft, block_3.left);

		block_2.setBlocksLinked(block_4, block_7);
		block_2.right.setActions(Action.JUMP);
		block_2.left.setActions(Action.WALK_LEFT);
		block_2.setWayPointToCome(block_4.right, block_7.left);

		block_3.setBlocksLinked(block_1, block_5);
		block_3.middle.setActions(Action.JUMP);
		block_3.left.setActions(Action.WALK_LEFT);
		block_3.setWayPointToCome(block_1.middle);

		block_4.setBlocksLinked(groundRight, block_6, block_2);
		block_4.left.setActions(Action.JUMP);
		block_4.right.setActions(Action.JUMP, Action.WALK_RIGHT);
		block_4.setWayPointToCome(groundWayPointRight, block_2.left);

		block_5.setBlocksLinked(block_3, block_6, block_8);
		block_5.left.setActions(Action.JUMP, Action.WALK_LEFT);
		block_5.right.setActions(Action.JUMP, Action.WALK_RIGHT);
		block_5.setWayPointToCome(block_6.left, block_3.middle, block_8.right);

		block_6.setBlocksLinked(block_5, block_4);
		block_6.left.setActions(Action.JUMP, Action.WALK_LEFT);
		block_6.right.setActions(Action.WALK_RIGHT);
		block_6.setWayPointToCome(block_5.right, block_4.left);

		block_7.setBlocksLinked(block_2, block_10);
		block_7.left.setActions(Action.WALK_LEFT);
		block_7.middle.setActions(Action.JUMP, Action.WALK_LEFT);
		block_7.setWayPointToCome(block_2.right, block_10.right);

		block_8.setBlocksLinked(block_5, block_9);
		block_8.right.setActions(Action.WALK_RIGHT);
		block_8.middle.setActions(Action.JUMP);
		block_8.setWayPointToCome(block_5.left, block_9.left);

		block_9.setBlocksLinked(block_8, bigblock);
		block_9.left.setActions(Action.WALK_LEFT);
		block_9.right.setActions(Action.JUMP, Action.WALK_RIGHT);
		block_9.setWayPointToCome(block_8.middle, bigblock.left);

		block_10.setBlocksLinked(block_7, bigblock);
		block_10.left.setActions(Action.JUMP, Action.WALK_LEFT);
		block_10.right.setActions(Action.WALK_RIGHT);
		block_10.setWayPointToCome(block_7.middle, bigblock.right);

		bigblock.setBlocksLinked(block_9, block_10);
		bigblock.left.setActions(Action.JUMP, Action.WALK_LEFT);
		bigblock.right.setActions(Action.WALK_RIGHT);
		bigblock.setWayPointToCome(block_9.right, block_10.left);
	}

	public Block getBlockObjective(Block finalBlockObjective, Character character)
	{
		// Test préliminaire
		if (finalBlockObjective == character.getCollisionBlock())
		{
			System.out.println("Je suis déja sur le block cible");
			return null;
		}
		// Liste fermée contenant les blocks qui auront été téstés, pour ne pas les retesté à nouveau.
		ArrayList<Block> blockChecked = new ArrayList<Block>();
		System.out.println("Je suis sur le block : " + character.getCollisionBlock().debugNumber);
		System.out.println("Mon block objectif final est : " + finalBlockObjective.debugNumber);
		Block currentObjective = null;
		currentObjective = checkCollisionForEachChildren(blockChecked, character, finalBlockObjective);
		return currentObjective;
	}

	private Block checkCollisionForEachChildren(ArrayList<Block> blockCheckedClosedList, Character character, Block blockObjective)
	{
		System.out.println("Check for block " + blockObjective.debugNumber);
		// Definit un block qui sera notre retour
		Block returnBlock = null;
		// Récupère la liste des blocks desquelles on peux venir sur notre objective
		ArrayList<Block> blocksLinked = blockObjective.getBlocksLinked();
		// Maintenant que l'on a ses enfant, on le met dans la liste fermé car on en a plus besoin.
		System.out.println("Le block #" + blockObjective.debugNumber + " est mit dans la liste fermée");
		blockCheckedClosedList.add(blockObjective);

		// On va maintenant s'occuper des linked
		for (Block blockLinked : blocksLinked)
		{
			// Si le block linked n'est pas dans la liste fermé (on l'a déja traité)
			if (!isInClosedList(blockCheckedClosedList, blockLinked))
			{
				System.out.println("Test du block #" + blockLinked.debugNumber);
				if (character.getCollisionBlock() == blockLinked)
				{
					returnBlock = blockLinked;
					System.out.println("BINGO FOUND !");
					return blockObjective; // On retourne le block parent
				} else
				{
					System.out.println("Le block " + blockLinked.debugNumber + " ne correspond pas, on check les block linkés a lui");
					// Le return block prend la valeur du block testé pour pouvoir renvoyer le parent des enfants.
					returnBlock = blockLinked;
					returnBlock = checkCollisionForEachChildren(blockCheckedClosedList, character, blockLinked);
					if (returnBlock != null)
					{
						return returnBlock;
						// return returnBlock;
					}
				}
			}
		}
		return returnBlock;
	}

	/**
	 * Renvoie true si le block est dans la liste
	 */
	private boolean isInClosedList(ArrayList<Block> blockChecked, Block block)
	{
		for (Block blockCheck : blockChecked)
		{
			if (blockCheck == block)
			{
				return true;
			}
		}
		return false;
	}

	public WayPoint getWayPointToCome(Character character, Block targetBlock)
	{
		float distance = 999999999;
		WayPoint target = null;

		for (WayPoint wayPoint : targetBlock.getWayPointToCome())
		{
			float distance_temp = Vector2.dst2(wayPoint.getCenterX(), wayPoint.getCenterY(), character.getCenterX(), character.getCenterY());
			if (distance_temp < distance)
			{
				distance = distance_temp;
				target = wayPoint;
			}
		}
		target.selected = true;
		return target;
	}

	/**
	 * Return bigpapa block & smalls blocks
	 */
	public Block getTopsBlocks()
	{
		ArrayList<Block> blockCollection = new ArrayList<Block>();
		blockCollection.addAll(BlockController.bigBlock);
		blockCollection.addAll(BlockController.smallBlock);
		return blockCollection.get(new Random().nextInt(blockCollection.size()));
	}

}
