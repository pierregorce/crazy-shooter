package com.oasix.crazyshooter;

import game.ennemies.Enemy_1_Spider_basic;
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
import game.entitiy.Enemies;
import globals.PlayerStats;
import globals.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class LevelParser
{

	private Element	m_dom;

	public LevelParser()
	{
		try
		{
			XmlReader xml = new XmlReader();
			m_dom = xml.parse(Gdx.files.internal("Levels.xml"));
			// sumByLevel();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Retourne le nombre de wave pour le niveau demandé
	 * 
	 * @param level
	 * @return
	 */
	public int getWaveCount(int level)
	{
		// level--;
		int waveCount = m_dom.getChild(level).getChildCount();
		return waveCount;
	}

	/**
	 * Retourne le temps de la wave demandé pour un niveau demandé
	 * 
	 * @param level
	 * @param wave
	 * @return
	 */
	public float getWaveTime(int level, int wave)
	{
		// level--;
		float waveTime = Integer.parseInt(m_dom.getChild(level).getChild(wave).getAttribute("time"));
		return waveTime;
	}

	/**
	 * Retourne l'ensemble des lignes composant la wave (chaque ligne comporte ses attribus : type, quantité, popTime
	 * 
	 * @param level
	 * @param wave
	 * @return
	 */
	public float[][] getpopForWave(int level, int wave)
	{
		// level--; // Le level commence à 1, le tableau à 0.
		int popCount = m_dom.getChild(level).getChild(wave).getChildCount();
		float[][] pop = new float[popCount][4];

		for (int i = 0; i < popCount; i++)
		{
			pop[i][0] = Float.parseFloat(m_dom.getChild(level).getChild(wave).getChild(i).getAttribute("enemyType"));
			pop[i][1] = Float.parseFloat(m_dom.getChild(level).getChild(wave).getChild(i).getAttribute("quantity"));
			pop[i][2] = Float.parseFloat(m_dom.getChild(level).getChild(wave).getChild(i).getAttribute("popTime"));
			pop[i][3] = Float.parseFloat(m_dom.getChild(level).getChild(wave).getChild(i).getAttribute("delay"));
		}

		return pop;

	}

	int	xpTotalWin			= 0;
	int	goldTotalWin		= 0;
	int	currentGold			= 0;
	int	currentDamageToDo	= 0;
	int	timePlay			= 0;

	public void sumByLevel()
	{
		int nombreDeLevel = 23;
		int weaponsBuy = 1; // ON COMMENCE PAR VOULOIR ACHETER L'ARME 1

		for (int i = 1; i <= nombreDeLevel; i++)
		{
			int nombreDeVague = getWaveCount(i);

			for (int j = 0; j < nombreDeVague; j++)
			{
				float[][] wavePopData = getpopForWave(i, j);
				sumUpWave(wavePopData);
				timePlay += getWaveTime(i, j);
			}

			System.out.println("XP CUMULEE LEVEL " + i + " : " + xpTotalWin + " - LEVEL COURANT : " + PlayerStats.getLevelForXP(xpTotalWin) + " - LIFE TO KILL : " + currentDamageToDo);
			currentDamageToDo = 0;
			String global = "GOLD CUMULEE LEVEL " + i + " : " + goldTotalWin;
			String level = "";

			if (currentGold > Weapons.values()[weaponsBuy].cost)
			{
				level = " - CURRENT GOLD " + currentGold + " ACHAT (w" + weaponsBuy + ")";
				currentGold -= Weapons.values()[weaponsBuy].cost;
				weaponsBuy++;
			} else
			{
				level = " - CURRENT GOLD " + currentGold;
			}
			System.out.println(global + level);
		}
		System.out.println("Xp Total : " + xpTotalWin);
		System.out.println("Golds Totaux : " + goldTotalWin);
		System.out.println("Temps Total : " + timePlay + " secondes - soit " + timePlay / 60 + " minutes");
	}

	private void sumUpWave(float[][] wavePopData)
	{
		for (int i = 0; i < wavePopData.length; i++)
		{
			float enemyType = wavePopData[i][0];
			float quantity = wavePopData[i][1];
			xpTotalWin += (getNewEnemy(enemyType).getXpGainOnKill() * quantity);
			Enemies enemy = getNewEnemy(enemyType);
			goldTotalWin += (enemy.m_goldQuantity * enemy.m_goldValue) * quantity;
			currentGold += (enemy.m_goldQuantity * enemy.m_goldValue) * quantity;
			currentDamageToDo += enemy.getMaxLife() * quantity;
		}
	}

	private Enemies getNewEnemy(float enemyVariable)
	{

		Enemies enemy = null;
		Player m_player = new Player();

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

		default:
			break;
		}

		return enemy;
	}

}
