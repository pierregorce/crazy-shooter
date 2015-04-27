package com.oasix.crazyshooter;

import game.ennemies.Enemy_13_Dino;
import game.ennemies.Enemy_14_Pot;
import game.ennemies.Enemy_15_Knight;
import game.ennemies.Enemy_16_Eyes;
import game.ennemies.Enemy_17_KingWizard;
import game.ennemies.Enemy_18_Turrel;
import game.ennemies.Enemy_19_Robot;
import game.ennemies.Enemy_1_Spider_basic;
import game.ennemies.Enemy_21_MiniKraken;
import game.ennemies.Enemy_2_Bat_Basic;
import game.ennemies.Enemy_3_FantomasTeleport;
import game.ennemies.Enemy_4_Limace;
import game.ennemies.Enemy_5_FantomeRunner;
import game.ennemies.Enemy_6_Wizard;
import game.ennemies.Enemy_7_Limon;
import game.ennemies.Enemy_8_Golem;
import game.ennemies.Enemy_9_Wasp;
import game.ennemies.Enemy_Boss_10_Golem;
import game.ennemies.Enemy_Boss_11_SuperFly;
import game.ennemies.Enemy_Boss_12_Invocator;
import game.ennemies.Enemy_Boss_20_Kraken;
import game.ennemies.Enemy_Boss_22_Scientist;
import game.entitiy.Enemies;

import com.badlogic.gdx.scenes.scene2d.Group;

public class EnemyFactory
{

	private float	m_timePop	= 0;
	private float	m_timeDelay	= 0;

	private float	m_enemyType;
	private int		m_quantity;
	private float	m_popTime;
	private float	m_delayTime;

	private Player	m_player;

	private boolean	m_complete	= false;

	public EnemyFactory(Group enemyController, Player player, float[] waveDetails)
	{
		super();
		m_player = player;
		m_enemyType = waveDetails[0];
		m_quantity = (int) waveDetails[1];
		m_popTime = waveDetails[2];

		m_timePop = m_popTime; // On fait ça pour le pop soit instant une fois le delay passé

		m_delayTime = waveDetails[3];
	}

	public void control(float delta)
	{

		// Controle le delay initial
		if (m_timeDelay < m_delayTime)
		{
			m_timeDelay += delta; // Si le timeDelay est inferieur a la valeur voulu alors on l'incremente, sinon on lance le pop
		} else
		{
			// Boucle sur le pop
			if (m_timePop < m_popTime)
			{
				m_timePop += delta;
			} else
			{
				m_timePop = 0;
				if (m_quantity > 0)
				{
					m_quantity--;
					GlobalController.enemyController.addActor(getNewEnemy(m_enemyType));
				}
			}

		}

		// Check sur la quantity
		if (m_quantity == 0)
		{
			m_complete = true;
		}
	}

	public boolean isComplete()
	{
		return m_complete;
	}

	private Enemies getNewEnemy(float enemyVariable)
	{

		Enemies enemy = null;

		int enemyType = (int) Math.floor(enemyVariable);
		float enemyCoef = enemyVariable - enemyType;
		switch (enemyType) {
		case 1:
			enemy = new Enemy_1_Spider_basic(m_player, enemyCoef);
			break;
		case 2:
			enemy = new Enemy_2_Bat_Basic(m_player, enemyCoef);
			break;
		case 3:
			enemy = new Enemy_3_FantomasTeleport(m_player, enemyCoef);
			break;
		case 4:
			enemy = new Enemy_4_Limace(m_player, enemyCoef);
			break;
		case 5:
			enemy = new Enemy_5_FantomeRunner(m_player, enemyCoef);
			break;
		case 6:
			enemy = new Enemy_6_Wizard(m_player, enemyCoef);
			break;
		case 7:
			enemy = new Enemy_7_Limon(m_player, enemyCoef);
			break;
		case 8:
			enemy = new Enemy_8_Golem(m_player, enemyCoef);
			break;
		case 9:
			enemy = new Enemy_9_Wasp(m_player, enemyCoef);
			break;
		case 10:
			enemy = new Enemy_Boss_10_Golem(m_player, enemyCoef);
			break;
		case 11:
			enemy = new Enemy_Boss_11_SuperFly(m_player, enemyCoef);
			break;
		case 12:
			enemy = new Enemy_Boss_12_Invocator(m_player, enemyCoef);
			break;
		case 13:
			enemy = new Enemy_13_Dino(m_player, enemyCoef);
			break;
		case 14:
			enemy = new Enemy_14_Pot(m_player, enemyCoef);
			break;
		case 15:
			enemy = new Enemy_15_Knight(m_player, enemyCoef);
			break;
		case 16:
			enemy = new Enemy_16_Eyes(m_player, enemyCoef);
			break;
		case 17:
			enemy = new Enemy_17_KingWizard(m_player, enemyCoef);
			break;
		case 18:
			enemy = new Enemy_18_Turrel(m_player, enemyCoef);
			break;
		case 19:
			enemy = new Enemy_19_Robot(m_player, enemyCoef);
			break;
		case 20:
			enemy = new Enemy_Boss_20_Kraken(m_player, enemyCoef);
			break;
		case 21:
			enemy = new Enemy_21_MiniKraken(m_player, enemyCoef);
			break;
		case 22:
			enemy = new Enemy_Boss_22_Scientist(m_player, enemyCoef);
			break;
		default:
			break;
		}

		return enemy;
	}

}
