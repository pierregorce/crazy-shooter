package com.oasix.crazyshooter;

public class Timer
{
	private float time = 0;
	private float durationTrue;
	private float durationFalse;

	/**
	 * Evenement over time. Démarre directement.
	 * 
	 * @param durationTrue
	 * @param durationFalse
	 */
	public Timer(float durationTrue, float durationFalse)
	{
		this.durationTrue = durationTrue;
		this.durationFalse = durationFalse;
	}

	/**
	 * Evenement unique. Ayant lieu au bout de X durationFalse secondes.
	 * 
	 * @param durationFalse
	 */
	public Timer(float durationFalse)
	{
		this.durationTrue = 0;
		this.durationFalse = durationFalse;
	}

	/**
	 * Evenement unique. Demarrant instantanément.
	 * 
	 * @param durationFalse
	 */
	public Timer(float durationFalse, boolean startImmediatly)
	{
		this.durationTrue = 0;
		this.durationFalse = durationFalse;
		time = durationFalse;
	}

	/**
	 * Retourne vrai si l'action doit se produire, faux sinon. Doit être mit dans une methode act pour pouvoir incrémenter la variable temps dans l'objet timer.
	 * 
	 * @param delta
	 * @return
	 */
	public boolean doAction(float delta)
	{

		if (time < durationTrue)
		{
			time += delta;
			return true;
		}

		if (time < (durationTrue + durationFalse))
		{
			time += delta;
			return false;
		} else
		{
			time = 0;
			return true;
		}
	}

	public void reset()
	{
		time = 0;
	}

	public void resetForLunchImmediatly()
	{
		time = durationFalse;
	}

	public void timerModification(float durationFalse)
	{
		this.durationFalse = durationFalse;
	}

}
