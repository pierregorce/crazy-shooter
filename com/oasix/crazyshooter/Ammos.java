package com.oasix.crazyshooter;

import game.entitiy.Character;
import game.entitiy.PhysicalEntity;

import java.util.Random;

import ressources.R;
import utilities.enumerations.Direction;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Ammos extends PhysicalEntity implements Poolable
{
	private Sprite	sprite	= new Sprite(R.c().ammo);

	public Ammos()
	{
		// TODO Auto-generated constructor stub
	}

	public Ammos(Character character)
	{
		init(character);
	}

	public void init(Character character)
	{
		// Les balles doivent être a l'invere de la direction et doivent être placée dans le dos du player
		if (character.direction == Direction.LEFT_DIRECTION)
		{
			setX(character.getRight());
			direction = Direction.RIGHT_DIRECTION;
		} else
		{
			setX(character.getX());
			direction = Direction.LEFT_DIRECTION;
		}

		setY(character.getY() + character.getHeight() / 2);
		setWidth(10);
		setHeight(5);

		sprite.setSize(getWidth(), getHeight());
		sprite.setPosition(getX(), getY());

		// Lance le premier saut.
		jump = true;
		// Modifie les constantes
		maxJumpSpeedY = new Random().nextInt(2) + 3;
		maxVelocityX = new Random().nextInt(10) + 7;
		acceleration.x = new Random().nextInt(200) - new Random().nextInt(100);
		// velocity.y = new Random().nextInt((int) (MAX_JUMP_SPEED - MAX_JUMP_SPEED / 2)) + MAX_JUMP_SPEED / 2;

		lifeTime = (int) (new Random().nextFloat() * 12 + 2);
		reboundQuantity = MAX_REBOUND_QUANTITY;
		MAX_REBOUND_QUANTITY = new Random().nextInt(2) + 1;
		stateTime = 0;
	}

	private int		MAX_REBOUND_QUANTITY;
	private int		reboundQuantity;

	private int		lifeTime;
	private float	stateTime;

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		sprite.draw(batch);
		sprite.setBounds(getX(), getY(), 10, 5);
	}

	int	i	= 0;

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (velocity.y > 0 && reboundQuantity == MAX_REBOUND_QUANTITY)
		{
			maxJumpSpeedY = 15;
		}

		// -------- Rotation des balles si il a encore des rebonds a faire.
		if (reboundQuantity > 0)
		{
			sprite.rotate(i);
			i += 90;
		} else
		{
			// sprite.setRotation(0);
		}

		// --------- Fait des jump tant qu'il y a des rebonds a faire
		if (reboundQuantity > 0 && getCollisionBlock() != null)
		{
			jump = true;
			float coef = reboundQuantity / MAX_REBOUND_QUANTITY;
			coef = 0.85f;
			maxJumpSpeedY = maxJumpSpeedY * coef;
			reboundQuantity--;

		}

		// ------Avance tant qu'il y a des rebonds a faire.
		if (reboundQuantity >= 0)
		{
			walk = true;
			maxVelocityX = 3;
			maxVelocityX = new Random().nextInt(2) + 2;
		}

		stateTime += delta;

		if (stateTime > lifeTime)
		{
			remove();
			// ERREUR CMT LE SUPPRIM2 DU ARRAYLIST ??
		}

		if (getCollisionBlock() != null)
		{
			maxVelocityX = 0;
		}

		/*
		 * 
		 * if (reboundQuantity>0) { //sprite.rotate(i); //i+=90; }else { sprite.setRotation(0); }
		 * 
		 * // TODO FAIRE AVEC COLLISION BLOCK COMME CHARACTER //METTRE LE COLISSION BLOCK DANS ENTITIES PLUTOT QUE DANS CHARACTER
		 * 
		 * if (getY() + moveSpeedY * moveIterator < GameStage.GROUND_HEIGHT && myWay == way.GO_DOWN) { // 1er collision if (reboundQuantity == MAX_REBOUND_QUANTITY) {
		 * longueurInitiale = GameStage.GROUND_HEIGHT; }
		 * 
		 * moveIterator = moveIterator * -1; myWay = way.GO_UP; reboundQuantity--; }
		 * 
		 * if (myWay == way.GO_UP && getY() + moveSpeedY * moveIterator > (longueurInitiale * reboundQuantity / MAX_REBOUND_QUANTITY + GameStage.GROUND_HEIGHT)) {
		 * moveIterator = moveIterator * -1; myWay = way.GO_DOWN; }
		 * 
		 * sprite.setPosition(getX(), getY());
		 * 
		 * if (reboundQuantity > 0) { setX(getX() + moveSpeedX*sens); setY(getY() + moveSpeedY * moveIterator);
		 * 
		 * }
		 */
	}

	@Override
	public void reset()
	{

	}

	@Override
	public boolean remove()
	{
		if (super.remove())
		{
			GlobalController.ammosPool.free(this);
			return true;
		}
		return false;
	}

}
